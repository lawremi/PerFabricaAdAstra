package org.pfaa.geologica.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockStairs;
import net.minecraft.util.Icon;

public class StairsBlock extends BlockStairs implements ProxyBlock {

	private final Block modelBlock;
	private final int modelBlockMeta;
	
	public StairsBlock(int id, Block block, int meta) {
		super(id, block, meta);
		this.modelBlock = block;
		this.modelBlockMeta = meta;
		this.setLightOpacity(0); // workaround for lighting issue
	}

	@Override
	@SideOnly(Side.CLIENT)
	public Icon getIcon(int side, int meta) {
		return modelBlock.getIcon(side, modelBlockMeta);
	}

	public Block getModelBlock() {
		return modelBlock;
	}

	public int getModelBlockMeta() {
		return modelBlockMeta;
	}

}
