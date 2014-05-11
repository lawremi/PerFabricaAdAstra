package org.pfaa.geologica;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

import org.pfaa.block.CompositeBlock;
import org.pfaa.chemica.block.FlowingBlock;
import org.pfaa.chemica.model.IndustrialMaterial;
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

import cpw.mods.fml.common.LoaderException;

public class GeologicaBlocks {
	public static final GeoBlock WEAK_STONE = createStoneBlock(Strength.WEAK);
	public static final GeoBlock MEDIUM_STONE = createStoneBlock(Strength.MEDIUM);
	public static final GeoBlock STRONG_STONE = createStoneBlock(Strength.STRONG);
	public static final GeoBlock VERY_STRONG_STONE = createStoneBlock(Strength.VERY_STRONG);
	
	public static final GeoBlock MEDIUM_COBBLE = createCobbleBlock(Strength.MEDIUM);
	public static final GeoBlock STRONG_COBBLE = createCobbleBlock(Strength.STRONG);
	public static final GeoBlock VERY_STRONG_COBBLE = createCobbleBlock(Strength.VERY_STRONG);
	
	public static final GeoBlock MEDIUM_STONE_BRICK = createStoneBrickBlock(Strength.MEDIUM);
	public static final GeoBlock STRONG_STONE_BRICK = createStoneBrickBlock(Strength.STRONG);
	public static final GeoBlock VERY_STRONG_STONE_BRICK = createStoneBrickBlock(Strength.VERY_STRONG);
	
	public static final SlabBlock MEDIUM_COBBLE_SLAB = createSlabBlock(MEDIUM_COBBLE, null);
	public static final SlabBlock MEDIUM_COBBLE_DOUBLE_SLAB = createSlabBlock(MEDIUM_COBBLE, MEDIUM_COBBLE_SLAB);
	public static final SlabBlock STRONG_COBBLE_SLAB = createSlabBlock(STRONG_COBBLE, null);
	public static final SlabBlock VERY_STRONG_COBBLE_SLAB = createSlabBlock(VERY_STRONG_COBBLE, null);
	public static final SlabBlock STRONG_COBBLE_DOUBLE_SLAB = createSlabBlock(STRONG_COBBLE, STRONG_COBBLE_SLAB);
	public static final SlabBlock VERY_STRONG_COBBLE_DOUBLE_SLAB = createSlabBlock(VERY_STRONG_COBBLE, VERY_STRONG_COBBLE_SLAB);
	
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
	
	public static final Block MEDIUM_COBBLE_WALL = createWallBlock(MEDIUM_COBBLE);
	public static final Block STRONG_COBBLE_WALL = createWallBlock(STRONG_COBBLE);
	public static final Block VERY_STRONG_COBBLE_WALL = createWallBlock(VERY_STRONG_COBBLE);
	
	public static final Block MEDIUM_STONE_BRICK_WALL = createWallBlock(MEDIUM_STONE_BRICK);
	public static final Block STRONG_STONE_BRICK_WALL = createWallBlock(STRONG_STONE_BRICK);
	public static final Block VERY_STRONG_STONE_BRICK_WALL = createWallBlock(VERY_STRONG_STONE_BRICK);
	
	public static final StairsBlock MEDIUM_COBBLE_STAIRS__LIMESTONE = createStairsBlock(MEDIUM_COBBLE, GeoMaterial.LIMESTONE);
	public static final StairsBlock STRONG_COBBLE_STAIRS__GRANITE = createStairsBlock(STRONG_COBBLE, GeoMaterial.GRANITE);
	public static final StairsBlock STRONG_COBBLE_STAIRS__MARBLE = createStairsBlock(STRONG_COBBLE, GeoMaterial.MARBLE);
	
	public static final StairsBlock MEDIUM_STONE_BRICK_STAIRS__LIMESTONE = createStairsBlock(MEDIUM_STONE_BRICK, GeoMaterial.LIMESTONE);
	public static final StairsBlock STRONG_STONE_BRICK_STAIRS__GRANITE = createStairsBlock(STRONG_STONE_BRICK, GeoMaterial.GRANITE);
	public static final StairsBlock STRONG_STONE_BRICK_STAIRS__MARBLE = createStairsBlock(STRONG_STONE_BRICK, GeoMaterial.MARBLE);
	
	public static final GeoBlock WEAK_RUBBLE = createRubbleBlock(Strength.WEAK);
	
	public static final GeoBlock WEAK_ORE_SAND = createOreSandBlock();
	
	public static final GeoBlock WEAK_ORE_ROCK = createOreRockBlock(Strength.WEAK);
	public static final GeoBlock MEDIUM_ORE_ROCK = createOreRockBlock(Strength.MEDIUM);
	public static final GeoBlock STRONG_ORE_ROCK = createOreRockBlock(Strength.STRONG);
	//public static final GeoBlock VERY_STRONG_ORE_ROCK = createOreRockBlock(Strength.VERY_STRONG);

	public static final GeoBlock WEAK_CLAY = createClayBlock();
	public static final GeoBlock WEAK_ORE_CLAY = createOreClayBlock();
	public static final GeoBlock WEAK_CLAY_BRICK = createClayBrickBlock();
	
	public static final FlowingBlock LIGHT_OIL = createFlowingBlock(GeoMaterial.LIGHT_OIL); 
	public static final FlowingBlock HEAVY_OIL = createFlowingBlock(GeoMaterial.HEAVY_OIL);
	
	static {
		//WEAK_STONE.addChanceDrop(GeoSubstance.CONGLOMERATE, new ChanceDrop().add(0.05, Item.goldNugget).add(0.05, "nuggetCopper"));
	}
	
	private static GeoBlock createStoneBlock(Strength strength) {
		return createGeoBlock(IntactGeoBlock.class, strength, Aggregate.class, Material.rock);
	}
	private static GeoBlock createCobbleBlock(Strength strength) {
		return createGeoBlock(BrokenGeoBlock.class, strength, Aggregate.class, Material.rock);
	}
	private static GeoBlock createStoneBrickBlock(Strength strength) {
		return createGeoBlock(BrickGeoBlock.class, strength, Aggregate.class, Material.rock);
	}
	private static GeoBlock createRubbleBlock(Strength strength) {
		return createGeoBlock(LooseGeoBlock.class, strength, Aggregate.class, Material.rock);
	}
	private static GeoBlock createOreSandBlock() {
		return createGeoBlock(LooseGeoBlock.class, Strength.WEAK, Ore.class, Material.sand);
	}
	private static GeoBlock createOreRockBlock(Strength strength) {
		return createGeoBlock(IntactGeoBlock.class, strength, Ore.class, Material.rock);
	}
	private static GeoBlock createClayBlock() {
		return createGeoBlock(IntactGeoBlock.class, Strength.WEAK, Aggregate.class, Material.clay);
	}
	private static GeoBlock createOreClayBlock() {
		return createGeoBlock(IntactGeoBlock.class, Strength.WEAK, Ore.class, Material.clay);
	}
	private static GeoBlock createClayBrickBlock() {
		return createGeoBlock(BrickGeoBlock.class, Strength.WEAK, Aggregate.class, Material.clay);
	}
	
	public static List<Block> getBlocks() {
		List<Block> blocks = new ArrayList<Block>(); 
		for (Field field : GeologicaBlocks.class.getFields()) {
			try {
				blocks.add((Block)field.get(null));
			} catch (Exception e) {
				Geologica.log.fatal("Failed to get block from field '" + field.getName() + "'");
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
		SlabBlock block = new SlabBlock(modelBlock, singleSlab);
		return block;
	}
	
	private static StairsBlock createStairsBlock(GeoBlock modelBlock, GeoMaterial substance) {
		StairsBlock block = new StairsBlock(modelBlock, modelBlock.getMeta(substance));
		return block;
	}
	
	private static GeoBlock createGeoBlock(Class<? extends GeoBlock> blockClass, Strength strength, Class<? extends IndustrialMaterial> composition, Material material) {
		GeoBlock block = null;
		try {
			Constructor<? extends GeoBlock> constructor = blockClass.getConstructor(Strength.class, Class.class, Material.class);
			block = constructor.newInstance(strength, composition, material);
		} catch (Exception e) {
			Geologica.log.fatal("Failed to construct GeoBlock");
			throw new LoaderException(e);
		}
		return block;
	}
	
	private static <T extends Block> T createDerivedBlock(Class<T> blockClass, CompositeBlock modelBlock) {
		T block = null;
		try {
			Constructor<T> constructor = blockClass.getConstructor(CompositeBlock.class);
			block = constructor.newInstance(modelBlock);
		} catch (Exception e) {
			Geologica.log.fatal("Failed to construct derived block");
			throw new LoaderException(e);
		}
		return block;
	}
	
	private static FlowingBlock createFlowingBlock(GeoMaterial material) {
		String name = material.getLowerName() + "Flowing";
		FlowingBlock block = new FlowingBlock(material.getBlockMaterial());
		block.setBlockTextureName("geologica:" + material.getLowerName());
		return block;
	}
}
