package org.pfaa.geologica.block;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import cpw.mods.fml.common.ObfuscationReflectionHelper;
import cpw.mods.fml.relauncher.ReflectionHelper;

public class VanillaOreOverrideBlock extends Block {
	public VanillaOreOverrideBlock(Block vanillaOre) {
		super(vanillaOre.getMaterial());
		this.setCreativeTab(vanillaOre.getCreativeTabToDisplayOn());
		this.setBlockTextureName((String)ReflectionHelper.getPrivateValue(Block.class, vanillaOre, 2));
		this.blockHardness = ReflectionHelper.getPrivateValue(Block.class, vanillaOre, 20);
		this.blockResistance = ReflectionHelper.getPrivateValue(Block.class, vanillaOre, 21);
		this.stepSound = vanillaOre.stepSound;
		this.setHarvestLevel("pickaxe", (vanillaOre == Blocks.lapis_ore || vanillaOre == Blocks.iron_ore) ? 1 : 2);
	}
}
