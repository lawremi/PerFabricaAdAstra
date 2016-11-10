package org.pfaa.fabrica.block;

import org.pfaa.fabrica.entity.TileEntityFluidReactor;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

public class FluidReactorBlock extends Block implements ITileEntityProvider {
	private IIcon topBottomIcon;

	protected FluidReactorBlock() {
		super(Material.iron);
	}

	@Override
	public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
		return new TileEntityFluidReactor();
	}

	@Override
	public void registerBlockIcons(IIconRegister register) {
		this.blockIcon = register.registerIcon("fabrica:fluid_reactor");
		this.topBottomIcon = register.registerIcon("furnace_top");
	}

	@Override
	public IIcon getIcon(int side, int meta) {
		return (side == 0 || side == 1) ? this.topBottomIcon : this.blockIcon;
	}
}
