package org.pfaa.geologica;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.src.ModLoader;
import net.minecraftforge.oredict.OreDictionary;

import org.pfaa.RecipeUtils;
import org.pfaa.Registrant;
import org.pfaa.RegistrationUtils;
import org.pfaa.block.CompositeBlockAccessors;
import org.pfaa.geologica.block.SlabBlock;
import org.pfaa.geologica.block.StairsBlock;
import org.pfaa.geologica.entity.item.CustomEntityFallingSand;
import org.pfaa.geologica.item.SlabItem;
import org.pfaa.item.CompositeBlockItem;

import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;

public class CommonRegistrant implements Registrant {

	private static final String LOCALIZATION_DIR = Geologica.RESOURCE_DIR + "/localizations";

	@Override
	public void preregister() {
		registerBlocks();
		registerEntities();
		registerLocalizations();
	}

	private void registerEntities() {
		EntityRegistry.registerModEntity(CustomEntityFallingSand.class, "customFallingSand", 1, Geologica.instance, 160, 20, false);
	}

	private void registerLocalizations() {
		registerLocalization("en_US");
	}

	private void registerLocalization(String lang) {
		String localizationFile = LOCALIZATION_DIR + "/" + lang + ".properties";
		LanguageRegistry.instance().loadLocalization(localizationFile, lang, false);
	}

	private void registerBlocks() {
		RegistrationUtils.registerDeclaredBlocks(GeologicaBlocks.class, CompositeBlockAccessors.class, CompositeBlockItem.class);
		RegistrationUtils.registerDeclaredBlocks(GeologicaBlocks.class, SlabBlock.class, SlabItem.class);
		RegistrationUtils.registerDeclaredBlocks(GeologicaBlocks.class, StairsBlock.class, ItemBlock.class);
	}

	@Override
	public void register() {
		registerOres();
		GeologicaRecipes.register();
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
		replacements.put(new ItemStack(Block.stoneBrick), "brickStone");
		ItemStack[] exclusions = new ItemStack[] {
			new ItemStack(Block.stoneBrick),
			new ItemStack(Block.stairsStoneBrickSmooth),
			new ItemStack(Block.stoneSingleSlab),
			new ItemStack(Block.stairCompactCobblestone),
			new ItemStack(Block.cobblestoneWall)
		};
		RecipeUtils.createOreRecipes(replacements, exclusions);
	}

}
