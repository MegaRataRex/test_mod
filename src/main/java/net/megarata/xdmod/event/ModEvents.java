package net.megarata.xdmod.event;

import com.mojang.datafixers.types.templates.Hook;
import net.megarata.xdmod.XdMod;
import net.megarata.xdmod.effect.ModEffects;
import net.megarata.xdmod.item.ModItems;
import net.megarata.xdmod.particle.ModParticles;
import net.megarata.xdmod.util.ModUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ExplosionDamageCalculator;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ComputeFovModifierEvent;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.print.DocFlavor;
import java.util.List;
import java.util.Random;

@Mod.EventBusSubscriber(modid = XdMod.MODID)
public class ModEvents {

    static Logger logger = LogManager.getLogger();

    @SubscribeEvent
    public static void onPhdFall(LivingFallEvent event){

        LivingEntity entity = event.getEntity();
        Random random = new Random();
        if(entity == null || !entity.hasEffect(ModEffects.PHD_FLOPPER.get() )){
            return;
        }
        if (event.getDistance() >= 3 && event.getEntity().hasEffect(ModEffects.PHD_FLOPPER.get())){
            float damageMultiplier = event.getDistance();
            int particleCount = Mth.clamp(Mth.floor(damageMultiplier),3,20);
            int particleRadius = Mth.clamp(Mth.floor(damageMultiplier),3,7);
            event.setDamageMultiplier(0);
            if(damageMultiplier<= 6){
                entity.level().addParticle(ModParticles.PHD_EXPLOSION_PARTICLE_SMALL.get(),true,entity.getX(),entity.getY(),entity.getZ(),
                        0,0,0);
            }else if(damageMultiplier> 6 && damageMultiplier<=17){
                entity.level().addParticle(ModParticles.PHD_EXPLOSION_PARTICLE.get(),true,entity.getX(),entity.getY(),entity.getZ(),
                        0,0,0);
            }else {
                entity.level().addParticle(ModParticles.PHD_EXPLOSION_PARTICLE_BIG.get(),true,entity.getX(),entity.getY(),entity.getZ(),
                        0,0,0);
            }
            for (int i = 0; i<particleCount; i++){
                int randNumber = random.nextInt(-particleRadius,particleRadius);
                int randNumber1 = random.nextInt(-particleRadius,particleRadius);
                entity.level().addParticle(ParticleTypes.EXPLOSION.getType(),entity.getX()+randNumber1,entity.getY()+0.25,
                        entity.getZ()+randNumber, 0,0,0);
            }
            if(entity.level().isClientSide()){
                    event.setCanceled(true);
            }else if(!entity.level().isClientSide()){
                double radius = Mth.clamp(damageMultiplier,2.5,6);
                List<Entity> entities = ModUtils.getEntitiesInRadius(entity.level(), entity.getX(), entity.getY(), entity.getZ(), radius);
                entity.level().playSound(null, entity.blockPosition(),SoundEvents.DRAGON_FIREBALL_EXPLODE,SoundSource.BLOCKS,
                        1.0f,1.3f);
                Vec3 vec3 = new Vec3(entity.getX(),entity.getY(),entity.getZ());
                for(Entity entity1 : entities){
                    if(entity1.is(entity)) continue;
                    double damageAmount = Mth.clamp(5*damageMultiplier,14, 100);
                    ModUtils.doCustomExplosionDamage(vec3,radius,entity1,entity,damageAmount);
                }
                event.setDistance(0);
            }
        }
    }

    @SubscribeEvent
    public static void onPhdExplosion(LivingDamageEvent event){
        LivingEntity entity = event.getEntity();
        if((event.getSource().is(DamageTypes.EXPLOSION))&& entity.hasEffect(ModEffects.PHD_FLOPPER.get())){
            event.setAmount(0);
            event.setCanceled(true);
        }
        if(!(event.getEntity() == null) && entity.hasEffect(ModEffects.PHD_FLOPPER.get())) {
            if (event.getSource().getEntity() instanceof Creeper) {
                event.setAmount(0);
                event.setCanceled(true);
            }
        }
    }
}
