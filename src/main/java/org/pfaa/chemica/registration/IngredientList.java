package org.pfaa.chemica.registration;

import java.util.AbstractList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.pfaa.chemica.item.IngredientStack;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import net.minecraft.item.ItemStack;

public class IngredientList<T extends IngredientStack> extends AbstractList<T> {

	private List<T> delegate;
	private Set<List<ItemStack>> itemStackLists;
	
	public IngredientList() {
		this.delegate = Collections.emptyList();
	}
	
	@SafeVarargs
	public IngredientList(T... ingredientStacks) {
		this.delegate = Lists.newArrayList(ingredientStacks);
	}
	
	public IngredientList(List<T> ingredientStacks) {
		this.delegate = Lists.newArrayList(ingredientStacks);
	}
	
	@Override
	public T get(int arg0) {
		return delegate.get(arg0);
	}

	@Override
	public int size() {
		return delegate.size();
	}
	
	@Override
	public void add(int index, T element) {
		this.itemStackLists = null;
		this.delegate.add(index, element);
	}

	public Set<List<ItemStack>> getItemStackLists() {
		if (this.itemStackLists == null) {
			List<Set<ItemStack>> expandedInputs = this.delegate.stream().map(IngredientStack::getItemStacks).
					collect(Collectors.toList());
			this.itemStackLists = Sets.cartesianProduct(expandedInputs);
		}
		return this.itemStackLists;
	}
	
	public Set<ItemStack> getItemStacks() {
		Set<ItemStack> ans = this.delegate.stream().map(IngredientStack::getItemStacks).
				reduce(new HashSet<ItemStack>(), (a, b) -> { a.addAll(b); return a; });
		return Collections.unmodifiableSet(ans);
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

	@SafeVarargs
	public static <T extends IngredientStack> IngredientList<T> of(T... stacks) {
		return new IngredientList<T>(stacks);
	}
	
	public static <T extends IngredientStack> IngredientList<T> of(List<T> stacks) {
		return new IngredientList<T>(stacks);
	}
}
