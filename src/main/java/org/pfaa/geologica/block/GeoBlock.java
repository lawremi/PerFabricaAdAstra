package org.pfaa.geologica.block;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;

import org.pfaa.block.CompositeBlock;
import org.pfaa.chemica.model.Condition;
import org.pfaa.chemica.model.IndustrialMaterial;
import org.pfaa.geologica.GeoMaterial;
import org.pfaa.geologica.GeoMaterial.Strength;
import org.pfaa.geologica.Geologica;
import org.pfaa.geologica.client.registration.ClientRegistrant;
import org.pfaa.geologica.processing.Aggregate;
import org.pfaa.geologica.processing.Aggregate.Aggregates;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/* Rendering strategy:
 * If the GeoMaterial has a host, then we render the host texture, with an overlay (32x32).
 * We get the color from the GeoMaterial, and the overlay style depends on the
 * strength and block Material (sand ore is 'wavy') of the GeoMaterial.
 * For example: 
 * * weak => "fragmented" -- kind of like emerald ore, but not shiny
 * * medium => "diagonal scratches" -- like netherquartz
 * * strong => "horizontal bands" -- like vanilla ores.
 * BrickGeoBlock and BrokenGeoBlock will override to draw "crack" overlay.
 * The same thing needs to happen for walls, stairs, slabs, etc.
 */
public abstract class GeoBlock extends CompositeBlock implements GeoBlockAccessors {

	private List<GeoMaterial> materials = new ArrayList<GeoMaterial>();
	
	private Strength strength;
	private Class<? extends IndustrialMaterial> composition;

	private int renderPass;
	private IIcon[] overlayIcons = new IIcon[4];
	private static Set<Block> stoneBlocks;
	private static Map<GeoMaterial, ItemStack> materialToNativeBlock = new HashMap();
	
	public GeoBlock(Strength strength, Class<? extends IndustrialMaterial> composition, Material material) {
		super(material);
		this.strength = strength;
		this.composition = composition;
		setCreativeTab(CreativeTabs.tabBlock);
		setGeoMaterials();
		setHardness(determineHardness());
		setResistance(determineResistance());
		setStepSound(determineStepSound());
		setHarvestLevel(determineHarvestTool(), determineHarvestLevel());
	}
	
	protected float determineResistance() {
		return Geologica.getConfiguration().getRockResistance(this.strength);
	}

	protected Block.SoundType determineStepSound() {
		Block.SoundType sound = null;
		if (blockMaterial == Material.rock) {
			sound = soundTypeStone;
		} else if (blockMaterial == Material.sand) {
			sound = soundTypeSand;
		} else if (blockMaterial == Material.clay) {
			sound = soundTypeGravel;
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
		return Geologica.getConfiguration().getRockHardness(this.strength);
	}

	private float determineClayHardness() {
		return 0.7F;
	}

	private float determineSandHardness() {
		return 0.6F;
	}

	public String determineHarvestTool() {
		if (blockMaterial == Material.rock)
			return "pickaxe";
		else return "shovel";
	}

	private int determineHarvestLevel() {
		return Geologica.getConfiguration().getHarvestLevel(this.composition, this.strength);
	}

	public boolean hasComposition(Class<? extends IndustrialMaterial> composition) {
		return composition.isAssignableFrom(this.composition);
	}

	private void setGeoMaterials() {
		List<GeoMaterial> materials = GeoMaterial.lookup(strength, composition, blockMaterial);
		if (materials.size() > 16 || materials.size() < 1)
			throw new IllegalArgumentException("GeoBlock only supports 1-16 materials");
		this.materials.clear();
		this.materials.addAll(materials);
	}
	
	public List<GeoMaterial> getGeoMaterials() {
		return Collections.unmodifiableList(materials);
	}

	/* (non-Javadoc)
	 * @see org.pfaa.geologica.block.GeoBlockAccessors#getSubstance(int)
	 */
	@Override
	public GeoMaterial getGeoMaterial(int meta) {
		return materials.get(meta);
	}

	/* (non-Javadoc)
	 * @see org.pfaa.geologica.block.GeoBlockAccessors#containsSubstance(org.pfaa.geologica.GeoSubstance)
	 */
	@Override
	public boolean containsGeoMaterial(GeoMaterial material) {
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
		return getGeoMaterial(meta).getLowerName();
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
	public boolean isReplaceableOreGen(World world, int x, int y, int z,
			Block target) {
		return Aggregate.class.isAssignableFrom(composition);
	}

}
