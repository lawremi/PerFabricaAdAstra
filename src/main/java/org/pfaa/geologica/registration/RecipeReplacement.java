package org.pfaa.geologica.registration;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import org.pfaa.RecipeUtils;

public class RecipeReplacement {
	public static void init() {
		replaceStoneRecipes();
	}

	private static void replaceStoneRecipes() {
		Map<ItemStack, String> replacements = new HashMap<ItemStack, String>();
		replacements.put(new ItemStack(Blocks.cobblestone, 1, OreDictionary.WILDCARD_VALUE), "cobblestone");
		replacements.put(new ItemStack(Blocks.cobblestone, 1), "cobblestone");
		replacements.put(new ItemStack(Blocks.stone, 1, OreDictionary.WILDCARD_VALUE), "stone");
		replacements.put(new ItemStack(Blocks.stone, 1), "stone");
		replacements.put(new ItemStack(Blocks.stonebrick, 1), "stoneBrick");
		ItemStack[] exclusions = new ItemStack[] {
			new ItemStack(Blocks.stonebrick),
			new ItemStack(Blocks.stone_brick_stairs),
			new ItemStack(Blocks.stone_slab),
			new ItemStack(Blocks.stone_slab, 1, 3), // cobble
			new ItemStack(Blocks.stone_slab, 1, 5), // smooth stone brick
			new ItemStack(Blocks.stone_stairs),
			new ItemStack(Blocks.cobblestone_wall),
			new ItemStack(Items.stone_axe),
			new ItemStack(Items.stone_pickaxe),
			new ItemStack(Items.stone_shovel),
			new ItemStack(Items.stone_hoe),
			new ItemStack(Items.stone_sword)
		};
		RecipeUtils.oreDictifyRecipes(replacements, exclusions);
	}

}
