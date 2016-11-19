package org.pfaa.fabrica.recipe;

import java.util.Set;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public class FluidReactorRecipe {
	public final FluidStack inputA;
	public final FluidStack inputB;
	public final int energy;
	public final Set<ItemStack> catalysts;
	
	public final FluidStack liquidOutput;
	public final FluidStack gasOutput;
	
	public FluidReactorRecipe(FluidStack inputA, FluidStack inputB, int energy, Set<ItemStack> catalyst, 
			FluidStack liquidOutput, FluidStack gasOutput) {
		super();
		this.inputA = inputA;
		this.inputB = inputB;
		this.energy = energy;
		this.catalysts = catalyst;
		this.liquidOutput = liquidOutput;
		this.gasOutput = gasOutput;
	}

	public FluidReactorRecipe reverseInputs() {
		return new FluidReactorRecipe(this.inputB, this.inputA, this.energy, this.catalysts, this.liquidOutput, this.gasOutput);
	}
}
