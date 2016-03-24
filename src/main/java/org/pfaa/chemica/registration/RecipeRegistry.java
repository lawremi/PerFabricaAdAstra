package org.pfaa.chemica.registration;

import java.util.List;

import org.pfaa.chemica.model.Strength;
import org.pfaa.chemica.processing.TemperatureLevel;
import org.pfaa.chemica.util.ChanceStack;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

/* On abstracting recipes via the ore dictionary:
 * 
 * In theory, we could add a layer on top of this that uses MaterialStack instead of ItemStack. It would
 * convert the MaterialStack to a list of ItemStacks and handle the combinatorics. 
 * 
 * But ultimately,
 * do we really want multiple items from different mods representing the same thing? Should not the 
 * mod pack resolve such conflicts through recipe configuration? There job would be easier if used 
 * the ore dictionary for recipe inputs, but there is still a lot of clutter, e.g., in NEI.
 * 
 * What then is the point of the ore dictionary? We currently use it as a table for looking up
 * an ItemStack by its form and material -- not for cross-mod compatibility. The fluid registry
 * is better at enabling compatibility, because it enforces a 1-to-1 mapping. In general, that would
 * not be feasible for items, but it probably is for the special case of materials, i.e., the type
 * of thing that is registered in the ore dictionary. Gregtech essentially does that.
 * 
 * It seems to be a matter of perspective. A mod like PFAA that primarily adds materials only needs
 * to ensure that its materials are able to processed. A mod that adds processing capabilities,
 * i.e., machines, might use the ore dictionary to absorb materials from other mods. 
 * 
 * Thus, it comes down to the role of PFAA. Historically, we relied upon the ore dictionary for all processing.
 * But now we define items for our materials, and recipes 
 * for those items. Should we instead define recipes in terms of the materials? What if someone were
 * to completely disable Geologica generation? Should not both Geologica and Chemica remain useful?
 */
public interface RecipeRegistry {
	public void registerCastingRecipe(ItemStack input, ItemStack output, ItemStack flux, int temp);
	public void registerCastingRecipe(FluidStack input, ItemStack output);
	public void registerMeltingRecipe(ItemStack input, FluidStack output, int temp);
	public void registerSmeltingRecipe(ItemStack input, ItemStack output, ItemStack flux, TemperatureLevel temp);
	public void registerSmeltingRecipe(ItemStack input, FluidStack output, ItemStack flux, TemperatureLevel temp);
	public void registerGrindingRecipe(ItemStack input, ItemStack output, List<ChanceStack> secondaries, Strength strength);
	public void registerCrushingRecipe(ItemStack input, ItemStack output, ChanceStack dust, Strength strength);
	public void registerAlloyingRecipe(ItemStack output, ItemStack base, List<ItemStack> solutes, int temp);
	public void registerAlloyingRecipe(FluidStack output, List<FluidStack> inputs);
	public void registerRoastingRecipe(List<ItemStack> inputs, ItemStack output, int temp);
	public void registerAbsorptionRecipe(List<ItemStack> inputs, FluidStack additive, ItemStack output, int temp);
	public void registerMixingRecipe(FluidStack input, List<ItemStack> additives, FluidStack output, int temp);
	public void registerMixingRecipe(List<ItemStack> inputs, ItemStack output);
	public void registerPhysicalSeparationRecipe(ItemStack input, List<ChanceStack> outputs);
}
