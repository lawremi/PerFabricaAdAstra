package org.pfaa.geologica.block;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.minecraft.block.StepSound;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;

import org.pfaa.block.CompositeBlock;
import org.pfaa.chemica.model.IndustrialMaterial;
import org.pfaa.geologica.GeoMaterial;
import org.pfaa.geologica.GeoMaterial.Strength;
import org.pfaa.geologica.Geologica;
import org.pfaa.geologica.processing.Aggregate;

public abstract class GeoBlock extends CompositeBlock implements GeoBlockAccessors {

	private List<GeoMaterial> materials = new ArrayList<GeoMaterial>();
	
	private Strength strength;
	private Class<? extends IndustrialMaterial> composition;
	
	public GeoBlock(int id, Strength strength, Class<? extends IndustrialMaterial> composition, Material material) {
		super(id, material);
		this.strength = strength;
		this.composition = composition;
		setCreativeTab(CreativeTabs.tabBlock);
		setSubstances();
		setHardness(determineHardness());
		setHarvestLevels();
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
	
	protected float determineHardness() {
		float hardness = 0;
		if (blockMaterial == Material.rock)
			hardness = determineRockHardness();
		else if (blockMaterial == Material.clay)
			hardness = determineClayHardness();
		else if (blockMaterial == Material.sand)
			hardness = determineSandHardness();
		return hardness;
	}
 
	private float determineRockHardness() {
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

	private float determineClayHardness() {
		return 0.7F;
	}

	private float determineSandHardness() {
		return 0.6F;
	}

	protected void setHarvestLevels() {
		MinecraftForge.setBlockHarvestLevel(this, getToolForMaterial(), getHarvestLevel());
	}

	private String getToolForMaterial() {
		if (blockMaterial == Material.rock)
			return "pickaxe";
		else return "shovel";
	}

	private int getHarvestLevel() {
		return Geologica.getConfiguration().getHarvestLevel(this.composition, this.strength);
	}

	public boolean hasComposition(Class<? extends IndustrialMaterial> composition) {
		return composition.isAssignableFrom(this.composition);
	}

	private void setSubstances() {
		List<GeoMaterial> materials = GeoMaterial.lookup(strength, composition, blockMaterial);
		if (materials.size() > 16 || materials.size() < 1)
			throw new IllegalArgumentException("GeoBlock only supports 1-16 materials");
		this.materials.clear();
		this.materials.addAll(materials);
	}
	
	public List<GeoMaterial> getSubstances() {
		return Collections.unmodifiableList(materials);
	}

	/* (non-Javadoc)
	 * @see org.pfaa.geologica.block.GeoBlockAccessors#getSubstance(int)
	 */
	@Override
	public GeoMaterial getSubstance(int meta) {
		return materials.get(meta);
	}

	/* (non-Javadoc)
	 * @see org.pfaa.geologica.block.GeoBlockAccessors#containsSubstance(org.pfaa.geologica.GeoSubstance)
	 */
	@Override
	public boolean containsSubstance(GeoMaterial material) {
		return materials.contains(material);
	}
	
	@Override
	public int getMeta(GeoMaterial material) {
		return materials.indexOf(material);
	}
	
	@Override
	public int getMetaCount() {
		return materials.size();
	}

	@Override
	public String getBlockNameSuffix(int meta) {
		return getSubstance(meta).getLowerName();
	}

	public ItemStack getItemStack(GeoMaterial material) {
		return new ItemStack(this, 1, getMeta(material));
	}

	public Strength getStrength() {
		return strength;
	}

	public Class<? extends IndustrialMaterial> getComposition() {
		return composition;
	}

	public Material getMaterial() {
		return blockMaterial;
	}

	@Override
	public String getModId() {
		return "geologica";
	}

	@Override
	public boolean isGenMineableReplaceable(World world, int x, int y, int z,
			int target) {
		return Aggregate.class.isAssignableFrom(composition);
	}

}
