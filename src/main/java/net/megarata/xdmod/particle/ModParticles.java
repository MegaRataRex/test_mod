package net.megarata.xdmod.particle;

import net.megarata.xdmod.XdMod;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModParticles {

    public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES =
            DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, XdMod.MODID);

    public static final RegistryObject<SimpleParticleType> RAY_GUN_PARTICLES =
            PARTICLE_TYPES.register("ray_gun_particles", () -> new SimpleParticleType(true));
    public static final RegistryObject<SimpleParticleType> RAY_GUN_TRAVEL_PARTICLES =
            PARTICLE_TYPES.register("ray_gun_travel_particles", () -> new SimpleParticleType(true));

    public static final RegistryObject<SimpleParticleType> PHD_EXPLOSION_PARTICLE =
            PARTICLE_TYPES.register("phd_explosion_particle1", () -> new SimpleParticleType(true));

    public static final RegistryObject<SimpleParticleType> PHD_EXPLOSION_PARTICLE_SMALL =
            PARTICLE_TYPES.register("phd_explosion_particle0", () -> new SimpleParticleType(true));

    public static final RegistryObject<SimpleParticleType> PHD_EXPLOSION_PARTICLE_BIG =
            PARTICLE_TYPES.register("phd_explosion_particle2", () -> new SimpleParticleType(true));


    public static void register(IEventBus eventBus){
        PARTICLE_TYPES.register(eventBus);
    }
}
