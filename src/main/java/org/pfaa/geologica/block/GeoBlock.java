package org.pfaa.geologica.block;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.pfaa.chemica.ChemicaBlocks;
import org.pfaa.chemica.block.ChanceDropRegistry;
import org.pfaa.chemica.block.IndustrialBlock;
import org.pfaa.chemica.model.Aggregate;
import org.pfaa.chemica.model.IndustrialMaterial;
import org.pfaa.chemica.model.Strength;
import org.pfaa.chemica.processing.Form;
import org.pfaa.chemica.processing.Form.Forms;
import org.pfaa.core.block.BlockWithMeta;
import org.pfaa.geologica.GeoMaterial;
import org.pfaa.geologica.Geologica;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public abstract class GeoBlock extends IndustrialBlock implements GeoBlockAccessors {

	private List<GeoMaterial> materials = new ArrayList<GeoMaterial>();
	
	private Strength strength;
	private Class<? extends IndustrialMaterial> composition;

	private static Map<GeoMaterial, BlockWithMeta<GeoBlock>> materialToNativeBlock = new HashMap<GeoMaterial, BlockWithMeta<GeoBlock>>();
	
	public GeoBlock(Strength strength, Class<? extends IndustrialMaterial> composition, Material material, 
			boolean defaultRendererEnabled) 
	{
		super(material, defaultRendererEnabled);
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
		} else if (blockMaterial == Material.clay || blockMaterial == Material.ground) {
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
		else if (blockMaterial == Material.ground)
			hardness = determineGroundHardness();
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

	private float determineGroundHardness() {
		return 0.7F;
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
		if (materials.size() > 16) {
			materials = materials.subList(0, 16);
		}
		this.materials.clear();
		this.materials.addAll(materials);
	}
	
	public List<GeoMaterial> getGeoMaterials() {
		return Collections.unmodifiableList(materials);
	}

	@Override
	public List<GeoMaterial> getIndustrialMaterials() {
		return this.getGeoMaterials();
	}
	
	/* (non-Javadoc)
	 * @see org.pfaa.geologica.block.GeoBlockAccessors#getSubstance(int)
	 */
	@Override
	public GeoMaterial getGeoMaterial(int meta) {
		return materials.get(meta);
	}

	public GeoMaterial getGeoMaterial(World world, int x, int y, int z) {
		int meta = world.getBlockMetadata(x, y, z);
		return this.getGeoMaterial(meta);
	}

	@Override
	public IndustrialMaterial getIndustrialMaterial(int meta) {
		return this.getGeoMaterial(meta);
	}
	
	/* (non-Javadoc)
	 * @see org.pfaa.geologica.block.GeoBlockAccessors#containsSubstance(org.pfaa.geologica.GeoSubstance)
	 */
	@Override
	public boolean containsGeoMaterial(GeoMaterial material) {
		return materials.contains(material);
	}
	
	@Override
	public String oreDictKey() {
		Form form = this.getForm();
		if (form != Forms.BLOCK && form != Forms.ORE) {
			return form.oreDictKey();
		} else {
			return null;
		}
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
		return this.getItemStack(getMeta(material));
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
	public boolean isReplaceableOreGen(World world, int x, int y, int z, Block target) {
		return Aggregate.class.isAssignableFrom(composition);
	}

	@Override
	public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int metadata, int fortune) {
		GeoMaterial material = this.getGeoMaterial(metadata);
		ArrayList<ItemStack> drops = ChanceDropRegistry.instance().getDrops(material, world.rand, fortune);
		if (drops != null && drops.size() > 0) {
			return drops;
		} else {
			return super.getDrops(world, x, y, z, metadata, fortune);
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	protected IIcon getUnderlayIcon(IBlockAccess world, int x, int y, int z, int side) {
		int meta = world.getBlockMetadata(x, y, z);
		IndustrialMaterial host = this.getGeoMaterial(meta).getHost();
		if (host != null) {
			IIcon icon = getHostIcon(world, x, y, z);
			if (icon != null) {
				return icon;
			}
		}
		return this.getUnderlayIcon(side, meta);
	}
	
	@SideOnly(Side.CLIENT)
	protected IIcon getHostIcon(IBlockAccess world, int x, int y, int z) {
		return null;
	}
	
	@Override
	public boolean useMultipassRendering() {
		return this.needsHost();
	}

	private boolean needsHost() {
		return !this.hasComposition(Aggregate.class);
	}
	
	@Override
	protected IIcon registerUnderlayIcon(IIconRegister registry, int i) {
		IndustrialMaterial host = this.getGeoMaterial(i).getHost();
		if (host instanceof GeoMaterial) {
			GeoMaterial material = (GeoMaterial)host;
			BlockWithMeta<GeoBlock> blockWithMeta = getNative(material);
			return blockWithMeta.block.registerMetaIcon(registry, blockWithMeta.meta);
		} else if (host instanceof Aggregate) {
			Block block = ChemicaBlocks.getBlock((Aggregate)host);
			if (block != null) {
				return block.getIcon(0, 0);
			}
		}
		return super.registerMetaIcon(registry, i);
	}

	public abstract GeoBlock getBrokenRockBlock();
	
	public static <T extends GeoBlock> T registerNative(T block) {
		List<GeoMaterial> materials = block.getGeoMaterials();
		int meta = 0;
		for (GeoMaterial key : materials) {
			materialToNativeBlock.put(key, new BlockWithMeta<GeoBlock>(block, meta++));
		}
		return block;
	}
	
	public static BlockWithMeta<GeoBlock> getNative(GeoMaterial material) {
		return materialToNativeBlock.get(material);
	}
}
