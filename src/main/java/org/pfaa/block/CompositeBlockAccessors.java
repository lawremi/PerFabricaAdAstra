package org.pfaa.block;


public interface CompositeBlockAccessors {
	public static final int META_WIDTH = 16;
	
	public abstract String getBlockNameSuffix(int meta);
	public abstract int getMetaCount();
	
	public abstract boolean enableOverlay();
	public abstract void disableOverlay();
}
