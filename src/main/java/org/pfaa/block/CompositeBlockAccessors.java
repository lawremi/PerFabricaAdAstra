package org.pfaa.block;

import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public interface CompositeBlockAccessors {
	public static final int META_WIDTH = 16;
	
	public abstract String getBlockNameSuffix(int meta);
	public abstract int getMetaCount();
	
	public abstract boolean enableOverlay();
	public abstract void disableOverlay();
}
