package org.pfaa.fabrica.block;

import org.pfaa.fabrica.Fabrica;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import openblocks.common.block.BlockCanvas;
import openblocks.common.tileentity.TileEntityCanvas;

public class PaintableBlock extends BlockCanvas {

	private Block delegate;

	public PaintableBlock(Block delegate) {
		super(delegate.getMaterial());
		this.delegate = delegate;
		this.setStepSound(delegate.stepSound);
	}

	@Override
	public float getBlockHardness(World p_149712_1_, int p_149712_2_, int p_149712_3_, int p_149712_4_) {
		return this.delegate.getBlockHardness(p_149712_1_, p_149712_2_, p_149712_3_, p_149712_4_);
	}

	@Override
	public float getExplosionResistance(Entity p_149638_1_) {
		return this.delegate.getExplosionResistance(p_149638_1_);
	}

	@Override
	public CreativeTabs getCreativeTabToDisplayOn() {
		return this.delegate.getCreativeTabToDisplayOn();
	}
	
	@Override
	public String getHarvestTool(int metadata) {
		return this.delegate.getHarvestTool(metadata);
	}

	@Override
	public int getHarvestLevel(int metadata) {
		return this.delegate.getHarvestLevel(metadata);
	}

	@Override
	public boolean isToolEffective(String type, int metadata) {
		return this.delegate.isToolEffective(type, metadata);
	}
	
	@Override
	public void setBlockBoundsBasedOnState(IBlockAccess p_149719_1_, int p_149719_2_, int p_149719_3_,
			int p_149719_4_) {
		this.delegate.setBlockBoundsBasedOnState(p_149719_1_, p_149719_2_, p_149719_3_, p_149719_4_);
		this.minX = this.delegate.getBlockBoundsMinX();
		this.maxX = this.delegate.getBlockBoundsMaxX();
		this.minY = this.delegate.getBlockBoundsMinY();
		this.maxY = this.delegate.getBlockBoundsMaxY();
		this.minZ = this.delegate.getBlockBoundsMinZ();
		this.maxZ = this.delegate.getBlockBoundsMaxZ();
	}

	@Override
	public TileEntity createTileEntity(World world, int meta) {
		TileEntityCanvas te = (TileEntityCanvas)super.createTileEntity(world, meta);
		te.setPaintedBlockBlock(this.delegate, meta);
		return te;
	}

	@Override
	public int getRenderType() {
		return Fabrica.renderIdFull;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister par1IconRegister) {
		this.delegate.registerBlockIcons(par1IconRegister);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
    public IIcon getIcon(int p_149691_1_, int p_149691_2_)
    {
        return this.delegate.getIcon(p_149691_1_, p_149691_2_);
    }

	@Override
	public int onBlockPlaced(World world, int x, int y, int z, int side,
			float hitX, float hitY, float hitZ, int meta) {
		world.getTileEntity(x, y, z);
		return super.onBlockPlaced(world, x, y, z, side, hitX, hitY, hitZ, meta);
	}
}
