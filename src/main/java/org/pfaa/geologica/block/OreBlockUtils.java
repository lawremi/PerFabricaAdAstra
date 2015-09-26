package org.pfaa.geologica.block;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.pfaa.core.block.BlockWithMeta;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.oredict.OreDictionary;

public class OreBlockUtils {
	private static Map<String,Set<BlockWithMeta<?>>> oreCache = new HashMap<String,Set<BlockWithMeta<?>>>();
	
	private static Set<BlockWithMeta<?>> getBlocksForOre(String key) {
		Set<BlockWithMeta<?>> set = oreCache.get(key);
		if (set == null) {
			set = getBlocksForNewOre(key);
			oreCache.put(key, set);
		}
		return set;
	}
	
	private static Set<BlockWithMeta<?>> getBlocksForNewOre(String key) {
		Set<BlockWithMeta<?>> set = new HashSet<BlockWithMeta<?>>();
		List<ItemStack> ores = OreDictionary.getOres(key);
		for (ItemStack ore : ores) {
			Item item = ore.getItem();
			if (item instanceof ItemBlock) {
				set.add(new BlockWithMeta<Block>(((ItemBlock) item).field_150939_a, ore.getItemDamage()));
			}
		}
		if (key == "stone") {
			set.add(new BlockWithMeta<Block>(Blocks.sandstone, 0));
		}
		return set;
	}

	private static ItemStack getAdjacentBlock(IBlockAccess world, int x, int y, int z, Set<BlockWithMeta<?>> allowed) {
		for (int ix = x - 1; ix <= x + 1; ix++) {
			for (int iy = y - 1; iy <= y + 1; iy++) {
				for (int iz = z - 1; iz <= z + 1; iz++) {
					Block block = world.getBlock(ix, iy, iz);
					int meta = world.getBlockMetadata(ix, iy, iz);
					if (allowed.contains(new BlockWithMeta<Block>(block, meta))) {
						return new ItemStack(block, 1, meta);
					}
				}
			}
		}
		return null;
	}
	
	private static String getHostOreForMaterial(Material material) {
		if (material == Material.rock) {
			return "stone";
		} else if (material == Material.clay) {
			return "clay";
		}
		return null;
	}
	
	private static Set<BlockWithMeta<?>> getHostBlocks(Block block) {
		return getBlocksForOre(getHostOreForMaterial(block.getMaterial()));	
	}
	
	public static ItemStack getHost(IBlockAccess world, int x, int y, int z) {
		Block block = world.getBlock(x, y, z);
		Set<BlockWithMeta<?>> hosts = getHostBlocks(block);
		if (hosts != null) {
			return getAdjacentBlock(world, x, y, z, hosts);
		} else {
			return null;
		}
	}
	
	public static IIcon getHostIcon(IBlockAccess world, int x, int y, int z) {
		ItemStack host = getHost(world, x, y, z);
		if (host == null)
			return null;
		Block block = ((ItemBlock)host.getItem()).field_150939_a;
		return block.getIcon(0, host.getItemDamage());
	}
}
