package org.pfaa.geologica.entity.item;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;

import net.minecraft.entity.item.EntityFallingSand;
import net.minecraft.world.World;
import cpw.mods.fml.common.registry.IEntityAdditionalSpawnData;

public class CustomEntityFallingSand extends EntityFallingSand implements IEntityAdditionalSpawnData {

	public CustomEntityFallingSand(World world) {
		super(world);
	}
	
	public CustomEntityFallingSand(World par1World, double par2, double par4,
			double par6, int par8, int par9) {
		super(par1World, par2, par4, par6, par8, par9);
	}

	@Override
	public void writeSpawnData(ByteArrayDataOutput data) {
		data.writeInt(blockID);
		data.writeInt(metadata);
	}

	@Override
	public void readSpawnData(ByteArrayDataInput data) {
		this.blockID = data.readInt();
		this.metadata = data.readInt();
	}

}
