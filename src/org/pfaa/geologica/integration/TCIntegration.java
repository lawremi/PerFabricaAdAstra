package org.pfaa.geologica.integration;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;

import org.pfaa.geologica.GeologicaBlocks;

import tconstruct.library.TConstructRegistry;
import tconstruct.library.tools.ToolMaterial;

public class TCIntegration {
	private static final int STONE_ID = 1;
	private static final int STONE_HARVEST_LEVEL = 1;
	
	public static void addStoneMaterials() {
		addStoneMaterial(50, GeologicaBlocks.WEAK_STONE, 0.25, 0.5);
		addStoneMaterial(51, GeologicaBlocks.MEDIUM_STONE, 0.50, 0.8);
		addStoneMaterial(52, GeologicaBlocks.STRONG_STONE, 1.0, 1.0);
		addStoneMaterial(53, GeologicaBlocks.VERY_STRONG_STONE, 1.5, 1.2);
	}
	
	public static void addStoneMaterial(int materialID, Block stone, double durabilityMultiplier, double speedMultiplier) {
		ToolMaterial baseStone = TConstructRegistry.getMaterial(STONE_ID);
		TConstructRegistry.addToolMaterial(materialID, 
				stone.getUnlocalizedName(), STONE_HARVEST_LEVEL, 
				(int)(baseStone.durability * durabilityMultiplier), 
				(int)(baseStone.miningspeed * speedMultiplier), 
				baseStone.attack, baseStone.handleModifier, 
				0 /* reinforced */, 1 /* stonebound */, 
				baseStone.style(), baseStone.ability);
		Set<List<Integer>> patternKeys = new HashSet(TConstructRegistry.patternPartMapping.keySet());
		for (List<Integer> key : patternKeys) {
			int keyMaterialID = key.get(2);
			if (keyMaterialID == STONE_ID) {
				int patternID = key.get(0);
				int patternMeta = key.get(1);
				ItemStack output = TConstructRegistry.patternPartMapping.get(key);
				TConstructRegistry.addPartMapping(patternID, patternMeta, materialID, output);
			}
		}
	}
}
