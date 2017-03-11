package org.pfaa.chemica.processing;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.pfaa.chemica.fluid.IndustrialFluids;
import org.pfaa.chemica.item.IndustrialItems;
import org.pfaa.chemica.item.IngredientStack;
import org.pfaa.chemica.model.IndustrialMaterial;
import org.pfaa.chemica.model.MaterialState;
import org.pfaa.chemica.model.State;
import org.pfaa.chemica.processing.Form.Forms;
import org.pfaa.chemica.registration.OreDictUtils;

import com.google.common.collect.Sets;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public class MaterialStack implements IngredientStack {
	private Form form;
	private MaterialState<?> materialState;
	private int size;
	private float chance;
	
	public MaterialStack(int size, MaterialState<?> materialState, Form form, float chance) {
		this.form = form;
		this.materialState = materialState;
		this.size = size;
		this.chance = chance;
	}

	public Form getForm() {
		return this.form;
	}
	
	public IndustrialMaterial getMaterial() {
		return this.materialState.material;
	}

	public State getState() {
		return this.materialState.state;
	}
	
	public String getOreDictKey() {
		return OreDictUtils.makeKey(this.form, this.getMaterial());
	}
	
	public Set<ItemStack> getItemStacks() {
		List<ItemStack> itemStacks = IndustrialItems.getItemStacks(this);
		return Sets.newHashSet(itemStacks);
	}
	
	public boolean hasItemStack() {
		return !this.getItemStacks().isEmpty();
	}

	public float getChance() {
		return this.chance;
	}

	@Override
	public int getSize() {
		return this.size;
	}

	public ItemStack getBestItemStack() {
		return IndustrialItems.getBestItemStack(this);
	}

	public FluidStack getFluidStack() {
		return IndustrialFluids.getFluidStack(this);
	}
	
	@Override
	public Object getCraftingIngredient() {
		return this.getOreDictKey();
	}
	
	public static MaterialStack of(IndustrialMaterial material) {
		return of(material, null);
	}
	
	public static MaterialStack of(IndustrialMaterial material, Form form) {
		return of(1, material, form);
	}
	
	public static MaterialStack of(MaterialState<?> materialState, Form form) {
		return of(1, materialState, form);
	}
	
	public static MaterialStack of(int size, IndustrialMaterial material, Form form) {
		return of(size, material.getStandardState().of(material), form);
	}
	
	public static MaterialStack of(int size, MaterialState<?> materialState, Form form) {
		return of(size, materialState, form, 1F);
	}

	public static MaterialStack of(int size, MaterialState<?> materialState, Form form, float chance) {
		return new MaterialStack(size, materialState, form, chance);
	}
	
	private static float roundScalar(float scalar) {
		float precision = (float)Math.pow(10, -Math.floor(Math.log10(scalar)));
		return Math.round(scalar * precision) / precision;
	}
	
	public static MaterialStack of(MaterialStoich<?> stoich, Form form) {
		float weight = stoich.stoich;
		if (stoich.state().isFluid()) {
			weight *= form.scaleTo(Forms.MILLIBUCKET);
			form = Forms.MILLIBUCKET;
		}
		while (weight < 1) {
			Form unstacked = form.unstack();
			if (unstacked == null) {
				break;
			}
			weight *= roundScalar(form.scaleTo(unstacked));
			form = unstacked;
		}
		Form stacked;
		double ratio;
		while((stacked = form.stack()) != null && 
				Math.rint(weight) >= (ratio = roundScalar(stacked.scaleTo(form)))) {
			weight /= ratio;
			form = stacked;
		}
		return of((int)Math.max(Math.rint(weight), 1), stoich.state().of(stoich.material()), form, Math.min(weight, 1F));
	}

	public static List<MaterialStack> of(Stream<MaterialStoich<?>> stoichs, Form form) {
		return stoichs.map((stoich) -> of(stoich, form)).collect(Collectors.toList());
	}
}
