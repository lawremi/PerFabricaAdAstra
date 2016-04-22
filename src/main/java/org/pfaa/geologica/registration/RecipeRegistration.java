package org.pfaa.geologica.registration;

import java.util.Collections;
import java.util.List;

import org.pfaa.chemica.fluid.IndustrialFluids;
import org.pfaa.chemica.item.IndustrialMaterialItem;
import org.pfaa.chemica.item.MaterialStack;
import org.pfaa.chemica.model.Compound.Compounds;
import org.pfaa.chemica.model.Constants;
import org.pfaa.chemica.model.IndustrialMaterial;
import org.pfaa.chemica.model.Strength;
import org.pfaa.chemica.processing.Form;
import org.pfaa.chemica.processing.Form.Forms;
import org.pfaa.chemica.processing.TemperatureLevel;
import org.pfaa.chemica.registration.GenericRecipeRegistry;
import org.pfaa.chemica.registration.IngredientList;
import org.pfaa.chemica.registration.RecipeRegistry;
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
import org.pfaa.geologica.processing.Ore;

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

public class RecipeRegistration {
	private static final RecipeRegistry registry = org.pfaa.chemica.registration.RecipeRegistration.getTarget();
	private static final GenericRecipeRegistry genericRegistry = 
			org.pfaa.chemica.registration.RecipeRegistration.getGenericTarget();
	
	public static void init() {
		registerRockCastingRecipes();
		registerClayProcessingRecipes();
		registerPeatDryingRecipe();
		registerCommunitionRecipes();
		registerPartitionRecipes();
		registerSiftingRecipes();
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
		registry.registerCrushingRecipe(new ItemStack(input), 
										new ItemStack(output, 1, damage), 
										null, model.getStrength());
	}
	
	private static void registerCrushingRecipes(Block input, GeoBlock output) {
		for(GeoMaterial material : output.getGeoMaterials()) {
			int damage = output.getMeta(material);
			registry.registerCrushingRecipe(new ItemStack(input, 1, damage),
					new ItemStack(output, 1, damage), 
					null, material.getStrength());
		}
	}


	private static final float CRUSHING_DUST_CHANCE = 0.1F;

	private static void registerOreGrindingRecipes(ItemStack input, GeoMaterial material) {
		ItemStack primary = GeologicaItems.ORE_DUST.getItemStack(material);
		List<ChanceStack> secondaries = RecipeUtils.getSeparationOutputs(Forms.DUST_IMPURE_TINY, material.getComposition(), false);
		registry.registerGrindingRecipe(input, primary, secondaries, material.getStrength());
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
		registry.registerPhysicalSeparationRecipe(input, outputs);
	}

	private static void registerOreCommunitionRecipes(GeoBlock input) {
		for(GeoMaterial material : input.getGeoMaterials()) {
			ItemStack crushed = GeologicaItems.ORE_CRUSHED.getItemStack(material, 2);
			ChanceStack dust = new ChanceStack(GeologicaItems.ORE_DUST_TINY.getItemStack(material), CRUSHING_DUST_CHANCE);
			registry.registerCrushingRecipe(input.getItemStack(material),
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
		registry.registerGrindingRecipe(input, outputs.get(0).itemStack, secondaries, material.getStrength());
	}
	
	private static void registerCrudeCommunitionRecipes(GeoBlock input) {
		for(GeoMaterial material : input.getGeoMaterials()) {
			ItemStack lump = GeologicaItems.CRUDE_LUMP.getItemStack(material, 2);
			ChanceStack tinyDust = new ChanceStack(GeologicaItems.CRUDE_DUST_TINY.getItemStack(material), CRUSHING_DUST_CHANCE);
			registry.registerCrushingRecipe(input.getItemStack(material), lump, tinyDust, input.getStrength());
			ItemStack dust = GeologicaItems.CRUDE_DUST.getItemStack(material);
			registry.registerGrindingRecipe(lump.copy().splitStack(1), dust, 
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
			registry.registerCastingRecipe(inputStack, outputStack, null, temp.getReferenceTemperature());
			FluidStack fluid = new FluidStack(FluidRegistry.LAVA, FluidContainerRegistry.BUCKET_VOLUME);
			registry.registerMeltingRecipe(inputStack, fluid, temp.getReferenceTemperature());
		}
	}
	
	private static void registerPartitionRecipes() {
		org.pfaa.chemica.registration.RecipeRegistration.registerPartitionRecipes(
				GeologicaItems.INDUSTRIAL_MINERAL_DUST, 
				GeologicaItems.INDUSTRIAL_MINERAL_DUST_TINY);
		org.pfaa.chemica.registration.RecipeRegistration.registerPartitionRecipes(
				GeologicaItems.CRUDE_DUST, 
				GeologicaItems.CRUDE_DUST_TINY);
		org.pfaa.chemica.registration.RecipeRegistration.registerPartitionRecipes(
				GeologicaItems.ORE_MINERAL_DUST, 
				GeologicaItems.ORE_MINERAL_DUST_TINY);
		org.pfaa.chemica.registration.RecipeRegistration.registerPartitionRecipes(
				GeologicaItems.ORE_DUST, 
				GeologicaItems.ORE_DUST_TINY);
	}
	
	private static void registerStandardClayProcessingRecipes() {
		for (GeoMaterial geoMaterial : GeologicaItems.CLAY_LUMP.getIndustrialMaterials()) {
			ItemStack clump = GeologicaItems.EARTHY_CLUMP.getItemStack(geoMaterial);
			ItemStack lump =  GeologicaItems.CLAY_LUMP.getItemStack(geoMaterial);
			ItemStack dust =  GeologicaItems.ORE_DUST.getItemStack(geoMaterial);
			registry.registerRoastingRecipe(Lists.newArrayList(clump), lump, 400);
			registry.registerGrindingRecipe(lump, dust, Collections.<ChanceStack>emptyList(), Strength.WEAK);
			registerOreSeparationRecipes(GeologicaItems.ORE_DUST, geoMaterial);
			registerOreSeparationRecipes(GeologicaItems.ORE_DUST_TINY, geoMaterial);
			FluidStack water = new FluidStack(FluidRegistry.WATER, IndustrialFluids.getAmount(Forms.DUST));
			registry.registerAbsorptionRecipe(Lists.newArrayList(dust), water, clump, Constants.STANDARD_TEMPERATURE);
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
		genericRegistry.registerMixingRecipe(inputs, GeologicaItems.EARTHY_CLUMP.getItemStack(GeoMaterial.SODIUM_BENTONITE));
	}
	
	private static void registerPeatDryingRecipe() {
		ItemStack clump = GeologicaItems.EARTHY_CLUMP.getItemStack(GeoMaterial.PEAT);
		ItemStack lump =  GeologicaItems.CRUDE_LUMP.getItemStack(GeoMaterial.PEAT);
		registry.registerRoastingRecipe(Lists.newArrayList(clump), lump, 400);
	}
}
