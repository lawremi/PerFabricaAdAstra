package org.pfaa.geologica.block;

import java.util.ArrayList;
import java.util.Random;

import org.pfaa.chemica.model.Aggregate;
import org.pfaa.chemica.model.IndustrialMaterial;
import org.pfaa.chemica.model.Strength;
import org.pfaa.chemica.processing.Form;
import org.pfaa.chemica.processing.Form.Forms;
import org.pfaa.chemica.processing.MaterialStack;
import org.pfaa.geologica.GeoMaterial;
import org.pfaa.geologica.GeologicaBlocks;
import org.pfaa.geologica.processing.Crude;
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
		if (material.getBlockMaterial() == Material.clay) {
			return this.getDrops(material, Forms.LUMP, fortune);
		} else if (material.getBlockMaterial() == Material.ground) {
			return this.getDrops(material, Forms.CLUMP, fortune);
		}
		return super.getDrops(world, x, y, z, metadata, fortune);
	}
	
	private ArrayList<ItemStack> getDrops(GeoMaterial material, Form form, int fortune) {
		GeoMaterial host = (GeoMaterial)material.getHost();
		int numOreDrops = material.getComposition() instanceof Ore && host != null ? fortune + 1 : 4;
		ArrayList<ItemStack> drops = new ArrayList<ItemStack>();
		for (int i = 1; i <= 4; i++) {
			if (i > numOreDrops) {
				material = host;
			}
			drops.add(MaterialStack.of(material, form).getBestItemStack()); 
		}
		return drops;
	}

	@Override
	public Item getItemDropped(int meta, Random random, int par3) {
		Item dropped = super.getItemDropped(meta, random, par3);
		GeoMaterial material = getGeoMaterial(meta);
		GeoBlock broken = this.getBrokenRockBlock();
		if (broken != null && material.getStrength() != Strength.VERY_STRONG) 
		{
			dropped = Item.getItemFromBlock(broken);
		}
		return dropped;
	}
	
	@Override
	public GeoBlock getBrokenRockBlock() {
		if (this.hasComposition(Aggregate.class) && this.blockMaterial == Material.rock) {
			return getBrokenRockBlock(this.getStrength());
		}
		return null;
	}
	
	public static GeoBlock getBrokenRockBlock(Strength strength) {
		GeoBlock dropped = null;
		switch(strength) {
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
			dropped = GeologicaBlocks.VERY_STRONG_COBBLE;
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
		return OreBlockUtils.getHostIcon(world, x, y, z);
	}
	
	@Override
	public Form getForm() {
		if (this.hasComposition(Ore.class)) {
			return Forms.ORE;
		} else if (this.hasComposition(Crude.class) && this.getMaterial().isSolid()) {
			if (this.getMaterial() == Material.ground)
				return Forms.BLOCK;
			else return Forms.ORE;
		} else if (this.getMaterial() == Material.clay) {
			return Forms.BLOCK;
		}
		return Forms.STONE;
	}
}
