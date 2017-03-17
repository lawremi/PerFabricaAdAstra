package org.pfaa.chemica.block;

public interface IndustrialProxyBlock extends IndustrialBlockAccessors {
	IndustrialBlock getModelBlock();
	
	default String oreDictKey() {
		if (this.getModelBlock().oreDictKey() != null && this.getForm() != null) {
			return this.getForm().oreDictKey();
		} else {
			return null;
		}
	}
}
