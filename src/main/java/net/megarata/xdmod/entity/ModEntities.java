package net.megarata.xdmod.entity;


import net.megarata.xdmod.XdMod;
import net.megarata.xdmod.entity.custom.RayGunProjectileEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.levelgen.Column;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, XdMod.MODID);

    public static final RegistryObject<EntityType<RayGunProjectileEntity>> RAY_GUN_PROJECTILE =
            ENTITY_TYPES.register("ray_gun_projectile",
                    ()-> EntityType.Builder.<RayGunProjectileEntity>of(RayGunProjectileEntity::new, MobCategory.MISC)
                            .sized(0.5f,0.5f)
                            .clientTrackingRange(4)
                            .updateInterval(20)
                            .build("ray_gun_projectile"));


    public static void register(IEventBus eventBus){ENTITY_TYPES.register(eventBus);}

}
