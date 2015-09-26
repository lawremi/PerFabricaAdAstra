package org.pfaa.chemica.registration;

import org.pfaa.chemica.ChemicaBlocks;
import org.pfaa.chemica.ChemicaItems;
import org.pfaa.chemica.item.IndustrialMaterialItem;
import org.pfaa.chemica.model.Aggregate.Aggregates;
import org.pfaa.chemica.processing.Form.Forms;
import org.pfaa.chemica.model.IndustrialMaterial;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.item.ItemStack;

public class RecipeRegistration {
	
	private static final CombinedRecipeRegistry target = new CombinedRecipeRegistry();

	public static CombinedRecipeRegistry getTarget() {
		return target;
	}
	
	public static void init() {
		registerQuarterToBlockRecipe(ChemicaItems.AGGREGATE_PILE);
		registerIngotToBlockRecipe(ChemicaItems.METAL_INGOT);
		// TODO: Loop over the ore dictionary. If we find an IndustrialMateralItem, 
		//       register the appropriate recipes for _all_ ItemStacks of that ore.
		//       This means we are potentially changing recipes from other mods.
		// * ore =crush=> (2x) crushed + host powder
		// * crushed =grind=> powder + side powders
		// * ore/crushed/powder =smelt=> ingot
	}
	
	private static <T extends Enum<?> & IndustrialMaterial> void registerIngotToBlockRecipe(IndustrialMaterialItem<T> input) {
		for(T material : input.getIndustrialMaterials()) {
			ItemStack outputStack = OreDictUtils.lookupBest(Forms.BLOCK, material);
			ItemStack inputStack = input.getItemStack(material);
			GameRegistry.addRecipe(outputStack, "###", "###", "###", '#', inputStack);
		}
	}

	private static void registerQuarterToBlockRecipe(IndustrialMaterialItem<Aggregates> input) {
		for(Aggregates material : input.getIndustrialMaterials()) {
			ItemStack outputStack = new ItemStack(ChemicaBlocks.getBlock(material));
			ItemStack inputStack = input.getItemStack(material);
			GameRegistry.addRecipe(outputStack, "##", "##", '#', inputStack);
		}
	}
}
