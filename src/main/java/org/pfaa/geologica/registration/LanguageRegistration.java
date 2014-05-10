package org.pfaa.geologica.registration;

import org.pfaa.geologica.Geologica;

import cpw.mods.fml.common.registry.LanguageRegistry;

public class LanguageRegistration {
	private static final String LANG_DIR = Geologica.RESOURCE_DIR + "/lang";

	public static void init() {
		registerLanguage("en_US");
	}
	
	private static void registerLanguage(String lang) {
		String localizationFile = LANG_DIR + "/" + lang + ".properties";
		LanguageRegistry.instance().loadLocalization(localizationFile, lang, false);
	}
}
