package org.pfaa.geologica.registration;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;

import org.pfaa.RecipeUtils;

public class RecipeReplacement {
	public static void init() {
		replaceStoneRecipes();
	}

	private static void replaceStoneRecipes() {
		Map<ItemStack, String> replacements = new HashMap<ItemStack, String>();
		replacements.put(new ItemStack(Block.cobblestone, 1, -1), "cobbleStone");
		replacements.put(new ItemStack(Block.stone), "solidStone");
		replacements.put(new ItemStack(Block.stoneBrick), "brickStone");
		ItemStack[] exclusions = new ItemStack[] {
			new ItemStack(Block.stoneBrick),
			new ItemStack(Block.stairsBrick),
			new ItemStack(Block.stoneSingleSlab),
			new ItemStack(Block.stairsCobblestone),
			new ItemStack(Block.cobblestoneWall)
		};
		RecipeUtils.createOreRecipes(replacements, exclusions);
	}

}
