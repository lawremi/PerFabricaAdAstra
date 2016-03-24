package org.pfaa.chemica.integration;

import java.util.List;

import org.pfaa.chemica.processing.Form.Forms;
import org.pfaa.chemica.processing.TemperatureLevel;
import org.pfaa.chemica.registration.OreDictUtils;
import org.pfaa.chemica.registration.RecipeRegistration;
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
	
	public static class TConstructRecipeRegistry extends AbstractRecipeRegistry {

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
		
		private static final int SEARED_STONE_FOR_BLOCK = 18;
		
		private void addMelting(ItemStack input, FluidStack output, int temp) {
			if (output.getFluid() == FluidRegistry.LAVA) {
				output = FluidRegistry.getFluidStack("stone.seared", SEARED_STONE_FOR_BLOCK);
			}
			BlockWithMeta<Block> render = getBlockWithMeta(input);
			if (render != null) {
				temp /= 2;
				if (!(input.getItem() instanceof ItemBlock)) {
					temp -= temp / 3;
				}
				Smeltery.addMelting(input, render.block, render.meta, temp, output);
			}
		}
		
		private void addCasting(FluidStack fluid, ItemStack output) {
			ItemStack cast = null;
			LiquidCasting casting;
			if (output.getItem() instanceof ItemBlock) {
				casting = TConstructRegistry.getBasinCasting();
			} else {
				casting = TConstructRegistry.getTableCasting();
				cast = TConstructRegistry.getItemStack("ingotCast");
			}
			casting.addCastingRecipe(output, fluid, cast, fluid.getFluid().getTemperature() / 200);
		}
		
		@Override
		public void registerSmeltingRecipe(ItemStack input, FluidStack output, ItemStack flux, TemperatureLevel temp) {
			if (flux != null) {
				return;
			}
			if (OreDictUtils.hasPrefix(input, "ore")) {
				output = output.copy();
				output.amount *= 2;
			}
			this.addMelting(input, output, temp.getReferenceTemperature());
		}
		
		@Override
		public void registerCastingRecipe(FluidStack input, ItemStack output) {
			this.addCasting(input, output);
		}

		@Override
		public void registerMeltingRecipe(ItemStack input, FluidStack output, int temp) {
			this.addMelting(input, output, output.getFluid().getTemperature());
		}
		
		@Override
		public void registerAlloyingRecipe(FluidStack output, List<FluidStack> inputs) {
			Smeltery.addAlloyMixing(output, inputs.toArray(new FluidStack[0]));
		}

	}

}
