package org.pfaa.geologica.block;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.StepSound;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;

import org.pfaa.block.CompositeBlock;
import org.pfaa.geologica.GeoSubstance;
import org.pfaa.geologica.GeoSubstance.Strength;
import org.pfaa.geologica.GeoSubstance.SubstanceType;

public abstract class GeoBlock extends CompositeBlock implements GeoBlockAccessors {

	private List<GeoSubstance> substances = new ArrayList<GeoSubstance>();
	private SubstanceType substanceType;
	
	public GeoBlock(int id, Strength strength, SubstanceType substanceType, Material material) {
		super(id, 0, material);
		this.substanceType = substanceType;
		setCreativeTab(CreativeTabs.tabBlock);
		setStrength(strength);
		setStepSound(determineStepSound(material));
	}
	
	protected StepSound determineStepSound(Material material) {
		StepSound sound = null;
		if (material == Material.rock) {
			sound = soundStoneFootstep;
		} else if (material == Material.sand) {
			sound = soundSandFootstep;
		}
		return sound;
	}
	
	private void setStrength(Strength strength) {
		setSubstances(GeoSubstance.lookup(strength, substanceType, blockMaterial));
		setHardness(determineHardness(strength));
		setResistance(determineResistance(strength));
	}
	
	protected abstract float determineResistance(Strength strength);
	protected abstract float determineHardness(Strength strength);

	private void setSubstances(List<GeoSubstance> substances) {
		if (substances.size() > 16 || substances.size() < 1)
			throw new IllegalArgumentException("GeoBlock only supports 1-16 substances");
		this.substances.clear();
		this.substances.addAll(substances);
	}

	/* (non-Javadoc)
	 * @see org.pfaa.geologica.block.GeoBlockAccessors#getSubstance(int)
	 */
	@Override
	public GeoSubstance getSubstance(int meta) {
		return substances.get(meta);
	}

	/* (non-Javadoc)
	 * @see org.pfaa.geologica.block.GeoBlockAccessors#containsSubstance(org.pfaa.geologica.GeoSubstance)
	 */
	@Override
	public boolean containsSubstance(GeoSubstance substance) {
		return substances.contains(substance);
	}
	
	/* (non-Javadoc)
	 * @see org.pfaa.geologica.block.GeoBlockAccessors#getMetaForSubstance(org.pfaa.geologica.GeoSubstance)
	 */
	@Override
	public int getMetaForSubstance(GeoSubstance substance) {
		return substances.indexOf(substance);
	}
	
	@Override
	public int getMetaCount() {
		return substances.size();
	}

	@Override
	public int getBlockTextureFromSideAndMetadata(int side, int meta) {
		return this.blockIndexInTexture + getSubstance(meta).getId();
	}

	@Override
	public String getBlockNameSuffixForMeta(int meta) {
		return getSubstance(meta).getLowerName();
	}

}
