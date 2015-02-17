package org.pfaa.geologica.client.render;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.IItemRenderer;

import org.lwjgl.opengl.GL11;
import org.pfaa.block.CompositeBlockAccessors;

public class CompositeBlockItemRenderer implements IItemRenderer {

	@Override
	public boolean handleRenderType(ItemStack item, ItemRenderType type) {
		return true;
	}

	@Override
	public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item,
			ItemRendererHelper helper) {
		return true;
	}

	@Override
	public void renderItem(ItemRenderType type, ItemStack item, Object... data) {
		Block block = Block.getBlockFromItem(item.getItem());
		RenderBlocks renderer = (RenderBlocks)data[0];
		renderer.useInventoryTint = false;
		if (type == ItemRenderType.EQUIPPED || type == ItemRenderType.EQUIPPED_FIRST_PERSON) {
			this.translateToHand();
		}
		CompositeBlockAccessors compositeBlock = (CompositeBlockAccessors)block;
		compositeBlock.disableOverlay();
		renderer.renderBlockAsItem(block, item.getItemDamage(), 1.0F);
		if (compositeBlock.enableOverlay()) {
			CompositeBlockRenderer.enableAlphaBlending();
			GL11.glScalef(1.01F, 1.01F, 1.01F);
			renderer.renderBlockAsItem(block, item.getItemDamage(), 1.0F);
			compositeBlock.disableOverlay();
		}
		renderer.useInventoryTint = true;
	}

	private void translateToHand() {
		GL11.glTranslatef(0.5F, 0.5F, 0.5F);
	}
}
