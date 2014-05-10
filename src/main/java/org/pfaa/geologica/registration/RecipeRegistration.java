package org.pfaa.geologica.registration;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

import org.pfaa.RecipeUtils;
import org.pfaa.block.CompositeBlock;
import org.pfaa.chemica.ChemicaItems;
import org.pfaa.chemica.model.Mixture;
import org.pfaa.chemica.model.Compound.Compounds;
import org.pfaa.geologica.GeoMaterial;
import org.pfaa.geologica.GeoMaterial.Strength;
import org.pfaa.geologica.Geologica;
import org.pfaa.geologica.GeologicaBlocks;
import org.pfaa.geologica.GeologicaItems;
import org.pfaa.geologica.block.BrickGeoBlock;
import org.pfaa.geologica.block.BrokenGeoBlock;
import org.pfaa.geologica.block.GeoBlock;
import org.pfaa.geologica.block.LooseGeoBlock;
import org.pfaa.geologica.block.ProxyBlock;
import org.pfaa.geologica.block.SlabBlock;
import org.pfaa.geologica.block.StairsBlock;
import org.pfaa.geologica.block.WallBlock;
import org.pfaa.geologica.integration.FMPIntegration;
import org.pfaa.geologica.integration.IC2Integration;
import org.pfaa.geologica.integration.TCIntegration;
import org.pfaa.geologica.integration.TEIntegration;
import org.pfaa.geologica.processing.IndustrialMineral.IndustrialMinerals;
import org.pfaa.geologica.processing.OreMineral;
import org.pfaa.geologica.processing.OreMineral.SmeltingTemperature;

import com.google.common.base.CaseFormat;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.registry.GameRegistry;

public class RecipeRegistration {
	public static void init() {
		registerOres();
		addSmeltingRecipes();
		addCraftingRecipes();
		addGrindingRecipes();
		addMeltingRecipes();
		addStoneToolRecipes();
		addStoneAbstractionRecipesForBrokenMods();
		registerMicroblocks();
	}
	
	private static void addStoneAbstractionRecipesForBrokenMods() {
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(Block.cobblestone), "cobblestone"));
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(Block.stone), "stone"));
	}

	private static void addStoneToolRecipes() {
		addStoneToolRecipes(GeologicaBlocks.WEAK_RUBBLE);
		addStoneToolRecipes(GeologicaBlocks.MEDIUM_COBBLESTONE);
		addStoneToolRecipes(GeologicaBlocks.STRONG_COBBLESTONE);
		addStoneToolRecipes(GeologicaBlocks.VERY_STRONG_COBBLESTONE);
		if (Loader.isModLoaded("TConstruct"))
			TCIntegration.addStoneMaterials();
	}

	private static void addStoneToolRecipes(GeoBlock block) {
		addStoneToolRecipe(block, Item.pickaxeStone);
		addStoneToolRecipe(block, Item.axeStone);
		addStoneToolRecipe(block, Item.shovelStone);
		addStoneToolRecipe(block, Item.hoeStone);
		addStoneToolRecipe(block, Item.swordStone);
	}

	private static void addStoneToolRecipe(GeoBlock block, Item tool) {
		ItemStack damaged = new ItemStack(tool, 1, (int)(getInitialStoneToolDamage(block.getStrength()) * tool.getMaxDamage()));
		ItemStack material = new ItemStack(block, 1, OreDictionary.WILDCARD_VALUE);
		ItemStack cobblestone = new ItemStack(Block.cobblestone, 1, OreDictionary.WILDCARD_VALUE);
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
						for (ItemStack ingredient : (List<ItemStack>)ingredients[i]) {
							if (ingredient.itemID == cobblestone.itemID) {
								ingredients[i] = material;
								origIngredients[i] = cobblestone;
							}
						}
					} else if (ingredients[i] instanceof ItemStack) {
						if (((ItemStack)ingredients[i]).itemID == cobblestone.itemID) {
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
	
	private static void registerOres() {
		oreDictifyGeoBlocks();
	}

	private static void addSmeltingRecipes() {
		addSmeltingRecipesByMeta(GeologicaBlocks.WEAK_RUBBLE, GeologicaBlocks.WEAK_STONE, SmeltingTemperature.MEDIUM);
		addSmeltingRecipesByMeta(GeologicaBlocks.MEDIUM_COBBLESTONE, GeologicaBlocks.MEDIUM_STONE, SmeltingTemperature.MEDIUM);
		addSmeltingRecipesByMeta(GeologicaBlocks.STRONG_COBBLESTONE, GeologicaBlocks.STRONG_STONE, SmeltingTemperature.HIGH);
	}

	private static void addCraftingRecipes() {
		addSlabRecipe(GeologicaBlocks.MEDIUM_COBBLESTONE, GeologicaBlocks.MEDIUM_COBBLE_SLAB);
		addSlabRecipe(GeologicaBlocks.STRONG_COBBLESTONE, GeologicaBlocks.STRONG_COBBLE_SLAB);
		addSlabRecipe(GeologicaBlocks.MEDIUM_STONE_BRICK, GeologicaBlocks.MEDIUM_STONE_BRICK_SLAB);
		addSlabRecipe(GeologicaBlocks.STRONG_STONE_BRICK, GeologicaBlocks.STRONG_STONE_BRICK_SLAB);
		addSlabRecipe(GeologicaBlocks.VERY_STRONG_STONE_BRICK, GeologicaBlocks.VERY_STRONG_STONE_BRICK_SLAB);
		addSlabRecipe(GeologicaBlocks.MEDIUM_STONE, GeologicaBlocks.MEDIUM_STONE_SLAB);
		addSlabRecipe(GeologicaBlocks.STRONG_STONE, GeologicaBlocks.STRONG_STONE_SLAB);
		addSlabRecipe(GeologicaBlocks.VERY_STRONG_STONE, GeologicaBlocks.VERY_STRONG_STONE_SLAB);
		addWallRecipe(GeologicaBlocks.MEDIUM_COBBLESTONE, GeologicaBlocks.MEDIUM_COBBLE_WALL);
		addWallRecipe(GeologicaBlocks.STRONG_COBBLESTONE, GeologicaBlocks.STRONG_COBBLE_WALL);
		addWallRecipe(GeologicaBlocks.VERY_STRONG_COBBLESTONE, GeologicaBlocks.VERY_STRONG_COBBLE_WALL);
		addWallRecipe(GeologicaBlocks.MEDIUM_STONE_BRICK, GeologicaBlocks.MEDIUM_STONE_BRICK_WALL);
		addWallRecipe(GeologicaBlocks.STRONG_STONE_BRICK, GeologicaBlocks.STRONG_STONE_BRICK_WALL);
		addWallRecipe(GeologicaBlocks.VERY_STRONG_STONE_BRICK, GeologicaBlocks.VERY_STRONG_STONE_BRICK_WALL);
		addBrickRecipe(GeologicaBlocks.MEDIUM_STONE, GeologicaBlocks.MEDIUM_STONE_BRICK);
		addBrickRecipe(GeologicaBlocks.STRONG_STONE, GeologicaBlocks.STRONG_STONE_BRICK);
		addBrickRecipe(GeologicaBlocks.VERY_STRONG_STONE, GeologicaBlocks.VERY_STRONG_STONE_BRICK);
		addStairsRecipe(GeologicaBlocks.LIMESTONE_COBBLE_STAIRS);
		addStairsRecipe(GeologicaBlocks.GRANITE_COBBLE_STAIRS);
		addStairsRecipe(GeologicaBlocks.MARBLE_COBBLE_STAIRS);
		addStairsRecipe(GeologicaBlocks.LIMESTONE_BRICK_STAIRS);
		addStairsRecipe(GeologicaBlocks.GRANITE_BRICK_STAIRS);
		addStairsRecipe(GeologicaBlocks.MARBLE_BRICK_STAIRS);
	}

	private static void addGrindingRecipes() {
		addStoneGrindingRecipes();
		addCobbleGrindingRecipes();
	}

	private static void addCobbleGrindingRecipes() {
		addCobbleGrindingRecipe(GeoMaterial.ANDESITE, Block.sand, IndustrialMinerals.FELDSPAR, 0.1);
		addCobbleGrindingRecipe(GeoMaterial.BRECCIA, new ItemStack(Block.gravel, 2));
		addCobbleGrindingRecipe(GeoMaterial.CARBONATITE, Block.sand, Compounds.CaCO3, 0.5);
		addCobbleGrindingRecipe(GeoMaterial.CONGLOMERATE, new ItemStack(Block.sand), new ItemStack(Block.gravel), 1.0);
		addCobbleGrindingRecipe(GeoMaterial.CLAYSTONE, new ItemStack(GeologicaItems.CLAY_DUST, 2));
		addCobbleGrindingRecipe(GeoMaterial.DIORITE, Block.sand, IndustrialMinerals.FELDSPAR, 0.1);
		addCobbleGrindingRecipe(GeoMaterial.GABBRO, Block.sand, IndustrialMinerals.FELDSPAR, 0.2);
		addCobbleGrindingRecipe(GeoMaterial.GNEISS, Block.sand, IndustrialMinerals.FELDSPAR, 0.1);
		addCobbleGrindingRecipe(GeoMaterial.GRANITE, Block.sand, IndustrialMinerals.QUARTZ, 0.1);
		addCobbleGrindingRecipe(GeoMaterial.GREENSCHIST, Block.sand, IndustrialMinerals.CHRYSOTILE, 0.1);
		addCobbleGrindingRecipe(GeoMaterial.HORNFELS, Block.sand, IndustrialMinerals.MICA, 0.2);
		addCobbleGrindingRecipe(GeoMaterial.LIMESTONE, Block.sand, Compounds.CaCO3, 0.5);
		addCobbleGrindingRecipe(GeoMaterial.MARBLE, Block.sand, Compounds.CaCO3, 1.0);
		addCobbleGrindingRecipe(GeoMaterial.MUDSTONE, new ItemStack(Block.sand), new ItemStack(GeologicaItems.CLAY_DUST), 0.1);
		addCobbleGrindingRecipe(GeoMaterial.PEGMATITE, IndustrialMinerals.FELDSPAR, IndustrialMinerals.QUARTZ, 1.0);
		addCobbleGrindingRecipe(GeoMaterial.PERIDOTITE, Block.sand, IndustrialMinerals.OLIVINE, 0.5);
		addCobbleGrindingRecipe(GeoMaterial.RHYOLITE, Block.sand, IndustrialMinerals.QUARTZ, 0.1);
		addCobbleGrindingRecipe(GeoMaterial.SCHIST, Block.sand, IndustrialMinerals.MICA, 0.2);
		addCobbleGrindingRecipe(GeoMaterial.SERPENTINITE, Block.sand, IndustrialMinerals.CHRYSOTILE, 0.1);
		addCobbleGrindingRecipe(GeoMaterial.SLATE, Block.sand, IndustrialMinerals.MICA, 0.1);
		addCobbleGrindingRecipe(GeoMaterial.SKARN, Block.sand, IndustrialMinerals.WOLLASTONITE, 0.1);
		addCobbleGrindingRecipe(GeoMaterial.QUARTZITE, new ItemStack(Block.sand, 2));
	}

	private static void addStoneGrindingRecipes() {
		addStoneGrindingRecipes(GeologicaBlocks.WEAK_STONE, GeologicaBlocks.WEAK_RUBBLE);
		addStoneGrindingRecipes(GeologicaBlocks.MEDIUM_STONE, GeologicaBlocks.MEDIUM_COBBLESTONE);
		addStoneGrindingRecipes(GeologicaBlocks.STRONG_STONE, GeologicaBlocks.STRONG_COBBLESTONE);
		addStoneGrindingRecipes(GeologicaBlocks.VERY_STRONG_STONE, GeologicaBlocks.VERY_STRONG_COBBLESTONE);
	}

	private static void addMeltingRecipes() {
		addStoneMeltingRecipes(GeologicaBlocks.MEDIUM_STONE);
		addStoneMeltingRecipes(GeologicaBlocks.MEDIUM_COBBLESTONE);
		addStoneMeltingRecipes(GeologicaBlocks.STRONG_STONE);
		addStoneMeltingRecipes(GeologicaBlocks.STRONG_COBBLESTONE);
		addStoneMeltingRecipes(GeologicaBlocks.VERY_STRONG_STONE);
		addStoneMeltingRecipes(GeologicaBlocks.VERY_STRONG_COBBLESTONE);
	}

	private static void addStoneMeltingRecipes(GeoBlock block) {
		addMeltingRecipe(new ItemStack(block), new FluidStack(FluidRegistry.LAVA, FluidContainerRegistry.BUCKET_VOLUME),
				block.getStrength());
	}
	
	private static void addMeltingRecipe(ItemStack solid, FluidStack liquid, Strength strength) {
		TEIntegration.addCrucibleRecipe(solid, liquid, strength);
	}

	private static void addSlabRecipe(CompositeBlock input, Block output) {
		addCraftingRecipesByMeta(input, output, 6, "###");
	}
	private static void addWallRecipe(CompositeBlock input, Block output) {
		addCraftingRecipesByMeta(input, output, 6, "###", "###");
	}
	private static void addBrickRecipe(CompositeBlock input, Block output) {
		addCraftingRecipesByMeta(input, output, 4, "##", "##");
	}
	
	private static void addCraftingRecipesByMeta(CompositeBlock input, Block output, int outputSize, String... shape) {
		for(int meta = 0; meta < input.getMetaCount(); meta++) {
			ItemStack outputStack = new ItemStack(output, outputSize, meta);
			ItemStack inputStack = new ItemStack(input, 1, meta);
			GameRegistry.addRecipe(outputStack, shape, shape[0].charAt(0), inputStack);
		}
	}

	private static void addStairsRecipe(StairsBlock output) {
		ItemStack outputStack = new ItemStack(output, 4, output.getModelBlockMeta());
		ItemStack inputStack = new ItemStack(output.getModelBlock(), 1, output.getModelBlockMeta());
		GameRegistry.addRecipe(outputStack, "#  ", "## ", "###", '#', inputStack);
	}
	
	private static void addSmeltingRecipesByMeta(CompositeBlock input, Block output, SmeltingTemperature temperature) {
		for(int meta = 0; meta < input.getMetaCount(); meta++) {
			ItemStack outputStack = new ItemStack(output, 1, meta);
			FurnaceRecipes.smelting().addSmelting(input.blockID, meta, outputStack, 0);
			TEIntegration.addFurnaceRecipe(new ItemStack(input, 1, meta), outputStack, temperature);
		}
	}

	private static void oreDictifyGeoBlocks() {
		for (Block block : GeologicaBlocks.getBlocks()) {
			oreDictify(block);
		}
	}
	
	private static void oreDictify(Block block) {
		if (block instanceof GeoBlock) {
			oreDictify((GeoBlock)block);
		} else if (block instanceof ProxyBlock) {
			oreDictify((ProxyBlock)block);
		}
	}
	
	private static void oreDictify(GeoBlock block) {
		if (block.hasComposition(Mixture.class)) {
			oreDictifyAggregate(block);
		} else if (block.hasComposition(OreMineral.class)) {
			oreDictifyOre(block);
		}
	}

	private static void oreDictifyOre(GeoBlock block) {
		for (GeoMaterial material : block.getSubstances()) {
			oreDictifyOre(block, material);
		}
	}

	private static void oreDictifyOre(GeoBlock block, GeoMaterial substance) {
		String postfix = substance.getOreDictKey();
		if (postfix == null ||Geologica.isTechnical()) {
			postfix = substance.getLowerName();
		}
		oreDictifyOre(postfix, block.getItemStack(substance));
	}
	
	private static String oreDictKey(String prefix, String postfix) {
		return prefix + CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_CAMEL, postfix);
	}
	private static void oreDictifyOre(String postfix, ItemStack itemStack) {
		String key = oreDictKey("ore", postfix);
		OreDictionary.registerOre(key, itemStack);
		ItemStack smeltingOutput = getSmeltingOutput(key);
		if (smeltingOutput != null) {
			FurnaceRecipes.smelting().addSmelting(itemStack.itemID, itemStack.getItemDamage(), smeltingOutput, 0);
		}		
	}


	private static ItemStack getSmeltingOutput(String key) {
		List<ItemStack> ores = OreDictionary.getOres(key);
		for (ItemStack ore : ores) {
			ItemStack output = FurnaceRecipes.smelting().getSmeltingResult(ore);
			if (output != null) {
				return output;
			}
		}
		return null;
	}

	private static String getAggregateOreDictKey(GeoBlock block) {
		if (block.getMaterial() == Material.clay) {
			return "clay";
		} else if (block.getMaterial() == Material.sand) {
			return "sand";
		} else if (block instanceof BrokenGeoBlock) {
			return "cobblestone";
		} else if (block instanceof BrickGeoBlock) {
			return "stoneBrick";
		} else if (block instanceof LooseGeoBlock) {
			return "rubble";
		}
		return "stone";
	}
	
	private static void oreDictify(ProxyBlock block) {
		String prefix = null;
		if (block instanceof StairsBlock) {
			prefix = "stair";
		} else if (block instanceof SlabBlock) {
			prefix = "slab";
		} else if (block instanceof WallBlock) {
			prefix = "wall";
		}
		String postfix = getAggregateOreDictKey((GeoBlock)block.getModelBlock());
		String key = oreDictKey(prefix, postfix);
		OreDictionary.registerOre(key, new ItemStack((Block)block, 1, OreDictionary.WILDCARD_VALUE));
	}

	private static void oreDictifyAggregate(GeoBlock block) {
		String key = getAggregateOreDictKey(block);
		OreDictionary.registerOre(key, new ItemStack(block, 1, OreDictionary.WILDCARD_VALUE));
		if (block instanceof BrickGeoBlock) // for recipes that accept any type of stone brick (mossy, etc)
			OreDictionary.registerOre("stoneBricks", new ItemStack(block, 1, OreDictionary.WILDCARD_VALUE));
	}
	
	private static GeoBlock getCobbleBlock(Strength strength) {
		switch(strength) {
		case WEAK:
			return GeologicaBlocks.WEAK_RUBBLE;
		case MEDIUM:
			return GeologicaBlocks.MEDIUM_COBBLESTONE;
		case STRONG:
			return GeologicaBlocks.STRONG_COBBLESTONE;
		case VERY_STRONG:
			return GeologicaBlocks.VERY_STRONG_COBBLESTONE;
		}
		return null;
	}

	private static void addCobbleGrindingRecipe(GeoMaterial material,
			IndustrialMinerals primaryDust, IndustrialMinerals secondaryDust, double secondaryChance) {
		addCobbleGrindingRecipe(material, GeologicaItems.CRUDE_DUST.getItemStack(primaryDust, 2), secondaryDust, 
				secondaryChance);
	}
	private static void addCobbleGrindingRecipe(GeoMaterial material, Block primaryOutput, 
			IndustrialMinerals secondaryDust, double secondaryChance) {
		addCobbleGrindingRecipe(material, new ItemStack(primaryOutput),  secondaryDust, secondaryChance);
	}
	private static void addCobbleGrindingRecipe(GeoMaterial material, ItemStack primaryOutput, 
			IndustrialMinerals secondaryDust, double secondaryChance) {
		ItemStack secondaryOutput = GeologicaItems.CRUDE_DUST.getItemStack(secondaryDust);
		addCobbleGrindingRecipe(material, primaryOutput, secondaryOutput, secondaryChance);
	}
	
	private static void addCobbleGrindingRecipe(GeoMaterial material, Block primaryOutput, 
			Compounds secondaryDust, double secondaryChance) {
		addCobbleGrindingRecipe(material, new ItemStack(primaryOutput),  secondaryDust, secondaryChance);
	}
	private static void addCobbleGrindingRecipe(GeoMaterial material, ItemStack primaryOutput, 
			Compounds secondaryDust, double secondaryChance) {
		ItemStack secondaryOutput = ChemicaItems.DUST.getItemStack(secondaryDust);
		addCobbleGrindingRecipe(material, primaryOutput, secondaryOutput, secondaryChance);
	}
	
	private static void addCobbleGrindingRecipe(GeoMaterial material, ItemStack primaryOutput, 
			ItemStack secondaryOutput, double secondaryChance) 
	{
		ItemStack input = getCobbleBlock(material.getStrength()).getItemStack(material);
		addGrindingRecipe(input, primaryOutput, secondaryOutput, secondaryChance, material.getStrength());
	}
	private static void addCobbleGrindingRecipe(GeoMaterial material, ItemStack primaryOutput) 
	{
		ItemStack input = getCobbleBlock(material.getStrength()).getItemStack(material);
		addGrindingRecipe(input, primaryOutput, null, 0, material.getStrength());
	}

	private static void addStoneGrindingRecipes(GeoBlock intact, GeoBlock broken) {
		for (GeoMaterial material : intact.getSubstances()) {
			addGrindingRecipe(intact.getItemStack(material), broken.getItemStack(material), null, 0, intact.getStrength());
		}
	}
	
	private static void addGrindingRecipe(ItemStack input, ItemStack output, ItemStack secondaryOutput, 
			double secondaryChance, Strength strength) {
		TEIntegration.addPulverizerRecipe(input, output, secondaryOutput, secondaryChance, strength);
		if (strength == Strength.WEAK || strength == Strength.MEDIUM) {
			IC2Integration.addMaceratorRecipe(input, output);
		}
	}

	private static void registerMicroblocks() {
		if (Loader.isModLoaded("ForgeMicroblock")) {
			FMPIntegration.registerMicroblock(GeologicaBlocks.WEAK_STONE);
			FMPIntegration.registerMicroblock(GeologicaBlocks.MEDIUM_STONE);
			FMPIntegration.registerMicroblock(GeologicaBlocks.STRONG_STONE);
			FMPIntegration.registerMicroblock(GeologicaBlocks.VERY_STRONG_STONE);
			FMPIntegration.registerMicroblock(GeologicaBlocks.MEDIUM_COBBLESTONE);
			FMPIntegration.registerMicroblock(GeologicaBlocks.STRONG_COBBLESTONE);
			FMPIntegration.registerMicroblock(GeologicaBlocks.VERY_STRONG_COBBLESTONE);
		}
	}

}
