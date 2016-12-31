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

	protected static <T extends Enum<?> & IndustrialMaterial> void registerStackings(Class<T> materialType) {
		if (materialType.isAssignableFrom(Aggregate.class)) {
			for (IndustrialMaterial material : materialType.getEnumConstants()) {
				if (((Aggregate)material).isLoose()) {
					CONVERSIONS.register(Stacking.of(material).from(Forms.PILE).to(Forms.BLOCK));
				}
			}
		} else {
			for (IndustrialMaterial material : materialType.getEnumConstants()) {
				Stacking stacking = Stacking.of(material);
				if (material.isPure()) {
					CONVERSIONS.register(stacking.from(Forms.DUST_TINY).to(Forms.DUST));
				} else {
					CONVERSIONS.register(stacking.from(Forms.DUST_IMPURE_TINY).to(Forms.DUST_IMPURE));
				}
				if (material.isForBuilding()) {
					CONVERSIONS.register(stacking.from(Forms.NUGGET).to(Forms.INGOT));
					CONVERSIONS.register(stacking.from(Forms.INGOT).to(Forms.BLOCK));
				}
			}
		}
	}
}