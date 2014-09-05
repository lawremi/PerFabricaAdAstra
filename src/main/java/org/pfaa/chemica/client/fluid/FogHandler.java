package org.pfaa.chemica.client.fluid;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.potion.Potion;
import net.minecraftforge.client.event.EntityViewRenderEvent.FogColors;
import net.minecraftforge.client.event.EntityViewRenderEvent.FogDensity;
import net.minecraftforge.fluids.Fluid;

import org.lwjgl.opengl.GL11;
import org.pfaa.chemica.block.IndustrialFluidBlock;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class FogHandler {

	public static FogHandler INSTANCE = new FogHandler();

	private FogHandler() {
	}


	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void onFogColors(FogColors event) {
		IndustrialFluidBlock block = IndustrialFluidBlock.atEyeLevel(event.entity);
		if (block != null) {
			int color = block.getFluid().getColor();
			event.red = (color >> 16 & 255) / 255.0F;
			event.green = (color >> 8 & 255) / 255.0F;
	        event.blue = (color & 255) / 255.0F;
		}
	}

	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void onFogDensity(FogDensity event) {
		IndustrialFluidBlock block = IndustrialFluidBlock.atEyeLevel(event.entity);
		if (block != null) {
			Fluid fluid = block.getFluid();
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
