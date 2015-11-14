package org.pfaa.chemica;

import java.util.List;

import org.pfaa.chemica.block.ConstructionMaterialBlock;
import org.pfaa.chemica.block.PollutedSoil;
import org.pfaa.chemica.fluid.IndustrialFluids;
import org.pfaa.chemica.model.Aggregate;
import org.pfaa.chemica.model.Aggregate.Aggregates;
import org.pfaa.chemica.model.Compound.Compounds;
import org.pfaa.chemica.model.Element;
import org.pfaa.chemica.model.State;
import org.pfaa.chemica.model.Strength;
import org.pfaa.core.catalog.BlockCatalog;
import org.pfaa.core.catalog.CatalogUtils;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;

public class ChemicaBlocks implements BlockCatalog {
    
	public static final Block POLLUTED_SOIL = new PollutedSoil();
    
    public static final Block STEAM = IndustrialFluids.getBlock(Compounds.H2O, State.GAS, "steam");
    
    public static final Block WEAK_METAL = new ConstructionMaterialBlock(Strength.WEAK, Element.class);
    public static final Block MEDIUM_METAL = new ConstructionMaterialBlock(Strength.MEDIUM, Element.class);
    public static final Block STRONG_METAL = new ConstructionMaterialBlock(Strength.STRONG, Element.class);
    public static final Block VERY_STRONG_METAL = new ConstructionMaterialBlock(Strength.VERY_STRONG, Element.class);
    
    public static Block getBlock(Aggregate aggregate) {
		if (aggregate == Aggregates.STONE) {
			return Blocks.stone;
		} else if (aggregate == Aggregates.SAND) {
			return Blocks.sand;
		} else if (aggregate == Aggregates.CLAY) {
			return Blocks.clay;
		} else if (aggregate == Aggregates.GRAVEL) {
			return Blocks.gravel;
		} else if (aggregate == Aggregates.DIRT) {
			return Blocks.dirt;
		} else if (aggregate == Aggregates.OBSIDIAN) {
			return Blocks.obsidian;
		}
		return null;
	}
    
	public static List<ConstructionMaterialBlock> getConstructionMaterialBlocks() {
		return CatalogUtils.getEntries(ChemicaBlocks.class, ConstructionMaterialBlock.class);
	}

}
