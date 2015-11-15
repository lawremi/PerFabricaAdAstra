package org.pfaa.chemica.client.registration;

import org.pfaa.chemica.ChemicaBlocks;
import org.pfaa.chemica.block.ConstructionMaterialBlock;
import org.pfaa.chemica.registration.CommonRegistrant;
import org.pfaa.core.client.render.CompositeBlockItemRenderer;

import net.minecraft.item.Item;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.client.MinecraftForgeClient;

public class ClientRegistrant extends CommonRegistrant {
	
	@Override
	public void register() {
		super.register();
		registerRenderers();
	}
	
	private void registerRenderers() {
		IItemRenderer renderer = new CompositeBlockItemRenderer();
		for (ConstructionMaterialBlock block : ChemicaBlocks.getConstructionMaterialBlocks()) {
			MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(block), renderer);
		}
	}
}
