package org.pfaa.chemica.registration;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.pfaa.chemica.fluid.IndustrialFluids;
import org.pfaa.chemica.item.IndustrialItems;
import org.pfaa.chemica.model.Compound.Compounds;
import org.pfaa.chemica.model.Condition;
import org.pfaa.chemica.model.Equation.Term;
import org.pfaa.chemica.model.Mixture;
import org.pfaa.chemica.model.MixtureComponent;
import org.pfaa.chemica.model.Reaction;
import org.pfaa.chemica.model.SimpleMixture;
import org.pfaa.chemica.model.State;
import org.pfaa.chemica.model.Strength;
import org.pfaa.chemica.processing.Alloying;
import org.pfaa.chemica.processing.Combination;
import org.pfaa.chemica.processing.Communition;
import org.pfaa.chemica.processing.Compaction;
import org.pfaa.chemica.processing.EnthalpyChange;
import org.pfaa.chemica.processing.Form;
import org.pfaa.chemica.processing.Form.Forms;
import org.pfaa.core.item.ChanceStack;
import org.pfaa.chemica.processing.MaterialRecipe;
import org.pfaa.chemica.processing.MaterialStack;
import org.pfaa.chemica.processing.Separation;
import org.pfaa.chemica.processing.Smelting;
import org.pfaa.chemica.processing.Stacking;

import com.google.common.collect.Lists;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

public class DefaultConversionRegistry implements ConversionRegistry {

	private RecipeRegistry delegate;
	private GenericRecipeRegistry genericDelegate;
	
	public DefaultConversionRegistry(RecipeRegistry delegate) {
		this.delegate = delegate;
		this.genericDelegate = delegate.getGenericRecipeRegistry();
	}

	private void stack(MaterialRecipe recipe) {
		RecipeUtils.addShapelessRecipe(recipe.getOutput().getBestItemStack(), 
				recipe.getInput().getBestItemStack());
	}
	
	@Override
	public void register(Stacking stacking) {
		stacking.getRecipes().forEach(this::stack);
	}
	
	@Override
	public void register(Separation separation) {
		Separation.Type type = separation.getType();
		Consumer<MaterialRecipe> consumer = null;
		if (type == Separation.Types.PRECIPITATION)
			consumer = this::precipitate;
		separation.getRecipes().forEach(consumer);
	}

	private void precipitate(MaterialRecipe recipe) {
		delegate.registerPrecipitationRecipe(
				recipe.getInput().getFluidStack(), 
				recipe.getOutput().getBestItemStack(), 
				recipe.getEnergy());
	}

	private void smelt(MaterialRecipe recipe) {
		ItemStack input = recipe.getInput().getBestItemStack();
		MaterialStack flux = recipe.getInputs().size() > 1 ? recipe.getInputs().get(1) : null;
		int temp = recipe.getTemperature();
		if (recipe.getOutput().getState().isFluid()) {
			FluidStack output = recipe.getOutput().getFluidStack();
			this.genericDelegate.registerSmeltingRecipe(input, output, flux, temp);
		} else {
			ItemStack output = recipe.getOutput().getBestItemStack();
			this.genericDelegate.registerSmeltingRecipe(input, output, flux, temp);
		}
	}
	
	@Override
	public void register(Smelting smelting) {
		smelting.getRecipes().forEach(this::smelt);
	}


	private void alloy(MaterialRecipe recipe) {
		if (recipe.getOutput().getState().isFluid()) {
			List<FluidStack> inputs = recipe.getInputs().stream().
					map(IndustrialFluids::getFluidStack).collect(Collectors.toList());
			FluidStack output = IndustrialFluids.getFluidStack(recipe.getOutput());
			this.delegate.registerAlloyingRecipe(output, inputs);
		} else {
			List<ItemStack> inputs = recipe.getInputs().stream().
					map(IndustrialItems::getBestItemStack).collect(Collectors.toList());
			ItemStack base = inputs.get(0);
			List<ItemStack> solutes = inputs.subList(1, inputs.size());
			ItemStack output = IndustrialItems.getBestItemStack(recipe.getOutput());
			this.delegate.registerAlloyingRecipe(output, base, solutes, recipe.getTemperature(), recipe.getEnergy());
		}
	}
	
	@Override
	public void register(Alloying alloying) {
		alloying.getRecipes().forEach(this::alloy);
	}

	private void combine(MaterialRecipe recipe) {
		IngredientList<MaterialStack> solidInputs = recipe.getSolidInputs();
		IngredientList<MaterialStack> fluidInputs = recipe.getFluidInputs();
		FluidStack fluidInput = fluidInputs.size() > 0 ? fluidInputs.get(0).getFluidStack() : null;
		FluidStack fluidInput2 = fluidInputs.size() > 1 ? fluidInputs.get(1).getFluidStack() : null;
		ItemStack solidOutput = recipe.getOutput(State.SOLID).map(MaterialStack::getBestItemStack).orElse(null);
		FluidStack liquidOutput = recipe.getOutput(State.LIQUID).map(MaterialStack::getFluidStack).orElse(null);
		FluidStack gasOutput = recipe.getOutput(State.GAS).map(MaterialStack::getFluidStack).orElse(null);
		IngredientList<MaterialStack> catalysts = recipe.getModulators();
		Condition condition = recipe.getCondition();
		this.genericDelegate.registerMixingRecipe(solidInputs, fluidInput, fluidInput2, 
				solidOutput, liquidOutput, gasOutput, 
				condition, catalysts);
	}
	
	@Override
	public void register(Combination combination) {
		combination.getRecipes().forEach(this::combine);
	}

	private void communite(MaterialRecipe recipe) {
		ItemStack input = IndustrialItems.getBestItemStack(recipe.getInput());
		List<MaterialStack> outputs = recipe.getOutputs();
		ItemStack primaryOutput = IndustrialItems.getBestItemStack(outputs.get(0));
		Stream<ChanceStack> secondaries = outputs.stream().skip(1).map(IndustrialItems::getChanceStack);
		Strength strength = recipe.getInput().getMaterial().getStrength();
		if (recipe.getOutput().getForm().isGranular()) {
			this.delegate.registerGrindingRecipe(input, primaryOutput, secondaries.collect(Collectors.toList()), strength);
		} else {
			this.delegate.registerCrushingRecipe(input, primaryOutput, secondaries.findFirst().orElse(null), strength);
		}
	}
	
	@Override
	public void register(Communition communition) {
		communition.getRecipes().forEach(this::communite);
	}

	private void compact(MaterialRecipe recipe) {
		ItemStack input = IndustrialItems.getBestItemStack(recipe.getInput());
		ItemStack output = IndustrialItems.getBestItemStack(recipe.getOutput());
		this.delegate.registerCastingRecipe(input, output, null, recipe.getTemperature(), recipe.getEnergy());
	}
	
	@Override
	public void register(Compaction compaction) {
		compaction.getRecipes().forEach(this::compact);
	}
	
	private void changeState(MaterialRecipe recipe) {
		MaterialStack input = recipe.getInput();
		MaterialStack output = recipe.getOutput();
		int temp = recipe.getTemperature();
		int energy = recipe.getEnergy();
		if (input.getState() == State.SOLID) {
			ItemStack solid = IndustrialItems.getBestItemStack(input);
			FluidStack fluid = IndustrialFluids.getFluidStack(output);
			delegate.registerMeltingRecipe(solid, fluid, temp, energy);
		} else if (output.getState() == State.SOLID) {
			FluidStack fluid = IndustrialFluids.getFluidStack(input);
			ItemStack solid = IndustrialItems.getBestItemStack(output);
			if (output.getForm().isRegular()) {
				delegate.registerCastingRecipe(fluid, solid);
			} else {
				delegate.registerFreezingRecipe(fluid, solid, temp, energy);
			}
		} else if (input.getState() == State.LIQUID) {
			FluidStack liquid = IndustrialFluids.getFluidStack(input);
			FluidStack gas = IndustrialFluids.getFluidStack(output);
			delegate.registerBoilingRecipe(liquid, gas, temp, energy);
		} else if (output.getState() == State.LIQUID) {
			FluidStack gas = IndustrialFluids.getFluidStack(input);
			FluidStack liquid = IndustrialFluids.getFluidStack(output);
			delegate.registerCondensingRecipe(gas, liquid, temp, energy);
		}
	}
	
	@Override
	public void register(EnthalpyChange enthalpyChange) {
		if (enthalpyChange.getType().getAxis() != EnthalpyChange.Type.Axis.STATE)
			return;
		enthalpyChange.getRecipes().forEach(this::changeState);
	}
	
	@Override
	public void register(Reaction reaction) {
		if (reaction.hasOnlySolidReactants())
			this.registerRoastingReaction(reaction);
		else this.registerReaction(reaction);
	}
		
	public void registerRoastingReaction(Reaction reaction) {
		this.registerRoastingReaction(Forms.DUST, reaction);
		this.registerRoastingReaction(Forms.DUST_TINY, reaction);
	}
	
	private void registerRoastingReaction(Form form, Reaction reaction) {
		float scale = 1F / reaction.getReactants().get(0).stoich;
		reaction = reaction.scale(scale);
		ItemStack output = this.getProductItemStack(form, reaction);
		if (output == null) {
			return;
		}
		FluidStack gas = this.getProductFluidStack(form, reaction, State.GAS);
		IngredientList<MaterialStack> inputs = this.getSolidReactants(form, reaction);
		int temp = reaction.getCondition().temperature;
		genericDelegate.registerRoastingRecipe(inputs, output, gas, temp);
	}
	
	private IngredientList<MaterialStack> getSolidReactants(Form form, Reaction reaction) {
		List<Term> reactants = reaction.getReactants(State.SOLID);
		List<MaterialStack> stacks = Lists.newArrayListWithCapacity(reactants.size());
		for (Term reactant : reactants) {
			stacks.add(form.of(reactant));
		}
		return IngredientList.of(stacks);
	}
	
	private ItemStack getProductItemStack(Form form, Reaction reaction) {
		for (Term product : reaction.getProducts(State.SOLID)) {
			float amount = product.stoich;
			if (amount < 1) {
				if (form == Forms.DUST) {
					form = Forms.DUST_TINY;
					amount *= Forms.DUST_TINY.getNumberPerBlock() / Forms.DUST.getNumberPerBlock();
				} else continue;
			}
			return MaterialStack.of((int)amount, product.material(), form).getBestItemStack();
		}
		return null;
	}

	public void registerReaction(Reaction reaction) {
		registerReaction(Forms.DUST_TINY, reaction);
		if (reaction.hasSolidReactants()) {
			registerReaction(Forms.DUST, reaction);
		}
	}

	private FluidStack getProductFluidStack(Form form, Reaction reaction, State state) {
		return IndustrialFluids.getFluidStack(reaction.getProduct(state), state, form);
	}
	
	private void registerReaction(Form form, Reaction reaction) {
		IngredientList<MaterialStack> solidReactants = this.getSolidReactants(form, reaction);
		FluidStack fluidReactantA, fluidReactantB = null;
		if (solidReactants.size() > 0) 
			fluidReactantA = this.getReactantFluidStacks(form, reaction, 1).get(0);
		else {
			List<FluidStack> fluidReactants = this.getReactantFluidStacks(form, reaction, 2);
			fluidReactantA = fluidReactants.get(0);
			if (fluidReactants.size() > 1)
				fluidReactantB = fluidReactants.get(1);
		}
		ItemStack solidProduct = this.getProductItemStack(form, reaction);
		FluidStack liquidProduct = this.getProductFluidStack(form, reaction, State.LIQUID);
		FluidStack gasProduct = this.getProductFluidStack(form, reaction, State.GAS);
		IngredientList<MaterialStack> catalyst = this.getCatalysts(reaction);
		genericDelegate.registerMixingRecipe(solidReactants, fluidReactantA, fluidReactantB, 
				solidProduct, liquidProduct, gasProduct, 
				reaction.getCondition(), catalyst);
	}
	
	private IngredientList<MaterialStack> getCatalysts(Reaction reaction) {
		return IngredientList.of(reaction.getCatalysts().stream().map(Forms.DUST::of).collect(Collectors.toList()));
	}

	private List<FluidStack> getReactantFluidStacks(Form form, Reaction reaction, int maxCount) {
		if (maxCount == 0)
			return Collections.emptyList();
		Stream<Term> reactants = reaction.getReactants().stream().
				filter((reactant) -> {
					return reactant.state() != State.SOLID && 
							!(reaction.isAqueous() && reactant.material() == Compounds.H2O) &&
							!(reaction.isAtmospheric() && 
									(reactant.material() == Compounds.O2 || reactant.material() == Compounds.N2));
				});
		reactants = addWaterAsSolvent(reaction, reactants);
		Stream<FluidStack> fluidStream = reactants.limit(maxCount - 1).
				map((reactant) -> IndustrialFluids.getFluidStack(reactant, form));
		List<FluidStack> fluidStacks = fluidStream.collect(Collectors.toList());
		List<Term> toMerge = reactants.skip(fluidStacks.size()).collect(Collectors.toList());
		if (toMerge.size() > 0) {
			Mixture mixture = toMerge.stream().collect(Term.TO_MIXTURE);
			if (toMerge.stream().map((t) -> t.state()).distinct().count() > 1) {
				throw new IllegalArgumentException("cannot mix reactants in different states");
			}
			State state = toMerge.get(0).state();
			fluidStacks.add(IndustrialFluids.getFluidStack(mixture, state, form));
			mixture.getComponents().stream().reduce((compA, compB) -> {
				double totalWeight = compA.weight + compB.weight;
				compA = compA.concentrate(1/totalWeight);
				compB = compB.concentrate(1/totalWeight);
				Mixture outputMixture = new SimpleMixture(compA).mix(compB);
				boolean alreadyRegistered = FluidRegistry.isFluidRegistered(outputMixture.getOreDictKey());
				if (!alreadyRegistered) {
					FluidStack inputA = IndustrialFluids.getFluidStack(compA, state, Forms.DUST_TINY);
					FluidStack inputB = IndustrialFluids.getFluidStack(compB, state, Forms.DUST_TINY);
					FluidStack output = IndustrialFluids.getFluidStack(outputMixture, state, Forms.DUST_TINY);
					genericDelegate.registerMixingRecipe(new IngredientList<MaterialStack>(), inputA, inputB, null, output, 
							null, Condition.STP, null);
				}
				return new MixtureComponent(outputMixture, 1.0F);
			});
		}
		return fluidStacks;
	}

	private static Stream<Term> addWaterAsSolvent(Reaction reaction, Stream<Term> reactants) {
		if (reaction.isAqueous()) {
			Optional<Float> stoich = reaction.getProducts(State.AQUEOUS).stream().
					map((t) -> t.stoich).reduce((a,b)->a+b);
			if (stoich.isPresent()) {
				reactants = reactants.map((reactant) -> {
					if (reactant.material() == Compounds.H2O) {
						float solventStoich = Forms.DUST.getNumberPerBlock() * stoich.get();
						reactant = new Term(solventStoich + reactant.stoich, reactant.material());
					}
					return reactant;
				});
			}
		}
		return reactants;
	}
}
