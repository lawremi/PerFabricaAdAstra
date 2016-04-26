package org.pfaa.fabrica.block;

import org.pfaa.fabrica.entity.TileEntityFurnaceVenting;
import org.pfaa.fabrica.entity.TileEntityHood;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;

public class HoodBlock extends Block implements ITileEntityProvider {

	private IIcon topBottomIcon;

	public HoodBlock() {
		super(Material.rock);
	}

	@Override
	public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
		return new TileEntityHood();
	}
	
	public void fill(World world, int x, int y, int z, FluidStack gas) {
		TileEntityHood te = (TileEntityHood) world.getTileEntity(x, y, z);
		te.fill(ForgeDirection.DOWN, gas, true);
	}

	@Override
	public int onBlockPlaced(World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ, int meta) {
		this.tryToAdoptFurnace(world, x, y, z);
		return super.onBlockPlaced(world, x, y, z, side, hitX, hitY, hitZ, meta);
	}

	private void tryToAdoptFurnace(World world, int x, int y, int z) {
		Block bottomBlock = world.getBlock(x, y - 1, z);
		if (bottomBlock == Blocks.furnace || bottomBlock == Blocks.lit_furnace) {
			TileEntityFurnace te = (TileEntityFurnace) world.getTileEntity(x, y - 1, z);
			if (!(te instanceof TileEntityFurnaceVenting)) {
				world.setTileEntity(x, y - 1, z, new TileEntityFurnaceVenting(te));
			}
		}
	}

	@Override
	public void onNeighborBlockChange(World world, int x, int y, int z, Block block) {
		this.tryToAdoptFurnace(world, x, y, z);
	}
	
	@Override
	public void registerBlockIcons(IIconRegister register) {
		this.blockIcon = register.registerIcon("fabrica:hood");
		this.topBottomIcon = register.registerIcon("furnace_top");
	}

	@Override
	public IIcon getIcon(int side, int meta) {
		return (side == 0 || side == 1) ? this.topBottomIcon : this.blockIcon;
	}
}
