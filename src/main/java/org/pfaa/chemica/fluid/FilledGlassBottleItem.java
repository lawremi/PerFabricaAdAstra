package org.pfaa.chemica.fluid;

import java.util.Collections;
import java.util.List;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;

import org.pfaa.chemica.Chemica;
import org.pfaa.chemica.fluid.IndustrialFluid;

import com.google.common.base.CaseFormat;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/* Alternative design:
 * Use a brewing API to define a custom potion for every fluid. Then, the FluidContainerRegistry
 * maps from fluid to potion (damage on the itemstack), and vice versa. 
 * Otherwise, this class may behave strangely in a brewing stand.
 */
public class FilledGlassBottleItem extends ItemPotion {
	
    public FilledGlassBottleItem() {
        this.setTextureName("potion");
    }
    
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
    @SideOnly(Side.CLIENT)
    public boolean hasEffect(ItemStack p_77636_1_)
    {
        return false;
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
	    String fluidPostfix = CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, 
	            this.getFluidForItemStack(itemStack).getName());
		return this.getUnlocalizedName() + "." + fluidPostfix;
	}

    @Override
    public String getItemStackDisplayName(ItemStack p_77653_1_)
    {
        return StatCollector.translateToLocal(this.getUnlocalizedName(p_77653_1_) + ".name").trim();
    }
}
