package org.pfaa;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;

import net.minecraft.src.CraftingManager;
import net.minecraft.src.IRecipe;
import net.minecraft.src.ItemStack;
import net.minecraft.src.ShapedRecipes;
import net.minecraft.src.ShapelessRecipes;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import cpw.mods.fml.common.FMLLog;

public class RecipeUtils {
	
	public static void createOreRecipes(Map<ItemStack, String> replacements) {
        ItemStack[] replaceStacks = replacements.keySet().toArray(new ItemStack[0]);

        List recipes = CraftingManager.getInstance().getRecipeList();
        List<IRecipe> recipesToRemove = new ArrayList<IRecipe>();
        List<IRecipe> recipesToAdd = new ArrayList<IRecipe>();

        // Search vanilla recipes for recipes to replace
        for(Object obj : recipes)
        {
            if(obj instanceof ShapedRecipes)
            {
                ShapedRecipes recipe = (ShapedRecipes)obj;
                
                if(recipeHasIngredient(true, recipe.recipeItems, replaceStacks))
                {
                	FMLLog.info("Found recipe for %s", recipe.getRecipeOutput().getItemName());
                    
                	recipesToRemove.add(recipe);
                    recipesToAdd.add(createOreRecipe(recipe, replacements));
                }
            }
            else if(obj instanceof ShapelessRecipes)
            {
                ShapelessRecipes recipe = (ShapelessRecipes)obj;
                ItemStack output = recipe.getRecipeOutput();
                
                if(recipeHasIngredient(true, (ItemStack[])recipe.recipeItems.toArray(new ItemStack[0]), replaceStacks))
                {
                    recipesToRemove.add((IRecipe)obj);
                    IRecipe newRecipe = createOreRecipe(recipe, replacements);
                    recipesToAdd.add(newRecipe);
                }
            }
        }

        recipes.removeAll(recipesToRemove);
        recipes.addAll(recipesToAdd);
	}

	
	private static boolean recipeHasIngredient(boolean strict, ItemStack[] recipe, ItemStack... ingredients)
	{
		for (ItemStack recipeIngredient : recipe)
		{
			for (ItemStack ingredient : ingredients)
			{
				if (OreDictionary.itemMatches(ingredient, recipeIngredient, strict))
				{
					return true;
				}
			}	
		}	
		return false;
	}
	
	private static IRecipe invokeReplacingConstructor(Class<? extends IRecipe> klass, Object recipe, Map<ItemStack, String> replacements) {
		IRecipe replacedRecipe = null;
		
		try {
			Constructor<? extends IRecipe> constructor = klass.getDeclaredConstructor(recipe.getClass(), Map.class);
			constructor.setAccessible(true);
			replacedRecipe = constructor.newInstance(recipe, replacements);
		} catch(Exception e) {
			FMLLog.log(Level.SEVERE, e, "Exception thrown during ore recipe creation");
		}
		return replacedRecipe;
	}
	
	private static IRecipe createOreRecipe(ShapedRecipes recipe, Map<ItemStack, String> replacements) {
		return invokeReplacingConstructor(ShapedOreRecipe.class, recipe, replacements);
	}
	private static IRecipe createOreRecipe(ShapelessRecipes recipe, Map<ItemStack, String> replacements) {
        return invokeReplacingConstructor(ShapelessOreRecipe.class, recipe, replacements);
	}
}
