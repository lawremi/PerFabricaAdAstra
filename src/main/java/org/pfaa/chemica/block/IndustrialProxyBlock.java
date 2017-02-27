package org.pfaa.chemica.block;

import org.pfaa.chemica.registration.OreDictUtils;

public interface IndustrialProxyBlock extends IndustrialBlockAccessors {
	IndustrialBlock getModelBlock();
	
	default String oreDictKey() {
		if (this.getModelBlock().oreDictKey() != null) {
			return OreDictUtils.makeKey(this.getForm().oreDictKey(), this.getModelBlock().oreDictKey());
		} else {
			return null;
		}
	}
}
