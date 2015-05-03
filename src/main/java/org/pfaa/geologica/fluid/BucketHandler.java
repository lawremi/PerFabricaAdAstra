/**
 * Derived from buildcraft.energy.BucketHandler; changed to use FluidContainerRegistry
 */
package org.pfaa.geologica.fluid;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.FillBucketEvent;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import cpw.mods.fml.common.eventhandler.Event.Result;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public final class BucketHandler {

	public static BucketHandler INSTANCE = new BucketHandler();

	private BucketHandler() {
	}

	@SubscribeEvent
	public void onBucketFill(FillBucketEvent event) {
		ItemStack result = fillCustomBucket(event.world, event.target, event.current);

		if (result == null) {
			return;
		}

		event.result = result;
		event.setResult(Result.ALLOW);
	}

	private ItemStack fillCustomBucket(World world, MovingObjectPosition pos, ItemStack container) {
		Block block = world.getBlock(pos.blockX, pos.blockY, pos.blockZ);
		Fluid fluid = FluidRegistry.lookupFluidForBlock(block);
		if (fluid != null) {
			boolean isSource = world.getBlockMetadata(pos.blockX, pos.blockY, pos.blockZ) == 0;
			if (!isSource) {
				return null;
			}
			FluidStack fluidStack = new FluidStack(fluid, FluidContainerRegistry.BUCKET_VOLUME);
			ItemStack bucket = FluidContainerRegistry.fillFluidContainer(fluidStack, container);
			if (bucket != null) {
				world.setBlockToAir(pos.blockX, pos.blockY, pos.blockZ);
				return bucket;
			}
		}
		return null;
	}
}
