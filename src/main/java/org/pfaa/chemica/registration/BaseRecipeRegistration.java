package org.pfaa.chemica.registration;

import org.pfaa.chemica.model.Aggregate;
import org.pfaa.chemica.model.IndustrialMaterial;
import org.pfaa.chemica.processing.Form.Forms;
import org.pfaa.chemica.processing.Stacking;

public class BaseRecipeRegistration {

	protected static final CombinedRecipeRegistry RECIPES = new CombinedRecipeRegistry();
	protected static final GenericRecipeRegistry GENERICS = RECIPES.getGenericRecipeRegistry();
	protected static final CombinedConversionRegistry CONVERSIONS = new CombinedConversionRegistry();
	
	static {
		CONVERSIONS.putRegistry("default", new DefaultConversionRegistry(RECIPES));
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
}