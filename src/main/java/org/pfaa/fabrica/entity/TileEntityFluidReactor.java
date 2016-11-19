package org.pfaa.fabrica.entity;

import org.pfaa.chemica.fluid.IndustrialFluids;
import org.pfaa.chemica.model.Compound.Compounds;
import org.pfaa.fabrica.recipe.FluidReactorRecipe;
import org.pfaa.fabrica.recipe.FluidReactorRecipes;

import cofh.api.energy.EnergyStorage;
import cofh.api.energy.IEnergyReceiver;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;

public class TileEntityFluidReactor extends TileEntity implements IFluidHandler, IEnergyReceiver {
	private static final int TICKS_PER_UPDATE = 20;
	private static final int TANK_CAPACITY = FluidContainerRegistry.BUCKET_VOLUME * 3;
	private static final int ENERGY_CAPACITY = 1000;
	private static final int ENERGY_MAX_RECEIVE = 200;
	
	private FluidTank liquidOutputTank;
	private FluidTank inputTankA;
	private FluidTank inputTankB;
	
	private EnergyStorage energy;
	
	private ItemStack catalyst;
	
	private int tick;
	
	public TileEntityFluidReactor() {
		this.liquidOutputTank = new FluidTank(TANK_CAPACITY);
		this.inputTankA = new FluidTank(TANK_CAPACITY);
		this.inputTankB = new FluidTank(TANK_CAPACITY);
		this.energy = new EnergyStorage(ENERGY_CAPACITY, ENERGY_MAX_RECEIVE);
	}
	
	@Override
	public void updateEntity() {
		if (this.worldObj.isRemote) {
			return;
		}
		this.tick++;
		if (!this.onStep(TICKS_PER_UPDATE)) {
			return;
		}
		FluidReactorRecipe recipe = this.getRecipe();
		if (recipe == null) {
			return;
		}
		this.produce(recipe);
		this.consume(recipe);
	}

	private void consume(FluidReactorRecipe recipe) {
		this.inputTankA.drain(recipe.inputA.amount, true);
		if (recipe.inputB != null) {
			this.inputTankB.drain(recipe.inputB.amount, true);
		}
		this.energy.extractEnergy(recipe.energy, false);
	}

	private void produce(FluidReactorRecipe recipe) {
		this.liquidOutputTank.fill(recipe.liquidOutput, true);
		this.fillHood(recipe.gasOutput);
	}

	private void fillHood(FluidStack gas) {
		TileEntity te = this.worldObj.getTileEntity(this.xCoord, this.yCoord + 1, this.zCoord);
		if (te instanceof TileEntityHood) {
			((TileEntityHood) te).fill(ForgeDirection.DOWN, gas, true);
		}
	}

	private boolean onStep(int ticksPerUpdate) {
		return this.tick % ticksPerUpdate == 0;
	}
	
	private FluidReactorRecipe getRecipe() {
		FluidReactorRecipe recipe;
		if (this.inputTankB.getFluid() != null) {
			recipe = this.getRecipe(this.inputTankB.getFluid());
		} else {
			recipe = this.getRecipe(null);
			if (recipe == null) { // TODO: check that we are in appropriate atmosphere
				recipe = this.getRecipeInAir();
			}
		}
		return recipe;
	}

	private FluidReactorRecipe getRecipeInAir() {
		FluidReactorRecipe recipe = this.getRecipe(IndustrialFluids.getCanonicalFluidStack(Compounds.N2));
		if (recipe == null) {
			recipe = this.getRecipe(IndustrialFluids.getCanonicalFluidStack(Compounds.O2));
		}
		return recipe;
	}

	private FluidReactorRecipe getRecipe(FluidStack fluidB) {
		return FluidReactorRecipes.getRecipeForResources(
				this.inputTankA.getFluid(),
				fluidB,
				this.getCatalyst(),
				this.energy.getEnergyStored());
	}
	
	public ItemStack getCatalyst() {
		return this.catalyst;
	}
	
	public void setCatalyst(ItemStack catalyst) {
		this.catalyst = catalyst;
	}

	@Override
	public int fill(ForgeDirection from, FluidStack resource, boolean doFill) {
		if (from == ForgeDirection.DOWN) {
			return 0;
		}
		if (resource.getFluid().isGaseous() && !hasHood()) {
			return resource.amount;
		}
		int amount = this.inputTankA.fill(resource, doFill);
		if (amount == 0) {
			amount = this.inputTankB.fill(resource, doFill);
		}
		return amount;
	}

	private boolean hasHood() {
		return this.worldObj.getTileEntity(this.xCoord, this.yCoord + 1, this.zCoord) instanceof TileEntityHood;
	}

	@Override
	public FluidStack drain(ForgeDirection from, FluidStack resource, boolean doDrain) {
		return this.drain(from, resource.amount, doDrain);
	}

	@Override
	public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain) {
		if (from == ForgeDirection.UP) {
			return null;
		}
		return this.liquidOutputTank.drain(maxDrain, doDrain);
	}

	@Override
	public boolean canFill(ForgeDirection from, Fluid fluid) {
		FluidStack resource = new FluidStack(fluid, 1);
		return this.inputTankA.fill(resource, false) > 0 || 
			   this.inputTankB.fill(resource, false) > 0;
	}

	@Override
	public boolean canDrain(ForgeDirection from, Fluid fluid) {
		if (from == ForgeDirection.UP) {
			return false;
		}
		return this.liquidOutputTank.getFluid().equals(fluid);
	}

	@Override
	public FluidTankInfo[] getTankInfo(ForgeDirection from) {
		return new FluidTankInfo[] { 
			this.liquidOutputTank.getInfo(), 
			this.inputTankA.getInfo(), this.inputTankB.getInfo() 
		};
	}
	
	public void flush(boolean flushLiquids) {
		if (flushLiquids || this.inputTankA.getFluid().getFluid().isGaseous())
			this.inputTankA.drain(this.inputTankA.getCapacity(), true);
		if (flushLiquids || this.inputTankB.getFluid().getFluid().isGaseous())
			this.inputTankB.drain(this.inputTankB.getCapacity(), true);
		if (flushLiquids || this.liquidOutputTank.getFluid().getFluid().isGaseous())
			this.liquidOutputTank.drain(this.liquidOutputTank.getCapacity(), true);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound tag) {
		this.inputTankA.readFromNBT(tag.getCompoundTag("inputTankA"));
		this.inputTankB.readFromNBT(tag.getCompoundTag("inputTankB"));
		this.liquidOutputTank.readFromNBT(tag.getCompoundTag("liquidOutputTank"));
		this.energy.readFromNBT(tag.getCompoundTag("energy"));
		this.catalyst.readFromNBT(tag.getCompoundTag("catalyst"));
	}

	@Override
	public void writeToNBT(NBTTagCompound tag) {
		tag.setTag("inputTankA", this.inputTankA.writeToNBT(new NBTTagCompound()));
		tag.setTag("inputTankB", this.inputTankB.writeToNBT(new NBTTagCompound()));
		tag.setTag("liquidOutputTank", this.liquidOutputTank.writeToNBT(new NBTTagCompound()));
		tag.setTag("energy", this.energy.writeToNBT(new NBTTagCompound()));
		tag.setTag("catalyst", this.catalyst.writeToNBT(new NBTTagCompound()));
	}

	@Override
	public boolean canConnectEnergy(ForgeDirection from) {
		return true;
	}

	@Override
	public int receiveEnergy(ForgeDirection from, int maxReceive, boolean simulate) {
		return this.energy.receiveEnergy(maxReceive, simulate);
	}

	@Override
	public int getEnergyStored(ForgeDirection from) {
		return this.energy.getEnergyStored();
	}

	@Override
	public int getMaxEnergyStored(ForgeDirection from) {
		return this.energy.getMaxEnergyStored();
	}
}
