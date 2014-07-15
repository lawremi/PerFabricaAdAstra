package org.pfaa.geologica.block;

import cpw.mods.fml.relauncher.ReflectionHelper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;

public class VanillaOreOverrideBlock extends Block {

	
	public VanillaOreOverrideBlock(Block vanillaOre) {
		super(vanillaOre.getMaterial());
		this.setCreativeTab(vanillaOre.getCreativeTabToDisplayOn());
		this.setBlockTextureName((String)ReflectionHelper.getPrivateValue(Block.class, vanillaOre, "textureName"));
		this.blockHardness = ReflectionHelper.getPrivateValue(Block.class, vanillaOre, "blockHardness");
		this.blockResistance = ReflectionHelper.getPrivateValue(Block.class, vanillaOre, "blockResistance");
		this.stepSound = vanillaOre.stepSound;
		this.setHarvestLevel("pickaxe", (vanillaOre == Blocks.lapis_ore || vanillaOre == Blocks.iron_ore) ? 1 : 2);
	}
	
}
