package org.pfaa.chemica.registration;

import org.pfaa.chemica.ChemicaBlocks;
import org.pfaa.chemica.block.ConstructionMaterialBlock;
import org.pfaa.chemica.block.IndustrialFluidBlock;
import org.pfaa.core.item.ColoredBlockItem;
import org.pfaa.core.item.CompositeBlockItem;
import org.pfaa.core.registration.RegistrationUtils;

import net.minecraft.item.ItemBlock;

public class BlockRegistration {
    public static void init() {
    	RegistrationUtils.registerBlock(ChemicaBlocks.POLLUTED_SOIL, ItemBlock.class, "pollutedSoil");
        RegistrationUtils.registerDeclaredBlocks(ChemicaBlocks.class, IndustrialFluidBlock.class, ColoredBlockItem.class);
        RegistrationUtils.registerDeclaredBlocks(ChemicaBlocks.class, ConstructionMaterialBlock.class, CompositeBlockItem.class);
    }
}
