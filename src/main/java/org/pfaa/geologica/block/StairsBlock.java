package org.pfaa.geologica.block;

import org.pfaa.chemica.block.IndustrialBlock;
import org.pfaa.chemica.block.IndustrialProxyBlock;
import org.pfaa.chemica.model.IndustrialMaterial;
import org.pfaa.chemica.processing.Form;
import org.pfaa.chemica.processing.Form.Forms;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.BlockStairs;
import net.minecraft.util.IIcon;

public class StairsBlock extends BlockStairs implements IndustrialProxyBlock {

	private final IndustrialBlock modelBlock;
	private final int modelBlockMeta;
	
	public StairsBlock(IndustrialBlock block, int meta) {
		super(block, meta);
		this.modelBlock = block;
		this.modelBlockMeta = meta;
		this.setLightOpacity(0); // workaround for lighting issue
		this.useNeighborBrightness = true;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int side, int meta) {
		return modelBlock.getIcon(side, modelBlockMeta);
	}

	public IndustrialBlock getModelBlock() {
		return modelBlock;
	}

	public int getModelBlockMeta() {
		return modelBlockMeta;
	}

	@Override
	public String getBlockNameSuffix(int meta) {
		return null;
	}

	@Override
	public int getMetaCount() {
		return 1;
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
	public boolean canRenderInPass(int pass) {
		return modelBlock.canRenderInPass(pass);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public int getRenderBlockPass() {
		return modelBlock.getRenderBlockPass();
	}

	@Override
	public int colorMultiplier(int meta) {
		return modelBlock.colorMultiplier(meta);
	}

	@Override
	public Form getForm() {
		return Forms.STAIR.of(modelBlock.getForm());
	}

	@Override
	public IndustrialMaterial getIndustrialMaterial(int meta) {
		return modelBlock.getIndustrialMaterial(modelBlockMeta);
	}

}
