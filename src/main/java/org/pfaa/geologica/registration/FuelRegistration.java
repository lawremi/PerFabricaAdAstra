package org.pfaa.geologica.registration;

import org.pfaa.geologica.GeoMaterial;
import org.pfaa.geologica.GeologicaItems;
import org.pfaa.geologica.processing.Crude.Crudes;

import cpw.mods.fml.common.IFuelHandler;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.item.ItemStack;

public class FuelRegistration {
	
	public static void init() {
		GameRegistry.registerFuelHandler(new CrudeFuelHandler());
	}
	
	private static class CrudeFuelHandler implements IFuelHandler {

		@Override
		public int getBurnTime(ItemStack fuel) {
			// FIXME: do this based on the chemical model later; for now it is hack city.
			// Whatever we do, it should probably round to the nearest multiple of 200.
			
			if (fuel.getItem() == GeologicaItems.EARTHY_CLUMP) {
				if (GeologicaItems.EARTHY_CLUMP.getIndustrialMaterial(fuel) == GeoMaterial.PEAT) {
					return 400;
				}
			}
			
			if (fuel.getItem() != GeologicaItems.CRUDE_LUMP && fuel.getItem() != GeologicaItems.CRUDE_DUST)
				return 0;
			
			Crudes material = GeologicaItems.CRUDE_LUMP.getIndustrialMaterial(fuel);
			switch(material) {
			case OIL_SHALE:
				return 400;
			case LIGNITE:
				return 800;
			case BITUMINOUS_COAL:
				return 1600;
			case ANTHRACITE:
				return 2400;
			case COKE:
				return 3200;
			case DRY_PEAT:
				return 800;
			default:
				return 0;
			}
		}

	}

}
