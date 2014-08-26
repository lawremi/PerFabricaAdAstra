package org.pfaa.chemica.fluid;

import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;

public class FluidMaterial extends Material {
	private boolean flammable;
	private boolean opaque;

	public FluidMaterial(MapColor par1MapColor, boolean flammable, boolean opaque) {
        super(par1MapColor);
        this.setReplaceable();
        this.flammable = flammable;
        this.opaque = opaque;
	}
	
	@Override
	public boolean getCanBurn() {
		return this.flammable;
	}

    /**
     * Returns if this material is considered solid or not
     */
    public boolean blocksMovement()
    {
        return false;
    }

    public boolean isSolid()
    {
        return false;
    }
    
	@Override
	public boolean isOpaque() {
		return this.opaque;
	}
}

