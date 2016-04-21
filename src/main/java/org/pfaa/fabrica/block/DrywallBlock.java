package org.pfaa.fabrica.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class DrywallBlock extends Block implements IDrywallBlock {

	public DrywallBlock() {
		super(Material.wood);
		this.setCreativeTab(CreativeTabs.tabBlock);
		this.setHardness(0.7F);
		this.setResistance(5.0F);
		this.setStepSound(soundTypeWood);
		this.setHarvestLevel("axe", 0);
	}

	public void setBlockBoundsBasedOnState(IBlockAccess world, int x, int y, int z) {
		boolean jointFilled = this.jointsFilled(world, x, y, z);
		float gap;
		if (jointFilled) {
			gap = 0F;
		} else {
			gap = 0.01F;
		}
		this.setBlockBounds(0.0F + gap, 0.0F + gap, 0.0F + gap, 1.0F - gap, 1.0F - gap, 1.0F - gap);
	}
	
    public boolean jointsFilled(IBlockAccess world, int x, int y, int z) {
		return world.getBlockMetadata(z, y, z) == 1;
	}
   
	public void fillJoints(World world, int x, int y, int z) {
		world.setBlockMetadataWithNotify(x, y, z, 1, 1);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister par1IconRegister) {
		blockIcon = par1IconRegister.registerIcon("fabrica:drywall");
	}
}
