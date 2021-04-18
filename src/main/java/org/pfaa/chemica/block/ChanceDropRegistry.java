package org.pfaa.chemica.block;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.pfaa.chemica.model.IndustrialMaterial;
import org.pfaa.core.item.ChanceStack;

import net.minecraft.item.ItemStack;

public class ChanceDropRegistry {
	
	private Map<IndustrialMaterial, ChanceDropSet> dropsByMaterial = new HashMap<IndustrialMaterial, ChanceDropSet>();
	
	private static final ChanceDropRegistry INSTANCE = new ChanceDropRegistry();
	
	private ChanceDropRegistry() {
		
	}
	
	public static ChanceDropRegistry instance() {
		return INSTANCE;
	}
	
	public void addChanceDrop(IndustrialMaterial material, ItemStack item, int bonus, float chance, boolean fortuneMultiplies) {
		ChanceDropSet drops = this.dropsByMaterial.get(material);
		if (drops == null) {
			drops = new ChanceDropSet();
			dropsByMaterial.put(material, drops);
		}
		drops.addDrop(new ChanceDrop(item, bonus, chance, fortuneMultiplies));
	}
	
	public void addChanceDrop(IndustrialMaterial material, ItemStack item, int bonus) {
		this.addChanceDrop(material, item, bonus, 1.0F, true);
	}
	
	public ArrayList<ItemStack> getDrops(IndustrialMaterial material, Random rand, int fortune) {
		ChanceDropSet drops = this.dropsByMaterial.get(material);
		return drops != null ? drops.getDrops(rand, fortune) : null;
	}
	
	public ArrayList<ItemStack> getAverageDrops(IndustrialMaterial material) {
		ChanceDropSet drops = this.dropsByMaterial.get(material);
		return drops != null ? drops.getAverageDrops() : null;
	}
	
	private static class ChanceDrop extends ChanceStack {
		public final int bonus;
		public final boolean fortuneMultiplies;
		
		public ChanceDrop(ItemStack itemStack, int bonus, float chance, boolean fortuneMultiplies) {
			super(itemStack, chance);
			this.bonus = bonus;
			this.fortuneMultiplies = fortuneMultiplies;
		}
	}

	private static class ChanceDropSet {
		private List<ChanceDrop> drops;
		
		public ChanceDropSet(ChanceDrop... drops) {
			this.drops = new ArrayList<ChanceDrop>(Arrays.asList(drops));
		}
		
		public void addDrop(ChanceDrop drop) {
			this.drops.add(drop);
		}
		
		public ArrayList<ItemStack> getDrops(Random rand, int fortune) {
			ArrayList<ItemStack> items = new ArrayList<ItemStack>();
			for (ChanceDrop drop : this.drops) {
				if (rand.nextFloat() < drop.chance) {
					ItemStack itemStack = drop.itemStack.copy();
					itemStack.stackSize += rand.nextInt(drop.bonus + 1);
					if (drop.fortuneMultiplies) {
						itemStack.stackSize *= (Math.max(rand.nextInt(fortune + 2) - 1, 0) + 1);
					} else {
						itemStack.stackSize += rand.nextInt(fortune + 1);
					}
					items.add(itemStack);
				}
			}
			return items;
		}
		
		public ArrayList<ItemStack> getAverageDrops() {
			ArrayList<ItemStack> items = new ArrayList<ItemStack>();
			for (ChanceDrop drop : this.drops) {
				ItemStack itemStack = drop.itemStack.copy();
				itemStack.stackSize = (int)Math.rint(itemStack.stackSize * drop.chance) + (drop.bonus + 1) / 2;
				items.add(itemStack);
			}
			return items;
		}
	}

}
