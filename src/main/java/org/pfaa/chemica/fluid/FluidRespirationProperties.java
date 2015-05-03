package org.pfaa.chemica.fluid;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.IExtendedEntityProperties;

public class FluidRespirationProperties implements IExtendedEntityProperties
{
    public final static String IDENTIFIER = "extendedPropertiesFluidRespiration";
	
    public final static int FULL_AIR_LEVEL = 300;
   
    private int airLevel = FULL_AIR_LEVEL;
    
    public int getAirLevel() {
    	return airLevel;
    }

    public void setAirLevel(int airLevel) {
    	this.airLevel = airLevel;
    }

    @Override
    public void saveNBTData(NBTTagCompound parCompound)
    {
    	NBTTagCompound compound = new NBTTagCompound();
    	parCompound.setTag(IDENTIFIER, compound); // set as a sub-compound
    	compound.setInteger("airLevel", this.getAirLevel());
    }

    @Override
    public void loadNBTData(NBTTagCompound parCompound)
    {
    	NBTTagCompound compound = (NBTTagCompound) 
    			parCompound.getTag(IDENTIFIER);

    	if (compound != null) {
    		this.setAirLevel(compound.getInteger("airLevel"));
    	}
    }

    @Override
    public void init(Entity entity, World world)
    {
    }
   
	public static void setAirLevel(EntityLivingBase entity, int airLevel) {
		IExtendedEntityProperties props = entity.getExtendedProperties(IDENTIFIER);
		((FluidRespirationProperties)props).setAirLevel(airLevel);
	}
	
	public static int getAirLevel(EntityLivingBase entity) {
		IExtendedEntityProperties props = entity.getExtendedProperties(IDENTIFIER);
		return ((FluidRespirationProperties)props).getAirLevel();
	}
	
	public static void register(EntityLivingBase entity) {
		entity.registerExtendedProperties(IDENTIFIER,  new FluidRespirationProperties());
	}
}