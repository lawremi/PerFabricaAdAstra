package org.pfaa.geologica.block;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.src.Block;
import net.minecraft.src.Material;

import org.pfaa.geologica.GeoSubstance;
import org.pfaa.geologica.Geologica;
import org.pfaa.geologica.GeoSubstance.Strength;
import org.pfaa.geologica.GeologicaTextures;

public class CobblestoneBlock extends BrokenGeoBlock {

	/* TODO: Sometimes the player will want to collect specific types of cobblestone.
	 * Other times, multiple cobblestone types will just get in the way. The player
	 * needs to make the decision. Proposal: introduce a rock collector bag item. If
	 * the player has it equipped when this item is picked up, this item is added to
	 * the inventory. Otherwise, plain old cobblestone is added. This would mean
	 * registering a pick up event handler that replaces the item stack in the entity.
	 */
	public CobblestoneBlock(int id, Strength strength) {
		super(id, Material.rock, strength);
		setStepSound(Block.soundStoneFootstep);
	}

}
