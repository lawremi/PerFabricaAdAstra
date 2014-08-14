package org.pfaa.chemica.fluid;

import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;

public class GasMaterial extends Material
{
    private static final String __OBFID = "CL_00000541";

    public GasMaterial(MapColor par1MapColor)
    {
        super(par1MapColor);
        this.setReplaceable();
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
}
