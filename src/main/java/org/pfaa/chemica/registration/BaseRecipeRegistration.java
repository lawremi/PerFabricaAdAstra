package org.pfaa.chemica.registration;

public class BaseRecipeRegistration {

	protected static final CombinedRecipeRegistry RECIPES = new CombinedRecipeRegistry();
	protected static final GenericRecipeRegistry GENERICS = RECIPES.getGenericRecipeRegistry();
	protected static final CombinedConversionRegistry CONVERSIONS = new CombinedConversionRegistry();
	protected static final ConversionRegistrant REGISTRANT = new ConversionRegistrant(CONVERSIONS);
	
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