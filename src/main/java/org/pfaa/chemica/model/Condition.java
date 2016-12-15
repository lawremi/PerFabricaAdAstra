package org.pfaa.chemica.model;

import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;

public class Condition {
	public int temperature;
	public double pressure;
	public boolean aqueous;
	public boolean atmospheric;
	
	public Condition(int temperature, double pressure) {
		this(temperature, pressure, false, false);
	}
	
	public Condition(int temperature, double pressure, boolean aqueous, boolean atmospheric) {
		super();
		this.temperature = temperature;
		this.pressure = pressure;
		this.aqueous = aqueous;
		this.atmospheric = atmospheric;
	}
	
	public Condition(int temperature) {
		this(temperature, Constants.STANDARD_PRESSURE);
	}
	
	public Condition() {
		this(Constants.STANDARD_TEMPERATURE);
	}
	
	public static Condition ofWorldCoordinates(World world, int x, int y, int z) {
		int temperature = getTemperatureAt(world, x, y, z);
		double pressure = getPressureAt(world, x, y, z);
		return new Condition(temperature, pressure);
	}
	
	private static double getPressureAt(World world, int x, int y, int z) {
		int sealevel = world.provider.hasNoSky ? world.provider.getActualHeight() : 
			world.provider.getAverageGroundLevel();
		return Constants.STANDARD_PRESSURE - 
				(y - sealevel) * (2/3) * Constants.STANDARD_PRESSURE / 
				                 (world.provider.getHeight() - sealevel);
	}

	private static int getTemperatureAt(World world, int x, int y, int z) {
		BiomeGenBase biomegenbase = world.getBiomeGenForCoords(x, z);
		float biometemp = biomegenbase.getFloatTemperature(x, y, z);
		int temperature = (int)((biometemp - 0.15F) * 45 / (2.0F - 0.15F));
		boolean isHeated = world.getSavedLightValue(EnumSkyBlock.Block, x, y, z) == 10;
		if (isHeated) {
			temperature = Math.max(temperature, Constants.STANDARD_TEMPERATURE);
		}
		// TODO: if near fire, we are >=100C; if near a fluid, we consider its temperature somehow. 
		// But how to determine "near" 3x3? But should we allow for heat insulation somehow? 
		// Maybe if surrounded by insulating blocks (or the same fluid)? 
		return temperature;
	}

	public static Condition STP = new Condition(Constants.STANDARD_TEMPERATURE, Constants.STANDARD_PRESSURE);
	public static Condition AQUEOUS_STP = new Condition(Constants.STANDARD_TEMPERATURE, Constants.STANDARD_PRESSURE, true, false);
	
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Condition)) {
			return false;
		}
		Condition other = (Condition)obj;
		return this.pressure == other.pressure && this.temperature == other.temperature;
	}

	@Override
	public int hashCode() {
		return (int)(this.pressure) ^ ((int)this.temperature);
	}
}
