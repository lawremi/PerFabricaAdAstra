package org.pfaa.chemica.item;

import java.util.List;
import java.util.Set;

import org.pfaa.chemica.model.Equation.Term;
import org.pfaa.chemica.model.IndustrialMaterial;
import org.pfaa.chemica.processing.Form;
import org.pfaa.chemica.processing.Form.Forms;
import org.pfaa.chemica.registration.OreDictUtils;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import net.minecraft.item.ItemStack;

public class MaterialStack implements IngredientStack {
	private Form form;
	private IndustrialMaterial material;
	private int size;
	
	public MaterialStack(Form form, IndustrialMaterial material, int size) {
		super();
		this.form = form;
		this.material = material;
		this.size = size;
	}
	
	public MaterialStack(Form form, IndustrialMaterial material) {
		this(form, material, 1);
	}

	public MaterialStack(IndustrialMaterial material) {
		this(material, 1);
	}
	
	public MaterialStack(IndustrialMaterial material, int size) {
		this(Forms.DUST, material, size);
	}
	
	public MaterialStack(Form form, Term term) {
		this(form, term.material(), (int)term.stoich);
	}
	
	public Form getForm() {
		return this.form;
	}
	
	public IndustrialMaterial getMaterial() {
		return this.material;
	}

	private Function<ItemStack, ItemStack> resizeItemStack = new Function<ItemStack, ItemStack>() {
		@Override
		public ItemStack apply(ItemStack input) {
			input = input.copy();
			input.stackSize = MaterialStack.this.size;
			return input;
		}
	};
	
	public String getOreDictKey() {
		return OreDictUtils.makeKey(this.form, this.material);
	}
	
	public Set<ItemStack> getItemStacks() {
		List<ItemStack> itemStacks = OreDictUtils.lookup(this.form, this.material);
		itemStacks = Lists.transform(itemStacks, resizeItemStack);
		return Sets.newHashSet(itemStacks);
	}
	
	public boolean hasItemStack() {
		return !this.getItemStacks().isEmpty();
	}

	@Override
	public int getSize() {
		return this.size;
	}

	public ItemStack getBestItemStack() {
		Set<ItemStack> itemStacks = this.getItemStacks();
		for (ItemStack itemStack : itemStacks) {
			if (itemStack.getItem() instanceof IndustrialMaterialItem) {
				return itemStack;
			}
		}
		return itemStacks.size() > 0 ? itemStacks.iterator().next() : null;
	}

	@Override
	public Object getCraftingIngredient() {
		return this.getOreDictKey();
	}
	
	public static MaterialStack of(IndustrialMaterial material) {
		return new MaterialStack(material);
	}
	
	public static MaterialStack of(IndustrialMaterial material, Form form) {
		return new MaterialStack(form, material);
	}
	
	public static MaterialStack of(int size, IndustrialMaterial material, Form form) {
		return new MaterialStack(form, material, size);
	}
}
