package org.pfaa.fabrica.client.render;

import org.lwjgl.opengl.GL11;
import org.pfaa.fabrica.entity.TileEntityFluidReactor;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import net.minecraft.block.Block;
import net.minecraft.block.BlockCauldron;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;

public class FluidReactorRenderer implements ISimpleBlockRenderingHandler {

	private int renderId;

	public FluidReactorRenderer(int renderId) {
		this.renderId = renderId;
	}
	
	@Override
	public void renderInventoryBlock(Block block, int metadata, int modelId, RenderBlocks renderer) {
        GL11.glPushMatrix();
        GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
        GL11.glTranslatef(-0.5F, -0.4F, -0.5F);
        GL11.glTranslatef(0F, -0.1F, 0F);
        renderer.renderBlockCauldron((BlockCauldron)block, 0, 0, 0);
        GL11.glPopMatrix();
	}

	@Override
	public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId,
			RenderBlocks renderer) {
		renderer.renderBlockCauldron((BlockCauldron)block, x, y, z);
		
		TileEntityFluidReactor te = (TileEntityFluidReactor)world.getTileEntity(x, y, z);
		FluidTankInfo[] tanks = te.getTankInfo(null);
		float productHeight = this.renderFluid(world, x, y, z, 0, 0, tanks[0].fluid, tanks[0].capacity*tanks.length, renderer);
		this.renderFluid(world, x, y, z, 0, productHeight, tanks[1].fluid, tanks[1].capacity*tanks.length, renderer);
		this.renderFluid(world, x, y, z, 0.5F, productHeight, tanks[2].fluid, tanks[2].capacity*tanks.length, renderer);
		
		return true;
	}

	@Override
	public boolean shouldRender3DInInventory(int modelId) {
		return true;
	}

	@Override
	public int getRenderId() {
		return this.renderId;
	}

	
    private float renderFluid(IBlockAccess world, int x, int y, int z, float xoff, float yoff, FluidStack fluid, int capacity, 
    		RenderBlocks render) {
    	float height = (float) (fluid.amount / capacity + yoff) * 0.9F;
    	
        if (fluid.getFluid() == null || fluid.getFluid().getIcon() == null) return height;
        
        Tessellator tessellator = Tessellator.instance;
        int color = fluid.getFluid().getColor();
        float red = (color >> 16 & 255) / 255.0F;
        float green = (color >> 8 & 255) / 255.0F;
        float blue = (color & 255) / 255.0F;
        
        IIcon iconStill = fluid.getFluid().getIcon();

        double u1, u2, u3, u4, v1, v2, v3, v4;
        u2 = iconStill.getInterpolatedU(0.0D);
        v2 = iconStill.getInterpolatedV(0.0D);
        u1 = u2;
        v1 = iconStill.getInterpolatedV(16.0D);
        u4 = iconStill.getInterpolatedU(16.0D);
        v4 = v1;
        u3 = u4;
        v3 = v2;

        tessellator.setBrightness(200);
        tessellator.setColorOpaque_F(red, green, blue);
        tessellator.addVertexWithUV(x + 0, y + height, z + 0, u2, v2);
        tessellator.addVertexWithUV(x + 0, y + height, z + 1, u1, v1);
        tessellator.addVertexWithUV(x + xoff, y + height, z + 1, u4, v4);
        tessellator.addVertexWithUV(x + xoff, y + height, z + 0, u3, v3);

        render.renderMinY = 0;
        render.renderMaxY = 1;
        
        return height;
    }

}
