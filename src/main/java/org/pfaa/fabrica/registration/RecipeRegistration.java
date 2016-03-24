package org.pfaa.fabrica.registration;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.pfaa.chemica.item.IndustrialMaterialItem;
import org.pfaa.chemica.item.MaterialStack;
import org.pfaa.chemica.model.Aggregate.Aggregates;
import org.pfaa.chemica.model.Strength;
import org.pfaa.chemica.processing.Form.Forms;
import org.pfaa.chemica.registration.RecipeUtils;
import org.pfaa.chemica.util.ChanceStack;
import org.pfaa.fabrica.FabricaItems;
import org.pfaa.fabrica.model.Intermediate.Intermediates;
import org.pfaa.geologica.processing.IndustrialMineral.IndustrialMinerals;
import org.pfaa.geologica.processing.OreMineral.Ores;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

// FIXME: use the ore dictionary for fabrication inputs

public class RecipeRegistration {
	private static final MaterialRecipeRegistry registry = 
			new MaterialRecipeRegistry(org.pfaa.chemica.registration.RecipeRegistration.getTarget());
	
	public static void init() {
		grindIntermediates();
		useFeldsparAsFlux();
		makePortlandCement();
	}

	private static void grindIntermediates() {
		for (Intermediates material : Intermediates.values()) {
			ItemStack input = FabricaItems.INTERMEDIATE_CRUSHED.getItemStack(material);
			ItemStack output = FabricaItems.INTERMEDIATE_DUST.getItemStack(material);
			registry.registerGrindingRecipe(input, output, Collections.<ChanceStack>emptyList(), Strength.WEAK);
		}
	}

	private static void makePortlandCement() {
		ItemStack clinker = FabricaItems.INTERMEDIATE_CRUSHED.getItemStack(Intermediates.PORTLAND_CLINKER, 4);	
		List<MaterialStack> inputs = Arrays.asList(
				new MaterialStack(Forms.DUST, Ores.CALCITE, 3),
				new MaterialStack(Forms.CLUMP, Aggregates.CLAY));
		registry.registerRoastingRecipes(inputs, clinker, 1700);
		mixIntermediate(FabricaItems.INTERMEDIATE_CRUSHED, Intermediates.PORTLAND_CEMENT);
	}

	private static void useFeldsparAsFlux() {
		ItemStack output = new ItemStack(Blocks.glass);
		ItemStack input = new ItemStack(Blocks.sand);
		MaterialStack feldspar = new MaterialStack(Forms.DUST_TINY, IndustrialMinerals.FELDSPAR);
		registry.registerCastingRecipes(input, output, feldspar, 1500);
		
		output = new ItemStack(Items.brick);
		input = new ItemStack(Items.clay_ball);
		registry.registerCastingRecipes(input, output, feldspar, 1500);
	}

	private static void mixIntermediate(IndustrialMaterialItem<Intermediates> item, Intermediates material) {
		List<MaterialStack> inputs = RecipeUtils.getMixtureInputs(item.getForm(), material);
		registry.registerMixingRecipes(inputs, item.getItemStack(material));
	}
}
