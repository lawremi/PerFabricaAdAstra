package org.pfaa.geologica;

import java.lang.reflect.Constructor;
import java.util.logging.Level;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

import org.pfaa.ConfigIDProvider;
import org.pfaa.block.CompositeBlock;
import org.pfaa.geologica.GeoSubstance.Composition;
import org.pfaa.geologica.GeoSubstance.Strength;
import org.pfaa.geologica.block.BrickGeoBlock;
import org.pfaa.geologica.block.BrokenGeoBlock;
import org.pfaa.geologica.block.GeoBlock;
import org.pfaa.geologica.block.IntactGeoBlock;
import org.pfaa.geologica.block.LooseGeoBlock;
import org.pfaa.geologica.block.SlabBlock;
import org.pfaa.geologica.block.StairsBlock;
import org.pfaa.geologica.block.WallBlock;

import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.LoaderException;

public class GeologicaBlocks {
	public static final GeoBlock WEAK_STONE = createStoneBlock(Strength.WEAK);
	public static final GeoBlock MEDIUM_STONE = createStoneBlock(Strength.MEDIUM);
	public static final GeoBlock STRONG_STONE = createStoneBlock(Strength.STRONG);
	public static final GeoBlock VERY_STRONG_STONE = createStoneBlock(Strength.VERY_STRONG);
	
	public static final GeoBlock MEDIUM_COBBLESTONE = createCobbleBlock(Strength.MEDIUM);
	public static final GeoBlock STRONG_COBBLESTONE = createCobbleBlock(Strength.STRONG);
	
	public static final GeoBlock MEDIUM_STONE_BRICK = createStoneBrickBlock(Strength.MEDIUM);
	public static final GeoBlock STRONG_STONE_BRICK = createStoneBrickBlock(Strength.STRONG);
	public static final GeoBlock VERY_STRONG_STONE_BRICK = createStoneBrickBlock(Strength.VERY_STRONG);
	
	public static final SlabBlock MEDIUM_COBBLE_SLAB = createSlabBlock(MEDIUM_COBBLESTONE, null);
	public static final SlabBlock MEDIUM_COBBLE_DOUBLE_SLAB = createSlabBlock(MEDIUM_COBBLESTONE, MEDIUM_COBBLE_SLAB);
	public static final SlabBlock STRONG_COBBLE_SLAB = createSlabBlock(STRONG_COBBLESTONE, null);
	public static final SlabBlock STRONG_COBBLE_DOUBLE_SLAB = createSlabBlock(STRONG_COBBLESTONE, STRONG_COBBLE_SLAB);
	
	public static final SlabBlock MEDIUM_STONE_BRICK_SLAB = createSlabBlock(MEDIUM_STONE_BRICK, null);
	public static final SlabBlock MEDIUM_STONE_BRICK_DOUBLE_SLAB = createSlabBlock(MEDIUM_STONE_BRICK, MEDIUM_STONE_BRICK_SLAB);
	public static final SlabBlock STRONG_STONE_BRICK_SLAB = createSlabBlock(STRONG_STONE_BRICK, null);
	public static final SlabBlock STRONG_STONE_BRICK_DOUBLE_SLAB = createSlabBlock(STRONG_STONE_BRICK, STRONG_STONE_BRICK_SLAB);
	public static final SlabBlock VERY_STRONG_STONE_BRICK_SLAB = createSlabBlock(VERY_STRONG_STONE_BRICK, null);
	public static final SlabBlock VERY_STRONG_STONE_BRICK_DOUBLE_SLAB = createSlabBlock(VERY_STRONG_STONE_BRICK, VERY_STRONG_STONE_BRICK_SLAB);
	
	public static final Block MEDIUM_COBBLE_WALL = createWallBlock(MEDIUM_COBBLESTONE);
	public static final Block STRONG_COBBLE_WALL = createWallBlock(STRONG_COBBLESTONE);
	
	public static final StairsBlock LIMESTONE_COBBLE_STAIRS = createStairsBlock(MEDIUM_COBBLESTONE, GeoSubstance.LIMESTONE);
	public static final StairsBlock GRANITE_COBBLE_STAIRS = createStairsBlock(STRONG_COBBLESTONE, GeoSubstance.GRANITE);
	public static final StairsBlock MARBLE_COBBLE_STAIRS = createStairsBlock(STRONG_COBBLESTONE, GeoSubstance.MARBLE);
	
	public static final StairsBlock LIMESTONE_BRICK_STAIRS = createStairsBlock(MEDIUM_STONE_BRICK, GeoSubstance.LIMESTONE);
	public static final StairsBlock GRANITE_BRICK_STAIRS = createStairsBlock(STRONG_STONE_BRICK, GeoSubstance.GRANITE);
	public static final StairsBlock MARBLE_BRICK_STAIRS = createStairsBlock(STRONG_STONE_BRICK, GeoSubstance.MARBLE);
	
	public static final GeoBlock WEAK_RUBBLE = createRubbleBlock(Strength.WEAK);
	
	public static final GeoBlock ORE_SAND = createOreSandBlock();
	
	public static final GeoBlock WEAK_ORE_ROCK = createOreRockBlock(Strength.WEAK);
	public static final GeoBlock MEDIUM_ORE_ROCK = createOreRockBlock(Strength.MEDIUM);
	public static final GeoBlock STRONG_ORE_ROCK = createOreRockBlock(Strength.STRONG);
	//public static final GeoBlock VERY_STRONG_ORE_ROCK = createOreRockBlock(Strength.VERY_STRONG);

	public static final GeoBlock CLAY = createClayBlock();
	public static final GeoBlock ORE_CLAY = createOreClayBlock();
	public static final GeoBlock CLAY_BRICK = createClayBrickBlock();
	
	private static GeoBlock createStoneBlock(Strength strength) {
		return createGeoBlock("Stone", IntactGeoBlock.class, strength, Composition.ROCK, Material.rock);
	}
	private static GeoBlock createCobbleBlock(Strength strength) {
		return createGeoBlock("Cobble", BrokenGeoBlock.class, strength, Composition.ROCK, Material.rock);
	}
	private static GeoBlock createStoneBrickBlock(Strength strength) {
		return createGeoBlock("StoneBrick", BrickGeoBlock.class, strength, Composition.ROCK, Material.rock);
	}
	private static GeoBlock createRubbleBlock(Strength strength) {
		return createGeoBlock("Rubble", LooseGeoBlock.class, strength, Composition.ROCK, Material.rock);
	}
	private static GeoBlock createOreSandBlock() {
		return createGeoBlock("OreSand", LooseGeoBlock.class, Strength.WEAK, Composition.ORE, Material.sand);
	}
	private static GeoBlock createOreRockBlock(Strength strength) {
		return createGeoBlock("OreRock", IntactGeoBlock.class, strength, Composition.ORE, Material.rock);
	}
	private static GeoBlock createClayBlock() {
		return createGeoBlock("Clay", IntactGeoBlock.class, Strength.WEAK, Composition.ROCK, Material.clay);
	}
	private static GeoBlock createOreClayBlock() {
		return createGeoBlock("OreClay", IntactGeoBlock.class, Strength.WEAK, Composition.ORE, Material.clay);
	}
	private static GeoBlock createClayBrickBlock() {
		return createGeoBlock("ClayBrick", BrickGeoBlock.class, Strength.WEAK, Composition.ROCK, Material.clay);
	}
	
	private static Block createWallBlock(CompositeBlock modelBlock) {
		return createDerivedBlock(WallBlock.class, modelBlock);
	}
	private static SlabBlock createSlabBlock(CompositeBlock modelBlock, SlabBlock singleSlab) {
		String doubleToken = singleSlab == null ? "" : "Double";
		String name = modelBlock.getBlockName().replaceFirst("tile\\.", "") + doubleToken + nameForBlockClass(SlabBlock.class);
		int id = ConfigIDProvider.getInstance().nextBlockID(name);
		SlabBlock block = new SlabBlock(id, modelBlock, singleSlab);
		block.setBlockName(name);
		return block;
	}
	
	private static StairsBlock createStairsBlock(GeoBlock modelBlock, GeoSubstance substance) {
		String name = modelBlock.getBlockName().replaceFirst("tile\\.", "") + "Stairs" + "." + substance.getLowerName();
		int id = ConfigIDProvider.getInstance().nextBlockID(name);
		StairsBlock block = new StairsBlock(id, modelBlock, modelBlock.getMetaForSubstance(substance));
		block.setBlockName(name);
		return block;
	}
	
	private static String nameForBlockClass(Class<? extends Block> blockClass) {
		return blockClass.getSimpleName().replaceAll("Block$", "");
	}
	
	private static GeoBlock createGeoBlock(String suffix, Class<? extends GeoBlock> blockClass, Strength strength, Composition composition, Material material) {
		GeoBlock block = null;
		String name = strength.getCamelName() + suffix;
		try {
			Constructor<? extends GeoBlock> constructor = blockClass.getConstructor(int.class, Strength.class, Composition.class, Material.class);
			int id = ConfigIDProvider.getInstance().nextBlockID(name);
			block = constructor.newInstance(id, strength, composition, material);
			block.setBlockName(name);
		} catch (Exception e) {
			FMLLog.log(Level.SEVERE, e, "Failed to construct GeoBlock: " + name);
			throw new LoaderException(e);
		}
		return block;
	}
	
	private static <T extends Block> T createDerivedBlock(Class<T> blockClass, CompositeBlock modelBlock) {
		T block = null;
		try {
			Constructor<T> constructor = blockClass.getConstructor(int.class, CompositeBlock.class);
			String name = modelBlock.getBlockName().replaceFirst("tile\\.", "") + nameForBlockClass(blockClass);
			int id = ConfigIDProvider.getInstance().nextBlockID(name);
			block = constructor.newInstance(id, modelBlock);
			block.setBlockName(name);
		} catch (Exception e) {
			FMLLog.log(Level.SEVERE, e, "Failed to construct derived block");
			throw new LoaderException(e);
		}
		return block;
	}
}
