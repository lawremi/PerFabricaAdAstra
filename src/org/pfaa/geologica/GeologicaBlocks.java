package org.pfaa.geologica;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

import org.pfaa.block.CompositeBlock;
import org.pfaa.chemica.model.IndustrialMaterial;
import org.pfaa.chemica.model.Mixture;
import org.pfaa.geologica.GeoMaterial.Strength;
import org.pfaa.geologica.block.BrickGeoBlock;
import org.pfaa.geologica.block.BrokenGeoBlock;
import org.pfaa.geologica.block.GeoBlock;
import org.pfaa.geologica.block.IntactGeoBlock;
import org.pfaa.geologica.block.LooseGeoBlock;
import org.pfaa.geologica.block.SlabBlock;
import org.pfaa.geologica.block.StairsBlock;
import org.pfaa.geologica.block.WallBlock;
import org.pfaa.geologica.processing.Aggregate;
import org.pfaa.geologica.processing.Ore;
import org.pfaa.geologica.processing.OreMineral;

import cpw.mods.fml.common.LoaderException;

public class GeologicaBlocks {
	public static final GeoBlock WEAK_STONE = createStoneBlock(Strength.WEAK);
	public static final GeoBlock MEDIUM_STONE = createStoneBlock(Strength.MEDIUM);
	public static final GeoBlock STRONG_STONE = createStoneBlock(Strength.STRONG);
	public static final GeoBlock VERY_STRONG_STONE = createStoneBlock(Strength.VERY_STRONG);
	
	public static final GeoBlock MEDIUM_COBBLESTONE = createCobbleBlock(Strength.MEDIUM);
	public static final GeoBlock STRONG_COBBLESTONE = createCobbleBlock(Strength.STRONG);
	public static final GeoBlock VERY_STRONG_COBBLESTONE = createCobbleBlock(Strength.VERY_STRONG);
	
	public static final GeoBlock MEDIUM_STONE_BRICK = createStoneBrickBlock(Strength.MEDIUM);
	public static final GeoBlock STRONG_STONE_BRICK = createStoneBrickBlock(Strength.STRONG);
	public static final GeoBlock VERY_STRONG_STONE_BRICK = createStoneBrickBlock(Strength.VERY_STRONG);
	
	public static final SlabBlock MEDIUM_COBBLE_SLAB = createSlabBlock(MEDIUM_COBBLESTONE, null);
	public static final SlabBlock MEDIUM_COBBLE_DOUBLE_SLAB = createSlabBlock(MEDIUM_COBBLESTONE, MEDIUM_COBBLE_SLAB);
	public static final SlabBlock STRONG_COBBLE_SLAB = createSlabBlock(STRONG_COBBLESTONE, null);
	public static final SlabBlock VERY_STRONG_COBBLE_SLAB = createSlabBlock(VERY_STRONG_COBBLESTONE, null);
	public static final SlabBlock STRONG_COBBLE_DOUBLE_SLAB = createSlabBlock(STRONG_COBBLESTONE, STRONG_COBBLE_SLAB);
	public static final SlabBlock VERY_STRONG_COBBLE_DOUBLE_SLAB = createSlabBlock(VERY_STRONG_COBBLESTONE, VERY_STRONG_COBBLE_SLAB);
	
	public static final SlabBlock MEDIUM_STONE_BRICK_SLAB = createSlabBlock(MEDIUM_STONE_BRICK, null);
	public static final SlabBlock MEDIUM_STONE_BRICK_DOUBLE_SLAB = createSlabBlock(MEDIUM_STONE_BRICK, MEDIUM_STONE_BRICK_SLAB);
	public static final SlabBlock STRONG_STONE_BRICK_SLAB = createSlabBlock(STRONG_STONE_BRICK, null);
	public static final SlabBlock STRONG_STONE_BRICK_DOUBLE_SLAB = createSlabBlock(STRONG_STONE_BRICK, STRONG_STONE_BRICK_SLAB);
	public static final SlabBlock VERY_STRONG_STONE_BRICK_SLAB = createSlabBlock(VERY_STRONG_STONE_BRICK, null);
	public static final SlabBlock VERY_STRONG_STONE_BRICK_DOUBLE_SLAB = createSlabBlock(VERY_STRONG_STONE_BRICK, VERY_STRONG_STONE_BRICK_SLAB);
	
	public static final SlabBlock MEDIUM_STONE_SLAB = createSlabBlock(MEDIUM_STONE, null);
	public static final SlabBlock MEDIUM_STONE_DOUBLE_SLAB = createSlabBlock(MEDIUM_STONE, MEDIUM_STONE_SLAB);
	public static final SlabBlock STRONG_STONE_SLAB = createSlabBlock(STRONG_STONE, null);
	public static final SlabBlock STRONG_STONE_DOUBLE_SLAB = createSlabBlock(STRONG_STONE, STRONG_STONE_SLAB);
	public static final SlabBlock VERY_STRONG_STONE_SLAB = createSlabBlock(VERY_STRONG_STONE, null);
	public static final SlabBlock VERY_STRONG_STONE_DOUBLE_SLAB = createSlabBlock(VERY_STRONG_STONE, VERY_STRONG_STONE_SLAB);
	
	public static final Block MEDIUM_COBBLE_WALL = createWallBlock(MEDIUM_COBBLESTONE);
	public static final Block STRONG_COBBLE_WALL = createWallBlock(STRONG_COBBLESTONE);
	public static final Block VERY_STRONG_COBBLE_WALL = createWallBlock(VERY_STRONG_COBBLESTONE);
	
	public static final Block MEDIUM_STONE_BRICK_WALL = createWallBlock(MEDIUM_STONE_BRICK);
	public static final Block STRONG_STONE_BRICK_WALL = createWallBlock(STRONG_STONE_BRICK);
	public static final Block VERY_STRONG_STONE_BRICK_WALL = createWallBlock(VERY_STRONG_STONE_BRICK);
	
	public static final StairsBlock LIMESTONE_COBBLE_STAIRS = createStairsBlock(MEDIUM_COBBLESTONE, GeoMaterial.LIMESTONE);
	public static final StairsBlock GRANITE_COBBLE_STAIRS = createStairsBlock(STRONG_COBBLESTONE, GeoMaterial.GRANITE);
	public static final StairsBlock MARBLE_COBBLE_STAIRS = createStairsBlock(STRONG_COBBLESTONE, GeoMaterial.MARBLE);
	
	public static final StairsBlock LIMESTONE_BRICK_STAIRS = createStairsBlock(MEDIUM_STONE_BRICK, GeoMaterial.LIMESTONE);
	public static final StairsBlock GRANITE_BRICK_STAIRS = createStairsBlock(STRONG_STONE_BRICK, GeoMaterial.GRANITE);
	public static final StairsBlock MARBLE_BRICK_STAIRS = createStairsBlock(STRONG_STONE_BRICK, GeoMaterial.MARBLE);
	
	public static final GeoBlock WEAK_RUBBLE = createRubbleBlock(Strength.WEAK);
	
	public static final GeoBlock ORE_SAND = createOreSandBlock();
	
	public static final GeoBlock WEAK_ORE_ROCK = createOreRockBlock(Strength.WEAK);
	public static final GeoBlock MEDIUM_ORE_ROCK = createOreRockBlock(Strength.MEDIUM);
	public static final GeoBlock STRONG_ORE_ROCK = createOreRockBlock(Strength.STRONG);
	//public static final GeoBlock VERY_STRONG_ORE_ROCK = createOreRockBlock(Strength.VERY_STRONG);

	public static final GeoBlock CLAY = createClayBlock();
	public static final GeoBlock ORE_CLAY = createOreClayBlock();
	public static final GeoBlock CLAY_BRICK = createClayBrickBlock();
	
	static {
		//WEAK_STONE.addChanceDrop(GeoSubstance.CONGLOMERATE, new ChanceDrop().add(0.05, Item.goldNugget).add(0.05, "nuggetCopper"));
	}
	
	private static GeoBlock createStoneBlock(Strength strength) {
		return createGeoBlock("Stone", IntactGeoBlock.class, strength, Aggregate.class, Material.rock);
	}
	private static GeoBlock createCobbleBlock(Strength strength) {
		return createGeoBlock("Cobble", BrokenGeoBlock.class, strength, Aggregate.class, Material.rock);
	}
	private static GeoBlock createStoneBrickBlock(Strength strength) {
		return createGeoBlock("StoneBrick", BrickGeoBlock.class, strength, Aggregate.class, Material.rock);
	}
	private static GeoBlock createRubbleBlock(Strength strength) {
		return createGeoBlock("Rubble", LooseGeoBlock.class, strength, Aggregate.class, Material.rock);
	}
	private static GeoBlock createOreSandBlock() {
		return createGeoBlock("OreSand", LooseGeoBlock.class, Strength.WEAK, Ore.class, Material.sand);
	}
	private static GeoBlock createOreRockBlock(Strength strength) {
		return createGeoBlock("OreRock", IntactGeoBlock.class, strength, Ore.class, Material.rock);
	}
	private static GeoBlock createClayBlock() {
		return createGeoBlock("Clay", IntactGeoBlock.class, Strength.WEAK, Aggregate.class, Material.clay);
	}
	private static GeoBlock createOreClayBlock() {
		return createGeoBlock("OreClay", IntactGeoBlock.class, Strength.WEAK, Ore.class, Material.clay);
	}
	private static GeoBlock createClayBrickBlock() {
		return createGeoBlock("ClayBrick", BrickGeoBlock.class, Strength.WEAK, Aggregate.class, Material.clay);
	}
	
	public static List<Block> getBlocks() {
		List<Block> blocks = new ArrayList<Block>(); 
		for (Field field : GeologicaBlocks.class.getFields()) {
			try {
				blocks.add((Block)field.get(null));
			} catch (Exception e) {
				Geologica.log.severe("Failed to get block from field '" + field.getName() + "'");
				throw new LoaderException(e);
			}
		}
		return blocks;
	}
	
	private static Block createWallBlock(CompositeBlock modelBlock) {
		return createDerivedBlock(WallBlock.class, modelBlock);
	}
	private static SlabBlock createSlabBlock(CompositeBlock modelBlock, SlabBlock singleSlab) {
		String doubleToken = singleSlab == null ? "" : "Double";
		String name = modelBlock.getUnlocalizedName().replaceFirst("tile\\.", "") + doubleToken + nameForBlockClass(SlabBlock.class);
		int id = Geologica.getConfiguration().nextBlockID(name);
		SlabBlock block = new SlabBlock(id, modelBlock, singleSlab);
		block.setUnlocalizedName(name);
		return block;
	}
	
	private static StairsBlock createStairsBlock(GeoBlock modelBlock, GeoMaterial substance) {
		String name = modelBlock.getUnlocalizedName().replaceFirst("tile\\.", "") + "Stairs" + "." + substance.getLowerName();
		int id = Geologica.getConfiguration().nextBlockID(name);
		StairsBlock block = new StairsBlock(id, modelBlock, modelBlock.getMeta(substance));
		block.setUnlocalizedName(name);
		return block;
	}
	
	private static String nameForBlockClass(Class<? extends Block> blockClass) {
		return blockClass.getSimpleName().replaceAll("Block$", "");
	}
	
	private static GeoBlock createGeoBlock(String suffix, Class<? extends GeoBlock> blockClass, Strength strength, Class<? extends IndustrialMaterial> composition, Material material) {
		GeoBlock block = null;
		String name = strength.getCamelName() + suffix;
		try {
			Constructor<? extends GeoBlock> constructor = blockClass.getConstructor(int.class, Strength.class, Class.class, Material.class);
			int id = Geologica.getConfiguration().nextBlockID(name);
			block = constructor.newInstance(id, strength, composition, material);
			block.setUnlocalizedName(name);
		} catch (Exception e) {
			Geologica.log.severe("Failed to construct GeoBlock: " + name);
			throw new LoaderException(e);
		}
		return block;
	}
	
	private static <T extends Block> T createDerivedBlock(Class<T> blockClass, CompositeBlock modelBlock) {
		T block = null;
		String name = modelBlock.getUnlocalizedName().replaceFirst("tile\\.", "") + nameForBlockClass(blockClass);
		try {
			Constructor<T> constructor = blockClass.getConstructor(int.class, CompositeBlock.class);
			int id = Geologica.getConfiguration().nextBlockID(name);
			block = constructor.newInstance(id, modelBlock);
			block.setUnlocalizedName(name);
		} catch (Exception e) {
			Geologica.log.severe("Failed to construct derived block: " + name);
			throw new LoaderException(e);
		}
		return block;
	}
}
