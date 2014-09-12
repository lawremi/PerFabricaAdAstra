package org.pfaa.chemica.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;

public class PollutedSoil extends Block {

    public PollutedSoil()
    {
        super(Material.ground);
        this.setCreativeTab(CreativeTabs.tabBlock);
    }

}
