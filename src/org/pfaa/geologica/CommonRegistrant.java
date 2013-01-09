package org.pfaa.geologica;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraftforge.oredict.OreDictionary;

import org.pfaa.RecipeUtils;
import org.pfaa.Registrant;
import org.pfaa.RegistrationUtils;
import org.pfaa.geologica.item.GeoBlockItem;

import cpw.mods.fml.common.registry.LanguageRegistry;

public class CommonRegistrant implements Registrant {

	private static final String LOCALIZATION_DIR = Geologica.RESOURCE_DIR + "/localizations";

	@Override
	public void preregister() {
		registerBlocks();
		registerLocalizations();
	}

	private void registerLocalizations() {
		registerLocalization("en_US");
	}

	private void registerLocalization(String lang) {
		String localizationFile = LOCALIZATION_DIR + "/" + lang + ".properties";
		LanguageRegistry.instance().loadLocalization(localizationFile, lang, false);
	}

	private void registerBlocks() {
		RegistrationUtils.registerDeclaredBlocks(GeologicaBlocks.class, GeoBlockItem.class);
	}

	@Override
	public void register() {
		registerOres();
		registerSmeltingRecipes();
	}
	
	private void registerSmeltingRecipes() {
		registerSmeltingRecipesForMultiblock(GeologicaBlocks.MEDIUM_COBBLESTONE, GeologicaBlocks.MEDIUM_STONE);
		registerSmeltingRecipesForMultiblock(GeologicaBlocks.STRONG_COBBLESTONE, GeologicaBlocks.STRONG_STONE);
	}

	private void registerSmeltingRecipesForMultiblock(Block input, Block output) {
		List<ItemStack> subStacks = new ArrayList<ItemStack>();
		input.getSubBlocks(input.blockID, null, subStacks);
		for(int meta = 0; meta < subStacks.size(); meta++) {
			ItemStack outputStack = new ItemStack(output, 1, meta);
			FurnaceRecipes.smelting().addSmelting(input.blockID, meta, outputStack, 0);
		}
	}

	private void registerOres() {
		OreDictionary.registerOre("cobbleStone", new ItemStack(GeologicaBlocks.MEDIUM_COBBLESTONE, 1, -1));
		OreDictionary.registerOre("cobbleStone", new ItemStack(GeologicaBlocks.STRONG_COBBLESTONE, 1, -1));
		OreDictionary.registerOre("solidStone", new ItemStack(GeologicaBlocks.MEDIUM_STONE, 1, -1));
		OreDictionary.registerOre("solidStone", new ItemStack(GeologicaBlocks.STRONG_STONE, 1, -1));
		OreDictionary.registerOre("solidStone", new ItemStack(GeologicaBlocks.VERY_STRONG_STONE, 1, -1));
	}
	
	@Override
	public void postregister() {
		registerOreRecipes();
	}
	
	private void registerOreRecipes() {
		Map<ItemStack, String> replacements = new HashMap<ItemStack, String>();
		replacements.put(new ItemStack(Block.cobblestone, 1, -1), "cobbleStone");
		replacements.put(new ItemStack(Block.stone), "solidStone");
		RecipeUtils.createOreRecipes(replacements);
	}

}
