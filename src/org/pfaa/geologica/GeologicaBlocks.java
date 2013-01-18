package org.pfaa.geologica;

import java.lang.reflect.Constructor;
import java.util.logging.Level;

import net.minecraft.block.Block;

import org.pfaa.ConfigIDProvider;
import org.pfaa.block.CompositeBlock;
import org.pfaa.geologica.GeoSubstance.Strength;
import org.pfaa.geologica.block.BrickBlock;
import org.pfaa.geologica.block.CobblestoneBlock;
import org.pfaa.geologica.block.GeoBlock;
import org.pfaa.geologica.block.GeoBlockAccessors;
import org.pfaa.geologica.block.SlabBlock;
import org.pfaa.geologica.block.StairsBlock;
import org.pfaa.geologica.block.StoneBlock;
import org.pfaa.geologica.block.WallBlock;

import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.LoaderException;

public class GeologicaBlocks {
	public static final GeoBlockAccessors WEAK_STONE = createStoneBlock(Strength.WEAK);
	public static final GeoBlockAccessors MEDIUM_STONE = createStoneBlock(Strength.MEDIUM);
	public static final GeoBlockAccessors STRONG_STONE = createStoneBlock(Strength.STRONG);
	public static final GeoBlockAccessors VERY_STRONG_STONE = createStoneBlock(Strength.VERY_STRONG);
	
	public static final GeoBlock MEDIUM_COBBLESTONE = createCobbleBlock(Strength.MEDIUM);
	public static final GeoBlock STRONG_COBBLESTONE = createCobbleBlock(Strength.STRONG);
	
	public static final GeoBlock MEDIUM_BRICK = createBrickBlock(Strength.MEDIUM);
	public static final GeoBlock STRONG_BRICK = createBrickBlock(Strength.STRONG);
	public static final GeoBlock VERY_STRONG_BRICK = createBrickBlock(Strength.VERY_STRONG);
	
	public static final SlabBlock MEDIUM_COBBLE_SLAB = createSlabBlock(MEDIUM_COBBLESTONE, null);
	public static final SlabBlock MEDIUM_COBBLE_DOUBLE_SLAB = createSlabBlock(MEDIUM_COBBLESTONE, MEDIUM_COBBLE_SLAB);
	public static final SlabBlock STRONG_COBBLE_SLAB = createSlabBlock(STRONG_COBBLESTONE, null);
	public static final SlabBlock STRONG_COBBLE_DOUBLE_SLAB = createSlabBlock(STRONG_COBBLESTONE, STRONG_COBBLE_SLAB);
	
	public static final SlabBlock MEDIUM_BRICK_SLAB = createSlabBlock(MEDIUM_BRICK, null);
	public static final SlabBlock MEDIUM_BRICK_DOUBLE_SLAB = createSlabBlock(MEDIUM_BRICK, MEDIUM_BRICK_SLAB);
	public static final SlabBlock STRONG_BRICK_SLAB = createSlabBlock(STRONG_BRICK, null);
	public static final SlabBlock STRONG_BRICK_DOUBLE_SLAB = createSlabBlock(STRONG_BRICK, STRONG_BRICK_SLAB);
	public static final SlabBlock VERY_STRONG_BRICK_SLAB = createSlabBlock(VERY_STRONG_BRICK, null);
	public static final SlabBlock VERY_STRONG_BRICK_DOUBLE_SLAB = createSlabBlock(VERY_STRONG_BRICK, VERY_STRONG_BRICK_SLAB);
	
	public static final Block MEDIUM_COBBLE_WALL = createWallBlock(MEDIUM_COBBLESTONE);
	public static final Block STRONG_COBBLE_WALL = createWallBlock(STRONG_COBBLESTONE);
	
	public static final StairsBlock LIMESTONE_COBBLE_STAIRS = createStairsBlock(MEDIUM_COBBLESTONE, GeoSubstance.LIMESTONE);
	public static final StairsBlock GRANITE_COBBLE_STAIRS = createStairsBlock(STRONG_COBBLESTONE, GeoSubstance.GRANITE);
	public static final StairsBlock MARBLE_COBBLE_STAIRS = createStairsBlock(STRONG_COBBLESTONE, GeoSubstance.MARBLE);
	
	public static final StairsBlock LIMESTONE_BRICK_STAIRS = createStairsBlock(MEDIUM_BRICK, GeoSubstance.LIMESTONE);
	public static final StairsBlock GRANITE_BRICK_STAIRS = createStairsBlock(STRONG_BRICK, GeoSubstance.GRANITE);
	public static final StairsBlock MARBLE_BRICK_STAIRS = createStairsBlock(STRONG_BRICK, GeoSubstance.MARBLE);
	
	//public static final StairsBlock[] stairsBlocks = createStairsBlocks();
	
	private static GeoBlockAccessors createStoneBlock(Strength strength) {
		return createGeoBlock(StoneBlock.class, strength);
	}
	private static GeoBlock createCobbleBlock(Strength strength) {
		return createGeoBlock(CobblestoneBlock.class, strength);
	}
	private static GeoBlock createBrickBlock(Strength strength) {
		return createGeoBlock(BrickBlock.class, strength);
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
	/*
	private static StairsBlock[] createStairsBlocks() {
		List<StairsBlock> blocks = new ArrayList<StairsBlock>();
		blocks.addAll(createStairsBlocksForGeoBlock(MEDIUM_COBBLESTONE));
		blocks.addAll(createStairsBlocksForGeoBlock(STRONG_COBBLESTONE));
		blocks.addAll(createStairsBlocksForGeoBlock(MEDIUM_BRICK));
		blocks.addAll(createStairsBlocksForGeoBlock(STRONG_BRICK));
		blocks.addAll(createStairsBlocksForGeoBlock(VERY_STRONG_BRICK));
		return blocks.toArray(new StairsBlock[] { });
	}
	private static List<? extends StairsBlock> createStairsBlocksForGeoBlock(GeoBlock modelBlock) {
		List<StairsBlock> blocks = new ArrayList<StairsBlock>();
		List<ItemStack> subStacks = new ArrayList<ItemStack>();
		modelBlock.getSubBlocks(modelBlock.blockID, null, subStacks);
		for(int meta = 0; meta < subStacks.size(); meta++) {
			String name = modelBlock.getBlockName().replaceFirst("tile\\.", "") + "Stairs" + "." + modelBlock.getSubstance(meta).getLowerName();
			int id = ConfigIDProvider.getInstance().nextBlockID(name);
			StairsBlock block = new StairsBlock(id, modelBlock, meta);
			block.setBlockName(name);
		}
		return blocks;
	}
	*/
	
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
	
	private static GeoBlock createGeoBlock(Class<? extends GeoBlock> blockClass, Strength strength) {
		GeoBlock block = null;
		try {
			Constructor<? extends GeoBlock> constructor = blockClass.getConstructor(int.class, Strength.class);
			String name = strength.getCamelName() + nameForBlockClass(blockClass);
			int id = ConfigIDProvider.getInstance().nextBlockID(name);
			block = constructor.newInstance(id, strength);
			block.setBlockName(name);
		} catch (Exception e) {
			FMLLog.log(Level.SEVERE, e, "Failed to construct GeoBlock");
			throw new LoaderException(e);
		}
		return block;
	}
	
	private static Block createDerivedBlock(Class<? extends Block> blockClass, CompositeBlock modelBlock) {
		Block block = null;
		try {
			Constructor<? extends Block> constructor = blockClass.getConstructor(int.class, CompositeBlock.class);
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
