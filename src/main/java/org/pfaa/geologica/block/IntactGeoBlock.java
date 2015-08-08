package org.pfaa.geologica.block;

import java.util.ArrayList;
import java.util.Random;

import org.pfaa.chemica.model.IndustrialMaterial;
import org.pfaa.geologica.GeoMaterial;
import org.pfaa.geologica.GeoMaterial.Strength;
import org.pfaa.geologica.GeologicaBlocks;
import org.pfaa.geologica.GeologicaItems;
import org.pfaa.geologica.processing.Aggregate;
import org.pfaa.geologica.processing.Ore;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class IntactGeoBlock extends GeoBlock {
	
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
		GeoMaterial host = (GeoMaterial)material.getHost();
		int numOreDrops = material.getComposition() instanceof Ore && host != null ? fortune + 1 : 4;
		ArrayList<ItemStack> drops = new ArrayList<ItemStack>();
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
		return BlockUtils.getHostIcon(world, x, y, z);
	}
}
