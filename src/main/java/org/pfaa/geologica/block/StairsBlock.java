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
		/*
		 * The joining here may be invalid with respect to the ore dictionary,
		 * because ore dict terms can only have two parts [prefix][Suffix],
		 * where prefix corresponds to a Form and suffix to a material.
		 * 
		 * It so happens that some forms correspond to aggregate materials,
		 * so we are tempted to simply join forms, but instead we should
		 * just return "stair" here as the form, and then relate somehow
		 * that the model block has a generic material. Joining forms 
		 * would increase the complexity of the ore dictionary.
		 * 
		 * The simpler case is that of the model block itself. Currently, we
		 * check whether the form (prefix) exists and, if it does, we
		 * add the block under that single-part term, which means essentially
		 * [prefix]*.
		 * 
		 * Proposal:
		 * 1) Introduce an .oreDictKey() on IndustrialItemAccessors that returns
		 *    the Form.oreDictKey() by default, while proxy blocks join the key.
		 * 2) getForm() always returns a top-level form, i.e., the prefix.
		 * 
		 * Alternative: .oreDictKey() returns null by default, overridden by proxies.
		 *              If oreDictKey() is not null, register with it.
		 *              Then *also* register with the form key (if it already exists).
		 * 
		 * 
		 */
		return Forms.STAIR;
	}

	@Override
	public IndustrialMaterial getIndustrialMaterial(int meta) {
		return modelBlock.getIndustrialMaterial(modelBlockMeta);
	}

}
