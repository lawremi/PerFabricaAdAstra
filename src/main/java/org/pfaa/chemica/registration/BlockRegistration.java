package org.pfaa.chemica.registration;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;

import org.pfaa.RegistrationUtils;
import org.pfaa.chemica.ChemicaBlocks;

public class BlockRegistration {
    public static void init() {
        RegistrationUtils.registerDeclaredBlocks(ChemicaBlocks.class, Block.class, ItemBlock.class);
    }
}
