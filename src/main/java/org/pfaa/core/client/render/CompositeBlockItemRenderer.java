package org.pfaa.core.client.render;

import org.lwjgl.opengl.GL11;
import org.pfaa.core.block.CompositeBlockAccessors;
import org.pfaa.geologica.client.render.CompositeBlockRenderer;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.IItemRenderer;

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
		} else if (type == ItemRenderType.ENTITY) {
			this.useColorMultiplier(item);
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

	private void useColorMultiplier(ItemStack item) {
		int c = item.getItem().getColorFromItemStack(item, 0);
		float r = (float)(c >> 16 & 255) / 255.0F;
		float g = (float)(c >> 8 & 255) / 255.0F;
		float b = (float)(c & 255) / 255.0F;
		GL11.glColor4f(r, g, b, 1.0F);
	}

	private void translateToHand() {
		GL11.glTranslatef(0.5F, 0.5F, 0.5F);
	}
}
