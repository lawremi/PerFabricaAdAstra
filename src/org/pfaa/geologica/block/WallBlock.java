package org.pfaa.geologica.block;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.BlockWall;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;

import org.pfaa.block.CompositeBlock;
import org.pfaa.block.CompositeBlockAccessors;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class WallBlock extends BlockWall implements CompositeBlockAccessors, ProxyBlock {

	private final CompositeBlock modelBlock;
	
	public WallBlock(int id, CompositeBlock modelBlock) {
		super(id, modelBlock);
		this.modelBlock = modelBlock;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public Icon getIcon(int side, int meta) {
		return modelBlock.getIcon(side, meta);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void getSubBlocks(int id, CreativeTabs creativeTabs, List list)
    {
		for (int i = 0; i < getMetaCount(); ++i)
        {
            list.add(new ItemStack(id, 1, damageDropped(i)));
        }
    }
	
	public int getMetaCount() {
		return modelBlock.getMetaCount();
	}

	@Override
	public int damageDropped(int meta) {
		return meta;
	}

	@Override
	public String getBlockNameSuffix(int meta) {
		return modelBlock.getBlockNameSuffix(meta);
	}

	@Override
	public Block getModelBlock() {
		return modelBlock;
	}

}
