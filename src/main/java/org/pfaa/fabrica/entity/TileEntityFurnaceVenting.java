package org.pfaa.fabrica.entity;

import org.pfaa.fabrica.FabricaBlocks;
import org.pfaa.fabrica.block.HoodBlock;
import org.pfaa.fabrica.recipe.HoodRecipes;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraftforge.fluids.FluidStack;

public class TileEntityFurnaceVenting extends TileEntityFurnace {

	public TileEntityFurnaceVenting(TileEntityFurnace te) {
		NBTTagCompound nbt = new NBTTagCompound();
		te.writeToNBT(nbt);
		this.readFromNBT(nbt);
	}

	private void vent(FluidStack gas) {
		Block above = this.worldObj.getBlock(this.xCoord, this.yCoord + 1, this.zCoord);
		if (above == FabricaBlocks.HOOD) {
			((HoodBlock)above).fill(this.worldObj, this.xCoord, this.yCoord + 1, this.zCoord, gas);
		}
	}
	
	@Override
	public void smeltItem() {
		ItemStack consumed = this.getStackInSlot(0).copy();
		super.smeltItem();
		consumed.stackSize = 1;
		this.vent(HoodRecipes.getOutput(consumed));
	}

}
