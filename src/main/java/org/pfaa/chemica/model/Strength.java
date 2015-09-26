package org.pfaa.chemica.model;

public enum Strength { 
	WEAK, MEDIUM, STRONG, VERY_STRONG;
	
	public String getCamelName() {
		if (this == VERY_STRONG)
			return "veryStrong";
		else return name().toLowerCase();
	}
}