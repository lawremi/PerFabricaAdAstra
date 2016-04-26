package org.pfaa.fabrica.entity;

import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.IFluidHandler;
import net.minecraftforge.fluids.TileFluidHandler;

public class TileEntityHood extends TileFluidHandler implements IFluidHandler {

	public TileEntityHood() {
		this.tank = new FluidTank(FluidContainerRegistry.BUCKET_VOLUME * 20);
	}
	
	public boolean canUpdate()
    {
        return false;
    }
	
    @Override
    public boolean canFill(ForgeDirection from, Fluid fluid)
    {
        return from == ForgeDirection.DOWN;
    }

    @Override
    public boolean canDrain(ForgeDirection from, Fluid fluid)
    {
        return from != ForgeDirection.DOWN;
    }
}
