package org.pfaa.chemica.fluid;

import java.awt.Color;

import net.minecraftforge.fluids.Fluid;

public class ColoredFluid extends Fluid {

	private Color color;

	public ColoredFluid(String fluidName, Color color) {
		super(fluidName);
		this.color = color;
	}

	@Override
	public int getColor() {
		return this.color.getRGB() | 0xff000000;
	}
	
}
