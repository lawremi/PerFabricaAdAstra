package org.pfaa.geologica;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;

import org.pfaa.block.CompositeBlock;
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
		registerSlabRecipe(GeologicaBlocks.MEDIUM_STONE_BRICK, GeologicaBlocks.MEDIUM_STONE_BRICK_SLAB);
		registerSlabRecipe(GeologicaBlocks.STRONG_STONE_BRICK, GeologicaBlocks.STRONG_STONE_BRICK_SLAB);
		registerSlabRecipe(GeologicaBlocks.VERY_STRONG_STONE_BRICK, GeologicaBlocks.VERY_STRONG_STONE_BRICK_SLAB);
		registerWallRecipe(GeologicaBlocks.MEDIUM_COBBLESTONE, GeologicaBlocks.MEDIUM_COBBLE_WALL);
		registerWallRecipe(GeologicaBlocks.STRONG_COBBLESTONE, GeologicaBlocks.STRONG_COBBLE_WALL);
		registerBrickRecipe(GeologicaBlocks.MEDIUM_STONE, GeologicaBlocks.MEDIUM_STONE_BRICK);
		registerBrickRecipe(GeologicaBlocks.STRONG_STONE, GeologicaBlocks.STRONG_STONE_BRICK);
		registerBrickRecipe(GeologicaBlocks.VERY_STRONG_STONE, GeologicaBlocks.VERY_STRONG_STONE_BRICK);
		registerStairsRecipe(GeologicaBlocks.LIMESTONE_COBBLE_STAIRS);
		registerStairsRecipe(GeologicaBlocks.GRANITE_COBBLE_STAIRS);
		registerStairsRecipe(GeologicaBlocks.MARBLE_COBBLE_STAIRS);
		registerStairsRecipe(GeologicaBlocks.LIMESTONE_BRICK_STAIRS);
		registerStairsRecipe(GeologicaBlocks.GRANITE_BRICK_STAIRS);
		registerStairsRecipe(GeologicaBlocks.MARBLE_BRICK_STAIRS);
	}

	private static void registerSlabRecipe(CompositeBlock input, Block output) {
		registerCraftingRecipesByMeta(input, output, "###");
	}
	private static void registerWallRecipe(CompositeBlock input, Block output) {
		registerCraftingRecipesByMeta(input, output, "###", "###");
	}
	private static void registerBrickRecipe(CompositeBlock input, Block output) {
		registerCraftingRecipesByMeta(input, output, "##", "##");
	}
	
	private static void registerCraftingRecipesByMeta(CompositeBlock input, Block output, String... shape) {
		for(int meta = 0; meta < input.getMetaCount(); meta++) {
			ItemStack outputStack = new ItemStack(output, 1, meta);
			ItemStack inputStack = new ItemStack(input, 1, meta);
			GameRegistry.addRecipe(outputStack, shape, shape[0].charAt(0), inputStack);
		}
	}

	private static void registerStairsRecipe(StairsBlock output) {
		ItemStack outputStack = new ItemStack(output, 1, output.getModelBlockMeta());
		GameRegistry.addRecipe(outputStack, "#  ", "## ", "###", '#', output.getModelBlock());
	}
	
	private static void registerSmeltingRecipesByMeta(CompositeBlock input, Block output) {
		for(int meta = 0; meta < input.getMetaCount(); meta++) {
			ItemStack outputStack = new ItemStack(output, 1, meta);
			FurnaceRecipes.smelting().addSmelting(input.blockID, meta, outputStack, 0);
		}
	}

}
