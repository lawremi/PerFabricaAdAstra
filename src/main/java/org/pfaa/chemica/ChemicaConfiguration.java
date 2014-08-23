package org.pfaa.chemica;

import java.io.File;

import net.minecraftforge.common.config.Configuration;

public class ChemicaConfiguration {
	private Configuration config;

	public ChemicaConfiguration(Configuration config) {
		this.config = config;
	}

	public ChemicaConfiguration(File file) {
		this(new Configuration(file));
	}
	
	public void save() {
		this.config.save();
	}
}
