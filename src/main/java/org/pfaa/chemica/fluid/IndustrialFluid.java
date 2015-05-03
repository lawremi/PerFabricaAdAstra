package org.pfaa.chemica.fluid;

import java.awt.Color;

import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;

import org.pfaa.chemica.model.Condition;
import org.pfaa.chemica.model.ConditionProperties;
import org.pfaa.chemica.model.Constants;
import org.pfaa.chemica.model.IndustrialMaterialUtils;
import org.pfaa.chemica.model.IndustrialMaterial;
import org.pfaa.chemica.model.State;
import org.pfaa.chemica.model.StateProperties;

public class IndustrialFluid extends Fluid {

	private Color color;
	private double pressure;
	private IndustrialMaterial material;
	private boolean opaque;
	
	public IndustrialFluid(String fluidName, IndustrialMaterial material) {
		super(fluidName);
		this.material = material;
	}

	@Override
	public int getColor() {
		return color.getRGB();
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public boolean isSuperHeated() {
		return this.getTemperature() >= StateProperties.MIN_GLOWING_TEMPERATURE;
	}
	
	public IndustrialMaterial getIndustrialMaterial() {
		return this.material;
	}
	
	public Condition getCondition() {
		return new Condition(this.getTemperature(), this.getPressure());
	}

	private void setCondition(Condition condition) {
		this.setTemperature((int)condition.temperature);
		this.setPressure(condition.pressure);
	}

	public double getPressure() {
		return this.pressure;
	}

	public void setPressure(double pressure) {
		this.pressure = pressure;
	}
	
	public static IndustrialFluid getCanonicalFluidForState(IndustrialMaterial material, Condition condition) {
		ConditionProperties props = material.getProperties(condition);
		if (props == null || props.state == State.SOLID) {
			return null;
		}
		String name = (props.state.name() + "_" + material.name()).toLowerCase();
		Fluid existingFluid = FluidRegistry.getFluid(name);
		IndustrialFluid fluid;
		if (existingFluid instanceof IndustrialFluid) {
			fluid = (IndustrialFluid)existingFluid;
		} else {
			Condition rounded = IndustrialMaterialUtils.getCanonicalCondition(material, props.state);
			fluid = createFluidForCondition(name, material, rounded);
			FluidRegistry.registerFluid(fluid);
		}
		return fluid;
	}

	private static IndustrialFluid createFluidForCondition(
			String name, IndustrialMaterial material, Condition condition) {
		ConditionProperties props = material.getProperties(condition);
		IndustrialFluid fluid = new IndustrialFluid(name, material);
		fluid.setGaseous(props.state == State.GAS);
		fluid.setDensity((int)(convertToForgeDensity(props.density)));
		fluid.setCondition(condition);
		if (Double.isNaN(props.viscosity)) {
			if (fluid.isSuperHeated()) {
				fluid.setViscosity(FluidRegistry.LAVA.getViscosity());
			}
		} else {
			fluid.setViscosity((int)(props.viscosity / props.density * 1000)); // Forge fluids are in cSt/1000
		}
		fluid.setLuminosity(props.luminosity);
		fluid.setColor(props.color);
		fluid.setOpaque(props.opaque);
		return fluid;
	}

	private static double convertToForgeDensity(double density) {
		return (density <= Constants.AIR_DENSITY ? (density - Constants.AIR_DENSITY) : density) * 1000;
	}

	public boolean isOpaque() {
		return this.opaque;
	}
	
	public void setOpaque(boolean opaque) {
		this.opaque = opaque;
	}

	public ConditionProperties getProperties() {
		return this.material.getProperties(this.getCondition());
	}

    public boolean isPollutant()
    {
        return true;
    }
}
