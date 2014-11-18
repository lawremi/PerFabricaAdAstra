package org.pfaa.geologica.block;

import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;

import org.pfaa.chemica.model.Condition;
import org.pfaa.chemica.model.IndustrialMaterial;
import org.pfaa.geologica.GeoMaterial;
import org.pfaa.geologica.GeoMaterial.Strength;
import org.pfaa.geologica.GeologicaBlocks;
import org.pfaa.geologica.processing.Aggregate;
import org.pfaa.geologica.processing.Aggregate.Aggregates;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class IntactGeoBlock extends GeoBlock {
	private static Set<Block> stoneBlocks;

	public IntactGeoBlock(Strength strength, Class<? extends IndustrialMaterial> composition, Material material) {
		super(strength, composition, material, false);
	}

	@Override
	public Item getItemDropped(int meta, Random random, int par3) {
		Item dropped = super.getItemDropped(meta, random, par3);
		GeoMaterial material = getGeoMaterial(meta);
		if (material.getComposition() instanceof Aggregate && blockMaterial == Material.rock) {
			dropped = dropRock(meta);
		}
		return dropped;
	}
	
	private Item dropRock(int meta) {
		Item dropped = null;
		GeoMaterial material = getGeoMaterial(meta);
		switch(material.getStrength()) {
		case WEAK:
			dropped = Item.getItemFromBlock(GeologicaBlocks.WEAK_RUBBLE);
			break;
		case MEDIUM:
			dropped = Item.getItemFromBlock(GeologicaBlocks.MEDIUM_COBBLE);
			break;
		case STRONG:
			dropped = Item.getItemFromBlock(GeologicaBlocks.STRONG_COBBLE);
			break;
		case VERY_STRONG:
			dropped = Item.getItemFromBlock(this);
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
	public IIcon getHostIcon(IndustrialMaterial host, IBlockAccess world, int x, int y, int z) {
		if (host == Aggregates.STONE) {
			return getAdjacentStoneIcon(world, x, y, z);
		}
		return null;
	}
	
	public static class HostTileEntity extends TileEntity {

		private ItemStack host;
		
		public HostTileEntity() {
			
		}
	
		@Override
		public void readFromNBT(NBTTagCompound nbt) {
			super.readFromNBT(nbt);
			NBTTagCompound hostTag = nbt.getCompoundTag("host");
			if (hostTag != null) {
				this.host = ItemStack.loadItemStackFromNBT(hostTag);
			}
		}

		@Override
		public void writeToNBT(NBTTagCompound nbt) {
			super.writeToNBT(nbt);
			if (this.host != null) {
				NBTTagCompound hostTag = new NBTTagCompound();
				this.host.writeToNBT(hostTag);
				nbt.setTag("host", hostTag);
			}
		}

		public ItemStack getHost() {
			return host;
		}

		public void setHost(ItemStack host) {
			this.host = host;
		}
	}

	@Override
	public TileEntity createTileEntity(World world, int metadata)
    {
		return new HostTileEntity();
    }
	
	@Override
	public boolean hasTileEntity(int metadata) {
		return metadata < this.getMetaCount() ? this.getGeoMaterial(metadata).getHost() instanceof Aggregate : false;
	}
	
	@SideOnly(Side.CLIENT)
	private static IIcon getAdjacentStoneIcon(IBlockAccess world, int x, int y, int z) {
		if (stoneBlocks == null) {
			initStoneBlocks();
		}
		HostTileEntity te = (HostTileEntity)world.getTileEntity(x, y, z);
		ItemStack host = te.getHost();
		if (host == null) {
			host = getAdjacentStone(world, x, y, z);
			if (host == null)
				return null;
			te.setHost(host);
		}
		Block block = ((ItemBlock)host.getItem()).field_150939_a;
		return block.getIcon(0, host.getItemDamage());
	}
	
	private static void initStoneBlocks() {
		stoneBlocks = new HashSet();
		List<ItemStack> ores = OreDictionary.getOres("stone");
		for (ItemStack ore : ores) {
			Item item = ore.getItem();
			if (item instanceof ItemBlock) {
				stoneBlocks.add(((ItemBlock)item).field_150939_a);
			}
		}
	}

	private static ItemStack getAdjacentStone(IBlockAccess world, int x, int y, int z) {
		for (int ix = x - 1; ix <= x + 1; ix++) {
			for (int iy = y - 1; iy <= y + 1; iy++) {
				for (int iz = z - 1; iz <= z + 1; iz++) {
					Block block = world.getBlock(ix, iy, iz);
					if (stoneBlocks.contains(block)) {
						int meta = world.getBlockMetadata(ix, iy, iz);
						return new ItemStack(block, 1, meta);
					}
				}
			}
		}
		return null;
	}
/*
	@Override
	public int onBlockPlaced(World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ, int meta) {
		TileEntity entity = world.getTileEntity(x, y, z);
		return meta;
	}
*/
}
