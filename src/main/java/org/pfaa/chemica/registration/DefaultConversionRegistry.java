package org.pfaa.chemica.registration;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.pfaa.chemica.ChemicaBlocks;
import org.pfaa.chemica.fluid.IndustrialFluids;
import org.pfaa.chemica.item.IndustrialItems;
import org.pfaa.chemica.item.IngredientStack;
import org.pfaa.chemica.item.MaterialStack;
import org.pfaa.chemica.model.Aggregate;
import org.pfaa.chemica.model.Compound.Compounds;
import org.pfaa.chemica.model.Condition;
import org.pfaa.chemica.model.Equation.Term;
import org.pfaa.chemica.model.IndustrialMaterial;
import org.pfaa.chemica.model.Mixture;
import org.pfaa.chemica.model.MixtureComponent;
import org.pfaa.chemica.model.Reaction;
import org.pfaa.chemica.model.SimpleMixture;
import org.pfaa.chemica.model.State;
import org.pfaa.chemica.processing.Combination;
import org.pfaa.chemica.processing.EnthalpyChange;
import org.pfaa.chemica.processing.Form;
import org.pfaa.chemica.processing.Form.Forms;
import org.pfaa.chemica.processing.Separation;
import org.pfaa.chemica.processing.Sizing;
import org.pfaa.chemica.processing.Stacking;

import com.google.common.collect.Lists;

import cpw.mods.fml.common.registry.GameRegistry;
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

	@Override
	public void register(Stacking stacking) {
		stack(stacking.getSolid(), stacking.getStackedForm(), stacking.getUnstackedForm());
	}
	
	private void stack(IndustrialMaterial solid, Form stackedForm, Form unstackedForm) {
		ItemStack stack;
		if (stackedForm == Forms.BLOCK && solid instanceof Aggregate) {
			// FIXME: in >= 1.10, we get this from ore dict
			stack = new ItemStack(ChemicaBlocks.getBlock((Aggregate)solid));
		} else {
			stack = IndustrialItems.getBestItemStack(stackedForm, solid);
		}
		if (stack != null) {
			ItemStack tinyStack = IndustrialItems.getBestItemStack(unstackedForm, solid);
			registerPartitionRecipes(stack, tinyStack, 
					unstackedForm.getNumberPerBlock() / stackedForm.getNumberPerBlock());
		}
	}
	
	private void registerPartitionRecipes(ItemStack itemStack, ItemStack partitionStack, int numPartitions) {
		partitionStack = partitionStack.copy();
		partitionStack.stackSize = numPartitions;
		this.delegate.registerMixingRecipe(Collections.singletonList(partitionStack), itemStack);
		GameRegistry.addShapelessRecipe(partitionStack, itemStack);
	}

	@Override
	public void register(Separation separation) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void register(Combination combination) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void register(Sizing sizing) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void register(EnthalpyChange enthalpyChange) {
		IndustrialMaterial material = enthalpyChange.getMaterial();
		EnthalpyChange.Type type = enthalpyChange.getType();
		int temp = enthalpyChange.getCondition().temperature;
		int energy = (int)enthalpyChange.getEnergy();
		if (type == EnthalpyChange.Types.MELTING || type == EnthalpyChange.Types.FREEZING) {
			this.melt(material, temp, energy);
		} else if (type == EnthalpyChange.Types.VAPORIZATION || type == EnthalpyChange.Types.CONDENSATION) {
			this.vaporize(material, temp, energy);
		}
	}

	private void melt(IndustrialMaterial material, int temp, int energy) {
		for (Form form : new Form[] { Forms.DUST_TINY, Forms.DUST, Forms.INGOT, Forms.NUGGET, Forms.BLOCK }) {
			this.melt(material, temp, energy, form);
		}
	}

	private void melt(IndustrialMaterial material, int temp, int energy, Form form) {
		ItemStack solid = IndustrialItems.getBestItemStack(form, material);
		if (solid != null) {
			FluidStack molten = IndustrialFluids.getCanonicalFluidStack(material, State.LIQUID, form);
			delegate.registerMeltingRecipe(solid, molten, temp, energy);
			if (form == Forms.INGOT || form == Forms.BLOCK) {
				delegate.registerCastingRecipe(molten, solid);
			} else if (form == Forms.DUST) {
				delegate.registerFreezingRecipe(molten, solid, temp, energy);
			}
		}
	}
	
	private void vaporize(IndustrialMaterial material, int temp, int energy) {
		FluidStack liquid = IndustrialFluids.getCanonicalFluidStack(material, State.LIQUID, Forms.DUST_TINY);
		FluidStack gas = IndustrialFluids.getCanonicalFluidStack(material, State.GAS, Forms.DUST_TINY);
		delegate.registerBoilingRecipe(liquid, gas, temp, energy);
		delegate.registerCondensingRecipe(gas, liquid, temp, energy);
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
		IngredientList inputs = this.getSolidReactants(form, reaction);
		int temp = reaction.getCondition().temperature;
		genericDelegate.registerRoastingRecipe(inputs, output, gas, temp);
	}
	
	private IngredientList getSolidReactants(Form form, Reaction reaction) {
		List<Term> reactants = reaction.getReactants(State.SOLID);
		List<IngredientStack> stacks = Lists.newArrayListWithCapacity(reactants.size());
		for (Term reactant : reactants) {
			stacks.add(new MaterialStack(form, reactant.material(), (int)reactant.stoich));
		}
		return new IngredientList(stacks);
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
			return new MaterialStack(form, product.material(), (int)amount).getBestItemStack();
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
		return IndustrialFluids.getCanonicalFluidStack(reaction.getProduct(state), state, form);
	}
	
	private void registerReaction(Form form, Reaction reaction) {
		IngredientList solidReactants = this.getSolidReactants(form, reaction);
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
		IngredientList catalyst = this.getCatalysts(reaction);
		genericDelegate.registerMixingRecipe(solidReactants, fluidReactantA, fluidReactantB, 
				solidProduct, liquidProduct, gasProduct, 
				reaction.getCondition(), catalyst);
	}
	
	private IngredientList getCatalysts(Reaction reaction) {
		return new IngredientList(reaction.getCatalysts().toArray(new IndustrialMaterial[0]));
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
				map((reactant) -> IndustrialFluids.getCanonicalFluidStack(reactant, form));
		List<FluidStack> fluidStacks = fluidStream.collect(Collectors.toList());
		Stream<Term> toMerge = reactants.skip(fluidStream.count());
		if (toMerge.count() > 0) {
			Mixture mixture = toMerge.collect(Term.TO_MIXTURE);
			State state = toMerge.findAny().get().state();
			if (toMerge.map((t) -> t.state()).distinct().count() > 1) {
				throw new IllegalArgumentException("cannot mix reactants in different states");
			}
			fluidStacks.add(IndustrialFluids.getCanonicalFluidStack(mixture, state, form));
			mixture.getComponents().stream().reduce((compA, compB) -> {
				double totalWeight = compA.weight + compB.weight;
				compA = compA.concentrate(1/totalWeight);
				compB = compB.concentrate(1/totalWeight);
				Mixture outputMixture = new SimpleMixture(compA).mix(compB);
				boolean alreadyRegistered = FluidRegistry.isFluidRegistered(outputMixture.getOreDictKey());
				if (!alreadyRegistered) {
					FluidStack inputA = IndustrialFluids.getCanonicalFluidStack(compA, state, Forms.DUST_TINY);
					FluidStack inputB = IndustrialFluids.getCanonicalFluidStack(compB, state, Forms.DUST_TINY);
					FluidStack output = IndustrialFluids.getCanonicalFluidStack(outputMixture, state, Forms.DUST_TINY);
					genericDelegate.registerMixingRecipe(new IngredientList(), inputA, inputB, null, output, 
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
