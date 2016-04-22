package org.pfaa.fabrica.item;

import java.awt.Color;

import org.pfaa.fabrica.block.DrywallBlock;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class JointCompoundItem extends Item {
	
	public boolean onItemUse(ItemStack item, EntityPlayer player, World world, int x, int y, int z, int side, 
			float hitX, float hitY, float hitZ)
    {
		Block block = world.getBlock(x, y, z);
		if (block instanceof DrywallBlock) {
			boolean changed = ((DrywallBlock) block).fillJoints(world, x, y, z);
			item.stackSize--;
			return changed;
		}
		return false;
    }
	
	@SideOnly(Side.CLIENT)
    public CreativeTabs getCreativeTab()
    {
		return CreativeTabs.tabMaterials;
    }
	
	@Override
	@SideOnly(Side.CLIENT)
	protected String getIconString() {
		return "chemica:clump";
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public int getColorFromItemStack(ItemStack itemStack, int par2) {
		return new Color(255, 255, 245).getRGB();
	}

}
