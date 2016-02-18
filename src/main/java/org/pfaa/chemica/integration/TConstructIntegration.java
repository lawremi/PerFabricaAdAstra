package org.pfaa.chemica.integration;

import java.util.List;

import org.pfaa.chemica.fluid.IndustrialFluids;
import org.pfaa.chemica.model.State;
import org.pfaa.chemica.model.Strength;
import org.pfaa.chemica.processing.Form.Forms;
import org.pfaa.chemica.processing.TemperatureLevel;
import org.pfaa.chemica.registration.OreDictUtils;
import org.pfaa.chemica.registration.RecipeRegistration;
import org.pfaa.chemica.registration.RecipeRegistry;
import org.pfaa.chemica.util.ChanceStack;
import org.pfaa.core.block.BlockWithMeta;

import cpw.mods.fml.common.Loader;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import tconstruct.library.TConstructRegistry;
import tconstruct.library.crafting.LiquidCasting;
import tconstruct.library.crafting.Smeltery;

// TODO: add weapon and tool parts for certain metals (alloys):
//       https://en.wikipedia.org/wiki/List_of_blade_materials

public class TConstructIntegration {
	public static void init() {
		if (Loader.isModLoaded(ModIds.TCONSTRUCT)) {
			RecipeRegistration.getTarget().addRegistry(new TConstructRecipeRegistry());
		}
	}
	
	public static class TConstructRecipeRegistry implements RecipeRegistry {

		@Override
		public void registerGrindingRecipe(ItemStack input, ItemStack output, List<ChanceStack> secondaries,
				Strength strength) { }

		@Override
		public void registerCrushingRecipe(ItemStack input, ItemStack output, Strength strength) { }

		private BlockWithMeta<Block> getBlockWithMeta(ItemStack itemStack) {
			int meta;
			ItemBlock itemBlock;
			if (itemStack.getItem() instanceof ItemBlock) {
				itemBlock = (ItemBlock)itemStack.getItem();
				meta = itemStack.getItemDamage();
			} else {
				ItemStack blockStack = OreDictUtils.lookupBest(Forms.BLOCK, itemStack);
				if (blockStack == null) {
					blockStack = OreDictUtils.lookupBest("ore", itemStack);
				}
				if (blockStack != null) {
					itemBlock = (ItemBlock)blockStack.getItem();
					meta = blockStack.getItemDamage();
				} else {
					return null;
				}
			}
			Block block = itemBlock.field_150939_a;
			return new BlockWithMeta<Block>(block, meta);
		}
		
		private FluidStack addMelting(ItemStack input, int temp) {
			return this.addMelting(input, input, temp);
		}
		
		// FIXME: rendering of fluids is always white; we think this is fixed in TCon 2.0
		private FluidStack addMelting(ItemStack input, ItemStack output, int temp) {
			FluidStack fluid;
			if (OreDictUtils.hasPrefix(output, "stone") || OreDictUtils.hasPrefix(output, "rubble") ||
					OreDictUtils.hasPrefix(output, "cobblestone")) {
				fluid = FluidRegistry.getFluidStack("stone.seared", IndustrialFluids.getAmount(Forms.BLOCK));
			} else {
				fluid = IndustrialFluids.toFluidStack(output, State.LIQUID);
			}
			BlockWithMeta<Block> render = getBlockWithMeta(input);
			if (render != null) {
				Smeltery.addMelting(input, render.block, render.meta, temp / 2, fluid);
			}
			return fluid;
		}
		
		private void addCasting(FluidStack fluid, ItemStack output, int temp) {
			ItemStack cast = TConstructRegistry.getItemStack("ingotCast");
			LiquidCasting casting; 
			if (output.getItem() instanceof ItemBlock) {
				casting = TConstructRegistry.getBasinCasting();
			} else {
				casting = TConstructRegistry.getTableCasting();
			}
			casting.addCastingRecipe(output, fluid, cast, temp / 200);
		}
		
		@Override
		public void registerSmeltingRecipe(ItemStack input, ItemStack output, ItemStack flux, TemperatureLevel temp) {
			if (flux != null) {
				return;
			}
			if (OreDictUtils.hasPrefix(input, "ore")) {
				output = output.copy();
				output.stackSize *= 2;
			}
			this.addMelting(input, output, temp.getReferenceTemperature());
		}
		
		@Override
		public void registerCastingRecipe(ItemStack input, ItemStack output, int temp) {
			FluidStack fluid = this.addMelting(input, temp);
			this.addCasting(fluid, output, temp);
			this.addMelting(output, temp);
		}
	}

}
