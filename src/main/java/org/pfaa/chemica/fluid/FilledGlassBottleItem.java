package org.pfaa.chemica.fluid;

import java.util.Collection;
import java.util.List;

import org.pfaa.chemica.model.ConditionProperties;
import org.pfaa.chemica.model.Hazard;

import com.google.common.base.CaseFormat;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.resources.I18n;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

/* Alternative design:
 * Use a brewing API to define a custom potion for every fluid. Then, the FluidContainerRegistry
 * maps from fluid to potion (damage on the itemstack), and vice versa. 
 * Otherwise, this class may behave strangely in a brewing stand.
 */
public class FilledGlassBottleItem extends ItemPotion {
	
    public FilledGlassBottleItem() {
        this.setTextureName("potion");
        this.setMaxStackSize(64);
        this.setHasSubtypes(true);
    }
    
	private Fluid getFluidForItemStack(ItemStack itemStack) {
		FluidStack fluidStack = FluidContainerRegistry.getFluidForFilledItem(itemStack);
		return fluidStack.getFluid();
	}
	
	private Hazard getHazardForItemStack(ItemStack itemStack) {
		Fluid fluid = this.getFluidForItemStack(itemStack);
		ConditionProperties props = IndustrialFluids.getProperties(fluid);
		return props == null ? new Hazard(0, 0, 0) : props.hazard;
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
    	Hazard hazard = this.getHazardForItemStack(itemStack);
		return hazard.getIngestionEffects();
	}

	@Override
	public List<PotionEffect> getEffects(int damage) {
		return this.getEffects(new ItemStack(this, 1, damage));
	}

	@Override
	public String getUnlocalizedName(ItemStack itemStack) {
	    String fluidToken = CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, 
	            this.getFluidForItemStack(itemStack).getName());
		return "item." + fluidToken + "Bottle";
	}

    @Override
    public String getItemStackDisplayName(ItemStack p_77653_1_)
    {
        return StatCollector.translateToLocal(this.getUnlocalizedName(p_77653_1_) + ".name").trim();
    }

    @SuppressWarnings("unchecked")
	@Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item item, CreativeTabs tabs, @SuppressWarnings("rawtypes") List itemStacks)
    {
        Collection<Fluid> fluids = FluidRegistry.getRegisteredFluids().values();
        for (Fluid fluid : fluids) {
            FluidStack fluidStack = new FluidStack(fluid, FluidContainerRegistry.BUCKET_VOLUME);
            ItemStack filledContainer = FluidContainerRegistry.fillFluidContainer(fluidStack, FluidContainerRegistry.EMPTY_BOTTLE);
            if (filledContainer != null && filledContainer.getItem() instanceof FilledGlassBottleItem) {
                itemStacks.add(filledContainer);
            }
        }
    }

    @SuppressWarnings("unchecked")
	@Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack itemStack, EntityPlayer player, @SuppressWarnings("rawtypes") List lines, boolean p_77624_4_)
    {
        Hazard hazard = this.getHazardForItemStack(itemStack);
        lines.add(EnumChatFormatting.BLUE + I18n.format("label.hazard.health") + ": " + hazard.health);
    }
}
