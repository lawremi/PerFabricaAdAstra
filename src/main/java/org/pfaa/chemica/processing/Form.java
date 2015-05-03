package org.pfaa.chemica.processing;

public interface Form {
	public enum Forms implements Form {
		BLOCK(1), // intact solid placeable as block, constructed from 9 ingots/lumps, 4 clumps, or cast directly
		CLUMP(4), // a compact lump, typically from a malleable solid like dirt or clay
		INGOT(9), // intact solid in regular shape, from sintering powder or casting, or dismantling blocks
		LUMP(9), // intact solid in irregular shape, found naturally
		NUGGET(9*9), // metal in irregular, natural form; can be combined into (or broken down from) ingot
		POWDER(9); // dry solid as fine particles, from pulverizing ingots and lumps, precipitation
		
		private int numberPerBlock;
		
		private Forms(int numberPerBlock) {
			this.setNumberPerBlock(numberPerBlock);
		}

		public int getNumberPerBlock() {
			return numberPerBlock;
		}

		private void setNumberPerBlock(int numberPerBlock) {
			this.numberPerBlock = numberPerBlock;
		}
	}
}
