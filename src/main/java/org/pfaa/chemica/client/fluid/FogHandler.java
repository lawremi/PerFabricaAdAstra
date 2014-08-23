package org.pfaa.chemica.client.fluid;

import net.minecraft.block.Block;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.potion.Potion;
import net.minecraftforge.client.event.EntityViewRenderEvent.FogColors;
import net.minecraftforge.client.event.EntityViewRenderEvent.FogDensity;
import net.minecraftforge.fluids.BlockFluidBase;
import net.minecraftforge.fluids.Fluid;

import org.lwjgl.opengl.GL11;
import org.pfaa.chemica.block.IndustrialFluidBlock;

import codechicken.lib.math.MathHelper;

import cpw.mods.fml.common.eventhandler.Event.Result;
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

	@SubscribeEvent
	public void onFogDensity(FogDensity event) {
		int x = (int)MathHelper.floor_double(event.entity.posX);
		int y = (int)MathHelper.floor_double(event.entity.posY);
		int z = (int)MathHelper.floor_double(event.entity.posZ);
		Block block = event.entity.worldObj.getBlock(x, y, z);
		if (block instanceof IndustrialFluidBlock) {
			Fluid fluid = ((BlockFluidBase)block).getFluid();
			GL11.glFogi(GL11.GL_FOG_MODE, GL11.GL_EXP);
			if (!fluid.isGaseous() && event.entity.isPotionActive(Potion.waterBreathing))
			{
				event.density = 0.05F;
			}
			else
			{
				event.density = 0.1F - (float)EnchantmentHelper.getRespiration(event.entity) * 0.03F;
			}
			event.setCanceled(true);
		}
	}
    
}
