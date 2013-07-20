package org.pfaa.chemica.item;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import org.pfaa.chemica.model.IndustrialMaterial;
import org.pfaa.geologica.processing.CrudeMaterials;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class IndustrialMaterialItem<T extends Enum & IndustrialMaterial> extends Item {
	
	private Class<T> enumClass;
	
	public IndustrialMaterialItem(int id, Class<T> enumClass) {
		super(id);
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
		return getIndustrialMaterial(itemStack).getColor().getRGB();
	}
	
}
