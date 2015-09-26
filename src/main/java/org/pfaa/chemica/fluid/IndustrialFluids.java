package org.pfaa.chemica.fluid;

import java.util.HashMap;

import org.pfaa.chemica.block.IndustrialFluidBlock;
import org.pfaa.chemica.model.Condition;
import org.pfaa.chemica.model.ConditionProperties;
import org.pfaa.chemica.model.Constants;
import org.pfaa.chemica.model.IndustrialMaterial;
import org.pfaa.chemica.model.IndustrialMaterialUtils;
import org.pfaa.chemica.model.State;
import org.pfaa.chemica.registration.OreDictUtils;

import com.google.common.base.CaseFormat;

import net.minecraft.block.Block;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;

public class IndustrialFluids {
	private static HashMap<Fluid,IndustrialMaterial> fluidToMaterial = new HashMap<Fluid, IndustrialMaterial>();
	
	public static void registerFluidMaterial(Fluid fluid, IndustrialMaterial material) {
		fluidToMaterial.put(fluid, material);
	}
	
	public static IndustrialMaterial getMaterial(Fluid fluid) {
		return fluidToMaterial.get(fluid);
	}
	
	public static ConditionProperties getProperties(Fluid fluid) {
		IndustrialMaterial material = getMaterial(fluid);
		return (material == null) ? null : material.getProperties(getCondition(fluid));
	}
	
	private static Condition getCondition(Fluid fluid) {
		return new Condition(fluid.getTemperature(), Constants.STANDARD_PRESSURE);	
	}
	
	public static Fluid getCanonicalFluid(IndustrialMaterial material) {
		ConditionProperties props = material.getProperties(Condition.STP);
		if (props == null) {
			return null;
		}
		return getCanonicalFluid(material, props.state);
	}
	
	public static Fluid getCanonicalFluid(IndustrialMaterial material, State state) {
		return getCanonicalFluid(material, state, null);
	}
	
	public static Fluid getCanonicalFluid(IndustrialMaterial material, State state, String name) {
		if (state == State.SOLID) {
			 return null;
		}
		Condition condition = IndustrialMaterialUtils.getCanonicalCondition(material, state);
		if (name == null) {
			name = material.getOreDictKey();
			boolean standardState = condition.equals(Condition.STP);
			if (!standardState) {
				name = OreDictUtils.makeKey(state.name(), name);
			}
		}
		Fluid fluid = FluidRegistry.getFluid(name);
		if (fluid == null) {
			fluid = FluidRegistry.getFluid(CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_CAMEL, name));
		}
		if (fluid == null) {
			fluid = FluidRegistry.getFluid(name.toLowerCase());
		}
		if (fluid == null) {
			fluid = createFluidForCondition(name, material, condition);
			FluidRegistry.registerFluid(fluid);	
		}
		registerFluidMaterial(fluid, material);
		return fluid;
	}

	private static Fluid createFluidForCondition(
			String name, IndustrialMaterial material, Condition condition) {
		ConditionProperties props = material.getProperties(condition);
		Fluid fluid = new Fluid(name);
		fluid.setGaseous(props.state == State.GAS);
		fluid.setDensity((int)(convertToForgeDensity(props.density)));
		if (!Double.isNaN(props.viscosity)) {
			fluid.setViscosity((int)(props.viscosity / props.density * 1000)); // Forge fluids are in cSt/1000
		}
		fluid.setLuminosity(props.luminosity);
		return fluid;
	}

	private static double convertToForgeDensity(double density) {
		return (density <= Constants.AIR_DENSITY ? (density - Constants.AIR_DENSITY) : density) * 1000;
	}
	
	public static Block getBlock(IndustrialMaterial material, State state, String name) {
		Fluid fluid = IndustrialFluids.getCanonicalFluid(material, state, name);
		return getBlock(fluid);
	}
	
	public static Block getBlock(IndustrialMaterial material) {
		Fluid fluid = IndustrialFluids.getCanonicalFluid(material);
		return getBlock(fluid);
	}

	private static Block getBlock(Fluid fluid) {
		Block block = fluid.getBlock();
		if (block == null) {
			block = new IndustrialFluidBlock(fluid); 
		}
		return block;
	}
}
