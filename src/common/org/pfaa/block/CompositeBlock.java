package org.pfaa.block;

import java.util.Arrays;
import java.util.List;

import net.minecraft.src.Block;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Material;
import net.minecraft.src.World;

public class CompositeBlock extends Block {

	public static final int META_WIDTH = 16;
	
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

	@Override
	public void getSubBlocks(int id, CreativeTabs creativeTabs, List list)
    {
		for (int i = 0; i < getMetaCount(); ++i)
        {
            list.add(new ItemStack(id, 1, damageDropped(i)));
        }
    }
	
	public String getBlockNameForMeta(int meta) {
		return this.getBlockName();
	}
	
	public int getMetaCount() {
		return META_WIDTH;
	}
}
