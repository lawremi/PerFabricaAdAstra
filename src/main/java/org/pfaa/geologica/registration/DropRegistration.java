package org.pfaa.geologica.registration;

import java.util.List;

import org.pfaa.chemica.block.ChanceDropRegistry;
import org.pfaa.chemica.item.IndustrialMaterialItem;
import org.pfaa.chemica.model.IndustrialMaterial;
import org.pfaa.geologica.GeoMaterial;
import org.pfaa.geologica.Geologica;
import org.pfaa.geologica.GeologicaItems;

import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class DropRegistration {
	
	// TODO: consider dropping gems (when other mods add them), and tiny dusts
	public static void init() {
		ChanceDropRegistry drops = ChanceDropRegistry.instance();
		registerOreDrop(drops, GeoMaterial.CONGLOMERATE, "nuggetCopper", 1, 3, 0.05F, true);
		registerOreDrop(drops, GeoMaterial.GARNET_SAND, Items.gold_nugget, 4, 4, 0.1F, true);
		registerOreDrop(drops, GeoMaterial.GARNET_SAND, "nuggetElectrum", 2, 2, 0.05F, true);
		registerOreDrop(drops, GeoMaterial.GARNET_SAND, "nuggetSilver", 1, 2, 0.05F, true);
		registerOreDrop(drops, GeoMaterial.CHALK, Items.flint, 2, 5, 0.05F, true);
		if (Geologica.getConfiguration().isVanillaOreGemDropEnabled()) {
			registerDropsOfItem(drops, GeologicaItems.CRUDE_LUMP);
			registerOreDrop(drops, GeoMaterial.DIAMOND, Items.diamond, 1, 0, 1.0F, true);
			registerOreDrop(drops, GeoMaterial.LAPIS, "gemLapis", 4, 5, 1.0F, true);
			registerOreDrop(drops, GeoMaterial.EMERALD, Items.emerald, 1, 0, 1.0F, true);
			registerOreDrop(drops, GeoMaterial.REDSTONE, Items.redstone, 4, 2, 1.0F, false);
		}
	}
	
	private static <T extends Enum<?> & IndustrialMaterial> void registerDropsOfItem(ChanceDropRegistry drops, 
			IndustrialMaterialItem<T> item) {
		for (T material : item.getIndustrialMaterials()) {
			registerOreDrop(drops, material, item.getItemStack(material), 0, 1.0F, true);
		}
	}

	private static void registerOreDrop(ChanceDropRegistry drops,
			IndustrialMaterial material, String key, int quantity, int bonus, float chance, 
			boolean fortuneMultiplies)
	{
		List<ItemStack> ores = OreDictionary.getOres(key);
		if (ores.size() > 0) {
			ItemStack drop = ores.get(0).copy();
			drop.stackSize = quantity;
			drops.addChanceDrop(material, drop, bonus, chance, fortuneMultiplies);
		}
	}

	private static void registerOreDrop(ChanceDropRegistry drops,
			IndustrialMaterial material, Item item, int quantity, int bonus, float chance, 
			boolean fortuneMultiplies)
	{
		registerOreDrop(drops, material, new ItemStack(item, quantity), bonus, chance, fortuneMultiplies);
	}

	private static void registerOreDrop(ChanceDropRegistry drops,
			IndustrialMaterial material, ItemStack item, int bonus, float chance, 
			boolean fortuneMultiplies)
	{
		drops.addChanceDrop(material, item, bonus, chance, fortuneMultiplies);
	}

}
