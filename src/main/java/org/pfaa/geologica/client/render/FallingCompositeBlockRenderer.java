package org.pfaa.geologica.client.render;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderFallingBlock;
import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

import org.lwjgl.opengl.GL11;
import org.pfaa.block.CompositeBlock;

public class FallingCompositeBlockRenderer extends RenderFallingBlock {

	private final RenderBlocks render = new RenderBlocks();
	
	@Override
	public void doRender(EntityFallingBlock entity, double x, double y, double z, float f, float f1) {
		Block block = entity.func_145805_f();
		if (block.canRenderInPass(0)) {
			this.doRenderPass(entity, x, y, z, f, f1);
		} 
		if (block.canRenderInPass(1)) {
			this.setupAlphaBlending(block);
			this.doRenderPass(entity, x, y, z, f, f1);
		}
	}
	
	private void doRenderPass(EntityFallingBlock entity, double x, double y, double z, float f, float f1) {
		World world = entity.func_145807_e();
        Block block = entity.func_145805_f();
        
        if (!(block instanceof CompositeBlock)) {
        	super.doRender(entity, x, y, z, f, f1);
        	return;
        }
        
        int i = MathHelper.floor_double(entity.posX);
        int j = MathHelper.floor_double(entity.posY);
        int k = MathHelper.floor_double(entity.posZ);

        if (block != null && block != world.getBlock(i, j, k))
        {
            GL11.glPushMatrix();
            GL11.glTranslatef((float)x, (float)y, (float)z);
            this.bindEntityTexture(entity);
            GL11.glDisable(GL11.GL_LIGHTING);

            this.render.setRenderBoundsFromBlock(block);
            this.renderBlockSandFalling(block, world, i, j, k, entity.field_145814_a);

            GL11.glEnable(GL11.GL_LIGHTING);
            GL11.glPopMatrix();
        }
	}
	
	private void renderBlockSandFalling(Block block, World world, int x, int y, int z, int meta)
    {
		int color = block.colorMultiplier(world, x, y, z);
        float r = (float)(color >> 16 & 0xff) / 255F;
        float g = (float)(color >> 8 & 0xff) / 255F;
        float b = (float)(color & 0xff) / 255F;
        float f = 0.5F;
        float f1 = 1.0F;
        float f2 = 0.8F;
        float f3 = 0.6F;
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.setBrightness(block.getMixedBrightnessForBlock(world, x, y, z));
        tessellator.setColorOpaque_F(f*r, f*g, f*b);
        render.renderFaceYNeg(block, -0.5D, -0.5D, -0.5D, render.getBlockIconFromSideAndMetadata(block, 0, meta));
        tessellator.setColorOpaque_F(f1*r, f1*g, f1*b);
        render.renderFaceYPos(block, -0.5D, -0.5D, -0.5D, render.getBlockIconFromSideAndMetadata(block, 1, meta));
        tessellator.setColorOpaque_F(f2*r, f2*g, f2*b);
        render.renderFaceZNeg(block, -0.5D, -0.5D, -0.5D, render.getBlockIconFromSideAndMetadata(block, 2, meta));
        tessellator.setColorOpaque_F(f2*r, f2*g, f2*b);
        render.renderFaceZPos(block, -0.5D, -0.5D, -0.5D, render.getBlockIconFromSideAndMetadata(block, 3, meta));
        tessellator.setColorOpaque_F(f3*r, f3*g, f*b);
        render.renderFaceXNeg(block, -0.5D, -0.5D, -0.5D, render.getBlockIconFromSideAndMetadata(block, 4, meta));
        tessellator.setColorOpaque_F(f3*r, f3*g, f3*b);
        render.renderFaceXPos(block, -0.5D, -0.5D, -0.5D, render.getBlockIconFromSideAndMetadata(block, 5, meta));
        tessellator.draw();
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
}
