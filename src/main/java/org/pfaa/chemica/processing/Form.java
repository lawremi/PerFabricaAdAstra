package org.pfaa.chemica.processing;

import com.google.common.base.CaseFormat;

public interface Form {
	public String name();
	public String oreDictKey();
	public int getNumberPerBlock();
	
	/*
	 * Potential Axes:
	 * - Size: number per block
	 * - Granularity: intact, crushed, dust
	 * - Shape: regular, irregular
	 * - Hydration: dry, moist
	 * - Purity: pure, impure
	 */
	
	/*
	 * Potential additions:
	 * - Gear
	 * - Plate, round, ring
	 * - Rod, tube
	 * - Foil, wire, spring
	 */
	public enum Forms implements Form {
		BLOCK(1), // intact solid placeable as block, constructed from 9 ingots/lumps, 4 clumps, or cast directly
		CLUMP(4), // a moist, continuous pile, typically from a malleable solid like dirt or clay
		CRUSHED(9), // the result of crushing a block, further crushing yields powder
		DUST(9), // dry solid as fine particles, from pulverizing ingots and lumps, precipitation
		INGOT(9), // intact solid in regular shape, from sintering powder or casting, or dismantling blocks
		LUMP(9), // intact solid in irregular shape, found naturally
		NUGGET(9*9), // metal in irregular, natural form; can be combined into (or broken down from) ingot
		PILE(4), // like a clump, except loose
		DUST_TINY(9*9), // dust from pulverizing nugget
		DUST_IMPURE(9),
		DUST_IMPURE_TINY(9*9); 
		
		private int numberPerBlock;
		
		private Forms(int numberPerBlock) {
			this.setNumberPerBlock(numberPerBlock);
		}

		@Override
		public int getNumberPerBlock() {
			return numberPerBlock;
		}

		private void setNumberPerBlock(int numberPerBlock) {
			this.numberPerBlock = numberPerBlock;
		}

		@Override
		public String oreDictKey() {
			return CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, name());
		}
	}
}
