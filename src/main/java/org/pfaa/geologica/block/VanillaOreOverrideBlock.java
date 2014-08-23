package org.pfaa.geologica.block;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import cpw.mods.fml.common.ObfuscationReflectionHelper;
import cpw.mods.fml.relauncher.ReflectionHelper;

public class VanillaOreOverrideBlock extends Block {
	public VanillaOreOverrideBlock(Block vanillaOre) {
		super(vanillaOre.getMaterial());
		this.setCreativeTab(vanillaOre.getCreativeTabToDisplayOn());
		this.setBlockTextureName((String)ObfuscationReflectionHelper.getPrivateValue(Block.class, vanillaOre, "textureName"));
		this.blockHardness = ObfuscationReflectionHelper.getPrivateValue(Block.class, vanillaOre, "blockHardness");
		this.blockResistance = ObfuscationReflectionHelper.getPrivateValue(Block.class, vanillaOre, "blockResistance");
		this.stepSound = vanillaOre.stepSound;
		this.setHarvestLevel("pickaxe", (vanillaOre == Blocks.lapis_ore || vanillaOre == Blocks.iron_ore) ? 1 : 2);
	}
}
