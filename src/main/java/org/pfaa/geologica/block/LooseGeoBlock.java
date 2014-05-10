package org.pfaa.geologica.block;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockSand;
import net.minecraft.block.material.Material;
import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;

import org.pfaa.chemica.model.IndustrialMaterial;
import org.pfaa.geologica.Geologica;
import org.pfaa.geologica.GeoMaterial.Strength;

public class LooseGeoBlock extends GeoBlock {

	public LooseGeoBlock(Strength strength, Class<? extends IndustrialMaterial> composition, Material material) {
		super(strength, composition, material);
	}

	@Override
    public void onBlockAdded(World par1World, int par2, int par3, int par4)
    {
        par1World.scheduleBlockUpdate(par2, par3, par4, this, this.tickRate(null));
    }

	@Override
    public void onNeighborBlockChange(World par1World, int par2, int par3, int par4, Block par5)
    {
        par1World.scheduleBlockUpdate(par2, par3, par4, this, this.tickRate(null));
    }

	@Override
    public int tickRate(World par1World)
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
        if (BlockSand.func_149831_e(par1World, par2, par3 - 1, par4) && par3 >= 0)
        {
            byte var8 = 32;

            if (!BlockSand.fallInstantly && par1World.checkChunksExist(par2 - var8, par3 - var8, par4 - var8, par2 + var8, par3 + var8, par4 + var8))
            {
                if (!par1World.isRemote)
                {
                	EntityFallingBlock var9 = new EntityFallingBlock(par1World, (double)((float)par2 + 0.5F), (double)((float)par3 + 0.5F), (double)((float)par4 + 0.5F), this, par1World.getBlockMetadata(par2, par3, par4));
                    par1World.spawnEntityInWorld(var9);
                }
            }
            else
            {
                par1World.setBlockToAir(par2, par3, par4);

                while (BlockSand.func_149831_e(par1World, par2, par3 - 1, par4) && par3 > 0)
                {
                    --par3;
                }

                if (par3 > 0)
                {
                    par1World.setBlock(par2, par3, par4, this);
                }
            }
        }
    }
    
	@Override
	protected Block.SoundType determineStepSound() {
		if (blockMaterial == Material.rock)
			return soundTypeGravel;
		else return super.determineStepSound();
	}
	
	@Override
	public String getHarvestTool(int metadata) {
		return "shovel";
	}

	@Override
	public int getHarvestLevel(int metadata) {
		return 0;
	}
	
	@Override
	protected float determineHardness() {
		float hardness = 0;
		switch(getStrength()) {
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

}
