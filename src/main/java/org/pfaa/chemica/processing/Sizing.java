package org.pfaa.chemica.processing;

public class Sizing extends UnitOperation {

	protected Sizing(Type type, MaterialSpec<?> input) {
		super(type, input);
	}

	/* Mechanical modification of particle size */
	public static interface Type extends UnitOperation.Type { }
	
	public static enum Types implements Type {
		COMPACTION,
		COMMUNITION;
		
		@Override
		public Sizing of(MaterialSpec<?> input) {
			return new Sizing(this, input);
		}
	}

}
