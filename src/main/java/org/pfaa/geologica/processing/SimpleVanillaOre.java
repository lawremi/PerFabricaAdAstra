package org.pfaa.geologica.processing;

import org.pfaa.chemica.model.Mixture;

public class SimpleVanillaOre extends SimpleOre implements VanillaOre {

	public SimpleVanillaOre(Mineral concentrate) {
		super(concentrate);
	}
	
	public SimpleVanillaOre(Ore ore) {
		super(ore);
	}

}
