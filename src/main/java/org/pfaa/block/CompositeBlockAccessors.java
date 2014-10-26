package org.pfaa.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public interface CompositeBlockAccessors {
	public static final int META_WIDTH = 16;
	
	public abstract String getBlockNameSuffix(int meta);
	public abstract int getMetaCount();
	
	@SideOnly(Side.CLIENT)
	public int colorMultiplier(int meta);
}
