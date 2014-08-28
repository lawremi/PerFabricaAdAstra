package org.pfaa.chemica.fluid;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;

import org.pfaa.chemica.block.IndustrialFluidBlock;
import org.pfaa.geologica.Geologica;

import codechicken.lib.math.MathHelper;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class FluidMovementHandler {
	public static final FluidMovementHandler INSTANCE = new FluidMovementHandler();
		
	private FluidMovementHandler() {	
	}

	// FIXME: we want to do this for every entity, but there is no hook.
	@SubscribeEvent
    public void onLivingUpdate(LivingUpdateEvent event) {
		handleEntityMovement(event.entity);
    }

	// NOTE:  By calling handleWaterMovement(), the entity is marked as "in water" and "wet".
	//        Being in water mostly affects movement and particle effects, which is a good thing.
	//        But being wet means that fire will be extinguished. This may be OK as long as
	//        a flammable block has the opportunity to ignite prior to the entity being extinguished.
	private static void handleEntityMovement(Entity entity) {
		int x = MathHelper.floor_double(entity.posX);
		int y = MathHelper.floor_double(entity.posY);
		int z = MathHelper.floor_double(entity.posZ);
		Block block = entity.worldObj.getBlock(x, y, z);
		if (block instanceof IndustrialFluidBlock) {
			IndustrialFluidBlock fluidBlock = (IndustrialFluidBlock)block;
			if (fluidBlock.getFluid().isGaseous()) {
				return;
			}
			//boolean burning = entity.isBurning();
			fluidBlock.spoofWater();
			entity.handleWaterMovement();
			fluidBlock.unspoofWater();
			//entity.setFire(burning ? 1 : 0);
		}
	}
}
