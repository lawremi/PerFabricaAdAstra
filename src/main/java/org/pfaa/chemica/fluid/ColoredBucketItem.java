package org.pfaa.chemica.fluid;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.init.Items;
import net.minecraft.item.ItemBucket;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ColoredBucketItem extends ItemBucket {

	private IIcon overlay;
	private Block fluidBlock;
	
	public ColoredBucketItem(Block block) {
		super(block);
		this.fluidBlock = block;
		this.setContainerItem(Items.bucket);
	}

	@Override
    @SideOnly(Side.CLIENT)
    public boolean requiresMultipleRenderPasses()
    {
        return true;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister register)
    {
    	super.registerIcons(register);
		this.overlay = register.registerIcon("chemica:bucket_overlay");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIconFromDamageForRenderPass(int damage, int renderPass)
    {
        return renderPass > 0 ? this.overlay : super.getIconFromDamageForRenderPass(damage, renderPass);
    }

	@Override
	@SideOnly(Side.CLIENT)
	public int getColorFromItemStack(ItemStack itemStack, int renderPass) {
		return renderPass > 0 ? this.getColor() : super.getColorFromItemStack(itemStack, renderPass); 
	}

	@Override
	@SideOnly(Side.CLIENT)
	protected String getIconString() {
		return "bucket_empty";
	}

	@SideOnly(Side.CLIENT)
	private int getColor() {
		return this.fluidBlock.colorMultiplier(null, 0, 0, 0);
	}
}
