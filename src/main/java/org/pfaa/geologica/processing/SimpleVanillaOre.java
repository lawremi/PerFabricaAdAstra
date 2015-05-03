package org.pfaa.geologica.processing;


public class SimpleVanillaOre extends SimpleOre implements VanillaOre {

	public SimpleVanillaOre(Mineral concentrate) {
		super(concentrate);
	}
	
	public SimpleVanillaOre(Ore ore) {
		super(ore);
	}

}
