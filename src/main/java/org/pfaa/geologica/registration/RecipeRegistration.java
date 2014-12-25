package org.pfaa.geologica.registration;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
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
import org.pfaa.chemica.model.Compound.Compounds;
import org.pfaa.geologica.GeoMaterial;
import org.pfaa.geologica.GeoMaterial.Strength;
import org.pfaa.geologica.Geologica;
import org.pfaa.geologica.GeologicaBlocks;
import org.pfaa.geologica.GeologicaItems;
import org.pfaa.geologica.block.BrickGeoBlock;
import org.pfaa.geologica.block.BrokenGeoBlock;
import org.pfaa.geologica.block.ChanceDropRegistry;
import org.pfaa.geologica.block.GeoBlock;
import org.pfaa.geologica.block.LooseGeoBlock;
import org.pfaa.geologica.block.ProxyBlock;
import org.pfaa.geologica.block.SlabBlock;
import org.pfaa.geologica.block.StairsBlock;
import org.pfaa.geologica.block.VanillaOreOverrideBlock;
import org.pfaa.geologica.block.WallBlock;
import org.pfaa.geologica.integration.FMPIntegration;
import org.pfaa.geologica.integration.IC2Integration;
import org.pfaa.geologica.integration.TCIntegration;
import org.pfaa.geologica.integration.TEIntegration;
import org.pfaa.geologica.processing.Aggregate;
import org.pfaa.geologica.processing.Crude;
import org.pfaa.geologica.processing.IndustrialMineral.IndustrialMinerals;
import org.pfaa.geologica.processing.Ore;
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
		registerOreDrops();
	}
	
	private static void addStoneAbstractionRecipesForBrokenMods() {
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(Blocks.cobblestone), "cobblestone"));
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(Blocks.stone), "stone"));
	}

	private static void addStoneToolRecipes() {
		addStoneToolRecipes(GeologicaBlocks.WEAK_RUBBLE);
		addStoneToolRecipes(GeologicaBlocks.MEDIUM_COBBLE);
		addStoneToolRecipes(GeologicaBlocks.STRONG_COBBLE);
		addStoneToolRecipes(GeologicaBlocks.VERY_STRONG_COBBLE);
		if (Loader.isModLoaded("TConstruct"))
			TCIntegration.addStoneMaterials();
	}

	private static void addStoneToolRecipes(GeoBlock block) {
		addStoneToolRecipe(block, Items.stone_pickaxe);
		addStoneToolRecipe(block, Items.stone_axe);
		addStoneToolRecipe(block, Items.stone_shovel);
		addStoneToolRecipe(block, Items.stone_hoe);
		addStoneToolRecipe(block, Items.stone_sword);
	}

	private static void addStoneToolRecipe(GeoBlock block, Item tool) {
		ItemStack damaged = new ItemStack(tool, 1, (int)(getInitialStoneToolDamage(block.getStrength()) * tool.getMaxDamage()));
		ItemStack material = new ItemStack(block, 1, OreDictionary.WILDCARD_VALUE);
		ItemStack cobblestone = new ItemStack(Blocks.cobblestone, 1, OreDictionary.WILDCARD_VALUE);
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
	
	private static void registerOres() {
		oreDictifyGeoBlocks();
		oreDictifyStoneBrick();
	}

	private static void addSmeltingRecipes() {
		addSmeltingRecipesByMeta(GeologicaBlocks.WEAK_RUBBLE, GeologicaBlocks.WEAK_STONE, SmeltingTemperature.MEDIUM);
		addSmeltingRecipesByMeta(GeologicaBlocks.MEDIUM_COBBLE, GeologicaBlocks.MEDIUM_STONE, SmeltingTemperature.MEDIUM);
		addSmeltingRecipesByMeta(GeologicaBlocks.STRONG_COBBLE, GeologicaBlocks.STRONG_STONE, SmeltingTemperature.HIGH);
	}

	private static void addCraftingRecipes() {
		addSlabRecipe(GeologicaBlocks.MEDIUM_COBBLE, GeologicaBlocks.MEDIUM_COBBLE_SLAB);
		addSlabRecipe(GeologicaBlocks.STRONG_COBBLE, GeologicaBlocks.STRONG_COBBLE_SLAB);
		addSlabRecipe(GeologicaBlocks.MEDIUM_STONE_BRICK, GeologicaBlocks.MEDIUM_STONE_BRICK_SLAB);
		addSlabRecipe(GeologicaBlocks.STRONG_STONE_BRICK, GeologicaBlocks.STRONG_STONE_BRICK_SLAB);
		addSlabRecipe(GeologicaBlocks.VERY_STRONG_STONE_BRICK, GeologicaBlocks.VERY_STRONG_STONE_BRICK_SLAB);
		addSlabRecipe(GeologicaBlocks.MEDIUM_STONE, GeologicaBlocks.MEDIUM_STONE_SLAB);
		addSlabRecipe(GeologicaBlocks.STRONG_STONE, GeologicaBlocks.STRONG_STONE_SLAB);
		addSlabRecipe(GeologicaBlocks.VERY_STRONG_STONE, GeologicaBlocks.VERY_STRONG_STONE_SLAB);
		addWallRecipe(GeologicaBlocks.MEDIUM_COBBLE, GeologicaBlocks.MEDIUM_COBBLE_WALL);
		addWallRecipe(GeologicaBlocks.STRONG_COBBLE, GeologicaBlocks.STRONG_COBBLE_WALL);
		addWallRecipe(GeologicaBlocks.VERY_STRONG_COBBLE, GeologicaBlocks.VERY_STRONG_COBBLE_WALL);
		addWallRecipe(GeologicaBlocks.MEDIUM_STONE_BRICK, GeologicaBlocks.MEDIUM_STONE_BRICK_WALL);
		addWallRecipe(GeologicaBlocks.STRONG_STONE_BRICK, GeologicaBlocks.STRONG_STONE_BRICK_WALL);
		addWallRecipe(GeologicaBlocks.VERY_STRONG_STONE_BRICK, GeologicaBlocks.VERY_STRONG_STONE_BRICK_WALL);
		addBrickRecipe(GeologicaBlocks.MEDIUM_STONE, GeologicaBlocks.MEDIUM_STONE_BRICK);
		addBrickRecipe(GeologicaBlocks.STRONG_STONE, GeologicaBlocks.STRONG_STONE_BRICK);
		addBrickRecipe(GeologicaBlocks.VERY_STRONG_STONE, GeologicaBlocks.VERY_STRONG_STONE_BRICK);
		addStairsRecipe(GeologicaBlocks.MEDIUM_COBBLE_STAIRS__LIMESTONE);
		addStairsRecipe(GeologicaBlocks.STRONG_COBBLE_STAIRS__GRANITE);
		addStairsRecipe(GeologicaBlocks.STRONG_COBBLE_STAIRS__MARBLE);
		addStairsRecipe(GeologicaBlocks.MEDIUM_STONE_BRICK_STAIRS__LIMESTONE);
		addStairsRecipe(GeologicaBlocks.STRONG_STONE_BRICK_STAIRS__GRANITE);
		addStairsRecipe(GeologicaBlocks.STRONG_STONE_BRICK_STAIRS__MARBLE);
	}

	private static void addGrindingRecipes() {
		addStoneGrindingRecipes();
		addCobbleGrindingRecipes();
	}

	private static void addCobbleGrindingRecipes() {
		addCobbleGrindingRecipe(GeoMaterial.ANDESITE, Blocks.sand, IndustrialMinerals.FELDSPAR, 0.1);
		addCobbleGrindingRecipe(GeoMaterial.BRECCIA, new ItemStack(Blocks.gravel, 2));
		addCobbleGrindingRecipe(GeoMaterial.CARBONATITE, Blocks.sand, Compounds.CaCO3, 0.5);
		addCobbleGrindingRecipe(GeoMaterial.CONGLOMERATE, new ItemStack(Blocks.sand), new ItemStack(Blocks.gravel), 1.0);
		addCobbleGrindingRecipe(GeoMaterial.CLAYSTONE, new ItemStack(GeologicaItems.CLAY_DUST, 2));
		addCobbleGrindingRecipe(GeoMaterial.DIORITE, Blocks.sand, IndustrialMinerals.FELDSPAR, 0.1);
		addCobbleGrindingRecipe(GeoMaterial.GABBRO, Blocks.sand, IndustrialMinerals.FELDSPAR, 0.2);
		addCobbleGrindingRecipe(GeoMaterial.GNEISS, Blocks.sand, IndustrialMinerals.FELDSPAR, 0.1);
		addCobbleGrindingRecipe(GeoMaterial.GRANITE, Blocks.sand, IndustrialMinerals.QUARTZ, 0.1);
		addCobbleGrindingRecipe(GeoMaterial.GREENSCHIST, Blocks.sand, IndustrialMinerals.CHRYSOTILE, 0.1);
		addCobbleGrindingRecipe(GeoMaterial.HORNFELS, Blocks.sand, IndustrialMinerals.MICA, 0.2);
		addCobbleGrindingRecipe(GeoMaterial.LIMESTONE, Blocks.sand, Compounds.CaCO3, 0.5);
		addCobbleGrindingRecipe(GeoMaterial.MARBLE, Blocks.sand, Compounds.CaCO3, 1.0);
		addCobbleGrindingRecipe(GeoMaterial.MUDSTONE, new ItemStack(Blocks.sand), new ItemStack(GeologicaItems.CLAY_DUST), 0.1);
		addCobbleGrindingRecipe(GeoMaterial.PEGMATITE, IndustrialMinerals.FELDSPAR, IndustrialMinerals.QUARTZ, 1.0);
		addCobbleGrindingRecipe(GeoMaterial.PERIDOTITE, Blocks.sand, IndustrialMinerals.OLIVINE, 0.5);
		addCobbleGrindingRecipe(GeoMaterial.RHYOLITE, Blocks.sand, IndustrialMinerals.QUARTZ, 0.1);
		addCobbleGrindingRecipe(GeoMaterial.SCHIST, Blocks.sand, IndustrialMinerals.MICA, 0.2);
		addCobbleGrindingRecipe(GeoMaterial.SERPENTINITE, Blocks.sand, IndustrialMinerals.CHRYSOTILE, 0.1);
		addCobbleGrindingRecipe(GeoMaterial.SLATE, Blocks.sand, IndustrialMinerals.MICA, 0.1);
		addCobbleGrindingRecipe(GeoMaterial.SKARN, Blocks.sand, IndustrialMinerals.WOLLASTONITE, 0.1);
		addCobbleGrindingRecipe(GeoMaterial.QUARTZITE, new ItemStack(Blocks.sand, 2));
	}

	private static void addStoneGrindingRecipes() {
		addStoneGrindingRecipes(GeologicaBlocks.WEAK_STONE, GeologicaBlocks.WEAK_RUBBLE);
		addStoneGrindingRecipes(GeologicaBlocks.MEDIUM_STONE, GeologicaBlocks.MEDIUM_COBBLE);
		addStoneGrindingRecipes(GeologicaBlocks.STRONG_STONE, GeologicaBlocks.STRONG_COBBLE);
		addStoneGrindingRecipes(GeologicaBlocks.VERY_STRONG_STONE, GeologicaBlocks.VERY_STRONG_COBBLE);
	}

	private static void addMeltingRecipes() {
		addStoneMeltingRecipes(GeologicaBlocks.MEDIUM_STONE);
		addStoneMeltingRecipes(GeologicaBlocks.MEDIUM_COBBLE);
		addStoneMeltingRecipes(GeologicaBlocks.STRONG_STONE);
		addStoneMeltingRecipes(GeologicaBlocks.STRONG_COBBLE);
		addStoneMeltingRecipes(GeologicaBlocks.VERY_STRONG_STONE);
		addStoneMeltingRecipes(GeologicaBlocks.VERY_STRONG_COBBLE);
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
			FurnaceRecipes.smelting().func_151394_a(new ItemStack(input, 1, meta), outputStack, 0);
			TEIntegration.addFurnaceRecipe(new ItemStack(input, 1, meta), outputStack, temperature);
		}
	}

	// FIXME: remove this hack when we get this into Forge
	private static void oreDictifyStoneBrick() {
		OreDictionary.registerOre("stoneBricks", Blocks.stonebrick);
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
		} else if (block instanceof VanillaOreOverrideBlock) {
			oreDictify((VanillaOreOverrideBlock)block);
		}
	}
	
	private static void oreDictify(GeoBlock block) {
		for (GeoMaterial material : block.getGeoMaterials()) {
			if (block.hasComposition(Aggregate.class)) {
				oreDictifyAggregate(block, material);
			} else if (block.hasComposition(Ore.class) || 
				(block.hasComposition(Crude.class) && block.getMaterial() == Material.rock)) {
				oreDictifyOre(block, material);
			}
		}
	}

	private static void oreDictifyOre(GeoBlock block, GeoMaterial substance) {
		ItemStack oreStack = block.getItemStack(substance);
		String postfix = substance.getOreDictKey();
		if (postfix != null && !Geologica.isTechnical()) {
		    oreDictifyOre(postfix, oreStack);
		}
		oreDictifyOre(substance.getLowerName(), oreStack);
	}
	
	private static String oreDictKey(String prefix, String postfix) {
		return prefix + CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_CAMEL, postfix);
	}
	private static void oreDictifyOre(String postfix, ItemStack itemStack) {
		String key = oreDictKey("ore", postfix);
		OreDictionary.registerOre(key, itemStack);
		ItemStack smeltingOutput = getSmeltingOutput(key);
		if (smeltingOutput != null) {
			FurnaceRecipes.smelting().func_151394_a(itemStack, smeltingOutput, 0);
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
			return "stoneBricks";
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

	private static void oreDictifyAggregate(GeoBlock block, GeoMaterial material) {
		String key = getAggregateOreDictKey(block);
		ItemStack itemStack = block.getItemStack(material);
		OreDictionary.registerOre(key, itemStack);
	}
	
	private static void oreDictify(VanillaOreOverrideBlock block) {
		String name = Block.blockRegistry.getNameForObject(block);
		if (name != null) {
			String material = name.substring(name.indexOf(':') + 1, name.length() - 3);
			OreDictionary.registerOre(oreDictKey("ore", material), new ItemStack(block, 1, OreDictionary.WILDCARD_VALUE));
		}
	}
	
	private static GeoBlock getCobbleBlock(Strength strength) {
		switch(strength) {
		case WEAK:
			return GeologicaBlocks.WEAK_RUBBLE;
		case MEDIUM:
			return GeologicaBlocks.MEDIUM_COBBLE;
		case STRONG:
			return GeologicaBlocks.STRONG_COBBLE;
		case VERY_STRONG:
			return GeologicaBlocks.VERY_STRONG_COBBLE;
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
		//ItemStack secondaryOutput = ChemicaItems.DUST.getItemStack(secondaryDust);
		//addCobbleGrindingRecipe(material, primaryOutput, secondaryOutput, secondaryChance);
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
		for (GeoMaterial material : intact.getGeoMaterials()) {
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
			FMPIntegration.registerMicroblock(GeologicaBlocks.MEDIUM_COBBLE);
			FMPIntegration.registerMicroblock(GeologicaBlocks.STRONG_COBBLE);
			FMPIntegration.registerMicroblock(GeologicaBlocks.VERY_STRONG_COBBLE);
		}
	}

	private static void registerOreDrops() {
		ChanceDropRegistry drops = ChanceDropRegistry.instance();
		registerOreDrop(drops, GeoMaterial.CONGLOMERATE, "nuggetCopper", 1, 3, 0.1F, true);
		registerOreDrop(drops, GeoMaterial.GARNET_SAND, Items.gold_nugget, 4, 4, 0.1F, true);
		registerOreDrop(drops, GeoMaterial.GARNET_SAND, "nuggetElectrum", 2, 2, 0.05F, true);
		registerOreDrop(drops, GeoMaterial.GARNET_SAND, "nuggetSilver", 1, 2, 0.05F, true);
		if (Geologica.getConfiguration().isVanillaOreGemDropEnabled()) {
			registerOreDrop(drops, GeoMaterial.COAL, Items.coal, 1, 0, 1.0F, true);
			registerOreDrop(drops, GeoMaterial.DIAMOND, Items.diamond, 1, 0, 1.0F, true);
			registerOreDrop(drops, GeoMaterial.LAPIS, "gemLapis", 4, 5, 1.0F, true);
			registerOreDrop(drops, GeoMaterial.EMERALD, Items.emerald, 1, 0, 1.0F, true);
			registerOreDrop(drops, GeoMaterial.REDSTONE, Items.redstone, 4, 2, 1.0F, false);
		}
	}

	private static void registerOreDrop(ChanceDropRegistry drops,
			GeoMaterial material, String key, int quantity, int bonus, float chance, 
			boolean fortuneMultiplies) 
	{
		List<ItemStack> ores = OreDictionary.getOres(key);
		if (ores.size() > 0) {
			ItemStack drop = ores.get(0).copy();
			drop.stackSize = quantity;
			drops.addChanceDrop(material, drop, bonus, chance, fortuneMultiplies);
		}
	}

	private static void registerOreDrop(ChanceDropRegistry drops,
			GeoMaterial material, Item item, int quantity, int bonus, float chance, 
			boolean fortuneMultiplies)
	{
		registerOreDrop(drops, material, new ItemStack(item, quantity), bonus, chance, fortuneMultiplies);
	}

	private static void registerOreDrop(ChanceDropRegistry drops,
			GeoMaterial material, ItemStack item, int bonus, float chance, 
			boolean fortuneMultiplies)
	{
		drops.addChanceDrop(material, item, bonus, chance, fortuneMultiplies);
	}

}
