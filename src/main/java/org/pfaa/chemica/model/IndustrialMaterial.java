package org.pfaa.chemica.model;


public interface IndustrialMaterial {
	public String name();
	public String getOreDictKey();
	public ConditionProperties getProperties(Condition condition);
	default Mixture mix(IndustrialMaterial material, double weight) {
		return this.mix(new MixtureComponent(material, weight));
	}
	default Mixture mix(MixtureComponent comp) {
		return this.mix(comp.material, comp.weight);
	}
	default ConditionProperties getStandardProperties() {
		return this.getProperties(Condition.STP);
	}
	default State getStandardState() {
		return this.getStandardProperties().state;
	}
}
