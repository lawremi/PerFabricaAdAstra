package org.pfaa.chemica.processing;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

import org.pfaa.chemica.model.IndustrialMaterial;
import org.pfaa.chemica.processing.Form;

import com.google.common.collect.Sets;

public class CanonicalForms {
	private static Map<IndustrialMaterial, Set<Form>> formsForMaterial;
	
	public static void register(IndustrialMaterial material, Form... forms) {
		Set<Form> materialForms = formsForMaterial.get(material);
		if (materialForms == null) {
			materialForms = Sets.newHashSet();
			formsForMaterial.put(material, materialForms);
		}
		materialForms.addAll(Arrays.asList(forms));
	}
	
	public static Set<Form> of(IndustrialMaterial material) {
		return Collections.unmodifiableSet(formsForMaterial.get(material));
	}
}
