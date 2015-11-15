package org.pfaa.chemica.model;

import static org.pfaa.chemica.model.Element.H;
import static org.pfaa.chemica.model.Element.Br;
import static org.pfaa.chemica.model.Element.C;
import static org.pfaa.chemica.model.Element.Cl;
import static org.pfaa.chemica.model.Element.F;
import static org.pfaa.chemica.model.Element.I;
import static org.pfaa.chemica.model.Element.N;
import static org.pfaa.chemica.model.Element.O;
import static org.pfaa.chemica.model.Element.P;
import static org.pfaa.chemica.model.Element.S;
import static org.pfaa.chemica.model.Element.W;

import org.pfaa.chemica.model.Formula.Part;
import org.pfaa.chemica.model.Formula.PartFactory;

public enum Anion {
	BROMIDE(Br),
	CARBONATE(new Part(C, O._(3))),
	CHLORIDE(Cl),
	CYANIDE(new Part(C, N)),
	FLUORIDE(F),
	HYDROXIDE(new Part(O, H)),
	IODATE(new Part(I, O._(4))),
	IODIDE(I),
	NITRATE(new Part(N, O._(3))),
	OXIDE(O),
	PHOSPHATE(new Part(P, O._(4))),
	SULFATE(new Part(S, O._(4))),
	SULFIDE(S),
	TUNGSTATE(new Part(W, O._(4)))
	;
	
	private Formula.Part formulaPart;
	
	private Anion(Formula.Part formulaPart) {
		this.formulaPart = formulaPart;
	}
	private Anion(PartFactory factory) {
		this(factory.getPart());
	}

	public Formula.Part getFormulaPart() {
		return formulaPart;
	}
}
