package org.pfaa.geologica.block;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.BlockWall;
import net.minecraft.creativetab.CreativeTabs;
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
		int l = par1iBlockAccess.getBlockId(par2, par3, par4);
		if (l == GeologicaBlocks.MEDIUM_STONE_BRICK_WALL.blockID ||
			l == GeologicaBlocks.STRONG_STONE_BRICK_WALL.blockID || 
			l == GeologicaBlocks.VERY_STRONG_STONE_BRICK_WALL.blockID) {
			return true;
		}
		return super.canConnectWallTo(par1iBlockAccess, par2, par3, par4);
	}

	private final CompositeBlock modelBlock;
	
	public WallBlock(int id, CompositeBlock modelBlock) {
		super(id, modelBlock);
		this.modelBlock = modelBlock;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int side, int meta) {
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
