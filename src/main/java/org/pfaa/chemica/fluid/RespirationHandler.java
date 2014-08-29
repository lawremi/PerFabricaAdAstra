package org.pfaa.chemica.fluid;

import java.util.Random;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraftforge.client.GuiIngameForge;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.event.entity.EntityEvent.EntityConstructing;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;

import org.lwjgl.opengl.GL11;
import org.pfaa.chemica.Chemica;
import org.pfaa.chemica.block.IndustrialFluidBlock;
import org.pfaa.chemica.model.Compound.Compounds;
import org.pfaa.chemica.model.IndustrialMaterial;
import org.pfaa.chemica.model.Mixture;
import org.pfaa.chemica.model.MixtureComponent;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent.Phase;
import cpw.mods.fml.common.gameevent.TickEvent.PlayerTickEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/* Ways to manage the air state:
 * 1) Implement IExtendedEntityProperties to manage our own air property, which is a lot of work and redundant
 *   - Some work is avoided by using the DataWatcher, but that is really risky
 * 2) Use the pre/post player update hooks to use the existing air property, which only works for players
 * 3) Hybrid strategy: Use #2 for players and #1 for non-players, which is easier, because there is no need
 *    to synchronize the non-player air setting to the client. 
 * 
 * Trying #3 for now.
 */
public class RespirationHandler {

	public static final RespirationHandler INSTANCE = new RespirationHandler();
	
	private static final float MIN_PERMISSIBLE_OXYGEN_CONTENT = 0.195F;
	private static final float ATMOSPHERIC_OXYGEN_CONTENT = 0.209F;

	private int playerAir;
	
	private RespirationHandler() {
	}

	@SubscribeEvent
	public void onEntityConstructing(EntityConstructing event) {
		if (event.entity instanceof EntityLivingBase && !(event.entity instanceof EntityPlayer))
		{
			FluidRespirationProperties.register((EntityLivingBase)event.entity);
	    }
	}
	
	@SubscribeEvent
	public void onPlayerTick(PlayerTickEvent event) {
		if (event.phase == Phase.START) {
			this.playerAir = event.player.getAir();
		} else {
			handleRespiration(event.player, this.playerAir);
		}
	}
	
	@SubscribeEvent
    public void onLivingUpdate(LivingUpdateEvent event) {
		if (!(event.entityLiving instanceof EntityPlayer))
			handleRespiration(event.entityLiving, getAirLevel(event.entityLiving));
    }
    
    private static void handleRespiration(EntityLivingBase entity, int initAirLevel) {
    	if (entity.isEntityAlive())
        {
    		// TODO: if liquid is irritant (health hazard), apply negative effects
    		//       this requires an algorithm like Entity.handleWaterMovement().
    		//       if fluid is extremely hot (>320K), we should just apply damage. 
    		IndustrialFluidBlock block = IndustrialFluidBlock.atEyeLevel(entity);
    		if (block != null && (block.getFluid().isGaseous() || entity.canBreatheUnderwater())) {
    			// TODO: apply health effects for gases here
    			// TODO: as part of the above, handle oxygen _over_dose
        		float oxygenContent = getBreathableOxygenContent(block, entity);
        		if (oxygenContent < MIN_PERMISSIBLE_OXYGEN_CONTENT) {
	        		Random rand = new Random();
	        		setAirLevel(entity, decreaseAirSupply(entity, initAirLevel, rand));
		        	if (getAirLevel(entity) == -20) {
		        		setAirLevel(entity, 0);
		            	float damage = getOxygenDeprivationDamage(oxygenContent);
		            	if (damage > 0) {
			            	entity.attackEntityFrom(DamageSource.drown, damage);
			            } else {
			            	addOxygenDeprivationEffects(entity, oxygenContent, 20);
			            }
		            } else if (!block.getFluid().isGaseous()) {
	            		generateBubbleParticles(entity, rand);
		            }
        		}
        	} else {
        		setAirLevel(entity, FluidRespirationProperties.FULL_AIR_LEVEL);
    		}
        }
    }

	private static int getAirLevel(EntityLivingBase entity) {
		if (entity instanceof EntityPlayer) {
			return entity.getAir();
		} else {
			return FluidRespirationProperties.getAirLevel(entity);
		}
	}

	private static void setAirLevel(EntityLivingBase entity, int airLevel) {
		if (entity instanceof EntityPlayer) {
			entity.setAir(airLevel);
		} else {
			FluidRespirationProperties.setAirLevel(entity, airLevel);
		}
	}

	private static float getOxygenDeprivationDamage(float oxygenContent) {
		return oxygenContent < 0.06 ? 2.0F : oxygenContent < 0.08 ? 1.0F : 0;
	}

	private static void addOxygenDeprivationEffects(EntityLivingBase entity, double oxygenContent, int duration) {
		if (oxygenContent < 0.10) {
			entity.addPotionEffect(new PotionEffect(Potion.confusion.id, duration));
		}
		if (oxygenContent < 0.15) {
			entity.addPotionEffect(new PotionEffect(Potion.moveSlowdown.id, duration));
		}
		if (oxygenContent < MIN_PERMISSIBLE_OXYGEN_CONTENT) {
			entity.addPotionEffect(new PotionEffect(Potion.digSlowdown.id, duration));
		}
	}
	
	private static int decreaseAirSupply(EntityLivingBase entity, int airLevel, Random rand)
    {
		int j = EnchantmentHelper.getRespiration(entity);
        return j > 0 && rand.nextInt(j + 1) > 0 ? airLevel : airLevel - 1;
    }
    
    private static void generateBubbleParticles(Entity entity, Random rand) {
        for (int i = 0; i < 8; ++i)
        {
            float f = rand.nextFloat() - rand.nextFloat();
            float f1 = rand.nextFloat() - rand.nextFloat();
            float f2 = rand.nextFloat() - rand.nextFloat();
            entity.worldObj.spawnParticle("bubble", 
            		entity.posX + (double)f, 
            		entity.posY + (double)f1, 
                    entity.posZ + (double)f2, 
                    entity.motionX, entity.motionY, entity.motionZ);
        }
    }
    
    
    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void onRenderAir(RenderGameOverlayEvent.Pre event) {
    	if (event.type == ElementType.AIR) {
    		float oxygenContent = getOxygenContentAtEyeLevel(Minecraft.getMinecraft().thePlayer);
    		if (oxygenContent < MIN_PERMISSIBLE_OXYGEN_CONTENT) {
    			this.renderAirBar(event.resolution);
    			event.setCanceled(true);
    		}
    	}
    }

    private static float getOxygenContentAtEyeLevel(EntityLivingBase entity) {
    	IndustrialFluidBlock block = IndustrialFluidBlock.atEyeLevel(entity);
    	return block != null ? getBreathableOxygenContent(block.getFluid()) : ATMOSPHERIC_OXYGEN_CONTENT;  
	}

	private static float getBreathableOxygenContent(IndustrialFluid fluid) {
		if (!fluid.isGaseous()) return 0; 
		IndustrialMaterial material = fluid.getIndustrialMaterial();
		if (material == Compounds.O2) {
			return 1.0F;
		} else if (material instanceof Mixture) {
			for (MixtureComponent comp : ((Mixture)material).getComponents()) {
				if (comp.material == Compounds.O2)
					return (float)comp.weight;
			}
		}
		return 0;
	}
	
	private static float getBreathableOxygenContent(IndustrialFluidBlock block, EntityLivingBase entity) {
    	float oxygenContent = getBreathableOxygenContent(block.getFluid());
    	if (block.getFluid().isGaseous()) {
    		double j0 = entity.posY + (entity.worldObj.isRemote ? 0 : entity.getEyeHeight());
    		int i = MathHelper.floor_double(entity.posX);
            int j = MathHelper.floor_double(j0);
            int k = MathHelper.floor_double(entity.posZ);
            float filled = Math.abs(block.getFilledPercentage(entity.worldObj, i, j, k));
            oxygenContent = oxygenContent * filled + ATMOSPHERIC_OXYGEN_CONTENT * (1 - filled);
    	}
        return oxygenContent;
	}

    @SideOnly(Side.CLIENT)
	private void renderAirBar(ScaledResolution res) {
        GL11.glEnable(GL11.GL_BLEND);
        int width = res.getScaledWidth();
        int height = res.getScaledHeight();
        int left = width / 2 + 91;
        int top = height - GuiIngameForge.right_height;

        Minecraft mc = Minecraft.getMinecraft();
        int air = getAirLevel(mc.thePlayer);
        int full = MathHelper.ceiling_double_int((double)(air - 2) * 10.0D / 300.0D);
        int partial = MathHelper.ceiling_double_int((double)air * 10.0D / 300.0D) - full;
        
        for (int i = 0; i < full + partial; ++i)
        {
        	mc.ingameGUI.drawTexturedModalRect(left - i * 8 - 9, top, (i < full ? 16 : 25), 18, 9, 9);
        }
        GuiIngameForge.right_height += 10;
        
        GL11.glDisable(GL11.GL_BLEND);
	}
}
