package org.pfaa.geologica.block;

import org.pfaa.geologica.client.registration.ClientRegistrant;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockStairs;
import net.minecraft.util.IIcon;

public class StairsBlock extends BlockStairs implements ProxyBlock {

	private final Block modelBlock;
	private final int modelBlockMeta;
	private boolean renderAsStairs;
	
	public StairsBlock(Block block, int meta) {
		super(block, meta);
		this.modelBlock = block;
		this.modelBlockMeta = meta;
		this.setLightOpacity(0); // workaround for lighting issue
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int side, int meta) {
		return modelBlock.getIcon(side, modelBlockMeta);
	}

	public Block getModelBlock() {
		return modelBlock;
	}

	public int getModelBlockMeta() {
		return modelBlockMeta;
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
	public int getRenderType() {
		if (this.renderAsStairs) {
			return super.getRenderType();
		} else {
			return ClientRegistrant.compositeStairsBlockRenderer.getRenderId();
		}
	}
	
	public void enableRenderAsStairs() {
		this.renderAsStairs = true;
	}
	
	public void disableRenderAsStairs() {
		this.renderAsStairs = false;
	}
}
