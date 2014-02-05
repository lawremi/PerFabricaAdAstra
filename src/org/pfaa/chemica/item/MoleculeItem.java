package org.pfaa.chemica.item;

import org.pfaa.chemica.model.Molecule.Molecules;

public class MoleculeItem extends IndustrialMaterialItem<Molecules> {
	public MoleculeItem(int id) {
		super(id, Molecules.class);
	}
}
