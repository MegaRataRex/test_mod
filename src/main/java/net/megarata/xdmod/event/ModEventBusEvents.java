package net.megarata.xdmod.event;

import net.megarata.xdmod.XdMod;
import net.megarata.xdmod.entity.client.RayGunProjectileModel;
import net.megarata.xdmod.entity.layers.ModModelLayers;
import net.megarata.xdmod.particle.client.PhdSliderExplosionModel;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = XdMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEventBusEvents {
    @SubscribeEvent
    public static void registerLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(ModModelLayers.RAYGUN_PROJECTILE_LAYER, RayGunProjectileModel::createBodyLayer);
        event.registerLayerDefinition(ModModelLayers.PHD_EXPLOSION_LAYER, PhdSliderExplosionModel::createBodyLayer);
    }



}
