package org.pfaa.chemica.block;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fluids.BlockFluidClassic;

import org.pfaa.chemica.fluid.IndustrialFluid;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class IndustrialFluidBlock extends BlockFluidClassic {

	private IndustrialFluid fluid;
	
	public IndustrialFluidBlock(IndustrialFluid fluid) {
		super(fluid, materialForIndustrialFluid(fluid));
		this.fluid = fluid;
	}

	private static Material materialForIndustrialFluid(IndustrialFluid fluid) {
		return fluid.isGaseous() ? Material.air : fluid.isMolten() ? Material.lava : Material.water;
	}


	@Override
	@SideOnly(Side.CLIENT)
	public int colorMultiplier(IBlockAccess world, int x, int y, int z) {
		return this.fluid.getColor();
	}

	@Override
    @SideOnly(Side.CLIENT)
	public IIcon getIcon(int side, int meta) {
		return side != 0 && side != 1 ? this.fluid.getFlowingIcon() : this.fluid.getStillIcon();
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister register) {
		// FIXME: eventually these textures should be in "chemica"
		if (fluid.isGaseous()) {
			fluid.setIcons(register.registerIcon("geologica:gas"));
		} else {
			String prefix = fluid.isMolten() ? "molten" : "fluid";
			String postfix = fluid.isOpaque() ? "_opaque" : "";
			fluid.setStillIcon(register.registerIcon("geologica:" + prefix + "_still" + postfix));
			fluid.setFlowingIcon(register.registerIcon("geologica:" + prefix + "_flow" + postfix));
		}
	}
}
