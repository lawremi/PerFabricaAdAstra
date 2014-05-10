package org.pfaa.block;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public abstract class CompositeBlock extends Block implements CompositeBlockAccessors {

	private IIcon[] icons;
	
	public CompositeBlock(Material material) {
		super(material);
	}
	
	@Override
	public int damageDropped(int meta) {
		return meta;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int side, int meta) {
		return icons[damageDropped(meta)];
	}
	
	/* (non-Javadoc)
	 * @see org.pfaa.block.ICompositeBlock#getSubBlocks(int, net.minecraft.creativetab.CreativeTabs, java.util.List)
	 */
	@Override
	public void getSubBlocks(Item item, CreativeTabs creativeTabs, List list)
    {
		for (int i = 0; i < getMetaCount(); ++i)
        {
            list.add(new ItemStack(item, 1, damageDropped(i)));
        }
    }
	
	public float getBlockResistance() {
		return this.blockResistance / 3.0F;
	}
	
	public abstract String getModId();
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister registry) {
		icons = new IIcon[getMetaCount()];
		String base = getModId() + ":" + getUnlocalizedName().replaceFirst("tile\\.", "");
		for (int i = 0; i < getMetaCount(); ++i)
        {
            icons[i] = registry.registerIcon(base + "_" + getBlockNameSuffix(i));
        }
	}
	
}
