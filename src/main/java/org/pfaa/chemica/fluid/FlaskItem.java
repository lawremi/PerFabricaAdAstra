package org.pfaa.chemica.fluid;

import java.util.Collections;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;

import org.pfaa.chemica.fluid.IndustrialFluid;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/* Alternative design:
 * Use a brewing API to define a custom potion for every fluid. Then, the FluidContainerRegistry
 * maps from fluid to potion (damage on the itemstack), and vice versa. 
 * Otherwise, this class may behave strangely in a brewing stand.
 */
public class FlaskItem extends ItemPotion {
	
	private Fluid getFluidForItemStack(ItemStack itemStack) {
		FluidStack fluidStack = FluidContainerRegistry.getFluidForFilledItem(itemStack);
		return fluidStack.getFluid();
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public int getColorFromItemStack(ItemStack itemStack, int renderPass) {
		if (renderPass > 0) {
			return super.getColorFromItemStack(itemStack, renderPass);
		} else {
			return this.getFluidForItemStack(itemStack).getBlock().colorMultiplier(null, 0, 0, 0); 
		}
	}

	@Override
	public List<PotionEffect> getEffects(ItemStack itemStack) {
		Fluid fluid = this.getFluidForItemStack(itemStack);
		if (fluid instanceof IndustrialFluid) {
			return ((IndustrialFluid)fluid).getProperties().hazard.getIngestionEffects();
		}
		return Collections.EMPTY_LIST;
	}

	@Override
	public List<PotionEffect> getEffects(int damage) {
		return this.getEffects(new ItemStack(this, 1, damage));
	}

	@Override
	public String getUnlocalizedName(ItemStack itemStack) {
		return this.getUnlocalizedName() + "." + this.getFluidForItemStack(itemStack).getUnlocalizedName();
	}
}
