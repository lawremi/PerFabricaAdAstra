package org.pfaa.block;

public interface CompositeBlockAccessors {

	public static final int META_WIDTH = 16;
	
	public abstract String getBlockNameSuffixForMeta(int meta);
	public abstract int getMetaCount();
}
