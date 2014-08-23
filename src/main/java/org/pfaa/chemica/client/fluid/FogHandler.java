package org.pfaa.chemica.client.fluid;

import net.minecraft.block.Block;
import net.minecraftforge.client.event.EntityViewRenderEvent.FogColors;
import net.minecraftforge.fluids.BlockFluidBase;

import org.pfaa.chemica.block.IndustrialFluidBlock;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class FogHandler {

	public static FogHandler INSTANCE = new FogHandler();

	private FogHandler() {
	}


	@SubscribeEvent
	public void onFogColors(FogColors event) {
		int x = (int)event.entity.posX;
		int y = (int)event.entity.posY;
		int z = (int)event.entity.posZ;
		Block block = event.entity.worldObj.getBlock(x, y, z);
		if (block instanceof IndustrialFluidBlock) {
			int color = ((BlockFluidBase)block).getFluid().getColor();
			event.red = (color >> 16 & 255) / 255.0F;
			event.green = (color >> 8 & 255) / 255.0F;
	        event.blue = (color & 255) / 255.0F;
		}
	}
}
