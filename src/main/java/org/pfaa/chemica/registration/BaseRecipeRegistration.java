package org.pfaa.chemica.registration;

import java.util.Collections;

import org.pfaa.chemica.item.IndustrialMaterialItem;
import org.pfaa.chemica.model.IndustrialMaterial;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.item.ItemStack;

public class BaseRecipeRegistration {

	protected static final CombinedRecipeRegistry RECIPES = new CombinedRecipeRegistry();
	protected static final GenericRecipeRegistry GENERICS = RECIPES.getGenericRecipeRegistry();
	protected static final CombinedConversionRegistry CONVERSIONS = new CombinedConversionRegistry();
	
	static {
		CONVERSIONS.putRegistry("default", new DefaultConversionRegistry(GENERICS));
	}
	
	public static void putRegistry(String key, RecipeRegistry registry) {
		RECIPES.putRegistry(key, registry);
	}

	public static void putRegistry(String key, ConversionRegistry registry) {
		CONVERSIONS.putRegistry(key, registry);
	}
	
	public static RecipeRegistry getRecipeRegistry() {
		return RECIPES;
	}

	public static ConversionRegistry getConversionRegistry() {
		return CONVERSIONS;
	}
	
	protected static void registerPartitionRecipes(ItemStack itemStack, ItemStack partitionStack, int numPartitions) {
		partitionStack = partitionStack.copy();
		partitionStack.stackSize = numPartitions;
		RECIPES.registerMixingRecipe(Collections.singletonList(partitionStack), itemStack);
		GameRegistry.addShapelessRecipe(partitionStack, itemStack);
	}
	
	protected static <T extends Enum<?> & IndustrialMaterial> void registerPartitionRecipes(IndustrialMaterialItem<T> item, 
			IndustrialMaterialItem<T> partition) {
		int numPartitions = partition.getForm().getNumberPerBlock() / item.getForm().getNumberPerBlock();
		for(T material : partition.getIndustrialMaterials()) {
			ItemStack itemStack = item.getItemStack(material);
			ItemStack partitionStack = partition.getItemStack(material);
			registerPartitionRecipes(itemStack, partitionStack, numPartitions);
		}
	}
}