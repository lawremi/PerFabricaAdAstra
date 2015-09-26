package org.pfaa.geologica.block;

import java.util.List;
import java.util.Random;

import org.pfaa.core.block.CompositeBlock;
import org.pfaa.core.block.CompositeBlockAccessors;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.BlockSlab;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public class SlabBlock extends BlockSlab implements CompositeBlockAccessors, ProxyBlock {

	private CompositeBlock modelBlock;
	private SlabBlock otherSlab;
	private boolean isDoubleSlab;
	
	public SlabBlock(CompositeBlock modelBlock, SlabBlock singleSlab) {
		super(singleSlab != null, modelBlock.getMaterial());
		this.modelBlock = modelBlock;
		if (singleSlab != null) {
			this.isDoubleSlab = true;
			this.otherSlab = singleSlab;
			this.otherSlab.otherSlab = this;
			this.setCreativeTab(null);
		}
		this.setHardness(modelBlock.getBlockHardness(null, 0, 0, 0));
		this.setResistance(modelBlock.getBlockResistance());
		this.setStepSound(modelBlock.stepSound);
		this.setLightOpacity(0); // workaround for lighting issue
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int side, int meta) {
		return modelBlock.getIcon(side, meta & 7);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister p_149651_1_) {
		// icon already registered by model block
	}

	@Override
	public Item getItemDropped(int par1, Random par2Random, int par3) {
		return getSingleSlabItem();
	}

	@Override
	protected ItemStack createStackedBlock(int meta) {
		return new ItemStack(this, 2, meta & 7);
	}

	@Override
	public String func_150002_b(int meta) {
		return super.getUnlocalizedName() + "." + modelBlock.getBlockNameSuffix(meta & 7);
	}

	@SuppressWarnings("unchecked")
	@Override
	@SideOnly(Side.CLIENT)
	public void getSubBlocks(Item item, CreativeTabs creativeTabs, @SuppressWarnings("rawtypes") List list)
    {
		for (int i = 0; i < getMetaCount(); ++i)
		{
			list.add(new ItemStack(item, 1, damageDropped(i)));
		}
    }
	
	public int getMetaCount() {
		return modelBlock.getMetaCount();
	}

	public boolean isDoubleSlab() {
		return isDoubleSlab;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public ItemStack getPickBlock(MovingObjectPosition target, World world, int x, int y, int z) {
		return new ItemStack(getSingleSlabItem(), 1, world.getBlockMetadata(x, y, z));
	}

	public SlabBlock getSingleSlab() {
		return isDoubleSlab() ? otherSlab : this;
	}

	private Item getSingleSlabItem() {
		return Item.getItemFromBlock(getSingleSlab());
	}
	
	public SlabBlock getDoubleSlab() {
		return isDoubleSlab() ? this : otherSlab;
	}

	public CompositeBlock getModelBlock() {
		return modelBlock;
	}

	@Override
	public String getBlockNameSuffix(int meta) {
		return modelBlock.getBlockNameSuffix(meta & 7);
	}

	@Override
	public int getRenderType() {
		return super.getRenderType();
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

}
