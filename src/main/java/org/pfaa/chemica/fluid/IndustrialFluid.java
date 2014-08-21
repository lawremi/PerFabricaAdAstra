package org.pfaa.chemica.fluid;

import java.awt.Color;

import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;

import org.pfaa.chemica.model.Chemical;
import org.pfaa.chemica.model.Condition;
import org.pfaa.chemica.model.ConditionProperties;
import org.pfaa.chemica.model.Constants;
import org.pfaa.chemica.model.IndustrialMaterial;
import org.pfaa.chemica.model.Phase;
import org.pfaa.chemica.model.PhaseProperties;

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
		return this.getTemperature() >= PhaseProperties.MIN_GLOWING_TEMPERATURE;
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
	
	public static IndustrialFluid getCanonicalFluidForPhase(IndustrialMaterial material, Condition condition) {
		ConditionProperties props = material.getProperties(condition);
		if (props == null || props.phase == Phase.SOLID) {
			return null;
		}
		String name = props.phase.name() + "_" + material.name();
		Fluid existingFluid = FluidRegistry.getFluid(name);
		IndustrialFluid fluid;
		if (existingFluid instanceof IndustrialFluid) {
			fluid = (IndustrialFluid)existingFluid;
		} else {
			Condition rounded = roundCondition(material, props.phase);
			fluid = createFluidForCondition(name, material, rounded);
			FluidRegistry.registerFluid(fluid);
		}
		return fluid;
	}

	private static IndustrialFluid createFluidForCondition(
			String name, IndustrialMaterial material, Condition condition) {
		ConditionProperties props = material.getProperties(condition);
		IndustrialFluid fluid = new IndustrialFluid(name, material);
		fluid.setGaseous(props.phase == Phase.GAS);
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

	private static Condition roundCondition(IndustrialMaterial material, Phase phase) {
		double temperature = Constants.STANDARD_TEMPERATURE;
		if (material instanceof Chemical) {
			Chemical chemical = (Chemical)material;
			temperature = getTemperatureClosestToStandard(chemical, phase);
		}
		return new Condition(temperature, Constants.STANDARD_PRESSURE);
	}

	private static double getTemperatureClosestToStandard(Chemical chemical, Phase phase) {
		switch(phase) {
		case SOLID:
			return Math.min(chemical.getFusion().getTemperature(), Constants.STANDARD_TEMPERATURE);
		case LIQUID:
			return Math.max(chemical.getFusion().getTemperature(), Constants.STANDARD_TEMPERATURE);
		case GAS:
			return Math.max(chemical.getVaporization().getTemperature(), Constants.STANDARD_TEMPERATURE);
		default:
			return Constants.STANDARD_TEMPERATURE;
		}
	}

	public boolean isOpaque() {
		return this.opaque;
	}
	
	public void setOpaque(boolean opaque) {
		this.opaque = opaque;
	}

	public double getMaterialDensity() {
		return this.material.getProperties(this.getCondition()).density;
	}
}
