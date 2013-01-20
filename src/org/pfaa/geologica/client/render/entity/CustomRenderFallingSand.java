package org.pfaa.geologica.client.render.entity;

import org.lwjgl.opengl.GL11;

import net.minecraft.block.Block;
import net.minecraft.block.BlockAnvil;
import net.minecraft.block.BlockDragonEgg;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderFallingSand;
import net.minecraft.entity.item.EntityFallingSand;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class CustomRenderFallingSand extends RenderFallingSand {
	private RenderBlocks renderBlocks = new RenderBlocks();

	@Override
	public void doRenderFallingSand(EntityFallingSand entity, double x, double y, double z, float par8, float par9)
    {
        GL11.glPushMatrix();
        GL11.glTranslatef((float)x, (float)y, (float)z);
        GL11.glDisable(GL11.GL_LIGHTING);
        
        World world = entity.getWorld();
        Block block = Block.blocksList[entity.blockID];
        this.loadTexture(block.getTextureFile());
        
        this.renderBlocks.updateCustomBlockBounds(block);
        this.renderBlocks.renderBlockSandFalling(block, world, MathHelper.floor_double(entity.posX), MathHelper.floor_double(entity.posY), MathHelper.floor_double(entity.posZ), entity.metadata);
        
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glPopMatrix();
    }
}
