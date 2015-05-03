package org.pfaa.geologica.block;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;

import org.pfaa.chemica.model.IndustrialMaterial;
import org.pfaa.geologica.GeoMaterial;
import org.pfaa.geologica.GeoMaterial.Strength;
import org.pfaa.geologica.GeologicaBlocks;
import org.pfaa.geologica.GeologicaItems;
import org.pfaa.geologica.processing.Aggregate;
import org.pfaa.geologica.processing.Aggregate.Aggregates;
import org.pfaa.geologica.processing.Ore;
import org.pfaa.util.BlockMeta;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class IntactGeoBlock extends GeoBlock {
	private Set<BlockMeta> hosts;

	public IntactGeoBlock(Strength strength, Class<? extends IndustrialMaterial> composition, Material material) {
		super(strength, composition, material, false);
	}

	
	@Override
	public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int metadata, int fortune) {
		GeoMaterial material = this.getGeoMaterial(metadata);
		if (material.getBlockMaterial() == Material.clay || material.getBlockMaterial() == Material.ground) {
			return this.getEarthyDrops(material, fortune);
		}
		return super.getDrops(world, x, y, z, metadata, fortune);
	}
	
	private ArrayList<ItemStack> getEarthyDrops(GeoMaterial material, int fortune) {
		int numOreDrops = material.getComposition() instanceof Ore ? fortune + 1 : 4;
		ArrayList<ItemStack> drops = new ArrayList<ItemStack>();
		GeoMaterial host = (GeoMaterial)material.getHost();
		for (int i = 1; i <= 4; i++) {
			if (i > numOreDrops) {
				material = host;
			}
			drops.add(GeologicaItems.EARTHY_CLUMP.getItemStack(material)); 
		}
		return drops;
	}

	@Override
	public Item getItemDropped(int meta, Random random, int par3) {
		Item dropped = super.getItemDropped(meta, random, par3);
		GeoMaterial material = getGeoMaterial(meta);
		if (material.getComposition() instanceof Aggregate && blockMaterial == Material.rock) {
			dropped = dropBrokenRock(meta);
		}
		return dropped;
	}
	
	public Item dropBrokenRock(int meta) {
		return Item.getItemFromBlock(this.getBrokenBlock(getGeoMaterial(meta)));
	}
	
	public GeoBlock getBrokenBlock(GeoMaterial material) {
		GeoBlock dropped = null;
		switch(material.getStrength()) {
		case WEAK:
			dropped = GeologicaBlocks.WEAK_RUBBLE;
			break;
		case MEDIUM:
			dropped = GeologicaBlocks.MEDIUM_COBBLE;
			break;
		case STRONG:
			dropped = GeologicaBlocks.STRONG_COBBLE;
			break;
		case VERY_STRONG:
			dropped = this;
			break;
		default:
			break;
		}
		return dropped;
	}

	@Override
	protected boolean canSilkHarvest() {
		return true;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getHostIcon(IBlockAccess world, int x, int y, int z) {
		ItemStack host = this.getAdjacentHost(world, x, y, z);
		if (host == null)
			return null;
		Block block = ((ItemBlock)host.getItem()).field_150939_a;
		return block.getIcon(0, host.getItemDamage());
	}
	
	private Set<BlockMeta> getHostBlocks(IBlockAccess world, int x, int y, int z) {
		String ore = this.getHostOre(world, x, y, z);
		if (ore == null) {
			return null;
		}
		if (this.hosts == null) {
			this.hosts = getBlocksForOre(ore);
		}
		return this.hosts;
	}
	
	private String getHostOre(IBlockAccess world, int x, int y, int z) {
		int meta = world.getBlockMetadata(x, y, z);
		IndustrialMaterial host = this.getGeoMaterial(meta).getHost();
		if (host == Aggregates.STONE) {
			return "stone";
		} else if (host == Aggregates.CLAY) {
			return "clay";
		} else return null;
	}

	private static Set<BlockMeta> getBlocksForOre(String key) {
		Set<BlockMeta> set = new HashSet<BlockMeta>();
		List<ItemStack> ores = OreDictionary.getOres(key);
		for (ItemStack ore : ores) {
			Item item = ore.getItem();
			if (item instanceof ItemBlock) {
				set.add(new BlockMeta(((ItemBlock) item).field_150939_a, ore.getItemDamage()));
			}
		}
		if (key == "stone") {
			set.add(new BlockMeta(Blocks.sandstone, 0));
		}
		return set;
	}
	
	private ItemStack getAdjacentHost(IBlockAccess world, int x, int y, int z) {
		Set<BlockMeta> hosts = this.getHostBlocks(world, x, y, z);
		if (hosts != null) {
			return getAdjacentBlock(world, x, y, z, hosts);
		} else {
			return null;
		}
	}
	
	private static ItemStack getAdjacentBlock(IBlockAccess world, int x, int y, int z, Set<BlockMeta> allowed) {
		for (int ix = x - 1; ix <= x + 1; ix++) {
			for (int iy = y - 1; iy <= y + 1; iy++) {
				for (int iz = z - 1; iz <= z + 1; iz++) {
					Block block = world.getBlock(ix, iy, iz);
					int meta = world.getBlockMetadata(ix, iy, iz);
					if (allowed.contains(new BlockMeta(block, meta))) {
						return new ItemStack(block, 1, meta);
					}
				}
			}
		}
		return null;
	}
}
