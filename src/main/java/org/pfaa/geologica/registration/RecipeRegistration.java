package org.pfaa.geologica.registration;

import java.util.List;

import org.pfaa.chemica.model.Chemical;
import org.pfaa.chemica.model.Compound;
import org.pfaa.chemica.model.Compound.Compounds;
import org.pfaa.chemica.model.Condition;
import org.pfaa.chemica.model.Element;
import org.pfaa.chemica.model.Equation.Term;
import org.pfaa.chemica.model.IndustrialMaterial;
import org.pfaa.chemica.model.Mixture;
import org.pfaa.chemica.model.MixtureComponent;
import org.pfaa.chemica.model.SimpleMixture;
import org.pfaa.chemica.model.State;
import org.pfaa.chemica.model.Strength;
import org.pfaa.chemica.processing.Combination;
import org.pfaa.chemica.processing.Reaction;
import org.pfaa.chemica.processing.Separation;
import org.pfaa.chemica.registration.BaseRecipeRegistration;
import org.pfaa.chemica.registration.Reactions;
import org.pfaa.chemica.registration.RecipeUtils;
import org.pfaa.core.block.CompositeBlock;
import org.pfaa.geologica.GeoMaterial;
import org.pfaa.geologica.Geologica;
import org.pfaa.geologica.GeologicaBlocks;
import org.pfaa.geologica.GeologicaItems;
import org.pfaa.geologica.block.GeoBlock;
import org.pfaa.geologica.block.StairsBlock;
import org.pfaa.geologica.integration.TCIntegration;
import org.pfaa.geologica.processing.Crude;
import org.pfaa.geologica.processing.Crude.Crudes;
import org.pfaa.geologica.processing.IndustrialMineral.IndustrialMinerals;
import org.pfaa.geologica.processing.Mixtures;
import org.pfaa.geologica.processing.OreMineral.Ores;

import com.google.common.collect.Lists;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

public class RecipeRegistration extends BaseRecipeRegistration {
	
	public static void init() {
		registerCompatibilityRecipes();
		registerStoneToolRecipes();
		registerCraftingRecipes();
		
		registerStackings();
		meltRocks();
		processGeoMaterials();
		dryPeat();
		processClays();
		processBrine();
		processNaturalGas();
		processOil();
	}
	
	private static void registerCompatibilityRecipes() {
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(Blocks.cobblestone), "cobblestone"));
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(Blocks.stone), "stone"));
		GameRegistry.addShapelessRecipe(new ItemStack(Items.coal), GeologicaItems.CRUDE_LUMP.of(Crudes.BITUMINOUS_COAL));
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
	}

	private static void registerStackings() {
		REGISTRANT.stack(IndustrialMinerals.class);
		REGISTRANT.stack(Ores.class);
		REGISTRANT.stack(GeoMaterial.class);
	}
	
	private static void meltRocks() {
	 	REGISTRANT.melt(GeoMaterial.class, GeoMaterial::isIgneousRock);
	}
	
	private static void processGeoMaterials() {
		REGISTRANT.communite(GeoMaterial.class);
		REGISTRANT.communite(Crudes.class);
		REGISTRANT.reduce(GeoMaterial.class, GeoMaterial::isRock);
		REGISTRANT.separatePhysically(GeoMaterial.class);
		REGISTRANT.compact(GeoMaterial.class, GeoMaterial::isRock);
	}
	
	private static void dryPeat() {
		REGISTRANT.dry(GeoMaterial.PEAT);
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
	
	private static void registerStairsRecipe(StairsBlock output) {
		ItemStack outputStack = new ItemStack(output, 4, output.getModelBlockMeta());
		ItemStack inputStack = new ItemStack(output.getModelBlock(), 1, output.getModelBlockMeta());
		GameRegistry.addRecipe(outputStack, "#  ", "## ", "###", '#', inputStack);
	}
	
	private static void processClays() {
		convertBentonite();
	}
	
	private static void convertBentonite() {
		CONVERSIONS.register(Combination.of(GeoMaterial.CALCIUM_BENTONITE, Compounds.Na2CO3, Compounds.H2O).
				yields(GeoMaterial.SODIUM_BENTONITE));
	}
	
	private static void processBrine() {
		purifyBrine(treatBrine(GeoMaterial.BRINE.getComposition(), Compounds.NaOH));
		purifyBrine(treatBrine(GeoMaterial.BRINE.getComposition(), Compounds.CaOH2));
		makeSodaAsh();
	}

	private static void purifyBrine(Mixture brine) {
		treatBrine(brine, Compounds.Na2CO3, Mixtures.PURIFIED_BRINE);
	}
	
	private static Mixture treatBrine(Mixture brine, Compound salt) {
		return treatBrine(brine, salt, null);
	}
	
	private static Mixture treatBrine(Mixture brine, Compound salt, Mixture product) {
		List<MixtureComponent> resultComps = Lists.newArrayList();
		Chemical solid = null;
		float reactedMoles = 0;
		for (MixtureComponent comp : brine.getComponents().subList(1, brine.getComponents().size())) {
			Reaction r = Reactions.metathesize(salt, (Compound)comp.material);
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
		
		if (product == null){
			product = new SimpleMixture("brine.treated." + salt.name(), resultComps.toArray(new MixtureComponent[0]));
		}
		
		float brineMoles = reactedMoles * 10;
		Combination comb = Combination.of(reactedMoles, salt).with(brineMoles, brine).yields(brineMoles, product);
		if (solid != null) {
			comb.yields(reactedMoles, solid);
		}
		
		return product;
	}
	
	private static void makeSodaAsh() {
		Reaction reaction = Reaction.inWaterOf(Compounds.NaCl).
				         			 with(Compounds.CO2).
				         			 with(Compounds.NH3).
				         			 with(Compounds.H2O).
				         			 yields(Compounds.NaHCO3). // decomposes to Na2CO3
				         			 and(Compounds.NH4Cl);
		CONVERSIONS.register(reaction.asCombinationForSolution(Mixtures.PURIFIED_BRINE));
	}
	
	private static void processNaturalGas() {
		Mixture ngl = separateMethane(separateHelium(desourNaturalGas()));
		steamCrack(ngl,
				   new SimpleMixture(Compounds.H2, 0.3).mix(Compounds.METHANE, 0.05).
				   		mix(Compounds.ETHENE, 0.25).mix(Compounds.PROPENE, 0.05).mix(Compounds.BUTADIENE, 0.02).
				   		mix(Compounds.H2O, 0.33), 2);
	}
	
	private static Mixture separateMethane(Mixture noHelium) {
		Separation sep = Separation.of(noHelium).extractsAllExcept(Compounds.METHANE).
				at(Compounds.ETHANE.getCanonicalCondition(State.LIQUID)).
				by(Separation.Types.CONDENSATION);
		CONVERSIONS.register(sep);
		return sep.getResiduum().material;
	}

	private static Mixture separateHelium(Mixture desoured) {
		Separation sep = Separation.of(desoured).extractsAllExcept(Element.He).by(Separation.Types.CONDENSATION).at(70);
		CONVERSIONS.register(sep);
		return sep.getResiduum().material;
	}

	private static Mixture desourNaturalGas() {
		Separation abs = Separation.
				of(GeoMaterial.NATURAL_GAS).
				with(State.AQUEOUS.of(Compounds.ETHANOLAMINE)).
				extracts(Compounds.CO2, Compounds.H2S).
				by(Separation.Types.ABSORPTION);
		CONVERSIONS.register(abs);
		Mixture richAmine = abs.getSeparatedMixture(0);
		// Dropping CO2 just to keep things simple
		Mixture richAmineNoCO2 = new SimpleMixture(richAmine.name(), richAmine.without(Compounds.CO2).getComponents()); 
		Separation regen = Separation.
				of(State.AQUEOUS.of(richAmineNoCO2)).
				extracts(Compounds.H2S).at(400).
				by(Separation.Types.DEGASIFICATION);
		CONVERSIONS.register(regen);
		return abs.getResiduum().material;
	}

	private static void processOil() {
		distillOil(GeoMaterial.LIGHT_OIL);
		distillOil(GeoMaterial.MEDIUM_OIL);
		distillOil(GeoMaterial.HEAVY_OIL);
		distillOil(GeoMaterial.EXTRA_HEAVY_OIL);
	}

	private static void distillOil(GeoMaterial oil) {
		Crude crude = (Crude)oil.getComposition();
		Separation firstCut = Separation.of(crude).
				extracts(Crudes.VOLATILES, Crudes.LIGHT_NAPHTHA, Crudes.HEAVY_NAPHTHA).
				extracts(Crudes.KEROSENE, Crudes.LIGHT_GAS_OIL, Crudes.HEAVY_GAS_OIL).
				by(Separation.Types.DISTILLATION);
		CONVERSIONS.register(firstCut);
		CONVERSIONS.register(Separation.of(firstCut.getSeparatedMixture(0)).extractsAll().
				by(Separation.Types.DISTILLATION));
		CONVERSIONS.register(Separation.of(firstCut.getSeparatedMixture(1)).extractsAll().
				by(Separation.Types.DISTILLATION));
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
		
		steamCrack(Crudes.LIGHT_NAPHTHA,
				   new SimpleMixture(Compounds.H2, 0.1).mix(Compounds.METHANE, 0.08).
				   		mix(Compounds.ETHENE, 0.18).mix(Compounds.PROPENE, 0.07).mix(Compounds.BUTADIENE, 0.07).
				   		mix(Mixtures.RPG, 0.1).mix(Mixtures.BUTENES, 0.07).
				   		mix(Compounds.H2O, 0.33), 3);
		steamCrack(Crudes.HEAVY_NAPHTHA,
				   new SimpleMixture(Compounds.H2, 0.08).mix(Compounds.METHANE, 0.08).
				   		mix(Compounds.ETHENE, 0.15).mix(Compounds.PROPENE, 0.07).mix(Compounds.BUTADIENE, 0.07).
				   		mix(Mixtures.RPG, 0.15).mix(Mixtures.BUTENES, 0.07).
				   		mix(Compounds.H2O, 0.33), 6);
		
		steamCrack(Crudes.LIGHT_GAS_OIL,
				   new SimpleMixture(Compounds.H2, 0.05).mix(Compounds.METHANE, 0.05).
				   		mix(Compounds.ETHENE, 0.13).mix(Compounds.PROPENE, 0.07).mix(Compounds.BUTADIENE, 0.05).
				   		mix(Mixtures.RPG, 0.1).mix(Mixtures.BUTENES, 0.05).mix(Crudes.LIGHT_FUEL_OIL, 0.1).
				   		mix(Compounds.H2O, 0.40), 12);
		steamCrack(Crudes.HEAVY_GAS_OIL,
				   new SimpleMixture(Compounds.H2, 0.05).mix(Compounds.METHANE, 0.05).
				   		mix(Compounds.ETHENE, 0.10).mix(Compounds.PROPENE, 0.05).mix(Compounds.BUTADIENE, 0.05).
				   		mix(Mixtures.RPG, 0.1).mix(Mixtures.BUTENES, 0.05).mix(Crudes.LIGHT_FUEL_OIL, 0.15).
				   		mix(Compounds.H2O, 0.40), 16);
	}
	
	private static void steamCrack(IndustrialMaterial input, Mixture output, float steamRatio) {
		CONVERSIONS.register(Combination.of(input).with(steamRatio, Compounds.H2O).at(new Condition(1100)).yields(output));
		Separation fuelOilSep = Separation.of(output).extracts(Crudes.LIGHT_FUEL_OIL).
				by(Separation.Types.CONDENSATION);
		CONVERSIONS.register(fuelOilSep);
		Separation h2oRpgSep = Separation.of(fuelOilSep.getResiduum()).extracts(Compounds.H2O, Mixtures.RPG).
				by(Separation.Types.CONDENSATION);
		CONVERSIONS.register(h2oRpgSep);
		Separation h2Ch4Sep = Separation.of(h2oRpgSep.getResiduum()).extracts(Compounds.H2, Compounds.METHANE).
				by(Separation.Types.CONDENSATION);
		CONVERSIONS.register(h2Ch4Sep);
		Separation ethPropSep = Separation.of(h2Ch4Sep.getResiduum()).extracts(Compounds.ETHENE, Compounds.PROPENE).
				by(Separation.Types.CONDENSATION);
		CONVERSIONS.register(ethPropSep);
		// TODO: chemically separate C4 into butadiene and butenes (distilled into iso-butene and 2-butene)
	}
}
