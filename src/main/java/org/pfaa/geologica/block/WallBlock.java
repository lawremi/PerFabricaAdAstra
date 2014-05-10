package org.pfaa.geologica.block;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.BlockWall;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;

import org.pfaa.block.CompositeBlock;
import org.pfaa.block.CompositeBlockAccessors;
import org.pfaa.geologica.GeologicaBlocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class WallBlock extends BlockWall implements CompositeBlockAccessors, ProxyBlock {

	@Override
	public boolean canConnectWallTo(IBlockAccess par1iBlockAccess, int par2,
			int par3, int par4) {
		Block block = par1iBlockAccess.getBlock(par2, par3, par4);
		if (block == GeologicaBlocks.MEDIUM_STONE_BRICK_WALL ||
			block == GeologicaBlocks.STRONG_STONE_BRICK_WALL || 
			block == GeologicaBlocks.VERY_STRONG_STONE_BRICK_WALL) {
			return true;
		}
		return super.canConnectWallTo(par1iBlockAccess, par2, par3, par4);
	}

	private final CompositeBlock modelBlock;
	
	public WallBlock(CompositeBlock modelBlock) {
		super(modelBlock);
		this.modelBlock = modelBlock;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int side, int meta) {
		return modelBlock.getIcon(side, meta);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void getSubBlocks(Item item, CreativeTabs creativeTabs, List list)
    {
		for (int i = 0; i < getMetaCount(); ++i)
        {
            list.add(new ItemStack(item, 1, damageDropped(i)));
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
