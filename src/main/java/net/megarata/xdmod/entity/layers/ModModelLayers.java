package net.megarata.xdmod.entity.layers;

import net.megarata.xdmod.XdMod;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.resources.ResourceLocation;

public class ModModelLayers {
    public static final ModelLayerLocation RAYGUN_PROJECTILE_LAYER = new ModelLayerLocation(
            new ResourceLocation(XdMod.MODID, "ray_gun_projectile_layer"), "ray_gun_projectile_layer");
    public static final ModelLayerLocation PHD_EXPLOSION_LAYER = new ModelLayerLocation(
            new ResourceLocation(XdMod.MODID, "phd_explosion_layer"), "phd_explosion_layer");

}
