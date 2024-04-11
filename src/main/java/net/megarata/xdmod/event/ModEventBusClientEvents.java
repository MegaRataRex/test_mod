package net.megarata.xdmod.event;

import net.megarata.xdmod.entity.client.RayGunProjectileModel;
import net.megarata.xdmod.entity.layers.ModModelLayers;
import net.megarata.xdmod.XdMod;
import net.megarata.xdmod.particle.ModParticles;
import net.megarata.xdmod.particle.client.PhdSliderExplosionModel;
import net.megarata.xdmod.particle.custom.*;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.entity.SpawnPlacementRegisterEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.rmi.registry.Registry;

@Mod.EventBusSubscriber(modid = XdMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ModEventBusClientEvents {

    @SubscribeEvent
    public static void registerParticleFactories(RegisterParticleProvidersEvent event){
        event.registerSpriteSet(ModParticles.RAY_GUN_PARTICLES.get(), RayGunParticles.Provider::new);
        event.registerSpriteSet(ModParticles.RAY_GUN_TRAVEL_PARTICLES.get(),
                RayGunTravelParticles.Provider::new);
        event.registerSpecial(ModParticles.PHD_EXPLOSION_PARTICLE.get(), new PhdSliderExplosionParticle.Provider());
        event.registerSpecial(ModParticles.PHD_EXPLOSION_PARTICLE_SMALL.get(), new PhdSliderParticleSmall.Provider());
        event.registerSpecial(ModParticles.PHD_EXPLOSION_PARTICLE_BIG.get(), new PhdSliderParticleBig.Provider());

    }
}