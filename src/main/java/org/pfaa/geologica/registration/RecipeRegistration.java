package org.pfaa.geologica.registration;

import java.util.List;

import org.pfaa.chemica.item.IndustrialMaterialItem;
import org.pfaa.chemica.model.Strength;
import org.pfaa.chemica.processing.TemperatureLevel;
import org.pfaa.chemica.registration.RecipeRegistry;
import org.pfaa.chemica.registration.RecipeUtils;
import org.pfaa.chemica.util.ChanceStack;
import org.pfaa.core.block.CompositeBlock;
import org.pfaa.geologica.GeoMaterial;
import org.pfaa.geologica.Geologica;
import org.pfaa.geologica.GeologicaBlocks;
import org.pfaa.geologica.GeologicaItems;
import org.pfaa.geologica.block.GeoBlock;
import org.pfaa.geologica.block.LooseGeoBlock;
import org.pfaa.geologica.block.ProxyBlock;
import org.pfaa.geologica.block.StairsBlock;
import org.pfaa.geologica.block.WallBlock;
import org.pfaa.geologica.integration.TCIntegration;
import org.pfaa.geologica.processing.Ore;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

public class RecipeRegistration {
	private static final RecipeRegistry registry = org.pfaa.chemica.registration.RecipeRegistration.getTarget();
	
	public static void init() {
		registerSmeltingRecipes();
		registerCraftingRecipes();
		registerCommunitionRecipes();
		registerStoneToolRecipes();
		registerCompatibilityRecipes();
	}
	
	private static void registerCompatibilityRecipes() {
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(Blocks.cobblestone), "cobblestone"));
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(Blocks.stone), "stone"));
		GameRegistry.addShapelessRecipe(new ItemStack(Items.coal), GeologicaItems.CRUDE_LUMP.of(GeoMaterial.BITUMINOUS_COAL));
	}

	private static void registerStoneToolRecipes() {
		registerStoneToolRecipes(GeologicaBlocks.WEAK_RUBBLE);
		registerStoneToolRecipes(GeologicaBlocks.MEDIUM_COBBLE);
		registerStoneToolRecipes(GeologicaBlocks.STRONG_COBBLE);
		registerStoneToolRecipes(GeologicaBlocks.VERY_STRONG_COBBLE);
		if (Loader.isModLoaded("TConstruct"))
			TCIntegration.addStoneMaterials(); // TODO: implement this
	}

	// TODO (chemica): add metal tool recipes, once we have ingots
	// - Valid metals/alloys: all types of steel, magnesium
	// - When GT loaded, tools are automatic, based on material system
	// - Should probably try to make tools balanced with Metallurgy/Railcraft tools
	// - No need for a plain steel pickaxe when those mods are loaded
	//   - Perhaps we could just check if the oredict-based recipe already exists?
	// - When Tinker's Construct is loaded, we skip this (but our materials are registered)
	private static void registerStoneToolRecipes(GeoBlock block) {
		registerStoneToolRecipe(block, Items.stone_pickaxe);
		registerStoneToolRecipe(block, Items.stone_axe);
		registerStoneToolRecipe(block, Items.stone_shovel);
		registerStoneToolRecipe(block, Items.stone_hoe);
		registerStoneToolRecipe(block, Items.stone_sword);
	}

	private static void registerStoneToolRecipe(GeoBlock block, Item tool) {
		ItemStack damaged = new ItemStack(tool, 1, (int)(getInitialStoneToolDamage(block.getStrength()) * tool.getMaxDamage()));
		ItemStack material = new ItemStack(block, 1, OreDictionary.WILDCARD_VALUE);
		ItemStack cobblestone = new ItemStack(Blocks.cobblestone, 1, OreDictionary.WILDCARD_VALUE);
		@SuppressWarnings("unchecked")
		List<IRecipe> recipes = (List<IRecipe>)CraftingManager.getInstance().getRecipeList();
		for (IRecipe recipe : recipes) {
			ItemStack output = recipe.getRecipeOutput();
			if (output != null && output.getItem() == tool && recipe instanceof ShapedOreRecipe) {
				ShapedOreRecipe shapedRecipe = (ShapedOreRecipe)recipe;
				Object[] origIngredients = shapedRecipe.getInput();
				Object[] ingredients = origIngredients.clone();
				for (int i = 0; i < ingredients.length; i++) {
					if (ingredients[i] instanceof List) 
					{
						@SuppressWarnings("unchecked")
						List<ItemStack> oreIngredient = (List<ItemStack>)ingredients[i];
						for (ItemStack ingredient : oreIngredient) {
							if (ingredient.getItem() == cobblestone.getItem()) {
								ingredients[i] = material;
								origIngredients[i] = cobblestone;
							}
						}
					} else if (ingredients[i] instanceof ItemStack) {
						if (((ItemStack)ingredients[i]).getItem() == cobblestone.getItem()) {
							ingredients[i] = material;
						}
					}
				}
				recipes.add(RecipeUtils.recreateOreRecipe(shapedRecipe, damaged, ingredients));
				break;
			}
		}
	}

	private static float getInitialStoneToolDamage(Strength strength) {
		return Geologica.getConfiguration().getInitialStoneToolDamage(strength);
	}
	
	private static void registerSmeltingRecipes() {
		for (Block block : GeologicaBlocks.getBlocks()) {
			if (block instanceof GeoBlock) {
				registerSmeltingRecipes((GeoBlock)block);
			}
		}
	}

	private static void registerSmeltingRecipes(GeoBlock output) {
		TemperatureLevel temp = TemperatureLevel.values()[output.getStrength().ordinal()];
		GeoBlock input = output.getBrokenRockBlock();
		if (input != null) {
			registerSmeltingRecipesByMeta(input, output, temp);
		}
	}

	private static void registerCraftingRecipes() {
		registerSlabRecipe(GeologicaBlocks.MEDIUM_COBBLE, GeologicaBlocks.MEDIUM_COBBLE_SLAB);
		registerSlabRecipe(GeologicaBlocks.STRONG_COBBLE, GeologicaBlocks.STRONG_COBBLE_SLAB);
		registerSlabRecipe(GeologicaBlocks.MEDIUM_STONE_BRICK, GeologicaBlocks.MEDIUM_STONE_BRICK_SLAB);
		registerSlabRecipe(GeologicaBlocks.STRONG_STONE_BRICK, GeologicaBlocks.STRONG_STONE_BRICK_SLAB);
		registerSlabRecipe(GeologicaBlocks.VERY_STRONG_STONE_BRICK, GeologicaBlocks.VERY_STRONG_STONE_BRICK_SLAB);
		registerSlabRecipe(GeologicaBlocks.MEDIUM_STONE, GeologicaBlocks.MEDIUM_STONE_SLAB);
		registerSlabRecipe(GeologicaBlocks.STRONG_STONE, GeologicaBlocks.STRONG_STONE_SLAB);
		registerSlabRecipe(GeologicaBlocks.VERY_STRONG_STONE, GeologicaBlocks.VERY_STRONG_STONE_SLAB);
		registerWallRecipe(GeologicaBlocks.MEDIUM_COBBLE, GeologicaBlocks.MEDIUM_COBBLE_WALL);
		registerWallRecipe(GeologicaBlocks.STRONG_COBBLE, GeologicaBlocks.STRONG_COBBLE_WALL);
		registerWallRecipe(GeologicaBlocks.VERY_STRONG_COBBLE, GeologicaBlocks.VERY_STRONG_COBBLE_WALL);
		registerWallRecipe(GeologicaBlocks.MEDIUM_STONE_BRICK, GeologicaBlocks.MEDIUM_STONE_BRICK_WALL);
		registerWallRecipe(GeologicaBlocks.STRONG_STONE_BRICK, GeologicaBlocks.STRONG_STONE_BRICK_WALL);
		registerWallRecipe(GeologicaBlocks.VERY_STRONG_STONE_BRICK, GeologicaBlocks.VERY_STRONG_STONE_BRICK_WALL);
		registerBrickRecipe(GeologicaBlocks.MEDIUM_STONE, GeologicaBlocks.MEDIUM_STONE_BRICK);
		registerBrickRecipe(GeologicaBlocks.STRONG_STONE, GeologicaBlocks.STRONG_STONE_BRICK);
		registerBrickRecipe(GeologicaBlocks.VERY_STRONG_STONE, GeologicaBlocks.VERY_STRONG_STONE_BRICK);
		registerBrickRecipe(GeologicaBlocks.WEAK_CLAY, GeologicaBlocks.WEAK_CLAY_BRICK);
		registerStairsRecipe(GeologicaBlocks.MEDIUM_COBBLE_STAIRS__LIMESTONE);
		registerStairsRecipe(GeologicaBlocks.STRONG_COBBLE_STAIRS__GRANITE);
		registerStairsRecipe(GeologicaBlocks.STRONG_COBBLE_STAIRS__MARBLE);
		registerStairsRecipe(GeologicaBlocks.MEDIUM_STONE_BRICK_STAIRS__LIMESTONE);
		registerStairsRecipe(GeologicaBlocks.STRONG_STONE_BRICK_STAIRS__GRANITE);
		registerStairsRecipe(GeologicaBlocks.STRONG_STONE_BRICK_STAIRS__MARBLE);
		registerQuarterToBlockRecipe(GeologicaItems.EARTHY_CLUMP);
	}

	private static void registerCommunitionRecipes() {
		for (Block block : GeologicaBlocks.getBlocks()) {
			registerCommunitionRecipes(block);
		}
	}
	
	private static void registerCommunitionRecipes(Block block) {
		if (block instanceof StairsBlock) {
			registerCrushingRecipes((StairsBlock)block);
			return;
		}
		GeoBlock geoBlock = null;
		if (block instanceof WallBlock) {
			geoBlock = (GeoBlock)((ProxyBlock)block).getModelBlock();
		} else if (block instanceof GeoBlock) {
			geoBlock = (GeoBlock)block;
		}
		if (geoBlock != null) {
			GeoBlock broken = geoBlock.getBrokenRockBlock();
			if (broken != null) {
				registerCrushingRecipes(block, broken);
			} else if (block instanceof LooseGeoBlock) {
				registerGrindingRecipes((GeoBlock)block);
			} else if (Ore.class.isAssignableFrom(geoBlock.getComposition())) {
				registerOreCommunitionRecipes((GeoBlock)block);
			}
		}
	}

	private static void registerCrushingRecipes(StairsBlock input) {
		GeoBlock model = (GeoBlock)input.getModelBlock();
		GeoBlock output = model.getBrokenRockBlock();
		if (output == null) {
			return;
		}
		int damage = input.getModelBlockMeta();
		registry.registerCrushingRecipe(new ItemStack(input), 
										new ItemStack(output, 1, damage), 
										model.getStrength());
	}
	
	private static void registerCrushingRecipes(Block input, GeoBlock output) {
		for(GeoMaterial material : output.getGeoMaterials()) {
			int damage = output.getMeta(material);
			registry.registerCrushingRecipe(new ItemStack(input, 1, damage),
					new ItemStack(output, 1, damage), 
					material.getStrength());
		}
	}
	
	private static void registerOreCommunitionRecipes(GeoBlock input) {
		for(GeoMaterial material : input.getGeoMaterials()) {
			ItemStack crushed = GeologicaItems.ORE_CRUSHED.getItemStack(material, 2);
			registry.registerCrushingRecipe(input.getItemStack(material),
					crushed, input.getStrength());
			registerGrindingRecipes(crushed.splitStack(1), material);
		}
	}

	private static void registerGrindingRecipes(GeoBlock input) {
		for(GeoMaterial material : input.getGeoMaterials()) {
			registerGrindingRecipes(input.getItemStack(material), material);
		}
	}

	private static void registerGrindingRecipes(ItemStack input, GeoMaterial material) {
		ItemStack primary = RecipeUtils.getPrimaryGrindingOutput(material.getComposition());
		List<ChanceStack> secondaries = RecipeUtils.getSecondaryGrindingOutputs(material.getComposition());
		if (secondaries.size() == 0) {
			primary.stackSize = 2;
		}
		registry.registerGrindingRecipe(input, primary, secondaries, material.getStrength());
	}

	private static void registerSlabRecipe(CompositeBlock input, Block output) {
		registerCraftingRecipesByMeta(input, output, 6, "###");
	}
	private static void registerWallRecipe(CompositeBlock input, Block output) {
		registerCraftingRecipesByMeta(input, output, 6, "###", "###");
	}
	private static void registerBrickRecipe(CompositeBlock input, Block output) {
		registerCraftingRecipesByMeta(input, output, 4, "##", "##");
	}
	
	private static void registerCraftingRecipesByMeta(CompositeBlock input, Block output, int outputSize, String... shape) {
		for(int meta = 0; meta < input.getMetaCount(); meta++) {
			ItemStack outputStack = new ItemStack(output, outputSize, meta);
			ItemStack inputStack = new ItemStack(input, 1, meta);
			GameRegistry.addRecipe(outputStack, shape, shape[0].charAt(0), inputStack);
		}
	}
	
	private static void registerQuarterToBlockRecipe(IndustrialMaterialItem<GeoMaterial> input) {
		for(GeoMaterial material : input.getIndustrialMaterials()) {
			ItemStack outputStack = GeoBlock.getNative(material).getItemStack(1);
			ItemStack inputStack = input.getItemStack(material);
			GameRegistry.addRecipe(outputStack, "##", "##", '#', inputStack);
		}
	}
	
	private static void registerStairsRecipe(StairsBlock output) {
		ItemStack outputStack = new ItemStack(output, 4, output.getModelBlockMeta());
		ItemStack inputStack = new ItemStack(output.getModelBlock(), 1, output.getModelBlockMeta());
		GameRegistry.addRecipe(outputStack, "#  ", "## ", "###", '#', inputStack);
	}
	
	private static void registerSmeltingRecipesByMeta(CompositeBlock input, Block output, TemperatureLevel temp) {
		for(int meta = 0; meta < input.getMetaCount(); meta++) {
			ItemStack outputStack = new ItemStack(output, 1, meta);
			ItemStack inputStack = new ItemStack(input, 1, meta);
			registry.registerSmeltingRecipe(inputStack, outputStack, temp);
		}
	}	
}
