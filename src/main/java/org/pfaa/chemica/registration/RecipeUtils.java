package org.pfaa.chemica.registration;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.pfaa.chemica.Chemica;
import org.pfaa.chemica.model.Aggregate.Aggregates;
import org.pfaa.chemica.model.Mixture;
import org.pfaa.chemica.model.MixtureComponent;
import org.pfaa.chemica.processing.Form;
import org.pfaa.chemica.processing.Form.Forms;
import org.pfaa.chemica.util.ChanceStack;

import com.google.common.base.Function;
import com.google.common.collect.Lists;

import cpw.mods.fml.common.ObfuscationReflectionHelper;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

public class RecipeUtils {

	public static ItemStack getPrimaryGrindingOutput(Mixture mixture) {
		List<MixtureComponent> comps = mixture.getComponents();
		MixtureComponent input;
		if (comps.size() > 0) {
			input = comps.get(0);
		} else {
			input = new MixtureComponent(mixture, 1.0); 
		}
		return getGrindingOutput(input).itemStack;	
	}
	
	public static List<ChanceStack> getSecondaryGrindingOutputs(Mixture mixture) {
		List<MixtureComponent> components = mixture.getComponents();
		List<MixtureComponent> secondaries = components.subList(components.size() > 0 ? 1 : 0, components.size());
		return Lists.transform(secondaries, new MixtureComponentToGrindingOutput());
	}

	private static final double MIN_DUST_WEIGHT = 0.1;
	
	public static ChanceStack getGrindingOutput(MixtureComponent input) {
		ItemStack itemStack;
		double weight = input.weight;
		Form form = Forms.DUST;
		if (input.material == Aggregates.SAND || input.material == Aggregates.GRAVEL) {
			form = Forms.PILE;
		} else if (weight < MIN_DUST_WEIGHT) {
			form = Forms.TINY_DUST;
			weight *= 10;
		}
		itemStack = OreDictUtils.lookupBest(form, input.material);
		return new ChanceStack(itemStack, weight);
	}
	
	private static class MixtureComponentToGrindingOutput implements Function<MixtureComponent,ChanceStack> {
		@Override
		public ChanceStack apply(MixtureComponent input) {
			return getGrindingOutput(input);
		}
	}
	
	public static void oreDictifyRecipes(Map<ItemStack, String> replacements, ItemStack[] exclusions) {
        ItemStack[] replaceStacks = replacements.keySet().toArray(new ItemStack[0]);

        @SuppressWarnings("unchecked")
		List<IRecipe> recipes = CraftingManager.getInstance().getRecipeList();
        List<IRecipe> recipesToRemove = new ArrayList<IRecipe>();
        List<IRecipe> recipesToAdd = new ArrayList<IRecipe>();

        // Search vanilla recipes for recipes to replace
        for(IRecipe recipe : recipes)
        {
        	ItemStack output = recipe.getRecipeOutput();
        	if (output != null && hasItem(false, exclusions, output))
            {
            	//FMLLog.info("excluded recipe: %s", output.getItemName());
                continue;
            }
        	
        	Object[] ingredients = getIngredients(recipe);
        	if(hasItem(true, ingredients, replaceStacks)) {
        		try {
        			recipesToAdd.add(createOreRecipe(recipe, replacements));
					recipesToRemove.add(recipe);
				} catch (Exception e) {
					Chemica.log.warn("Failed to ore dictify recipe for '" + output.getUnlocalizedName() + "'");
				}
        	}
        }

        recipes.removeAll(recipesToRemove);
        recipes.addAll(recipesToAdd);
	}

	private static Object[] getIngredients(IRecipe obj) {
		if (obj instanceof ShapedRecipes) {
			return ((ShapedRecipes) obj).recipeItems;
        }
		if (obj instanceof ShapelessRecipes) {
			return ((ShapelessRecipes) obj).recipeItems.toArray();
		}
		if (obj instanceof ShapedOreRecipe) {
			return ((ShapedOreRecipe) obj).getInput();
		}
		if (obj instanceof ShapelessOreRecipe) {
			return ((ShapelessOreRecipe) obj).getInput().toArray();
		}
		return new Object[] { };
	}
	
	private static boolean hasItem(boolean strict, Object[] recipe, ItemStack... ingredients)
	{
		for (Object recipeIngredient : recipe)
		{
			if (recipeIngredient instanceof ItemStack) {
				for (ItemStack ingredient : ingredients)
				{
					if (OreDictionary.itemMatches(ingredient, (ItemStack)recipeIngredient, strict))
					{
						return true;
					}
				}
			}
		}	
		return false;
	}
	
	private static <T extends IRecipe> T createOreRecipe(Class<T> klass, IRecipe recipe, Map<ItemStack, String> replacements) throws Exception {
		Constructor<T> constructor = klass.getDeclaredConstructor(recipe.getClass(), Map.class);
		constructor.setAccessible(true);
		T replacedRecipe = constructor.newInstance(recipe, replacements);
		return replacedRecipe;
	}
	
	private static IRecipe createOreRecipe(IRecipe recipe, Map<ItemStack, String> replacements) throws Exception {
		if (recipe instanceof ShapedRecipes) {
			return createOreRecipe(ShapedOreRecipe.class, recipe, replacements);
		}
		else if (recipe instanceof ShapelessRecipes) {
			return createOreRecipe(ShapelessOreRecipe.class, recipe, replacements);
		}
		else if (recipe instanceof ShapedOreRecipe) {
			return recreateOreRecipe((ShapedOreRecipe)recipe, replacements);
		}
		else if (recipe instanceof ShapelessOreRecipe) {
			return recreateOreRecipe((ShapelessOreRecipe)recipe, replacements);
		}
		throw new IllegalArgumentException("Unknown recipe type");
	}

	private static IRecipe recreateOreRecipe(ShapelessOreRecipe recipe,	Map<ItemStack, String> replacements) {
		Object[] ingredients = recipe.getInput().toArray();
		replaceIngredients(ingredients, replacements);
		return new ShapelessOreRecipe(recipe.getRecipeOutput(), ingredients);
	}

	private static IRecipe recreateOreRecipe(ShapedOreRecipe recipe, Map<ItemStack, String> replacements) {
		Object[] ingredients = recipe.getInput().clone();
		replaceIngredients(ingredients, replacements);
		return recreateOreRecipe(recipe, recipe.getRecipeOutput(), ingredients);
	}
	
	public static IRecipe recreateOreRecipe(ShapedOreRecipe template, ItemStack output, Object[] input) {
		int width = ObfuscationReflectionHelper.getPrivateValue(ShapedOreRecipe.class, template, "width");
		int height = ObfuscationReflectionHelper.getPrivateValue(ShapedOreRecipe.class, template, "height");
        for (int i = 0; i < input.length; i++) {
        	if (input[i] instanceof String) {
        		input[i] = OreDictionary.getOres((String) input[i]);
        	}
        }
		ShapedOreRecipe recipe = new ShapedOreRecipe(output, 'x', Blocks.anvil);
		ObfuscationReflectionHelper.setPrivateValue(ShapedOreRecipe.class, recipe, input, "input");
		/* field names repeated to dispatch to correct overload */
		ObfuscationReflectionHelper.setPrivateValue(ShapedOreRecipe.class, recipe, width, "width", "width");
		ObfuscationReflectionHelper.setPrivateValue(ShapedOreRecipe.class, recipe, height, "height", "height");
		return recipe;
	}
	
	private static void replaceIngredients(Object[] ingredients, Map<ItemStack, String> replacements) {
		for (int i = 0; i < ingredients.length; i++) {
			if (ingredients[i] instanceof ItemStack) {
				ItemStack ingredient = (ItemStack)ingredients[i];
				for(Entry<ItemStack, String> replace : replacements.entrySet())
	            {
	                if(OreDictionary.itemMatches(replace.getKey(), ingredient, true))
	                {
	                    ingredients[i] = replace.getValue();
	                    break;
	                }
	            }
			}
		}
	}
}
