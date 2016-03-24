package org.pfaa.chemica.block;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.pfaa.chemica.model.Condition;
import org.pfaa.chemica.model.ConstructionMaterial;
import org.pfaa.chemica.model.IndustrialMaterial;
import org.pfaa.chemica.model.Metal;
import org.pfaa.chemica.model.Strength;

import com.google.common.base.CaseFormat;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.IBlockAccess;

public class ConstructionMaterialBlock extends Block implements IndustrialBlockAccessors {

	private List<ConstructionMaterial> materials;
	
	public <T extends Enum<?> & ConstructionMaterial> ConstructionMaterialBlock(Strength strength, Class<T> enumClass) {
		super(Metal.class.isAssignableFrom(enumClass) ? Material.iron : Material.rock);
		this.materials = getMaterials(enumClass, strength);
		this.setStepSound(Metal.class.isAssignableFrom(enumClass) ? soundTypeMetal : soundTypeStone);
		this.setCreativeTab(CreativeTabs.tabBlock);
		this.setHardness(this.determineHardness(strength));
		this.setResistance(this.determineResistance(strength));
		this.setHarvestLevel("pickaxe", this.determineHarvestLevel(strength));
	}

	private int determineHarvestLevel(Strength strength) {
		return strength.ordinal();
	}

	private float determineResistance(Strength strength) {
		return this.determineHardness(strength) * 2.0F;
	}

	private float determineHardness(Strength strength) {
		float hardness = 0;
		switch(strength) {
		case WEAK:
			hardness = 1.0F;
			break;
		case MEDIUM:
			hardness = 3.0F;
			break;
		case STRONG:
			hardness = 5.0F;
			break;
		case VERY_STRONG:
			hardness = 7.0F;
			break;
		default:
		}
		return hardness;
	}

	private <T extends Enum<?> & ConstructionMaterial> List<ConstructionMaterial> getMaterials(Class<T> enumClass, Strength strength) {
		ArrayList<ConstructionMaterial> materials = new ArrayList<ConstructionMaterial>();
		for (T material : enumClass.getEnumConstants()) {
			if (material.getStrength() == strength) {
				materials.add(material);
			}
		}
		return materials;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public int colorMultiplier(IBlockAccess world, int x, int y, int z) {
		int meta = world.getBlockMetadata(x, y, z);
		return this.colorMultiplier(meta);
	}
	
	@Override
	public int colorMultiplier(int meta) {
		return this.materials.get(meta).getProperties(Condition.STP).color.getRGB();
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister par1IconRegister) {
		blockIcon = par1IconRegister.registerIcon("chemica:block");
	}
	
	@Override
	public int damageDropped(int meta) {
		return meta;
	}

	@SuppressWarnings("unchecked")
	@Override
	@SideOnly(Side.CLIENT)
	public void getSubBlocks(Item item, CreativeTabs creativeTabs, @SuppressWarnings("rawtypes") List list)
    {
		for (int i = 0; i < this.materials.size(); ++i)
        {
            list.add(new ItemStack(item, 1, damageDropped(i)));
        }
    }

	public List<ConstructionMaterial> getConstructionMaterials() {
		return Collections.unmodifiableList(this.materials);
	}

	public ItemStack getItemStack(ConstructionMaterial material) {
		return new ItemStack(this, 1, this.getMeta(material));
	}

	private int getMeta(ConstructionMaterial material) {
		return this.materials.indexOf(material);
	}

	@Override
	public String getBlockNameSuffix(int meta) {
		String name = this.getConstructionMaterial(meta).name();
		return CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, name);
	}

	private ConstructionMaterial getConstructionMaterial(int meta) {
		return this.materials.get(meta);
	}
	
	@Override
	public IndustrialMaterial getIndustrialMaterial(int meta) {
		return this.getConstructionMaterial(meta);
	}

	@Override
	public int getMetaCount() {
		return this.materials.size();
	}

	@Override
	public boolean enableOverlay() {
		return false;
	}

	@Override
	public void disableOverlay() {
	}	
}
