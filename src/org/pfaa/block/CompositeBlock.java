package org.pfaa.block;

import java.util.List;

import org.pfaa.RegistrationUtils;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;

public abstract class CompositeBlock extends Block implements CompositeBlockAccessors {

	private Icon[] icons;
	
	public CompositeBlock(int id, Material material) {
		super(id, material);
	}
	
	@Override
	public int damageDropped(int meta) {
		return meta;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public Icon getIcon(int side, int meta) {
		return icons[damageDropped(meta)];
	}
	
	/* (non-Javadoc)
	 * @see org.pfaa.block.ICompositeBlock#getSubBlocks(int, net.minecraft.creativetab.CreativeTabs, java.util.List)
	 */
	@Override
	public void getSubBlocks(int id, CreativeTabs creativeTabs, List list)
    {
		for (int i = 0; i < getMetaCount(); ++i)
        {
            list.add(new ItemStack(id, 1, damageDropped(i)));
        }
    }
	
	public float getBlockResistance() {
		return this.blockResistance / 3.0F;
	}
	
	public abstract String getModId();
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister registry) {
		icons = new Icon[getMetaCount()];
		String base = getModId() + ":" + getUnlocalizedName().replaceFirst("tile\\.", "");
		for (int i = 0; i < getMetaCount(); ++i)
        {
            icons[i] = registry.registerIcon(base + "_" + getBlockNameSuffix(i));
        }
	}
	
}
