package org.pfaa.chemica.registration;

import java.util.List;

import org.pfaa.chemica.item.MaterialStack;
import org.pfaa.chemica.model.Strength;
import org.pfaa.chemica.util.ChanceStack;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public interface MaterialRecipeRegistry {
	public void registerGrindingRecipe(MaterialStack input, ItemStack output, List<ChanceStack> secondaries, Strength strength);
	public void registerCastingRecipe(ItemStack input, ItemStack output, MaterialStack flux, int temp);
	public void registerRoastingRecipe(MaterialStackList inputs, ItemStack output, int temp);
	public void registerAbsorptionRecipe(MaterialStackList inputs, FluidStack additive, ItemStack output, int temp);
	public void registerMixingRecipe(FluidStack input, MaterialStackList additives, FluidStack output, int temp);
	public void registerMixingRecipe(MaterialStackList inputs, ItemStack output);
}
