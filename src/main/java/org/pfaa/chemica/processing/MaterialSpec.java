package org.pfaa.chemica.processing;

import org.pfaa.chemica.model.Condition;
import org.pfaa.chemica.model.IndustrialMaterial;
import org.pfaa.chemica.model.Mixture;
import org.pfaa.chemica.model.State;

public class MaterialSpec<T extends IndustrialMaterial> {
	public final T material;
	public final State state;
	public final float stoich;
	
	protected MaterialSpec(T material, State state, float stoich) {
		super();
		this.material = material;
		this.state = state;
		this.stoich = stoich;
	}

	@SuppressWarnings("unchecked")
	public MaterialSpec<T> without(IndustrialMaterial... materials) {
		if (this.material instanceof Mixture) {
			return (MaterialSpec<T>)this.reconstitute(((Mixture) this.material).without(material));
		}
		return this;
	}
	
	public <U extends IndustrialMaterial> MaterialSpec<U> reconstitute(U material) {
		return new MaterialSpec<U>(material, this.state, this.stoich);
	}
	
	public static <T extends IndustrialMaterial> MaterialSpec<T> of(float stoich, State state, T material) {
		return new MaterialSpec<T>(material, state, stoich);
	}

	public static <T extends IndustrialMaterial> MaterialSpec<T> of(State state, T material) {
		return of(1.0F, state, material);
	}
	
	public static <T extends IndustrialMaterial> MaterialSpec<T> of(T material) {
		return of(material.getProperties(Condition.STP).state, material);
	}
}
