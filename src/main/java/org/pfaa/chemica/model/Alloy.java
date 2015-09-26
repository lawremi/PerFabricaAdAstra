package org.pfaa.chemica.model;

import java.util.List;

import org.pfaa.chemica.model.Condition;
import org.pfaa.chemica.model.ConditionProperties;
import org.pfaa.chemica.model.Element;
import org.pfaa.chemica.model.IndustrialMaterial;
import org.pfaa.chemica.model.Mixture;
import org.pfaa.chemica.model.MixtureComponent;

import com.google.common.base.CaseFormat;

import static org.pfaa.chemica.model.Element.Elements.Ag;
import static org.pfaa.chemica.model.Element.Elements.Al;
import static org.pfaa.chemica.model.Element.Elements.Au;
import static org.pfaa.chemica.model.Element.Elements.Be;
import static org.pfaa.chemica.model.Element.Elements.Bi;
import static org.pfaa.chemica.model.Element.Elements.C;
import static org.pfaa.chemica.model.Element.Elements.Ca;
import static org.pfaa.chemica.model.Element.Elements.Cd;
import static org.pfaa.chemica.model.Element.Elements.Ce;
import static org.pfaa.chemica.model.Element.Elements.Co;
import static org.pfaa.chemica.model.Element.Elements.Cr;
import static org.pfaa.chemica.model.Element.Elements.Cu;
import static org.pfaa.chemica.model.Element.Elements.Fe;
import static org.pfaa.chemica.model.Element.Elements.Ge;
import static org.pfaa.chemica.model.Element.Elements.Ir;
import static org.pfaa.chemica.model.Element.Elements.Pb;
import static org.pfaa.chemica.model.Element.Elements.La;
import static org.pfaa.chemica.model.Element.Elements.Li;
import static org.pfaa.chemica.model.Element.Elements.Mg;
import static org.pfaa.chemica.model.Element.Elements.Mn;
import static org.pfaa.chemica.model.Element.Elements.Mo;
import static org.pfaa.chemica.model.Element.Elements.Nb;
import static org.pfaa.chemica.model.Element.Elements.Nd;
import static org.pfaa.chemica.model.Element.Elements.Ni;
import static org.pfaa.chemica.model.Element.Elements.Os;
import static org.pfaa.chemica.model.Element.Elements.Pt;
import static org.pfaa.chemica.model.Element.Elements.Pr;
import static org.pfaa.chemica.model.Element.Elements.Sb;
import static org.pfaa.chemica.model.Element.Elements.Si;
import static org.pfaa.chemica.model.Element.Elements.Sn;
import static org.pfaa.chemica.model.Element.Elements.Te;
import static org.pfaa.chemica.model.Element.Elements.Ti;
import static org.pfaa.chemica.model.Element.Elements.V;
import static org.pfaa.chemica.model.Element.Elements.W;
import static org.pfaa.chemica.model.Element.Elements.Zn;
import static org.pfaa.chemica.model.Element.Elements.Zr;

// TODO: set strengths where the alloy differs from the base metal

public interface Alloy extends Mixture, Metal {
	public Element getBase();
	public Alloy alloy(Element material, double weight);
	
	public enum Alloys implements Alloy {
		AIRCRAFT_ALUMINUM(Al.alloy(Zn, 0.02).alloy(Mg, 0.02).alloy(Cu, 0.005)),
		ALNICO(Fe.alloy(Al, 0.4).alloy(Ni, 0.3).alloy(Co, 0.2)),
		ALUMINUM_BRONZE(Cu.alloy(Al, 0.2)),
		ALUMINUM_LITHIUM(Al.alloy(Li, 0.1)),
		ALUMINUM_6061(Al.alloy(Mg, 0.01).alloy(Si, 0.005)),
		BERYLLIUM_COPPER(Cu.alloy(Be, 0.1)),
		BRASS(Cu.alloy(Zn, 0.33)),
		CUPRONICKEL(Cu.alloy(Ni, 0.1).alloy(Fe, 0.01)),
		ELECTRUM(Au.alloy(Ag, 1.0)),
		DURALUMIN(Al.alloy(Mg, 0.015).alloy(Zn, 0.01).alloy(Mn, 0.005)),
		FERROCERIUM(Ce.alloy(Fe, 0.7).alloy(La, 0.6).alloy(Nd, 0.1).alloy(Pr, 0.1).alloy(Mg, 0.1)),
		FERROCHROME(Fe.alloy(Cr, 1.0)),
		FERROMOLYBDENUM(Fe.alloy(Mo, 0.5)),
		FERROSILICON(Si.alloy(Fe, 0.2)),
		FERROTITANIUM(Ti.alloy(Fe, 0.25)),
		FERROVANADIUM(Fe.alloy(V, 0.5)),
		GREY_CAST_IRON(Fe.alloy(C, 0.1).alloy(Si, 0.05).alloy(Mn, 0.005)),
		HIGH_CARBON_STEEL(Fe.alloy(C, 0.05)),
		HIGH_SPEED_STEEL(Fe.alloy(W, 0.05).alloy(Cr, 0.05).alloy(V, 0.01)),
		ICONEL_718(Ni.alloy(Cr, 0.4).alloy(Fe, 0.4).alloy(Nb, 0.05).alloy(Mo, 0.03).alloy(Ti, 0.02).alloy(Al, 0.02)),
		INVAR(Fe.alloy(Ni, 0.5)),
		LEAD_ANTIMONY(Pb.alloy(Sb, 0.05)),
		LEAD_CALCIUM(Pb.alloy(Ca, 0.001)),
		MARINE_ALUMINUM(Al.alloy(Mg, 0.04).alloy(Mn, 0.005)),
		NICHROME(Ni.alloy(Cr, 0.3)),
		NICKEL_SILVER(Cu.alloy(Zn, 0.25).alloy(Ni, 0.25)),
		NICKEL_STELLITE(Co.alloy(Cr, 0.3).alloy(Ni, 0.2).alloy(W, 0.05).alloy(C, 0.02)),
		PLATINUM_IRIDIUM(Pt.alloy(Ir, 0.1)),
		IRIDOSMINE(Ir.alloy(Os, 1.0)),
		SILICON_GERMANIUM(Si.alloy(Ge, 1.0)),
		SOLDER(Sn.alloy(Pb, 0.5)),
		STAINLESS_STEEL(Fe.alloy(Cr, 0.25).alloy(Ni, 0.1)),
		STEEL(Fe.alloy(C, 0.01)),
		STELLITE(Co.alloy(Cr, 0.4).alloy(W, 0.05).alloy(C, 0.1)),
		STERLING_SILVER(Ag.alloy(Cu, 0.08)),
		TITANIUM_6_4(Ti.alloy(Al, 0.06).alloy(V, 0.04)),
		TYPE_ALLOY(Pb.alloy(Sn, 0.1).alloy(Sb, 0.15)),
		VITALLIUM(Co.alloy(Cr, 0.4).alloy(Mo, 0.03)),
		WHITE_CAST_IRON(Fe.alloy(C, 0.1).alloy(Si, 0.02).alloy(Mn, 0.005)),
		WIRE_ALUMINUM(Al.alloy(Fe, 0.01)),
		WOODS_METAL(Bi.alloy(Pb, 0.5).alloy(Sn, 0.4).alloy(Cd, 0.3)),
		ZAMAK(Zn.alloy(Al, 0.08).alloy(Mg, 0.001)),
		ZIRCALOY(Zr.alloy(Sn, 0.01)),
		TELLURIUM_COPPER(Cu.alloy(Te, 0.005)),
		;

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
			return CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, name());
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
