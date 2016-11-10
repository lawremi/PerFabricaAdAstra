package org.pfaa.chemica.fluid;

import java.util.HashMap;

import org.pfaa.chemica.block.IndustrialFluidBlock;
import org.pfaa.chemica.model.Compound.Compounds;
import org.pfaa.chemica.model.Condition;
import org.pfaa.chemica.model.ConditionProperties;
import org.pfaa.chemica.model.Constants;
import org.pfaa.chemica.model.Equation.Term;
import org.pfaa.chemica.model.IndustrialMaterial;
import org.pfaa.chemica.model.IndustrialMaterialUtils;
import org.pfaa.chemica.model.SimpleMixture;
import org.pfaa.chemica.model.State;
import org.pfaa.chemica.model.StateProperties;
import org.pfaa.chemica.processing.Form;
import org.pfaa.chemica.processing.Form.Forms;

import com.google.common.base.CaseFormat;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

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
		return getCanonicalFluid(material, null);
	}
	
	public static Fluid getCanonicalFluid(IndustrialMaterial material, State state) {
		return getCanonicalFluid(material, state, null);
	}
	
	public static Fluid getCanonicalFluid(IndustrialMaterial material, State state, String name) {
		if (state == null) {
			ConditionProperties props = material.getProperties(Condition.STP);
			if (props == null) {
				return null;
			}
			state = props.state;
		}
		if (state == State.SOLID) {
			 return null;
		}
		Condition condition = IndustrialMaterialUtils.getCanonicalCondition(material, state);
		if (condition == null) {
			return null;
		}
		if (name == null) {
			name = material.getOreDictKey();
			boolean standardState = condition.equals(Condition.STP);
			if (!standardState) {
				String stateName = state == State.GAS ? "vaporized" : "molten";
				// Greg likes "molten.iron", but we prefer "iron.molten".
				String gtName = stateName + "." + name;
				if (FluidRegistry.getFluid(gtName) != null)
					name = gtName;
				else name = name + "." + stateName;
			} else if (state == State.AQUEOUS) {
				name = "aqueous." + name;
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
		Fluid fluid = new ColoredFluid(name, props.color);
		fluid.setGaseous(props.state == State.GAS);
		fluid.setTemperature((int)condition.temperature);
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
	
	public static FluidStack getCanonicalFluidStack(IndustrialMaterial material, State state, int amount) {
		Fluid fluid = getCanonicalFluid(material, state);
		return fluid == null ? null : new FluidStack(fluid, amount);
	}
	
	public static FluidStack getCanonicalFluidStack(IndustrialMaterial material, int amount) {
		return getCanonicalFluidStack(material, null, amount);
	}

	public static FluidStack getCanonicalFluidStack(IndustrialMaterial material) {
		return getCanonicalFluidStack(material, getAmount(Forms.DUST));
	}
	
	public static FluidStack getCanonicalFluidStack(IndustrialMaterial material, State state, Form form) {
		return getCanonicalFluidStack(material, state, getAmount(state, form));
	}
	
	public static FluidStack getCanonicalFluidStack(Term term, Form form) {
		IndustrialMaterial solute = term.chemical;
		State state = term.state;
		if (state == State.AQUEOUS && term.concentration != 1.0F) {
			solute = new SimpleMixture(Compounds.H2O).mix(solute, term.concentration);
			state = State.LIQUID;
		}
		int amount = (int)(term.stoichiometry / term.concentration * getAmount(state, form));
		return getCanonicalFluidStack(solute, state, amount);
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
	
	private static int MB_PER_BLOCK = 1296;
	
	public static int getAmount(Form form) {
		return getAmount(State.LIQUID, form);
	}
	
	public static int getAmount(State state, Form form) {
		int nPerBlock = form.getNumberPerBlock(); 
		if (state == State.AQUEOUS) {
			// Dilution: 1 dust (mol) / bucket (L) => 1 M solution
			nPerBlock /= Forms.DUST.getNumberPerBlock();
		}
		return MB_PER_BLOCK / nPerBlock * (state == State.GAS ? 100 : 1);
	}
	
	public static State getState(Fluid fluid) {
		return fluid.isGaseous() ? State.GAS : State.LIQUID;
	}
	
	public static State getState(FluidStack fluidStack) {
		return getState(fluidStack.getFluid());
	}
	
	private static String getIconName(Fluid fluid, boolean flowing, boolean opaque) {
		boolean molten = fluid.getTemperature() >= StateProperties.MIN_GLOWING_TEMPERATURE;
		return "chemica:" + 
				(fluid.isGaseous() ? "gas" : molten ? "molten" : "fluid") + "_" + 
				(flowing ? "flow" : "still") + 
				(!molten && opaque ? "_opaque" : "");
	}

	public static class TextureHook {
		@SubscribeEvent
        @SideOnly(Side.CLIENT)
        public void textureHook(TextureStitchEvent.Pre event) {
            if (event.map.getTextureType() == 0)
                for (Fluid fluid : fluidToMaterial.keySet()) {
                	boolean opaque = getProperties(fluid).opaque;
                	if (fluid.getStillIcon() == null)
                		fluid.setStillIcon(event.map.registerIcon(getIconName(fluid, false, opaque)));
                	if (fluid.getFlowingIcon() == null)
                		fluid.setFlowingIcon(event.map.registerIcon(getIconName(fluid, true, opaque)));
                }
        }
	}
	
	public static Object getTextureHook() {
		return new TextureHook();
	}
}
