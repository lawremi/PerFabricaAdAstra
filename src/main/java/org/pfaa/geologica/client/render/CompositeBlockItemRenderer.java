package org.pfaa.geologica.client.render;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.IItemRenderer;

import org.lwjgl.opengl.GL11;

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
		} else if (type == ItemRenderType.INVENTORY) {
			this.setupAlphaBlending(block);
		}
		if (block.canRenderInPass(0)) {
			renderer.renderBlockAsItem(block, item.getItemDamage(), 1.0F);
		}
		if (block.canRenderInPass(1)) {
			this.setupOverlayColor(item);
			renderer.renderBlockAsItem(block, item.getItemDamage(), 1.0F);
		}
		renderer.useInventoryTint = true;
	}

	private void setupOverlayColor(ItemStack itemStack) {
		int color = itemStack.getItem().getColorFromItemStack(itemStack, 1);
        float r = (float)(color >> 16 & 0xff) / 255F;
        float g = (float)(color >> 8 & 0xff) / 255F;
        float b = (float)(color & 0xff) / 255F;
        GL11.glColor4f(r, g, b, 1.0F);
	}

	private void setupAlphaBlending(Block block) {
		GL11.glEnable(GL11.GL_ALPHA_TEST);
        if (block.getRenderBlockPass() != 0)
        {
            GL11.glAlphaFunc(GL11.GL_GREATER, 0.1F);
            GL11.glEnable(GL11.GL_BLEND);
            OpenGlHelper.glBlendFunc(770, 771, 1, 0);
        }
        else
        {
            GL11.glAlphaFunc(GL11.GL_GREATER, 0.5F);
            GL11.glDisable(GL11.GL_BLEND);
        }
	}

	private void translateToHand() {
		GL11.glTranslatef(0.5F, 0.5F, 0.5F);
	}
}
