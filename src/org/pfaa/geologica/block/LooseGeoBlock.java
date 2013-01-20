package org.pfaa.geologica.block;

import java.util.Random;

import net.minecraft.block.BlockSand;
import net.minecraft.block.StepSound;
import net.minecraft.block.material.Material;
import net.minecraft.entity.item.EntityFallingSand;
import net.minecraft.world.World;

import org.pfaa.geologica.GeoSubstance.Strength;
import org.pfaa.geologica.GeoSubstance.SubstanceType;
import org.pfaa.geologica.GeologicaTextures;
import org.pfaa.geologica.entity.item.CustomEntityFallingSand;

public class LooseGeoBlock extends GeoBlock {

	public LooseGeoBlock(int id, Strength strength, SubstanceType substanceType, Material material) {
		super(id, strength, substanceType, material);
	}

	@Override
	public String getTextureFile() {
		return GeologicaTextures.LOOSE;
	}
	
	@Override
    public void onBlockAdded(World par1World, int par2, int par3, int par4)
    {
        par1World.scheduleBlockUpdate(par2, par3, par4, this.blockID, this.tickRate());
    }

	@Override
    public void onNeighborBlockChange(World par1World, int par2, int par3, int par4, int par5)
    {
        par1World.scheduleBlockUpdate(par2, par3, par4, this.blockID, this.tickRate());
    }

	@Override
    public int tickRate()
    {
        return 5;
    }

	@Override
	public void updateTick(World par1World, int par2, int par3, int par4, Random par5Random)
    {
        if (!par1World.isRemote)
        {
            this.tryToFall(par1World, par2, par3, par4);
        }
    }

    /**
     * If there is space to fall below will start this block falling
     */
    private void tryToFall(World par1World, int par2, int par3, int par4)
    {
        if (BlockSand.canFallBelow(par1World, par2, par3 - 1, par4) && par3 >= 0)
        {
            byte var8 = 32;

            if (!BlockSand.fallInstantly && par1World.checkChunksExist(par2 - var8, par3 - var8, par4 - var8, par2 + var8, par3 + var8, par4 + var8))
            {
                if (!par1World.isRemote)
                {
                    EntityFallingSand var9 = new CustomEntityFallingSand(par1World, (double)((float)par2 + 0.5F), (double)((float)par3 + 0.5F), (double)((float)par4 + 0.5F), this.blockID, par1World.getBlockMetadata(par2, par3, par4));
                    par1World.spawnEntityInWorld(var9);
                }
            }
            else
            {
                par1World.setBlockWithNotify(par2, par3, par4, 0);

                while (BlockSand.canFallBelow(par1World, par2, par3 - 1, par4) && par3 > 0)
                {
                    --par3;
                }

                if (par3 > 0)
                {
                    par1World.setBlockWithNotify(par2, par3, par4, this.blockID);
                }
            }
        }
    }
    
	@Override
	protected float determineResistance(Strength strength) {
		float resistance = 0;
		switch(strength) {
		case WEAK:
			resistance = 1.0F;
			break;
		case MEDIUM:
			resistance = 1.3F;
			break;
		case STRONG:
			resistance = 1.6F;
			break;
		case VERY_STRONG:
			resistance = 2.0F;
			break;
		default:
		}
		return resistance;
	}

	@Override
	protected float determineHardness(Strength strength) {
		float hardness = 0;
		switch(strength) {
		case WEAK:
			hardness = 0.6F;
			break;
		case MEDIUM:
			hardness = 0.8F;
			break;
		case STRONG:
			hardness = 1.0F;
			break;
		case VERY_STRONG:
			hardness = 1.2F;
			break;
		default:
		}
		return hardness;
	}

	@Override
	protected StepSound determineStepSound(Material material) {
		if (material == Material.rock)
			return soundGravelFootstep;
		else return super.determineStepSound(material);
	}
}
