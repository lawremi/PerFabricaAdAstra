package org.pfaa.geologica.block;

import java.util.List;
import java.util.Random;

import net.minecraft.block.BlockStep;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import net.minecraft.world.World;

import org.pfaa.RegistrationUtils;
import org.pfaa.block.CompositeBlock;
import org.pfaa.block.CompositeBlockAccessors;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class SlabBlock extends BlockStep implements CompositeBlockAccessors, ProxyBlock {

	private CompositeBlock modelBlock;
	private SlabBlock otherSlab;
	private boolean isDoubleSlab;
	private Icon[] icons;
	
	public SlabBlock(int id, CompositeBlock modelBlock, SlabBlock singleSlab) {
		super(id, singleSlab != null);
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
	}

	@Override
	@SideOnly(Side.CLIENT)
	public Icon getIcon(int side, int meta) {
		return modelBlock.getIcon(side, meta);
	}

	@Override
	public int idDropped(int par1, Random par2Random, int par3) {
		return getSingleSlabId();
	}

	@Override
	protected ItemStack createStackedBlock(int par1) {
		return new ItemStack(this.blockID, 2, par1);
	}

	@Override
	public String getFullSlabName(int meta) {
		return super.getUnlocalizedName() + "." + modelBlock.getBlockNameSuffix(meta);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void getSubBlocks(int id, CreativeTabs creativeTabs, List list)
    {
		for (int i = 0; i < getMetaCount(); ++i)
        {
            list.add(new ItemStack(id, 1, damageDropped(i)));
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
	public int idPicked(World par1World, int par2, int par3, int par4) {
		return getSingleSlabId();
	}

	public SlabBlock getSingleSlab() {
		return isDoubleSlab() ? otherSlab : this;
	}

	private int getSingleSlabId() {
		return getSingleSlab().blockID;
	}
	
	public SlabBlock getDoubleSlab() {
		return isDoubleSlab() ? this : otherSlab;
	}

	public CompositeBlock getModelBlock() {
		return modelBlock;
	}

	@Override
	public String getBlockNameSuffix(int meta) {
		return modelBlock.getBlockNameSuffix(meta);
	}

}
