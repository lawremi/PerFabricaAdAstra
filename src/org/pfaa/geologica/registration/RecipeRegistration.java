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

import org.pfaa.RecipeUtils;
import org.pfaa.block.CompositeBlock;
import org.pfaa.chemica.ChemicaItems;
import org.pfaa.chemica.model.Mixture;
import org.pfaa.chemica.model.Molecule.Molecules;
import org.pfaa.geologica.GeoMaterial;
import org.pfaa.geologica.GeoMaterial.Strength;
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
import org.pfaa.geologica.integration.IC2Integration;
import org.pfaa.geologica.integration.TEIntegration;
import org.pfaa.geologica.processing.IndustrialMineral.IndustrialMinerals;
import org.pfaa.geologica.processing.Ore;
import org.pfaa.geologica.processing.Ore.SmeltingTemperature;

import com.google.common.base.CaseFormat;

import cpw.mods.fml.common.registry.GameRegistry;

public class RecipeRegistration {
	public static void init() {
		registerOres();
		addSmeltingRecipes();
		addCraftingRecipes();
		addGrindingRecipes();
		addMeltingRecipes();
		addStoneToolRecipes();
	}
	
	private static void addStoneToolRecipes() {
		addStoneToolRecipes(GeologicaBlocks.WEAK_RUBBLE, 0.75F);
		addStoneToolRecipes(GeologicaBlocks.MEDIUM_COBBLESTONE, 0.50F);
		addStoneToolRecipes(GeologicaBlocks.STRONG_COBBLESTONE, 0F);
		addStoneToolRecipes(GeologicaBlocks.VERY_STRONG_COBBLESTONE, 0F);
	}

	private static void addStoneToolRecipes(GeoBlock block, float damage) {
		addStoneToolRecipe(block, Item.pickaxeStone, damage);
		addStoneToolRecipe(block, Item.axeStone, damage);
		addStoneToolRecipe(block, Item.shovelStone, damage);
		addStoneToolRecipe(block, Item.hoeStone, damage);
		addStoneToolRecipe(block, Item.swordStone, damage);
	}

	private static void addStoneToolRecipe(GeoBlock block, Item tool, float damage) {
		ItemStack damaged = new ItemStack(tool, 1, (int)(damage * tool.getMaxDamage()));
		ItemStack material = new ItemStack(block, 1, OreDictionary.WILDCARD_VALUE);
		ItemStack cobblestone = new ItemStack(Block.cobblestone, 1, OreDictionary.WILDCARD_VALUE);
		List<IRecipe> recipes = (List<IRecipe>)CraftingManager.getInstance().getRecipeList();
		for (IRecipe recipe : recipes) {
			ItemStack output = recipe.getRecipeOutput();
			if (output != null && output.getItem() == tool) {
				ShapedOreRecipe shapedRecipe = (ShapedOreRecipe)recipe;
				Object[] ingredients = shapedRecipe.getInput().clone();
				for (int i = 0; i < ingredients.length; i++) {
					if (ingredients[i] instanceof ItemStack && 
							cobblestone.isItemEqual((ItemStack)ingredients[i])) 
					{
						ingredients[i] = material;
					}
				}
				recipes.add(RecipeUtils.recreateOreRecipe(shapedRecipe, damaged, ingredients));
				break;
			}
		}
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
		addWallRecipe(GeologicaBlocks.MEDIUM_COBBLESTONE, GeologicaBlocks.MEDIUM_COBBLE_WALL);
		addWallRecipe(GeologicaBlocks.STRONG_COBBLESTONE, GeologicaBlocks.STRONG_COBBLE_WALL);
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
		addCobbleGrindingRecipe(GeoMaterial.CARBONATITE, Block.sand, Molecules.CaCO3, 0.5);
		addCobbleGrindingRecipe(GeoMaterial.CONGLOMERATE, new ItemStack(Block.sand), new ItemStack(Block.gravel), 1.0);
		addCobbleGrindingRecipe(GeoMaterial.CLAYSTONE, new ItemStack(GeologicaItems.CLAY_DUST, 2));
		addCobbleGrindingRecipe(GeoMaterial.DIORITE, Block.sand, IndustrialMinerals.FELDSPAR, 0.1);
		addCobbleGrindingRecipe(GeoMaterial.GABBRO, Block.sand, IndustrialMinerals.FELDSPAR, 0.2);
		addCobbleGrindingRecipe(GeoMaterial.GNEISS, Block.sand, IndustrialMinerals.FELDSPAR, 0.1);
		addCobbleGrindingRecipe(GeoMaterial.GRANITE, Block.sand, IndustrialMinerals.QUARTZ, 0.1);
		addCobbleGrindingRecipe(GeoMaterial.GREENSCHIST, Block.sand, IndustrialMinerals.CHRYSOTILE, 0.1);
		addCobbleGrindingRecipe(GeoMaterial.HORNFELS, Block.sand, IndustrialMinerals.MICA, 0.2);
		addCobbleGrindingRecipe(GeoMaterial.LIMESTONE, Block.sand, Molecules.CaCO3, 0.5);
		addCobbleGrindingRecipe(GeoMaterial.MARBLE, Block.sand, Molecules.CaCO3, 1.0);
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
		addCraftingRecipesByMeta(input, output, "###");
	}
	private static void addWallRecipe(CompositeBlock input, Block output) {
		addCraftingRecipesByMeta(input, output, "###", "###");
	}
	private static void addBrickRecipe(CompositeBlock input, Block output) {
		addCraftingRecipesByMeta(input, output, "##", "##");
	}
	
	private static void addCraftingRecipesByMeta(CompositeBlock input, Block output, String... shape) {
		for(int meta = 0; meta < input.getMetaCount(); meta++) {
			ItemStack outputStack = new ItemStack(output, 1, meta);
			ItemStack inputStack = new ItemStack(input, 1, meta);
			GameRegistry.addRecipe(outputStack, shape, shape[0].charAt(0), inputStack);
		}
	}

	private static void addStairsRecipe(StairsBlock output) {
		ItemStack outputStack = new ItemStack(output, 1, output.getModelBlockMeta());
		GameRegistry.addRecipe(outputStack, "#  ", "## ", "###", '#', output.getModelBlock());
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
		} else if (block.hasComposition(Ore.class)) {
			oreDictifyOre(block);
		}
	}

	private static void oreDictifyOre(GeoBlock block) {
		for (GeoMaterial material : block.getSubstances()) {
			oreDictifyOre(block, material);
		}
	}

	private static void oreDictifyOre(GeoBlock block, GeoMaterial material) {
		String postfix = material.getOreDictKey();
		if (postfix == null) {
			postfix = material.getLowerName();
		}
		String key = "ore" + CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_CAMEL, postfix);
		OreDictionary.registerOre(key, block.getItemStack(material));
	}

	private static String getAggregateOreDictKey(GeoBlock block) {
		if (block.getMaterial() == Material.clay) {
			return "clay";
		} else if (block.getMaterial() == Material.sand) {
			return "sand";
		} else if (block instanceof BrokenGeoBlock) {
			return "cobble";
		} else if (block instanceof BrickGeoBlock) {
			return "stoneBrick";
		} else if (block instanceof LooseGeoBlock) {
			return "rubble";
		}
		return "stone";
	}
	
	private static void oreDictify(ProxyBlock block) {
		String postfix = null;
		if (block instanceof StairsBlock) {
			postfix = "Stair";
		} else if (block instanceof SlabBlock) {
			postfix = "Slab";
		} else if (block instanceof WallBlock) {
			postfix = "Wall";
		}
		String key = getAggregateOreDictKey((GeoBlock)block.getModelBlock()) + postfix;
		OreDictionary.registerOre(key, new ItemStack((Block)block, 1, OreDictionary.WILDCARD_VALUE));
	}

	private static void oreDictifyAggregate(GeoBlock block) {
		String key = "block" + CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_CAMEL, getAggregateOreDictKey(block));
		OreDictionary.registerOre(key, new ItemStack(block, 1, OreDictionary.WILDCARD_VALUE));
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
			Molecules secondaryDust, double secondaryChance) {
		addCobbleGrindingRecipe(material, new ItemStack(primaryOutput),  secondaryDust, secondaryChance);
	}
	private static void addCobbleGrindingRecipe(GeoMaterial material, ItemStack primaryOutput, 
			Molecules secondaryDust, double secondaryChance) {
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

}
