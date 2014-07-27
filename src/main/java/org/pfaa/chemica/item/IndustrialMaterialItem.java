package org.pfaa.chemica.item;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import org.pfaa.chemica.model.Condition;
import org.pfaa.chemica.model.ConditionProperties;
import org.pfaa.chemica.model.IndustrialMaterial;
import org.pfaa.chemica.model.PhaseProperties;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class IndustrialMaterialItem<T extends Enum & IndustrialMaterial> extends Item {
	
	private Class<T> enumClass;
	
	public IndustrialMaterialItem(Class<T> enumClass) {
		this.enumClass = enumClass;
	}

	public IndustrialMaterial getIndustrialMaterial(int damage) {
		return enumClass.getEnumConstants()[damage];
	}

	public IndustrialMaterial getIndustrialMaterial(ItemStack item) {
		return getIndustrialMaterial(item.getItemDamage());
	}

	public ItemStack getItemStack(T material) {
		return getItemStack(material, 1);
	}
	
	public ItemStack getItemStack(T material, int quantity) {
		return new ItemStack(this, quantity, getDamage(material));
	}
	
	private int getDamage(T material) {
		return material.ordinal();
	}

	@Override
	@SideOnly(Side.CLIENT)
	public int getColorFromItemStack(ItemStack itemStack, int par2) {
		return this.getProperties(itemStack).color.getRGB();
	}

	public ConditionProperties getProperties(ItemStack itemStack) {
		return getProperties(itemStack.getItemDamage());
	}
	
	public ConditionProperties getProperties(int damage) {
		return getIndustrialMaterial(damage).getProperties(Condition.STP);
	}
}
