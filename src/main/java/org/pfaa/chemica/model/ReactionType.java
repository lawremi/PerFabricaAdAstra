package org.pfaa.chemica.model;

import java.util.Collections;

import org.pfaa.chemica.model.Equation.Term;
import org.pfaa.chemica.processing.ConversionType;
import org.pfaa.chemica.processing.MaterialStoich;

import com.google.common.collect.Lists;

public interface ReactionType extends ConversionType<Chemical, Reaction> {

	public enum ReactionTypes implements ReactionType {
		SYNTHESIS,
		DECOMPOSITION,
		SINGLE_DISPLACEMENT,
		DOUBLE_DISPLACEMENT,
		NEUTRALIZATION,
		COMBUSTION,
		ORGANIC,
		REDOX;

		@Override
		public Reaction of(MaterialStoich<Chemical> input) {
			Equation equation = new Equation(Lists.newArrayList(new Term(input)), Collections.<Term>emptyList(), null);
			return new Reaction(this, equation);
		}
	}

}
