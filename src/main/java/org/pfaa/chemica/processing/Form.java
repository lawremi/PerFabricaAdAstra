package org.pfaa.chemica.processing;

import java.util.List;
import java.util.stream.Stream;

import org.pfaa.chemica.model.IndustrialMaterial;
import org.pfaa.chemica.model.MaterialState;

import com.google.common.base.CaseFormat;

public interface Form {
	public String name();
	default String oreDictKey() {
		return CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, name());
	}
	
	public int getNumberPerBlock();
	default int getNumberPerStack() {
		return this.getNumberPerBlock() / this.stack().getNumberPerBlock();
	}
	default boolean isGranular() { return !this.isIntact(); }
	default boolean isIntact() { return !this.isGranular(); }
	default boolean isPure() { return true; }
	default boolean isRegular() { return true; }
	
	Form stack();
	Form unstack();
	Form communite();
	Form reduce();
	Form compact();
	
	default float scaleTo(Form target) {
		return target.getNumberPerBlock() / this.getNumberPerBlock();
	}
	
	default MaterialStack of(IndustrialMaterial material) {
		return of(1, material);
	}
	default MaterialStack of(int inputAmount, IndustrialMaterial material) {
		return MaterialStack.of(inputAmount, material, this);
	}
	default MaterialStack of(MaterialState<?> materialState) {
		return of(1, materialState);
	}
	default MaterialStack of(int inputAmount, MaterialState<?> materialState) {
		return MaterialStack.of(materialState, this);
	}
	default MaterialStack of(MaterialStoich<?> stoich) {
		return MaterialStack.of(stoich, this);
	}
	default List<MaterialStack> of(Stream<MaterialStoich<?>> stoich) {
		return MaterialStack.of(stoich, this);
	}
	
	default Form of(Form subForm) {
		return new CompoundForm(this, subForm);
	}
	
	/*
	 * Potential machine parts:
	 * - Gear
	 * - Plate, frame, round, ring
	 * - Rod, tube
	 * - Foil, wire, spring
	 * - Ball
	 * - Screw
	 */

	public enum Forms implements Form {
		BLOCK(1), // naturally occurring or construction block 
		BRICK(4),
		CLUMP(4), // an intact form of large dust, typically from a malleable solid like dirt or clay
		COBBLESTONE(1),
		CRUSHED(9), // the result of crushing a block, further crushing yields powder
		DUST(9), // dry solid as fine particles, from pulverizing ingots and lumps, precipitation
		DUST_TINY(9*9), // dust from pulverizing nugget
		DUST_IMPURE(9), // for avoiding name collisions between ores and purified minerals
		DUST_IMPURE_TINY(9*9),
		DUST_LARGE(4), // like a clump, except loose
		INGOT(9), // intact solid in regular shape, from sintering powder or casting, stored in blocks
		LUMP(9), // intact solid in irregular shape, found naturally
		MILLIBUCKET(1296),
		MOLAR(9), // abstract equivalent of one mol
		NUGGET(9*9), // metal in irregular, natural form; can be combined into (or broken down from) ingot
		ORE(4), // manifested as a block, but only 1/4 of the block is concentrate
		RUBBLE(1),
		SAND(1),
		SLAB(2),
		STAIR(1),
		STONE(1),
		STONE_BRICKS(1),
		WALL(1),
		;
		
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
		public boolean isPure() {
			switch(this) {
			case ORE:
			case CRUSHED:
			case DUST_IMPURE:
			case DUST_IMPURE_TINY:
				return false;
			default:
				return true;
			}
		}
		
		@Override
		public boolean isRegular() {
			switch(this) {
			case BLOCK:
			case INGOT:
			case BRICK:
			case STONE:
			case STONE_BRICKS:
			case STAIR:
			case WALL:
			case SLAB:
				return true;
			default:
				return false;
			}
		}

		@Override
		public boolean isIntact() {
			switch(this) {
			case CLUMP:
			case LUMP:
			case NUGGET:
			case ORE:
				return true;
			default:
				return this.isRegular();
			}
		}
		
		@Override
		public boolean isGranular() {
			switch(this) {
			case DUST:
			case DUST_TINY:
			case DUST_IMPURE:
			case DUST_IMPURE_TINY:
			case DUST_LARGE:
			case SAND:
				return true;
			default:
				return false;
			}
		}
		
		@Override
		public Form stack() {
			switch(this) {
			case CLUMP:
			case INGOT:
			case LUMP:
			case BRICK:
				return BLOCK;
			case DUST_TINY:
				return DUST;
			case DUST_IMPURE_TINY:
				return DUST_IMPURE;
			case NUGGET:
				return INGOT;
			default:
				return null;
			}
		}

		@Override
		public Form unstack() {
			switch(this) {
			case DUST:
				return DUST_TINY;
			case DUST_IMPURE:
				return DUST_IMPURE_TINY;
			case INGOT:
				return NUGGET;
			default:
				return null;
			}
		}

		@Override
		public Form communite() {
			switch(this) {
			case BLOCK:
				return DUST_LARGE;
			case BRICK:
			case INGOT:
			case LUMP:
				return DUST;
			case CRUSHED:
				return DUST_IMPURE;
			case COBBLESTONE:
				return RUBBLE;
			case NUGGET:
				return DUST_TINY;
			case ORE:
				return CRUSHED;
			case STONE:
			case STONE_BRICKS:
				return COBBLESTONE;
			default:
				return null;
			}
		}

		@Override
		public Form reduce() {
			switch(this) {
			case RUBBLE:
				return BLOCK;
			case ORE:
				return LUMP;
			default:
				return null;
			}
		}
		
		@Override
		public Form compact() {
			switch(this) {
			case RUBBLE:
				return COBBLESTONE;
			case COBBLESTONE:
				return STONE;
			case DUST_TINY:
				return NUGGET;
			case DUST:
				return INGOT;
			default:
				return null;
			}
		}
	}
}
