package org.pfaa.fabrica;

import java.io.File;

import net.minecraftforge.common.config.Configuration;

public class FabricaConfiguration {
	private Configuration config;

	public FabricaConfiguration(Configuration config) {
		this.config = config;
	}

	public FabricaConfiguration(File file) {
		this(new Configuration(file));
	}
	
	public void save() {
		this.config.save();
	}
}
