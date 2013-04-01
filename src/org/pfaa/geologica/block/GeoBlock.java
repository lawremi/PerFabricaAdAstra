package org.pfaa.geologica.block;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.StepSound;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraftforge.common.MinecraftForge;

import org.pfaa.block.CompositeBlock;
import org.pfaa.geologica.GeoSubstance;
import org.pfaa.geologica.GeoSubstance.Strength;
import org.pfaa.geologica.GeoSubstance.Composition;

public abstract class GeoBlock extends CompositeBlock implements GeoBlockAccessors {

	private List<GeoSubstance> substances = new ArrayList<GeoSubstance>();
	
	public GeoBlock(int id, Strength strength, Composition composition, Material material) {
		super(id, 0, material);
		setCreativeTab(CreativeTabs.tabBlock);
		setSubstances(GeoSubstance.lookup(strength, composition, blockMaterial));
		setHardness(determineHardness(strength));
		setHarvestLevels(strength);
		setStepSound(determineStepSound());
	}
	
	protected StepSound determineStepSound() {
		StepSound sound = null;
		if (blockMaterial == Material.rock) {
			sound = soundStoneFootstep;
		} else if (blockMaterial == Material.sand) {
			sound = soundSandFootstep;
		} else if (blockMaterial == Material.clay) {
			sound = soundGravelFootstep;
		}
		return sound;
	}
	
	protected float determineHardness(Strength strength) {
		float hardness = 0;
		if (blockMaterial == Material.rock)
			hardness = determineRockHardness(strength);
		else if (blockMaterial == Material.clay)
			hardness = determineClayHardness(strength);
		else if (blockMaterial == Material.sand)
			hardness = determineSandHardness(strength);
		return hardness;
	}
 
	private float determineRockHardness(Strength strength) {
		float hardness = 0;
		switch(strength) {
		case WEAK:
			hardness = 1.0F;
			break;
		case MEDIUM:
			hardness = 2.0F;
			break;
		case STRONG:
			hardness = 3.0F;
			break;
		case VERY_STRONG:
			hardness = 4.0F;
			break;
		default:
		}
		return hardness;
	}

	private float determineClayHardness(Strength strength) {
		return 0.7F;
	}

	private float determineSandHardness(Strength strength) {
		return 0.6F;
	}

	protected void setHarvestLevels(Strength strength) {
		MinecraftForge.setBlockHarvestLevel(this, getToolForMaterial(), getHarvestLevelForStrength(strength));
	}

	private String getToolForMaterial() {
		if (blockMaterial == Material.rock)
			return "pickaxe";
		else return "shovel";
	}

	private int getHarvestLevelForStrength(Strength strength) {
		int level = 0;
		switch(strength) {
		case WEAK:
		case MEDIUM:
			level = 0;
			break;
		case STRONG:
			level = 1;
			break;
		case VERY_STRONG:
			level = 2;
			break;
		default:
		}
		return level;
	}

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
