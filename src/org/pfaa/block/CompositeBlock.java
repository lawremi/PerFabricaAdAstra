package org.pfaa.block;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

public abstract class CompositeBlock extends Block implements CompositeBlockAccessors {

	public CompositeBlock(int id, int textureIndex, Material material) {
		super(id, textureIndex, material);
	}
	public CompositeBlock(int id, Material material) {
		this(id, 0, material);
	}
	
	@Override
	public int damageDropped(int meta) {
		return meta;
	}

	@Override
	public int getBlockTextureFromSideAndMetadata(int side, int meta) 
	{
		return blockIndexInTexture + damageDropped(meta);
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
}
