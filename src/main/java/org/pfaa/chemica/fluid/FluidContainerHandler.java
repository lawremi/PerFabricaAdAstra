/**
 * Derived from buildcraft.energy.BucketHandler; changed to use FluidContainerRegistry
 */
package org.pfaa.chemica.fluid;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.FillBucketEvent;
import net.minecraftforge.event.entity.player.FillFluidContainerEvent;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import cpw.mods.fml.common.eventhandler.Event.Result;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public final class FluidContainerHandler {

	public static FluidContainerHandler INSTANCE = new FluidContainerHandler();

	private FluidContainerHandler() {
	}

	@SubscribeEvent
	public void onFill(FillFluidContainerEvent event) {
		ItemStack result = fillCustomContainer(event.world, event.target, event.current);

		if (result == null) {
			return;
		}

		if (event instanceof FillBucketEvent) {
		    event.world.setBlockToAir(event.target.blockX, event.target.blockY, event.target.blockZ);
		}
		
		event.result = result;
		event.setResult(Result.ALLOW);
	}

	private ItemStack fillCustomContainer(World world, MovingObjectPosition pos, ItemStack itemStack) {
		Block block = world.getBlock(pos.blockX, pos.blockY, pos.blockZ);
		Fluid fluid = FluidRegistry.lookupFluidForBlock(block);
		if (fluid != null) {
			boolean isSource = world.getBlockMetadata(pos.blockX, pos.blockY, pos.blockZ) == 0;
			if (isSource) {
			    FluidStack fluidStack = new FluidStack(fluid, FluidContainerRegistry.BUCKET_VOLUME);
	            return FluidContainerRegistry.fillFluidContainer(fluidStack, itemStack);
			}
		}
		return null;
	}
}
