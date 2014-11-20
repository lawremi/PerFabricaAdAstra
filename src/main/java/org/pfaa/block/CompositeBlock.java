package org.pfaa.block;

import java.util.List;

import org.pfaa.chemica.model.Condition;
import org.pfaa.geologica.GeoMaterial;
import org.pfaa.geologica.Geologica;
import org.pfaa.geologica.client.registration.ClientRegistrant;
import org.pfaa.geologica.processing.Aggregate;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public abstract class CompositeBlock extends Block implements CompositeBlockAccessors {

	private IIcon[] underlayIcons;
	private IIcon[] oreOverlayIcons;
	private boolean overlayEnabled;
	private boolean defaultRendererEnabled;
	
	public CompositeBlock(Material material, boolean defaultRendererEnabled) {
		super(material);
		this.defaultRendererEnabled = defaultRendererEnabled;
	}
	
	@Override
	public int damageDropped(int meta) {
		return meta;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int side, int meta) {
		if (this.overlayEnabled) {
			return this.getOverlayIcon(side, meta);
		} else {
			return this.getUnderlayIcon(side, meta);
		}
	}
	
	@SideOnly(Side.CLIENT)
	protected IIcon getUnderlayIcon(int side, int meta) {
		return this.underlayIcons[damageDropped(meta)];
	}
	
	@SideOnly(Side.CLIENT)
	public IIcon getOverlayIcon(int side, int meta) {
		return this.oreOverlayIcons[damageDropped(meta)];
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(IBlockAccess world, int x, int y, int z, int side) {
		if (this.overlayEnabled) {
			return this.getOverlayIcon(world, x, y, z, side);
		} else {
			return this.getUnderlayIcon(world, x, y, z, side);
		}
	}
	
	@SideOnly(Side.CLIENT)
	protected IIcon getUnderlayIcon(IBlockAccess world, int x, int y, int z, int side) {
		return this.getUnderlayIcon(side, world.getBlockMetadata(x, y, z));
	}

	@SideOnly(Side.CLIENT)
	protected IIcon getOverlayIcon(IBlockAccess world, int x, int y, int z, int side) {
		return this.getOverlayIcon(side, world.getBlockMetadata(x, y, z));
	}
	
	/* (non-Javadoc)
	 * @see org.pfaa.block.ICompositeBlock#getSubBlocks(int, net.minecraft.creativetab.CreativeTabs, java.util.List)
	 */
	@Override
	@SideOnly(Side.CLIENT)
	public void getSubBlocks(Item item, CreativeTabs creativeTabs, List list)
    {
		for (int i = 0; i < getMetaCount(); ++i)
        {
            list.add(new ItemStack(item, 1, damageDropped(i)));
        }
    }
	
	public float getBlockResistance() {
		return this.blockResistance / 3.0F;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister registry) {
		this.underlayIcons = new IIcon[getMetaCount()];
		for (int i = 0; i < getMetaCount(); ++i)
        {
            underlayIcons[i] = this.registerUnderlayIcon(registry, i); 
        }
		this.oreOverlayIcons = new IIcon[getMetaCount()];
		if (this.useMultipassRendering()) {
			for (int i = 0; i < getMetaCount(); ++i)
			{
				oreOverlayIcons[i] = this.registerOverlayIcon(registry, i);
            }
        }
	}

	protected IIcon registerMetaIcon(IIconRegister registry, int i) {
		return registry.registerIcon(this.getTextureName() + "_" + getBlockNameSuffix(i));
	}
	
	protected IIcon registerUnderlayIcon(IIconRegister registry, int i) {
		return this.registerMetaIcon(registry, i);
	}
	
	protected IIcon registerOverlayIcon(IIconRegister registry, int i) {
		return this.registerMetaIcon(registry, i);
	}
	
	public boolean useMultipassRendering() {
		return false;
	}
	
	@Override
	public int getRenderType() {
		if (this.defaultRendererEnabled) {
			return super.getRenderType();
		} else {
			return Geologica.instance.registrant.getCompositeBlockRendererId();
		}
	}

	@Override
	public boolean enableOverlay() {
		if (this.useMultipassRendering()) {
			this.overlayEnabled = true;
		}
		return this.overlayEnabled;
	}

	@Override
	public void disableOverlay() {
		this.overlayEnabled = false;
	}

	public void enableDefaultRenderer() {
		this.defaultRendererEnabled = true;
	}

	public void disableDefaultRenderer() {
		this.defaultRendererEnabled = false;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public int getRenderBlockPass() {
		return this.defaultRendererEnabled && this.useMultipassRendering() ? 1 : 0;
	}

	@Override
	public boolean canRenderInPass(int pass) {
		if (pass <= this.getRenderBlockPass()) {
			if (pass == 1) {
				this.enableOverlay();
			} else {
				this.disableOverlay();
			}
			return true;
		}
		return false;
	}

}
