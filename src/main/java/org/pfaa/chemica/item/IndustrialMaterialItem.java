package org.pfaa.chemica.item;

import java.util.Arrays;
import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import org.pfaa.chemica.model.Chemical;
import org.pfaa.chemica.model.Condition;
import org.pfaa.chemica.model.ConditionProperties;
import org.pfaa.chemica.model.IndustrialMaterial;
import org.pfaa.chemica.model.IndustrialMaterialUtils;
import org.pfaa.chemica.model.State;
import org.pfaa.chemica.processing.Form;

import com.google.common.base.CaseFormat;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class IndustrialMaterialItem<T extends Enum<?> & IndustrialMaterial> extends Item {
	
	private Form form;
	private Class<T> enumClass;
	private Predicate<? super T> predicate;
	
	private static Predicate<IndustrialMaterial> FormsSolid = new Predicate<IndustrialMaterial>() {
		public boolean apply(IndustrialMaterial obj) {
			return obj instanceof Chemical || obj.getProperties(Condition.STP).state == State.SOLID;
		}
	};
	
	public IndustrialMaterialItem(Form form, Class<T> enumClass) {
		this(form, enumClass, FormsSolid);
	}
	
	public IndustrialMaterialItem(Form form, Class<T> enumClass, Predicate<? super T> predicate) {
		this.form = form;
		this.enumClass = enumClass;
		this.predicate = predicate;
		this.setHasSubtypes(true);
	}

	public T getIndustrialMaterial(int damage) {
		return enumClass.getEnumConstants()[damage];
	}

	public T getIndustrialMaterial(ItemStack item) {
		return getIndustrialMaterial(item.getItemDamage());
	}

	public ItemStack getItemStack(T material) {
		return getItemStack(material, 1);
	}
	
	public ItemStack of(T material) {
		return getItemStack(material, 1);
	}
	
	public ItemStack getItemStack(T material, int quantity) {
		return new ItemStack(this, quantity, getDamage(material));
	}
	
	public int getDamage(T material) {
		return material.ordinal();
	}

	@Override
	@SideOnly(Side.CLIENT)
	public int getColorFromItemStack(ItemStack itemStack, int par2) {
		return this.getProperties(itemStack).color.getRGB();
	}
	
	@Override
	public String getUnlocalizedName(ItemStack itemStack) {
		String materialName = this.getIndustrialMaterial(itemStack).name();
		return super.getUnlocalizedName(itemStack) + "." + CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, materialName);
	}

	public ConditionProperties getProperties(ItemStack itemStack) {
		return getProperties(itemStack.getItemDamage());
	}
	
	public ConditionProperties getProperties(int damage) {
		IndustrialMaterial material = getIndustrialMaterial(damage);
		Condition canonicalSolid = IndustrialMaterialUtils.getCanonicalCondition(material, State.SOLID);
		return material.getProperties(canonicalSolid);
	}

	public List<T> getIndustrialMaterials() {
		List<T> materials = Arrays.asList(this.enumClass.getEnumConstants());
		return Lists.newArrayList(Collections2.filter(materials, this.predicate));
	}
	
	@SuppressWarnings("unchecked")
	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(Item item, CreativeTabs tabs, @SuppressWarnings("rawtypes") List itemStacks) {
		for (T material : this.getIndustrialMaterials()) {
			itemStacks.add(this.getItemStack(material));
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	protected String getIconString() {
		return "chemica:" + form.toString().toLowerCase();
	}
}
