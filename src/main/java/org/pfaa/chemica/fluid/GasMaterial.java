package org.pfaa.chemica.fluid;

import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;

public class GasMaterial extends Material
{
    private static final String __OBFID = "CL_00000541";
	private boolean flammable;

    public GasMaterial(MapColor par1MapColor, boolean flammable)
    {
        super(par1MapColor);
        this.setReplaceable();
        this.flammable = flammable;
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
		return false;
	}
	
	@Override
	public boolean getCanBurn() {
		return this.flammable;
	}
}
