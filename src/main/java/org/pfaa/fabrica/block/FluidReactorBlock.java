package org.pfaa.fabrica.block;

import org.pfaa.fabrica.Fabrica;
import org.pfaa.fabrica.entity.TileEntityFluidReactor;

import net.minecraft.block.Block;
import net.minecraft.block.BlockCauldron;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;

public class FluidReactorBlock extends BlockCauldron implements ITileEntityProvider {
	
	public FluidReactorBlock() {
		super();
	}

	@Override
	public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
		return new TileEntityFluidReactor();
	}

	@Override
	public int getRenderType() {
		return Fabrica.registrant.getFluidReactorRendererId();
	}
	
	@Override
	public void onNeighborBlockChange(World world, int x, int y, int z, Block block) {
		this.tryToVentGas(world, x, y, z);
	}
	
	private void tryToVentGas(World world, int x, int y, int z) {
		if (!hasHood(world, x, y, z)) {
			TileEntityFluidReactor te = (TileEntityFluidReactor)world.getTileEntity(x, y, z);
			te.flush(false);
		}
	}
	
	private boolean hasHood(World world, int x, int y, int z) {
		return world.getBlock(x, y + 1, z) instanceof HoodBlock;
	}

	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
        if (world.isRemote) {
            return true;
        }
        ItemStack item = player.inventory.getCurrentItem();
        TileEntityFluidReactor te = (TileEntityFluidReactor)world.getTileEntity(x, y, z);
        if (item == null) {
        	boolean gotCatalyst = player.inventory.addItemStackToInventory(te.getCatalyst());
        	if (gotCatalyst) {
        		te.setCatalyst(null);
        	}
            return true;
        } else {
        	if (FluidContainerRegistry.isEmptyContainer(item)) {
        		if (player.isSneaking()) {
        			te.flush(true);
        			return true;
        		}
        		int capacity = FluidContainerRegistry.getContainerCapacity(item);
        		FluidStack fluid = te.drain(ForgeDirection.DOWN, capacity, false);
        		if (fluid.amount == capacity) {
        			te.drain(ForgeDirection.DOWN, capacity, true);
        			ItemStack filledContainer = FluidContainerRegistry.fillFluidContainer(fluid, item);
            		player.inventory.setInventorySlotContents(player.inventory.currentItem, filledContainer);
        		}
        	} else if (FluidContainerRegistry.isFilledContainer(item)) {
        		int capacity = FluidContainerRegistry.getContainerCapacity(item);
        		FluidStack fluid = FluidContainerRegistry.getFluidForFilledItem(item);
        		int amount = te.fill(ForgeDirection.UNKNOWN, fluid, false);
        		if (amount == capacity) {
        			te.fill(ForgeDirection.UNKNOWN, fluid, true);
        			ItemStack drainedContainer = FluidContainerRegistry.drainFluidContainer(item);
            		player.inventory.setInventorySlotContents(player.inventory.currentItem, drainedContainer);
        		}
        	} else {
        		te.setCatalyst(item);
        	}
        }
		
		return true;
	}
	
	// TODO: when destroyed, drop catalyst

	
}
