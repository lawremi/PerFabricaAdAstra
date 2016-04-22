package org.pfaa.fabrica.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class DrywallBlock extends Block {

	public DrywallBlock() {
		super(Material.wood);
		this.setCreativeTab(CreativeTabs.tabBlock);
		this.setHardness(0.7F);
		this.setResistance(5.0F);
		this.setStepSound(soundTypeWood);
		this.setHarvestLevel("axe", 0);
	}

	@Override
	public void setBlockBoundsBasedOnState(IBlockAccess world, int x, int y, int z) {
		boolean jointFilled = this.jointsFilled(world, x, y, z);
		if (jointFilled) {
			this.setBlockBoundsFull();
		} else {
			this.setBlockBoundsWithGap();
		}
	}
	
	@Override
	public void setBlockBoundsForItemRender() {
		this.setBlockBoundsWithGap();
	}

	private void setBlockBoundsFull() {
		this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
	}
	
	private void setBlockBoundsWithGap() {
		float gap = 0.025F;
		this.setBlockBounds(0.0F + gap, 0.0F + gap, 0.0F + gap, 1.0F - gap, 1.0F - gap, 1.0F - gap);
	}
	
    public boolean jointsFilled(IBlockAccess world, int x, int y, int z) {
		return world.getBlockMetadata(x, y, z) == 1;
	}
   
	public boolean fillJoints(World world, int x, int y, int z) {
		if (!this.jointsFilled(world, x, y, z)) {
			world.setBlockMetadataWithNotify(x, y, z, 1, 3);
			return true;
		}
		return false;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister par1IconRegister) {
		blockIcon = par1IconRegister.registerIcon("fabrica:drywall");
	}

	@Override
	public boolean renderAsNormalBlock() {
		return false;
	}

	@Override
	public boolean isOpaqueCube() {
		return false;
	}
	
	@Override
	public boolean shouldSideBeRendered(IBlockAccess world, int x, int y, int z, int side) {
		return !this.jointsFilled(world, x, y, z) || super.shouldSideBeRendered(world, x, y, z, side);
	}
}
