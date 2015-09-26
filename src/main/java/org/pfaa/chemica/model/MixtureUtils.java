package org.pfaa.chemica.model;

import java.util.List;

import com.google.common.base.Function;
import com.google.common.collect.Lists;

public abstract class MixtureUtils {
	public static Mixture concentrate(Mixture mixture, int factor) {
		List<MixtureComponent> comps = mixture.getComponents();
		return new SimpleMixture(Lists.transform(comps, new ConcentrateFunction(factor)));
	}
	
	public static class ConcentrateFunction implements Function<MixtureComponent,MixtureComponent>  {
		
		private int factor;

		public ConcentrateFunction(int factor) {
			this.factor = factor;
		}
		
		@Override
		public MixtureComponent apply(MixtureComponent input) {
			if (input.weight < 1.0)
				return input.concentrate(this.factor);
			return input;
		}
		
	}
}
