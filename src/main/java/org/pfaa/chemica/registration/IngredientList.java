package org.pfaa.chemica.registration;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.pfaa.chemica.item.IngredientStack;
import org.pfaa.chemica.item.MaterialStack;
import org.pfaa.chemica.model.IndustrialMaterial;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import net.minecraft.item.ItemStack;

public class IngredientList extends AbstractList<IngredientStack> {

	private List<IngredientStack> delegate;
	private Set<List<ItemStack>> itemStackLists;
	
	public IngredientList(IngredientStack... materialStacks) {
		this.delegate = Lists.newArrayList(materialStacks);
	}
	
	public IngredientList(List<IngredientStack> materialStacks) {
		this.delegate = new ArrayList<IngredientStack>(materialStacks);
	}
	
	public IngredientList(IndustrialMaterial... materials) {
		List<IngredientStack> stacks = new ArrayList<IngredientStack>(materials.length);
		for (IndustrialMaterial material : materials) {
			stacks.add(new MaterialStack(material));
		}
		this.delegate = stacks;
	}
	
	@Override
	public IngredientStack get(int arg0) {
		return delegate.get(arg0);
	}

	@Override
	public int size() {
		return delegate.size();
	}
	
	private Function<IngredientStack, Set<ItemStack>> getItemStacks = new Function<IngredientStack, Set<ItemStack>>() {
		@Override
		public Set<ItemStack> apply(IngredientStack input) {
			return input.getItemStacks();
		}
	};
	
	public Set<List<ItemStack>> getItemStackLists() {
		if (this.itemStackLists == null) {
			List<Set<ItemStack>> expandedInputs = Lists.transform(this.delegate, getItemStacks);
			this.itemStackLists = Sets.cartesianProduct(expandedInputs);
		}
		return this.itemStackLists;
	}
	
	private Function<IngredientStack, String> getOreDictKey = new Function<IngredientStack, String>() {
		@Override
		public String apply(IngredientStack input) {
			return input.getOreDictKey();
		}
	};
	
	public List<String> getOreDictKeys() {
		return Lists.transform(this.delegate, getOreDictKey);
	}
	
	private Function<IngredientStack, Object> getCraftingIngredient = new Function<IngredientStack, Object>() {
		@Override
		public Object apply(IngredientStack input) {
			return input.getCraftingIngredient();
		}
	};
	
	public List<Object> getCraftingIngredients() {
		return Lists.transform(this.delegate, getCraftingIngredient);
	}
}
