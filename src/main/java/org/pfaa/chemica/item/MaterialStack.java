package org.pfaa.chemica.item;

import java.util.List;
import java.util.Set;

import org.pfaa.chemica.model.IndustrialMaterial;
import org.pfaa.chemica.processing.Form;
import org.pfaa.chemica.registration.OreDictUtils;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import net.minecraft.item.ItemStack;

public class MaterialStack {
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
		this(null, material, 1);
	}
	
	public MaterialStack(IndustrialMaterial material, int size) {
		this(null, material, size);
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
}
