package org.pfaa.chemica;

import java.util.List;

import org.pfaa.chemica.fluid.FilledGlassBottleItem;
import org.pfaa.chemica.item.IndustrialMaterialItem;
import org.pfaa.chemica.model.Aggregate;
import org.pfaa.chemica.model.Aggregate.Aggregates;
import org.pfaa.chemica.model.Alloy.Alloys;
import org.pfaa.chemica.model.Catalysts;
import org.pfaa.chemica.model.Compound.Compounds;
import org.pfaa.chemica.model.Element;
import org.pfaa.chemica.processing.Form.Forms;
import org.pfaa.core.catalog.CatalogUtils;
import org.pfaa.core.catalog.ItemCatalog;

public class ChemicaItems implements ItemCatalog {
	public static final FilledGlassBottleItem FILLED_GLASS_BOTTLE = new FilledGlassBottleItem();
	
	public static final IndustrialMaterialItem<Aggregates> AGGREGATE_LARGE_DUST = 
			new IndustrialMaterialItem<Aggregates>(Forms.DUST_LARGE, Aggregates.class, Aggregate::isHard);
	
	public static final IndustrialMaterialItem<Element> ELEMENT_DUST = 
			new IndustrialMaterialItem<Element>(Forms.DUST, Element.class, Element::isMonatomic);
	public static final IndustrialMaterialItem<Element> ELEMENT_TINY_DUST = 
			new IndustrialMaterialItem<Element>(Forms.DUST_TINY, Element.class, Element::isMonatomic);
	
	public static final IndustrialMaterialItem<Compounds> COMPOUND_DUST = 
			new IndustrialMaterialItem<Compounds>(Forms.DUST, Compounds.class);
	public static final IndustrialMaterialItem<Compounds> COMPOUND_TINY_DUST = 
			new IndustrialMaterialItem<Compounds>(Forms.DUST_TINY, Compounds.class);
	
	public static final IndustrialMaterialItem<Element> ELEMENT_INGOT = 
			new IndustrialMaterialItem<Element>(Forms.INGOT, Element.class, Element::isMetallic);
	public static final IndustrialMaterialItem<Element> ELEMENT_NUGGET = 
			new IndustrialMaterialItem<Element>(Forms.NUGGET, Element.class, Element::isMetallic);
	
	public static final IndustrialMaterialItem<Alloys> ALLOY_INGOT = 
			new IndustrialMaterialItem<Alloys>(Forms.INGOT, Alloys.class);
	public static final IndustrialMaterialItem<Alloys> ALLOY_NUGGET = 
			new IndustrialMaterialItem<Alloys>(Forms.NUGGET, Alloys.class);
	public static final IndustrialMaterialItem<Alloys> ALLOY_DUST = 
			new IndustrialMaterialItem<Alloys>(Forms.DUST, Alloys.class);
	public static final IndustrialMaterialItem<Alloys> ALLOY_TINY_DUST = 
			new IndustrialMaterialItem<Alloys>(Forms.DUST_TINY, Alloys.class);
	
	public static final IndustrialMaterialItem<Catalysts> CATALYST_DUST = 
			new IndustrialMaterialItem<Catalysts>(Forms.DUST, Catalysts.class);
	
	@SuppressWarnings("rawtypes")
	public static List<IndustrialMaterialItem> getIndustrialMaterialItems() {
		return CatalogUtils.getEntries(ChemicaItems.class, IndustrialMaterialItem.class);
	}
}
