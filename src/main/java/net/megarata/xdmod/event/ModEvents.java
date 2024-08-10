package net.megarata.xdmod.event;

import net.megarata.xdmod.XdMod;
import net.megarata.xdmod.effect.ModEffects;
import net.megarata.xdmod.particle.ModParticles;
import net.megarata.xdmod.util.ModDamageCalculator;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Random;

@Mod.EventBusSubscriber(modid = XdMod.MODID)
public class ModEvents {

    static int touchCooldown = 60;

    static Logger logger = LogManager.getLogger();

    @SubscribeEvent
    public static void onPhdFall(LivingFallEvent event) {

        LivingEntity entity = event.getEntity();
        Random random = new Random();
        if (entity == null || !entity.hasEffect(ModEffects.PHD_FLOPPER.get())) {
            return;
        }
        if (event.getDistance() >= 3 && event.getEntity().hasEffect(ModEffects.PHD_FLOPPER.get())) {
            float damageMultiplier = event.getDistance();
            int particleCount = Mth.clamp(Mth.floor(damageMultiplier), 3, 20);
            int particleRadius = Mth.clamp(Mth.floor(damageMultiplier), 3, 7);
            event.setDamageMultiplier(0);
            entity.level().addParticle(ModParticles.PHD_EXPLOSION_PARTICLE.get(), true, entity.getX(), entity.getY(), entity.getZ(),
                    0, 0, 0);
            for (int i = 0; i < particleCount; i++) {
                int randNumber = random.nextInt(-particleRadius, particleRadius);
                int randNumber1 = random.nextInt(-particleRadius, particleRadius);
                entity.level().addParticle(ParticleTypes.EXPLOSION.getType(), entity.getX() + randNumber1, entity.getY() + 0.25,
                        entity.getZ() + randNumber, 0, 0, 0);
            }
            if (entity.level().isClientSide()) {
                event.setCanceled(true);
            } else if (!entity.level().isClientSide()) {
                double radius = Mth.clamp(damageMultiplier, 2.5, 6);
                entity.level().playSound(null, entity.blockPosition(), SoundEvents.DRAGON_FIREBALL_EXPLODE, SoundSource.BLOCKS,
                        1.0f, 1.3f);
                entity.level().explode(entity, null, new ModDamageCalculator(), entity.getX(), entity.getY(), entity.getZ(), (float) radius, false, Level.ExplosionInteraction.MOB, false);

                event.setDistance(0);
            }
        }
    }
}
