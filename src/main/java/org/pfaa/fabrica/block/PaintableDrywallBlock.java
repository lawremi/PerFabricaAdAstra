package org.pfaa.fabrica.block;

import org.pfaa.fabrica.Fabrica;
import org.pfaa.fabrica.entity.TileEntityColored;

import net.minecraft.block.ITileEntityProvider;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import openblocks.api.IPaintableBlock;
import openmods.utils.ColorUtils;
import openmods.utils.ColorUtils.ColorMeta;

public class PaintableDrywallBlock extends DrywallBlock implements IPaintableBlock, ITileEntityProvider {
	
	private boolean renderAllSides;
	private int renderSide;
	
	public void setRenderSide(int renderSide) {
		this.renderAllSides = false;
		this.renderSide = renderSide;
	}
	
	public void renderAllSides() {
		this.renderAllSides = true;                                                                                                                                                                                                                                                                                                                                                                                                                    
	}

	@Override
	public boolean shouldSideBeRendered(IBlockAccess world, int x, int y, int z, int side) {
		return (this.renderAllSides || this.renderSide == side) && super.shouldSideBeRendered(world, x, y, z, side);
	}

	@Override
	public int colorMultiplier(IBlockAccess world, int x, int y, int z) {
		final TileEntity te = world.getTileEntity(x, y, z);
		return (te instanceof TileEntityColored)? ((TileEntityColored)te).getColor(this.renderSide) : 
			super.colorMultiplier(world, x, y, z);
	}

	@Override
	public boolean recolourBlockRGB(World world, int x, int y, int z, ForgeDirection side, int color) {
		final TileEntity te = world.getTileEntity(x, y, z);
		return (te instanceof TileEntityColored)? ((TileEntityColored)te).setColor(color, side) : false;
	}

	@Override
	public boolean recolourBlock(World world, int x, int y, int z, ForgeDirection side, int colour) {
		ColorMeta color = ColorUtils.vanillaBlockToColor(colour);
		return recolourBlockRGB(world, x, y, z, side, color.rgb);
	}
	
	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileEntityColored();
	}

	@Override
	public int getRenderType() {
		return Fabrica.renderIdFull;
	}
}
