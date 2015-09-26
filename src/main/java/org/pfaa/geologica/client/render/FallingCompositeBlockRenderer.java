package org.pfaa.geologica.client.render;

import org.pfaa.core.block.CompositeBlock;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.entity.RenderFallingBlock;
import net.minecraft.entity.item.EntityFallingBlock;

public class FallingCompositeBlockRenderer extends RenderFallingBlock {
	@Override
	public void doRender(EntityFallingBlock entity, double x, double y, double z, float f, float f1) {
		Block block = entity.func_145805_f();
		if (block instanceof CompositeBlock) {
			CompositeBlock compositeBlock = (CompositeBlock)block;
			compositeBlock.disableOverlay();
			super.doRender(entity, x, y, z, f, f1);
			if (compositeBlock.enableOverlay()) {
				CompositeBlockRenderer.enableAlphaBlending();
				super.doRender(entity, x, y, z, f, f1);	
			}
		} else {
			super.doRender(entity, x, y, z, f, f1);
		}
	}
}
