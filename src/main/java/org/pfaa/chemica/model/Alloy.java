package org.pfaa.chemica.model;

import java.util.List;

import org.pfaa.chemica.model.Condition;
import org.pfaa.chemica.model.ConditionProperties;
import org.pfaa.chemica.model.Element;
import org.pfaa.chemica.model.IndustrialMaterial;
import org.pfaa.chemica.model.Mixture;
import org.pfaa.chemica.model.MixtureComponent;

import static org.pfaa.chemica.model.Element.Elements.Au;
import static org.pfaa.chemica.model.Element.Elements.Ag;

public interface Alloy extends Mixture {
	public Element getBase();
	public Alloy alloy(Element material, double weight);
	
	public enum Alloys implements Alloy {
		ELECTRUM(Au.alloy(Ag, 1.0));

		private Alloy delegate;
		
		private Alloys(Alloy delegate) {
			this.delegate = delegate;
		}
		
		@Override
		public List<MixtureComponent> getComponents() {
			return delegate.getComponents();
		}

		@Override
		public String getOreDictKey() {
			return delegate.getOreDictKey();
		}

		@Override
		public ConditionProperties getProperties(Condition condition) {
			return delegate.getProperties(condition);
		}

		@Override
		public Mixture mix(IndustrialMaterial material, double weight) {
			return delegate.mix(material, weight);
		}

		@Override
		public Element getBase() {
			return delegate.getBase();
		}

		@Override
		public Alloy alloy(Element material, double weight) {
			return delegate.alloy(material, weight);
		}
	}
}
