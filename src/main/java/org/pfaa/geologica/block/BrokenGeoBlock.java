package org.pfaa.geologica.block;

import org.pfaa.chemica.model.IndustrialMaterial;
import org.pfaa.chemica.model.Strength;
import org.pfaa.geologica.GeologicaBlocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;

public class BrokenGeoBlock extends GeoBlock {

	public BrokenGeoBlock(Strength strength, Class<? extends IndustrialMaterial> materialType, Material material) {
		super(strength, materialType, material, true);
	}

	@Override
	protected float determineHardness() {
		return super.determineHardness() * 2 / 3;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public boolean useMultipassRendering() {
		return true;
	}

	@Override
	protected IIcon registerUnderlayIcon(IIconRegister registry, int i) {
		return getNative(this.getGeoMaterial(i)).block.registerUnderlayIcon(registry, i);
	}
	
	@Override
	protected IIcon registerOverlayIcon(IIconRegister registry, int i) {
		return registry.registerIcon("geologica:cobbleOverlay");
	}

	@Override
	public GeoBlock getBrokenRockBlock() {
		GeoBlock dropped = null;
		switch(this.getStrength()) {
		case WEAK:
			dropped = GeologicaBlocks.WEAK_RUBBLE;
			break;
		case MEDIUM:
			dropped = GeologicaBlocks.MEDIUM_RUBBLE;
			break;
		case STRONG:
			dropped = GeologicaBlocks.STRONG_RUBBLE;
			break;
		case VERY_STRONG:
			dropped = GeologicaBlocks.VERY_STRONG_RUBBLE;
			break;
		default:
			break;
		}
		return dropped;
	}
}
