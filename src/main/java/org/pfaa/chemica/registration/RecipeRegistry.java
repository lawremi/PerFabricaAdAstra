package org.pfaa.chemica.registration;

import java.util.List;

import org.pfaa.chemica.model.Strength;
import org.pfaa.chemica.processing.TemperatureLevel;
import org.pfaa.chemica.util.ChanceStack;

import net.minecraft.item.ItemStack;

public interface RecipeRegistry {
	public void registerCastingRecipe(ItemStack input, ItemStack output, int temp);
	public void registerSmeltingRecipe(ItemStack input, ItemStack output, ItemStack flux, TemperatureLevel temp);
	public void registerGrindingRecipe(ItemStack input, ItemStack output, List<ChanceStack> secondaries, Strength strength);
	public void registerCrushingRecipe(ItemStack input, ItemStack output, Strength strength);
}
