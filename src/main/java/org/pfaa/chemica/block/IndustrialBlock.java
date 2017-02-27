package org.pfaa.chemica.block;

import org.pfaa.core.block.CompositeBlock;

import net.minecraft.block.material.Material;

public abstract class IndustrialBlock extends CompositeBlock implements IndustrialBlockAccessors {

	public IndustrialBlock(Material material, boolean defaultRendererEnabled) {
		super(material, defaultRendererEnabled);
	}

}
