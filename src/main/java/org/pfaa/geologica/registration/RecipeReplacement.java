package org.pfaa.geologica.registration;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import org.pfaa.RecipeUtils;

public class RecipeReplacement {
	public static void init() {
		replaceStoneRecipes();
	}

	private static void replaceStoneRecipes() {
		Map<ItemStack, String> replacements = new HashMap<ItemStack, String>();
		replacements.put(new ItemStack(Block.cobblestone, 1, OreDictionary.WILDCARD_VALUE), "cobblestone");
		replacements.put(new ItemStack(Block.cobblestone, 1), "cobblestone");
		replacements.put(new ItemStack(Block.stone, 1, OreDictionary.WILDCARD_VALUE), "stone");
		replacements.put(new ItemStack(Block.stone, 1), "stone");
		replacements.put(new ItemStack(Block.stoneBrick, 1), "stoneBrick");
		ItemStack[] exclusions = new ItemStack[] {
			new ItemStack(Block.stoneBrick),
			new ItemStack(Block.stairsStoneBrick),
			new ItemStack(Block.stoneSingleSlab),
			new ItemStack(Block.stoneSingleSlab, 1, 3), // cobble
			new ItemStack(Block.stoneSingleSlab, 1, 5), // smooth stone brick
			new ItemStack(Block.stairsCobblestone),
			new ItemStack(Block.cobblestoneWall),
			new ItemStack(Item.axeStone),
			new ItemStack(Item.pickaxeStone),
			new ItemStack(Item.shovelStone),
			new ItemStack(Item.hoeStone),
			new ItemStack(Item.swordStone)
		};
		RecipeUtils.oreDictifyRecipes(replacements, exclusions);
	}

}
