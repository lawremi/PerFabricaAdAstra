package org.pfaa.chemica.processing;

import org.pfaa.chemica.model.Condition;
import org.pfaa.chemica.model.IndustrialMaterial;

/* An industrial material in some condition and form */
public class Stream {
	private IndustrialMaterial material;
	private Condition condition;
	private Form form;
	
	public Stream(IndustrialMaterial material, Form form) {
		this(material, form, Condition.STP);
	}
	
	public Stream(IndustrialMaterial material, Form form, Condition condition) {
		super();
		this.material = material;
		this.condition = condition;
		this.form = form;
	}
	
	public IndustrialMaterial getIndustrialMaterial() {
		return this.material;
	}
	
	public Condition getCondition() {
		return this.condition;
	}
	
	public Form getForm() {
		return this.form;
	}
}
