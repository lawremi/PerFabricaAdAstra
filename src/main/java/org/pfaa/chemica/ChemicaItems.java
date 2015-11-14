package org.pfaa.chemica;

import java.util.List;

import org.pfaa.chemica.fluid.FilledGlassBottleItem;
import org.pfaa.chemica.item.IndustrialMaterialItem;
import org.pfaa.chemica.model.Aggregate;
import org.pfaa.chemica.model.Aggregate.Aggregates;
import org.pfaa.chemica.model.Alloy.Alloys;
import org.pfaa.chemica.model.Compound.Compounds;
import org.pfaa.chemica.model.Element;
import org.pfaa.chemica.model.Element.Category;
import org.pfaa.chemica.processing.Form.Forms;
import org.pfaa.core.catalog.CatalogUtils;
import org.pfaa.core.catalog.ItemCatalog;

import com.google.common.base.Predicate;

public class ChemicaItems implements ItemCatalog {
	public static final FilledGlassBottleItem FILLED_GLASS_BOTTLE = new FilledGlassBottleItem();
	
	private static Predicate<Aggregate> StoneAndHardenedClay = new Predicate<Aggregate>() {
		public boolean apply(Aggregate obj) {
			return obj == Aggregates.STONE || obj == Aggregates.HARDENED_CLAY || obj == Aggregates.OBSIDIAN;
		}
	};
	private static Predicate<Aggregate> SandAndGravel = new Predicate<Aggregate>() {
		public boolean apply(Aggregate obj) {
			return obj == Aggregates.SAND || obj == Aggregates.GRAVEL;
		}
	};
	private static Predicate<Element> Castable = new Predicate<Element>() {
		public boolean apply(Element obj) {
			return obj.getCategory().isMetallic() && obj.getFusion() != null;
		}
	};
	private static Predicate<Element> Monatomic = new Predicate<Element>() {
		public boolean apply(Element obj) {
			return obj.getCategory() != Category.DIATOMIC_NONMETAL;
		}
	};
	
	public static final IndustrialMaterialItem<Aggregates> AGGREGATE_DUST = 
			new IndustrialMaterialItem<Aggregates>(Forms.DUST, Aggregates.class, StoneAndHardenedClay);
	public static final IndustrialMaterialItem<Aggregates> AGGREGATE_TINY_DUST = 
			new IndustrialMaterialItem<Aggregates>(Forms.TINY_DUST, Aggregates.class, StoneAndHardenedClay);
	public static final IndustrialMaterialItem<Aggregates> AGGREGATE_PILE = 
			new IndustrialMaterialItem<Aggregates>(Forms.PILE, Aggregates.class, SandAndGravel);
	
	public static final IndustrialMaterialItem<Element> ELEMENT_DUST = 
			new IndustrialMaterialItem<Element>(Forms.DUST, Element.class, Monatomic);
	public static final IndustrialMaterialItem<Element> ELEMENT_TINY_DUST = 
			new IndustrialMaterialItem<Element>(Forms.TINY_DUST, Element.class, Monatomic);
	
	public static final IndustrialMaterialItem<Compounds> COMPOUND_DUST = 
			new IndustrialMaterialItem<Compounds>(Forms.DUST, Compounds.class);
	public static final IndustrialMaterialItem<Compounds> COMPOUND_TINY_DUST = 
			new IndustrialMaterialItem<Compounds>(Forms.TINY_DUST, Compounds.class);
	
	public static final IndustrialMaterialItem<Element> METAL_INGOT = 
			new IndustrialMaterialItem<Element>(Forms.INGOT, Element.class, Castable);
	public static final IndustrialMaterialItem<Element> METAL_NUGGET = 
			new IndustrialMaterialItem<Element>(Forms.NUGGET, Element.class, Castable);
	
	public static final IndustrialMaterialItem<Alloys> ALLOY_INGOT = 
			new IndustrialMaterialItem<Alloys>(Forms.INGOT, Alloys.class);
	public static final IndustrialMaterialItem<Alloys> ALLOY_NUGGET = 
			new IndustrialMaterialItem<Alloys>(Forms.NUGGET, Alloys.class);
	public static final IndustrialMaterialItem<Alloys> ALLOY_DUST = 
			new IndustrialMaterialItem<Alloys>(Forms.DUST, Alloys.class);
	public static final IndustrialMaterialItem<Alloys> ALLOY_TINY_DUST = 
			new IndustrialMaterialItem<Alloys>(Forms.TINY_DUST, Alloys.class);
	
	@SuppressWarnings("rawtypes")
	public static List<IndustrialMaterialItem> getIndustrialMaterialItems() {
		return CatalogUtils.getEntries(ChemicaItems.class, IndustrialMaterialItem.class);
	}
}
