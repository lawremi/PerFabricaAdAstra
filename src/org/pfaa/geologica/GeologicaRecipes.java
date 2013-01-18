package org.pfaa.geologica;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;

import org.pfaa.geologica.block.GeoBlock;
import org.pfaa.geologica.block.StairsBlock;

import cpw.mods.fml.common.registry.GameRegistry;

public class GeologicaRecipes {
	public static void register() {
		registerSmeltingRecipes();
		registerCraftingRecipes();
	}

	private static void registerSmeltingRecipes() {
		registerSmeltingRecipesByMeta(GeologicaBlocks.MEDIUM_COBBLESTONE, GeologicaBlocks.MEDIUM_STONE);
		registerSmeltingRecipesByMeta(GeologicaBlocks.STRONG_COBBLESTONE, GeologicaBlocks.STRONG_STONE);
	}

	private static void registerCraftingRecipes() {
		registerSlabRecipe(GeologicaBlocks.MEDIUM_COBBLESTONE, GeologicaBlocks.MEDIUM_COBBLE_SLAB);
		registerSlabRecipe(GeologicaBlocks.STRONG_COBBLESTONE, GeologicaBlocks.STRONG_COBBLE_SLAB);
		registerSlabRecipe(GeologicaBlocks.MEDIUM_BRICK, GeologicaBlocks.MEDIUM_BRICK_SLAB);
		registerSlabRecipe(GeologicaBlocks.STRONG_BRICK, GeologicaBlocks.STRONG_BRICK_SLAB);
		registerSlabRecipe(GeologicaBlocks.VERY_STRONG_BRICK, GeologicaBlocks.VERY_STRONG_BRICK_SLAB);
		registerWallRecipe(GeologicaBlocks.MEDIUM_COBBLESTONE, GeologicaBlocks.MEDIUM_COBBLE_WALL);
		registerWallRecipe(GeologicaBlocks.STRONG_COBBLESTONE, GeologicaBlocks.STRONG_COBBLE_WALL);
		registerBrickRecipe(GeologicaBlocks.MEDIUM_STONE, GeologicaBlocks.MEDIUM_BRICK);
		registerBrickRecipe(GeologicaBlocks.STRONG_STONE, GeologicaBlocks.STRONG_BRICK);
		registerBrickRecipe(GeologicaBlocks.VERY_STRONG_STONE, GeologicaBlocks.VERY_STRONG_BRICK);
		registerStairsRecipe(GeologicaBlocks.LIMESTONE_COBBLE_STAIRS);
		registerStairsRecipe(GeologicaBlocks.GRANITE_COBBLE_STAIRS);
		registerStairsRecipe(GeologicaBlocks.MARBLE_COBBLE_STAIRS);
		registerStairsRecipe(GeologicaBlocks.LIMESTONE_BRICK_STAIRS);
		registerStairsRecipe(GeologicaBlocks.GRANITE_BRICK_STAIRS);
		registerStairsRecipe(GeologicaBlocks.MARBLE_BRICK_STAIRS);
	}

	private static void registerSlabRecipe(Block input, Block output) {
		registerCraftingRecipesByMeta(input, output, "###");
	}
	private static void registerWallRecipe(Block input, Block output) {
		registerCraftingRecipesByMeta(input, output, "###", "###");
	}
	private static void registerBrickRecipe(Block input, Block output) {
		registerCraftingRecipesByMeta(input, output, "##", "##");
	}
	
	private static void registerCraftingRecipesByMeta(Block input, Block output, String... shape) {
		List<ItemStack> subStacks = new ArrayList<ItemStack>();
		input.getSubBlocks(input.blockID, null, subStacks);
		for(int meta = 0; meta < subStacks.size(); meta++) {
			ItemStack outputStack = new ItemStack(output, 1, meta);
			ItemStack inputStack = new ItemStack(input, 1, meta);
			GameRegistry.addRecipe(outputStack, shape, shape[0].charAt(0), inputStack);
		}
	}

	private static void registerStairsRecipe(StairsBlock output) {
		ItemStack outputStack = new ItemStack(output, 1, output.getModelBlockMeta());
		GameRegistry.addRecipe(outputStack, "#  ", "## ", "###", '#', output.getModelBlock());
	}
	
	private static void registerSmeltingRecipesByMeta(Block input, Block output) {
		List<ItemStack> subStacks = new ArrayList<ItemStack>();
		input.getSubBlocks(input.blockID, null, subStacks);
		for(int meta = 0; meta < subStacks.size(); meta++) {
			ItemStack outputStack = new ItemStack(output, 1, meta);
			FurnaceRecipes.smelting().addSmelting(input.blockID, meta, outputStack, 0);
		}
	}

}
