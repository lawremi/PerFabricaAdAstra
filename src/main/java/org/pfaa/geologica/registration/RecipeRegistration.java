package org.pfaa.geologica.registration;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.pfaa.chemica.fluid.IndustrialFluids;
import org.pfaa.chemica.item.IndustrialItems;
import org.pfaa.chemica.item.IndustrialMaterialItem;
import org.pfaa.chemica.item.MaterialStack;
import org.pfaa.chemica.model.Chemical;
import org.pfaa.chemica.model.Compound;
import org.pfaa.chemica.model.Compound.Compounds;
import org.pfaa.chemica.model.Condition;
import org.pfaa.chemica.model.Equation.Term;
import org.pfaa.chemica.model.IndustrialMaterial;
import org.pfaa.chemica.model.MaterialState;
import org.pfaa.chemica.model.Mixture;
import org.pfaa.chemica.model.MixtureComponent;
import org.pfaa.chemica.model.Reaction;
import org.pfaa.chemica.model.SimpleMixture;
import org.pfaa.chemica.model.State;
import org.pfaa.chemica.model.Strength;
import org.pfaa.chemica.processing.Form;
import org.pfaa.chemica.processing.Form.Forms;
import org.pfaa.chemica.processing.Separation;
import org.pfaa.chemica.processing.TemperatureLevel;
import org.pfaa.chemica.registration.BaseRecipeRegistration;
import org.pfaa.chemica.registration.IngredientList;
import org.pfaa.chemica.registration.ReactionFactory;
import org.pfaa.chemica.registration.RecipeUtils;
import org.pfaa.chemica.util.ChanceStack;
import org.pfaa.core.block.CompositeBlock;
import org.pfaa.geologica.GeoMaterial;
import org.pfaa.geologica.Geologica;
import org.pfaa.geologica.GeologicaBlocks;
import org.pfaa.geologica.GeologicaItems;
import org.pfaa.geologica.block.GeoBlock;
import org.pfaa.geologica.block.IntactGeoBlock;
import org.pfaa.geologica.block.LooseGeoBlock;
import org.pfaa.geologica.block.ProxyBlock;
import org.pfaa.geologica.block.StairsBlock;
import org.pfaa.geologica.block.WallBlock;
import org.pfaa.geologica.integration.TCIntegration;
import org.pfaa.geologica.processing.Crude;
import org.pfaa.geologica.processing.Crude.Crudes;
import org.pfaa.geologica.processing.Ore;
import org.pfaa.geologica.processing.Solutions;

import com.google.common.collect.Lists;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

public class RecipeRegistration extends BaseRecipeRegistration {
	
	public static void init() {
		registerRockCastingRecipes();
		registerClayProcessingRecipes();
		registerPeatDryingRecipe();
		registerCommunitionRecipes();
		registerPartitionRecipes();
		registerSiftingRecipes();
		registerBrineProcessingRecipes();
		registerNaturalGasProcessingRecipes();
		registerOilProcessingRecipes();
		registerCraftingRecipes();
		registerStoneToolRecipes();
		registerCompatibilityRecipes();
	}
	
	private static void registerCompatibilityRecipes() {
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(Blocks.cobblestone), "cobblestone"));
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(Blocks.stone), "stone"));
		GameRegistry.addShapelessRecipe(new ItemStack(Items.coal), GeologicaItems.CRUDE_LUMP.of(GeoMaterial.BITUMINOUS_COAL));
	}

	private static void registerStoneToolRecipes() {
		registerStoneToolRecipes(GeologicaBlocks.WEAK_RUBBLE);
		registerStoneToolRecipes(GeologicaBlocks.MEDIUM_COBBLE);
		registerStoneToolRecipes(GeologicaBlocks.STRONG_COBBLE);
		registerStoneToolRecipes(GeologicaBlocks.VERY_STRONG_COBBLE);
		if (Loader.isModLoaded("TConstruct"))
			TCIntegration.addStoneMaterials(); // TODO: implement this
	}

	private static void registerStoneToolRecipes(GeoBlock block) {
		registerStoneToolRecipe(block, Items.stone_pickaxe);
		registerStoneToolRecipe(block, Items.stone_axe);
		registerStoneToolRecipe(block, Items.stone_shovel);
		registerStoneToolRecipe(block, Items.stone_hoe);
		registerStoneToolRecipe(block, Items.stone_sword);
	}

	private static void registerStoneToolRecipe(GeoBlock block, Item tool) {
		ItemStack damaged = new ItemStack(tool, 1, (int)(getInitialStoneToolDamage(block.getStrength()) * tool.getMaxDamage()));
		ItemStack material = new ItemStack(block, 1, OreDictionary.WILDCARD_VALUE);
		ItemStack cobblestone = new ItemStack(Blocks.cobblestone, 1, OreDictionary.WILDCARD_VALUE);
		@SuppressWarnings("unchecked")
		List<IRecipe> recipes = (List<IRecipe>)CraftingManager.getInstance().getRecipeList();
		for (IRecipe recipe : recipes) {
			ItemStack output = recipe.getRecipeOutput();
			if (output != null && output.getItem() == tool && recipe instanceof ShapedOreRecipe) {
				ShapedOreRecipe shapedRecipe = (ShapedOreRecipe)recipe;
				Object[] origIngredients = shapedRecipe.getInput();
				Object[] ingredients = origIngredients.clone();
				for (int i = 0; i < ingredients.length; i++) {
					if (ingredients[i] instanceof List) 
					{
						@SuppressWarnings("unchecked")
						List<ItemStack> oreIngredient = (List<ItemStack>)ingredients[i];
						for (ItemStack ingredient : oreIngredient) {
							if (ingredient.getItem() == cobblestone.getItem()) {
								ingredients[i] = material;
								origIngredients[i] = cobblestone;
							}
						}
					} else if (ingredients[i] instanceof ItemStack) {
						if (((ItemStack)ingredients[i]).getItem() == cobblestone.getItem()) {
							ingredients[i] = material;
						}
					}
				}
				recipes.add(RecipeUtils.recreateOreRecipe(shapedRecipe, damaged, ingredients));
				break;
			}
		}
	}

	private static float getInitialStoneToolDamage(Strength strength) {
		return Geologica.getConfiguration().getInitialStoneToolDamage(strength);
	}
	
	private static void registerRockCastingRecipes() {
		for (Block block : GeologicaBlocks.getBlocks()) {
			if (block instanceof IntactGeoBlock) {
				GeoBlock broken = ((GeoBlock)block).getBrokenRockBlock();
				if (broken != null) {
					registerRockCastingRecipes(broken, block);
				}
			}
		}
	}

	private static void registerCraftingRecipes() {
		registerSlabRecipe(GeologicaBlocks.MEDIUM_COBBLE, GeologicaBlocks.MEDIUM_COBBLE_SLAB);
		registerSlabRecipe(GeologicaBlocks.STRONG_COBBLE, GeologicaBlocks.STRONG_COBBLE_SLAB);
		registerSlabRecipe(GeologicaBlocks.MEDIUM_STONE_BRICK, GeologicaBlocks.MEDIUM_STONE_BRICK_SLAB);
		registerSlabRecipe(GeologicaBlocks.STRONG_STONE_BRICK, GeologicaBlocks.STRONG_STONE_BRICK_SLAB);
		registerSlabRecipe(GeologicaBlocks.VERY_STRONG_STONE_BRICK, GeologicaBlocks.VERY_STRONG_STONE_BRICK_SLAB);
		registerSlabRecipe(GeologicaBlocks.MEDIUM_STONE, GeologicaBlocks.MEDIUM_STONE_SLAB);
		registerSlabRecipe(GeologicaBlocks.STRONG_STONE, GeologicaBlocks.STRONG_STONE_SLAB);
		registerSlabRecipe(GeologicaBlocks.VERY_STRONG_STONE, GeologicaBlocks.VERY_STRONG_STONE_SLAB);
		registerWallRecipe(GeologicaBlocks.MEDIUM_COBBLE, GeologicaBlocks.MEDIUM_COBBLE_WALL);
		registerWallRecipe(GeologicaBlocks.STRONG_COBBLE, GeologicaBlocks.STRONG_COBBLE_WALL);
		registerWallRecipe(GeologicaBlocks.VERY_STRONG_COBBLE, GeologicaBlocks.VERY_STRONG_COBBLE_WALL);
		registerWallRecipe(GeologicaBlocks.MEDIUM_STONE_BRICK, GeologicaBlocks.MEDIUM_STONE_BRICK_WALL);
		registerWallRecipe(GeologicaBlocks.STRONG_STONE_BRICK, GeologicaBlocks.STRONG_STONE_BRICK_WALL);
		registerWallRecipe(GeologicaBlocks.VERY_STRONG_STONE_BRICK, GeologicaBlocks.VERY_STRONG_STONE_BRICK_WALL);
		registerBrickRecipe(GeologicaBlocks.MEDIUM_STONE, GeologicaBlocks.MEDIUM_STONE_BRICK);
		registerBrickRecipe(GeologicaBlocks.STRONG_STONE, GeologicaBlocks.STRONG_STONE_BRICK);
		registerBrickRecipe(GeologicaBlocks.VERY_STRONG_STONE, GeologicaBlocks.VERY_STRONG_STONE_BRICK);
		registerBrickRecipe(GeologicaBlocks.WEAK_CLAY, GeologicaBlocks.WEAK_CLAY_BRICK);
		registerStairsRecipe(GeologicaBlocks.MEDIUM_COBBLE_STAIRS__LIMESTONE);
		registerStairsRecipe(GeologicaBlocks.STRONG_COBBLE_STAIRS__GRANITE);
		registerStairsRecipe(GeologicaBlocks.STRONG_COBBLE_STAIRS__MARBLE);
		registerStairsRecipe(GeologicaBlocks.MEDIUM_STONE_BRICK_STAIRS__LIMESTONE);
		registerStairsRecipe(GeologicaBlocks.STRONG_STONE_BRICK_STAIRS__GRANITE);
		registerStairsRecipe(GeologicaBlocks.STRONG_STONE_BRICK_STAIRS__MARBLE);
		registerQuarterToBlockRecipe(GeologicaItems.EARTHY_CLUMP);
	}

	private static void registerCommunitionRecipes() {
		for (Block block : GeologicaBlocks.getBlocks()) {
			registerCommunitionRecipes(block);
		}
	}
	
	private static void registerCommunitionRecipes(Block block) {
		if (block.getMaterial() == Material.sand) {
			registerSandSeparationRecipes((GeoBlock)block);
		}
		if (block.getMaterial() != Material.rock) {
			return;
		}
		if (block instanceof StairsBlock) {
			registerCrushingRecipes((StairsBlock)block);
			return;
		}
		GeoBlock geoBlock = null;
		if (block instanceof WallBlock) {
			geoBlock = (GeoBlock)((ProxyBlock)block).getModelBlock();
		} else if (block instanceof GeoBlock) {
			geoBlock = (GeoBlock)block;
		}
		if (geoBlock != null) {
			GeoBlock broken = geoBlock.getBrokenRockBlock();
			if (broken != null) {
				registerCrushingRecipes(block, broken);
			} else if (block instanceof LooseGeoBlock) {
				registerGrindingRecipes((GeoBlock)block);
			} else if (Ore.class.isAssignableFrom(geoBlock.getComposition())) {
				registerOreCommunitionRecipes((GeoBlock)block);
			} else if (Crude.class.isAssignableFrom(geoBlock.getComposition())) {
				registerCrudeCommunitionRecipes((GeoBlock)block);
			}
		}
	}

	private static void registerSandSeparationRecipes(GeoBlock block) {
		if (!Ore.class.isAssignableFrom(block.getComposition())) {
			return;
		}
		for (GeoMaterial material : block.getGeoMaterials()) {
			registerOreSeparationRecipes(block.getItemStack(material), Forms.BLOCK, material);
		}
	}

	private static void registerCrushingRecipes(StairsBlock input) {
		GeoBlock model = (GeoBlock)input.getModelBlock();
		GeoBlock output = model.getBrokenRockBlock();
		if (output == null) {
			return;
		}
		int damage = input.getModelBlockMeta();
		RECIPES.registerCrushingRecipe(new ItemStack(input), 
										new ItemStack(output, 1, damage), 
										null, model.getStrength());
	}
	
	private static void registerCrushingRecipes(Block input, GeoBlock output) {
		for(GeoMaterial material : output.getGeoMaterials()) {
			int damage = output.getMeta(material);
			RECIPES.registerCrushingRecipe(new ItemStack(input, 1, damage),
					new ItemStack(output, 1, damage), 
					null, material.getStrength());
		}
	}


	private static final float CRUSHING_DUST_CHANCE = 0.1F;

	private static void registerOreGrindingRecipes(ItemStack input, GeoMaterial material) {
		ItemStack primary = GeologicaItems.ORE_DUST.getItemStack(material);
		List<ChanceStack> secondaries = RecipeUtils.getSeparationOutputs(Forms.DUST_IMPURE_TINY, material.getComposition(), false);
		RECIPES.registerGrindingRecipe(input, primary, secondaries, material.getStrength());
	}
	
	private static void registerOreSeparationRecipes(IndustrialMaterialItem<GeoMaterial> item, GeoMaterial material) {
		ItemStack input = item.getItemStack(material);
		registerOreSeparationRecipes(input, item.getForm(), material);
	}
	
	private static void registerOreSeparationRecipes(ItemStack input, Form form, GeoMaterial material) {	
		List<ChanceStack> outputs = RecipeUtils.getSeparationOutputs(form, material.getComposition(), false);
		if (outputs.size() == 0) {
			return;
		}
		IndustrialMaterial host = material.getHost();
		if (host instanceof GeoMaterial) {
			List<ChanceStack> hostSecondaries = 
					RecipeUtils.getSeparationOutputs(form, ((GeoMaterial)host).getComposition(), true);
			for (ChanceStack hostSecondary : hostSecondaries) {
				ChanceStack downWeighted = hostSecondary.weightChance(0.2F);
				if (downWeighted.chance >= RecipeUtils.MIN_SIGNIFICANT_COMPONENT_WEIGHT)
					outputs.add(downWeighted);
			}
		}
		RECIPES.registerPhysicalSeparationRecipe(input, outputs);
	}

	private static void registerOreCommunitionRecipes(GeoBlock input) {
		for(GeoMaterial material : input.getGeoMaterials()) {
			ItemStack crushed = GeologicaItems.ORE_CRUSHED.getItemStack(material, 2);
			ChanceStack dust = new ChanceStack(GeologicaItems.ORE_DUST_TINY.getItemStack(material), CRUSHING_DUST_CHANCE);
			RECIPES.registerCrushingRecipe(input.getItemStack(material),
					crushed, dust, input.getStrength());
			registerOreGrindingRecipes(crushed.copy().splitStack(1), material);
			registerOreSeparationRecipes(GeologicaItems.ORE_DUST, material);
			registerOreSeparationRecipes(GeologicaItems.ORE_DUST_TINY, material);
		}
	}

	private static void registerGrindingRecipes(GeoBlock input) {
		for(GeoMaterial material : input.getGeoMaterials()) {
			registerGrindingRecipes(input.getItemStack(material), material);
		}
	}

	private static void registerGrindingRecipes(ItemStack input, GeoMaterial material) {
		List<ChanceStack> outputs = RecipeUtils.getSeparationOutputs(Forms.DUST, material.getComposition(), false);
		List<ChanceStack> secondaries = outputs.subList(1, outputs.size());
		RECIPES.registerGrindingRecipe(input, outputs.get(0).itemStack, secondaries, material.getStrength());
	}
	
	private static void registerCrudeCommunitionRecipes(GeoBlock input) {
		for(GeoMaterial material : input.getGeoMaterials()) {
			ItemStack lump = GeologicaItems.CRUDE_LUMP.getItemStack(material, 2);
			ChanceStack tinyDust = new ChanceStack(GeologicaItems.CRUDE_DUST_TINY.getItemStack(material), CRUSHING_DUST_CHANCE);
			RECIPES.registerCrushingRecipe(input.getItemStack(material), lump, tinyDust, input.getStrength());
			ItemStack dust = GeologicaItems.CRUDE_DUST.getItemStack(material);
			RECIPES.registerGrindingRecipe(lump.copy().splitStack(1), dust, 
					Collections.<ChanceStack> emptyList(), input.getStrength());
		}
	}

	private static void registerSiftingRecipes() {
		// TODO: Interact with ChanceDropRegistry to add separation recipes for LooseGeoBlocks
	}
	
	private static void registerSlabRecipe(CompositeBlock input, Block output) {
		registerCraftingRecipesByMeta(input, output, 6, "###");
	}
	private static void registerWallRecipe(CompositeBlock input, Block output) {
		registerCraftingRecipesByMeta(input, output, 6, "###", "###");
	}
	private static void registerBrickRecipe(CompositeBlock input, Block output) {
		registerCraftingRecipesByMeta(input, output, 4, "##", "##");
	}
	
	private static void registerCraftingRecipesByMeta(CompositeBlock input, Block output, int outputSize, String... shape) {
		for(int meta = 0; meta < input.getMetaCount(); meta++) {
			ItemStack outputStack = new ItemStack(output, outputSize, meta);
			ItemStack inputStack = new ItemStack(input, 1, meta);
			GameRegistry.addRecipe(outputStack, shape, shape[0].charAt(0), inputStack);
		}
	}
	
	private static void registerQuarterToBlockRecipe(IndustrialMaterialItem<GeoMaterial> input) {
		for(GeoMaterial material : input.getIndustrialMaterials()) {
			ItemStack outputStack = GeoBlock.getNative(material).getItemStack(1);
			ItemStack inputStack = input.getItemStack(material);
			GameRegistry.addRecipe(outputStack, "##", "##", '#', inputStack);
		}
	}
	
	private static void registerStairsRecipe(StairsBlock output) {
		ItemStack outputStack = new ItemStack(output, 4, output.getModelBlockMeta());
		ItemStack inputStack = new ItemStack(output.getModelBlock(), 1, output.getModelBlockMeta());
		GameRegistry.addRecipe(outputStack, "#  ", "## ", "###", '#', inputStack);
	}
	
	private static void registerRockCastingRecipes(GeoBlock input, Block output) {
		TemperatureLevel temp = TemperatureLevel.values()[input.getStrength().ordinal()];
		for(int meta = 0; meta < input.getMetaCount(); meta++) {
			ItemStack outputStack = new ItemStack(output, 1, meta);
			ItemStack inputStack = new ItemStack(input, 1, meta);
			RECIPES.registerCastingRecipe(inputStack, outputStack, null, temp.getReferenceTemperature());
			FluidStack fluid = new FluidStack(FluidRegistry.LAVA, FluidContainerRegistry.BUCKET_VOLUME);
			RECIPES.registerMeltingRecipe(inputStack, fluid, temp.getReferenceTemperature());
		}
	}
	
	private static void registerPartitionRecipes() {
		registerPartitionRecipes(
				GeologicaItems.INDUSTRIAL_MINERAL_DUST, 
				GeologicaItems.INDUSTRIAL_MINERAL_DUST_TINY);
		registerPartitionRecipes(
				GeologicaItems.CRUDE_DUST, 
				GeologicaItems.CRUDE_DUST_TINY);
		registerPartitionRecipes(
				GeologicaItems.ORE_MINERAL_DUST, 
				GeologicaItems.ORE_MINERAL_DUST_TINY);
		registerPartitionRecipes(
				GeologicaItems.ORE_DUST, 
				GeologicaItems.ORE_DUST_TINY);
	}
	
	private static void registerStandardClayProcessingRecipes() {
		for (GeoMaterial geoMaterial : GeologicaItems.CLAY_LUMP.getIndustrialMaterials()) {
			ItemStack clump = GeologicaItems.EARTHY_CLUMP.getItemStack(geoMaterial);
			ItemStack lump =  GeologicaItems.CLAY_LUMP.getItemStack(geoMaterial);
			ItemStack dust =  GeologicaItems.ORE_DUST.getItemStack(geoMaterial);
			RECIPES.registerRoastingRecipe(Lists.newArrayList(clump), lump, null, 400);
			RECIPES.registerGrindingRecipe(lump, dust, Collections.<ChanceStack>emptyList(), Strength.WEAK);
			registerOreSeparationRecipes(GeologicaItems.ORE_DUST, geoMaterial);
			registerOreSeparationRecipes(GeologicaItems.ORE_DUST_TINY, geoMaterial);
			FluidStack water = new FluidStack(FluidRegistry.WATER, IndustrialFluids.getAmount(Forms.DUST));
			RECIPES.registerMixingRecipe(Collections.singletonList(dust), water, null, clump, null, null, Condition.STP, null);
		}
	}
	
	private static void registerClayProcessingRecipes() {
		registerStandardClayProcessingRecipes();
		convertBentonite();
	}
	
	private static void convertBentonite() {
		IngredientList inputs = new IngredientList(
				new MaterialStack(Forms.CLUMP, GeoMaterial.CALCIUM_BENTONITE),
				new MaterialStack(Compounds.Na2CO3));
		GENERICS.registerMixingRecipe(inputs, GeologicaItems.EARTHY_CLUMP.getItemStack(GeoMaterial.SODIUM_BENTONITE));
	}
	
	private static void registerPeatDryingRecipe() {
		ItemStack clump = GeologicaItems.EARTHY_CLUMP.getItemStack(GeoMaterial.PEAT);
		ItemStack lump =  GeologicaItems.CRUDE_LUMP.getItemStack(GeoMaterial.PEAT);
		RECIPES.registerRoastingRecipe(Lists.newArrayList(clump), lump, null, 400);
	}
	
	private static void registerBrineProcessingRecipes() {
		purifyBrine(treatBrine(GeoMaterial.BRINE.getComposition(), Compounds.NaOH));
		purifyBrine(treatBrine(GeoMaterial.BRINE.getComposition(), Compounds.CaOH2));
	}

	private static void purifyBrine(Mixture brine) {
		treatBrine(brine, Compounds.Na2CO3, Solutions.PURIFIED_BRINE);
	}
	
	private static Mixture treatBrine(Mixture brine, Compound salt) {
		return treatBrine(brine, salt, null);
	}
	
	private static Mixture treatBrine(Mixture brine, Compound salt, Mixture product) {
		List<MixtureComponent> resultComps = Lists.newArrayList();
		Chemical solid = null;
		float reactedMoles = 0;
		for (MixtureComponent comp : brine.getComponents().subList(1, brine.getComponents().size())) {
			Reaction r = ReactionFactory.makeSaltMetathesisReaction(salt, (Compound)comp.material);
			if (r == null) {
				continue;
			}
			Term productA = r.getProducts().get(0);
			Term productB = r.getProducts().get(1);
			Chemical aqueous = null;
			if (productA.state() == State.SOLID) {
				if (solid != null) solid = productA.material();
			} else {
				aqueous = productA.material();
			}
			if (productB.state() == State.SOLID) {
				if (solid != null) solid = productB.material();
			} else {
				aqueous = productB.material();
			}
			if (aqueous != null)
				resultComps.add(new MixtureComponent(aqueous, comp.weight));
			reactedMoles += comp.weight * 1000 / ((Compound)comp.material).getFormula().getMolarMass();
		}
		
		int brineAmount = FluidContainerRegistry.BUCKET_VOLUME;
		Form inputForm = Forms.DUST;
		int inputAmount = 1;
		if (reactedMoles < 1) {
			inputForm = Forms.DUST_TINY;
			inputAmount = (int)Math.rint(reactedMoles * 10);
			if (inputAmount == 0) {
				brineAmount *= reactedMoles * 10;
				inputAmount = 1;
			}
		}
		MaterialStack solidInput = new MaterialStack(salt, inputAmount);
		FluidStack fluidInput = IndustrialFluids.getCanonicalFluidStack(brine, State.LIQUID, brineAmount);
		ItemStack solidOutput = null;
		if (solid != null) {
			solidOutput = IndustrialItems.getBestItemStack(inputForm, solid).copy();
			solidOutput.stackSize = inputAmount;
		}
		if (product == null){
			product = new SimpleMixture("brine.treated." + salt.name(), resultComps.toArray(new MixtureComponent[0]));
		}
		FluidStack fluidOutput = IndustrialFluids.getCanonicalFluidStack(product, State.LIQUID, brineAmount);
		GENERICS.registerMixingRecipe(new IngredientList(solidInput), fluidInput, 
				null, solidOutput, fluidOutput, null, Condition.AQUEOUS_STP, null);
		
		return product;
	}
	
	private static void registerNaturalGasProcessingRecipes() {
		Mixture ngl = extractMethane(extractHelium(desourNaturalGas()));
		steamCrack(ngl,
				   new SimpleMixture(Compounds.H2, 0.3).mix(Compounds.METHANE, 0.05).
				   		mix(Compounds.ETHENE, 0.25).mix(Compounds.PROPENE, 0.05).mix(Compounds.BUTADIENE, 0.02).
				   		mix(Compounds.H2O, 0.33), 2);
	}
	
	private static Mixture extractMethane(Mixture noHelium) {
		FluidStack gas = IndustrialFluids.getCanonicalFluidStack(noHelium);
		FluidStack liquid = IndustrialFluids.getCanonicalFluidStack(noHelium, State.LIQUID);
		RECIPES.registerCoolingRecipe(gas, liquid, (int)Compounds.METHANE.getEnthalpyChange(State.LIQUID));
		Mixture.Phases sep = noHelium.separateByState(Compounds.ETHANE.getCanonicalCondition(State.LIQUID));
		List<FluidStack> outputs = IndustrialFluids.getFluidStacks(sep);
		RECIPES.registerDistillationRecipe(gas, outputs, Condition.STP);
		return sep.liquid;
	}

	private static Mixture extractHelium(MaterialState<Mixture> desoured) {
		Separation sep = Separation.of(desoured).at(70);
		CONVERSIONS.register(sep);
		return sep.getResiduum().material;
	}

	private static MaterialState<Mixture> desourNaturalGas() {
		// Dropping CO2 just to keep things simple
		MaterialState<IndustrialMaterial> ethanolamine = State.AQUEOUS.of(Compounds.ETHANOLAMINE); 
		Separation abs = Separation.
				of(GeoMaterial.NATURAL_GAS).
				with(ethanolamine).
				extracts(Compounds.CO2, Compounds.H2S);
		CONVERSIONS.register(abs);
		MaterialState<Mixture> richAmine = State.AQUEOUS.of(abs.getSeparated().material.without(Compounds.CO2));
		Separation regen = Separation.
				of(richAmine).
				extracts(State.GAS.of(Compounds.H2S)).at(400);
		CONVERSIONS.register(regen);
		return abs.getResiduum();
	}

	private static void registerOilProcessingRecipes() {
		distillOil(GeoMaterial.LIGHT_OIL);
		distillOil(GeoMaterial.MEDIUM_OIL);
		distillOil(GeoMaterial.HEAVY_OIL);
		distillOil(GeoMaterial.EXTRA_HEAVY_OIL);
	}

	private static void distillOil(GeoMaterial oil) {
		Crude crude = (Crude)oil.getComposition();
		Crude topCut = crude.extract(Crudes.VOLATILES, Crudes.LIGHT_NAPHTHA, Crudes.HEAVY_NAPHTHA);
		Crude middleCut = crude.extract(Crudes.KEROSENE, Crudes.LIGHT_GAS_OIL, Crudes.HEAVY_GAS_OIL);
		distill(oil, Arrays.asList(topCut, middleCut, Crudes.BITUMEN));
		distill(topCut);
		distill(middleCut);
	}
	
	private static void distill(Crude crude) {
		distill(crude, crude.fractions());
	}
	
	private static void distill(Mixture mixture, List<IndustrialMaterial> fractions) {
		List<IndustrialMaterial> sortedFractions = fractions.stream().sorted((a, b) -> {
			return Integer.compare(a.getVaporization().getTemperature(), b.getVaporization().getTemperature());
		}).collect(Collectors.toList());
		List<FluidStack> outputs = sortedFractions.stream().map((m) -> {
			return IndustrialFluids.getCanonicalFluidStack(m, State.LIQUID, Forms.DUST_TINY);
		}).collect(Collectors.toList());
		Condition condition = sortedFractions.get(sortedFractions.size() - 2).getCanonicalCondition(State.GAS);
		FluidStack input = IndustrialFluids.getCanonicalFluidStack(mixture, State.LIQUID, Forms.DUST_TINY);
		RECIPES.registerDistillationRecipe(input, outputs, condition);
	}
	
	private static void steamCrack() {
		steamCrack(Compounds.ETHANE,
				   new SimpleMixture(Compounds.H2, 0.45).mix(Compounds.METHANE, 0.05).
				   		mix(Compounds.ETHENE, 0.25).mix(Compounds.PROPENE, 0.03).mix(Compounds.BUTADIENE, 0.02).
				   		mix(Compounds.H2O, 0.2), 1);
		steamCrack(Compounds.PROPANE,
				   new SimpleMixture(Compounds.H2, 0.2).mix(Compounds.METHANE, 0.1).
				   		mix(Compounds.ETHENE, 0.3).mix(Compounds.PROPENE, 0.15).mix(Compounds.BUTADIENE, 0.05).
				   		mix(Compounds.H2O, 0.2), 1.5F);
		
		Mixture rpg = new SimpleMixture(Compounds.BENZENE, 0.5).mix(Compounds.TOLUENE, 0.5);
		Mixture butenes = new SimpleMixture(Compounds.ISO_BUTENE, 0.5).mix(Compounds.N_BUTENE, 0.5);
		
		steamCrack(Crudes.LIGHT_NAPHTHA,
				   new SimpleMixture(Compounds.H2, 0.1).mix(Compounds.METHANE, 0.08).
				   		mix(Compounds.ETHENE, 0.18).mix(Compounds.PROPENE, 0.07).mix(Compounds.BUTADIENE, 0.07).
				   		mix(rpg, 0.1).mix(butenes, 0.07).
				   		mix(Compounds.H2O, 0.33), 3);
		steamCrack(Crudes.HEAVY_NAPHTHA,
				   new SimpleMixture(Compounds.H2, 0.08).mix(Compounds.METHANE, 0.08).
				   		mix(Compounds.ETHENE, 0.15).mix(Compounds.PROPENE, 0.07).mix(Compounds.BUTADIENE, 0.07).
				   		mix(rpg, 0.15).mix(butenes, 0.07).
				   		mix(Compounds.H2O, 0.33), 6);
		
		steamCrack(Crudes.LIGHT_GAS_OIL,
				   new SimpleMixture(Compounds.H2, 0.05).mix(Compounds.METHANE, 0.05).
				   		mix(Compounds.ETHENE, 0.13).mix(Compounds.PROPENE, 0.07).mix(Compounds.BUTADIENE, 0.05).
				   		mix(rpg, 0.1).mix(butenes, 0.05).mix(Crudes.LIGHT_FUEL_OIL, 0.1).
				   		mix(Compounds.H2O, 0.40), 12);
		steamCrack(Crudes.HEAVY_GAS_OIL,
				   new SimpleMixture(Compounds.H2, 0.05).mix(Compounds.METHANE, 0.05).
				   		mix(Compounds.ETHENE, 0.10).mix(Compounds.PROPENE, 0.05).mix(Compounds.BUTADIENE, 0.05).
				   		mix(rpg, 0.1).mix(butenes, 0.05).mix(Crudes.LIGHT_FUEL_OIL, 0.15).
				   		mix(Compounds.H2O, 0.40), 16);
	}
	
	private static void steamCrack(IndustrialMaterial input, Mixture output, float steamRatio) {
		FluidStack inputFluid = IndustrialFluids.getCanonicalFluidStack(input, Forms.DUST_TINY);
		FluidStack steam = IndustrialFluids.getCanonicalFluidStack(Compounds.H2O, State.GAS, (int)(steamRatio*inputFluid.amount));
		FluidStack outputFluid = IndustrialFluids.getCanonicalFluidStack(output, Forms.DUST_TINY);
		RECIPES.registerMixingRecipe(Collections.emptyList(), inputFluid, steam, null, null, outputFluid, new Condition(1100), null);
		Mixture.Phases sep = output.separateByState(new Condition(700));
		RECIPES.registerDistillationRecipe(outputFluid, IndustrialFluids.getFluidStacks(sep), Condition.STP);
	}
}
