package org.pfaa.geologica.block;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.src.CreativeTabs;
import net.minecraft.src.Material;

import org.pfaa.block.CompositeBlock;
import org.pfaa.geologica.GeoSubstance;
import org.pfaa.geologica.GeoSubstance.Strength;

public abstract class GeoBlock extends CompositeBlock {

	private List<GeoSubstance> substances = new ArrayList<GeoSubstance>();
	
	public GeoBlock(int id, Material material, Strength strength) {
		super(id, 0, material);
		setCreativeTab(CreativeTabs.tabBlock);
		setStrength(strength);
	}
	
	private GeoBlock setStrength(Strength strength) {
		setSubstances(GeoSubstance.getForMaterialAndStrength(blockMaterial, strength));
		setHardness(getHardnessForStrength(strength));
		setResistance(getResistanceForStrength(strength));
		return this;
	}
	
	protected abstract float getResistanceForStrength(Strength strength);
	protected abstract float getHardnessForStrength(Strength strength);

	protected void setSubstances(List<GeoSubstance> substances) {
		if (substances.size() > 16 || substances.size() < 1)
			throw new IllegalArgumentException("GeoBlock only supports 1-16 substances");
		this.substances.clear();
		this.substances.addAll(substances);
	}

	public GeoSubstance getSubstance(int meta) {
		return substances.get(meta);
	}

	public boolean containsSubstance(GeoSubstance substance) {
		return substances.contains(substance);
	}
	
	public int getMetaForSubstance(GeoSubstance rock) {
		return substances.indexOf(rock);
	}
	
	@Override
	public int getMetaCount() {
		return substances.size();
	}

	@Override
	public String getBlockNameForMeta(int meta) {
		return this.getBlockName() + "." + getSubstance(meta).getLowerName();
	}

	@Override
	public int getBlockTextureFromSideAndMetadata(int side, int meta) {
		return this.blockIndexInTexture + getSubstance(meta).getId();
	}

}
