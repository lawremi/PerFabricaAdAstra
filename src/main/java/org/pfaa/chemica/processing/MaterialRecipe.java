package org.pfaa.chemica.processing;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.pfaa.chemica.model.Condition;
import org.pfaa.chemica.model.State;
import org.pfaa.chemica.registration.IngredientList;

public class MaterialRecipe {
	private IngredientList<MaterialStack> inputs;
	private IngredientList<MaterialStack> outputs;
	private IngredientList<MaterialStack> modulators;
	
	private int energy;
	private Condition condition;
	
	private static final Collector<MaterialStack, ?, IngredientList<MaterialStack>> TO_INGREDIENTS = 
			Collector.of(IngredientList::new, (r, t) -> r.add(t), (a, b) -> { a.addAll(b); return a; });
	
	protected MaterialRecipe() { }
	
	protected MaterialRecipe(MaterialRecipe other) {
		this.inputs = other.inputs;
		this.outputs = other.outputs;
		this.modulators = other.modulators;
		this.energy = other.energy;
		this.condition = other.condition;
	}
	
	public IngredientList<MaterialStack> getInputs() {
		return this.inputs;
	}

	public IngredientList<MaterialStack> getSolidInputs() {
		return this.getInputs().stream().filter((stack) -> stack.getState() == State.SOLID).collect(TO_INGREDIENTS);
	}
	
	public IngredientList<MaterialStack> getFluidInputs() {
		return this.getInputs().stream().filter((stack) -> stack.getState().isFluid()).collect(TO_INGREDIENTS);
	}
	
	public MaterialStack getInput() {
		return this.inputs.get(0);
	}
	
	public List<MaterialStack> getOutputs() {
		return this.outputs;
	}
	
	public List<MaterialStack> getOutputs(State state) {
		return this.getOutputs().stream().filter((stack) -> stack.getState() == state).collect(Collectors.toList());
	}
	
	public MaterialStack getOutput() {
		return this.outputs.get(0);
	}
	
	public Optional<MaterialStack> getOutput(State state) {
		List<MaterialStack> outputs = this.getOutputs(state);
		return Optional.ofNullable(outputs.size() > 0 ? outputs.get(0) : null);
	}
	
	public IngredientList<MaterialStack> getModulators() {
		return this.modulators;
	}
	
	public int getEnergy() {
		return this.energy;
	}

	public Condition getCondition() {
		return this.condition;
	}
	
	public int getTemperature() {
		return this.getCondition().temperature;
	}

	public MaterialRecipe to(MaterialStack... stacks) {
		return this.to(Arrays.asList(stacks));
	}
	
	public MaterialRecipe to(List<MaterialStack> stacks) {
		this.outputs = IngredientList.of(stacks);
		return this;
	}

	public MaterialRecipe via(MaterialStack... stacks) {
		return this.via(Arrays.asList(stacks));
	}
	
	public MaterialRecipe via(List<MaterialStack> stacks) {
		this.modulators = IngredientList.of(stacks);
		return this;
	}
	
	public MaterialRecipe given(int energy) {
		this.energy = energy;
		return this;
	}
	
	public MaterialRecipe at(Condition condition) {
		this.condition = condition;
		return this;
	}
	
	public MaterialRecipe reverse() {
		MaterialRecipe reversed = new MaterialRecipe(this);
		reversed.inputs = this.outputs;
		reversed.outputs = this.inputs;
		reversed.energy = -this.energy;
		return reversed;
	}
	
	public static MaterialRecipe converts(List<MaterialStack> stacks) {
		MaterialRecipe recipe = new MaterialRecipe();
		recipe.inputs = IngredientList.of(stacks);
		return recipe;
	}
	
	public static MaterialRecipe converts(MaterialStack... stacks) {
		return converts(Arrays.asList(stacks));
	}
}
