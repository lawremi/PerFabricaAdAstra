package org.pfaa.geologica.block;

import java.util.List;

import org.pfaa.core.block.CompositeBlock;
import org.pfaa.core.block.CompositeBlockAccessors;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockWall;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class WallBlock extends BlockWall implements CompositeBlockAccessors, ProxyBlock {

	@Override
	public boolean canConnectWallTo(IBlockAccess par1iBlockAccess, int par2,
			int par3, int par4) {
		Block block = par1iBlockAccess.getBlock(par2, par3, par4);
		if (block instanceof BlockWall) {
			return true;
		}
		return super.canConnectWallTo(par1iBlockAccess, par2, par3, par4);
	}

	private final CompositeBlock modelBlock;
	
	public WallBlock(CompositeBlock modelBlock) {
		super(modelBlock);
		this.modelBlock = modelBlock;
	}

	@SuppressWarnings("unchecked")
	@Override
	@SideOnly(Side.CLIENT)
	public void getSubBlocks(Item item, CreativeTabs creativeTabs, @SuppressWarnings("rawtypes") List list)
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

	@Override
	public boolean canRenderInPass(int pass) {
		return modelBlock.canRenderInPass(pass);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public int getRenderBlockPass() {
		return modelBlock.getRenderBlockPass();
	}

	@Override
	public boolean enableOverlay() {
		return modelBlock.enableOverlay();
	}

	@Override
	public void disableOverlay() {
		modelBlock.disableOverlay();
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int side, int meta) {
		return modelBlock.getIcon(side, meta);
	}
	
	@Override
	public boolean canPlaceTorchOnTop(World world, int x, int y, int z) {
		return true;
	}
}
