package net.megarata.xdmod.entity.client;

import net.megarata.xdmod.XdMod;
import net.megarata.xdmod.entity.custom.RayGunProjectileEntity;
import net.megarata.xdmod.entity.layers.ModModelLayers;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fml.common.Mod;

public class RayGunProjectileRenderer extends EntityRenderer<RayGunProjectileEntity> {
    public static final ResourceLocation TEXTURE = new ResourceLocation(XdMod.MODID, "textures/entity/magic_projectile.png");
    protected RayGunProjectileModel model;

    public RayGunProjectileRenderer(EntityRendererProvider.Context pContext) {

        super(pContext);
        model = new RayGunProjectileModel(pContext.bakeLayer(ModModelLayers.RAYGUN_PROJECTILE_LAYER));
        this.shadowRadius = 0.5f;
    }


    @Override
    public ResourceLocation getTextureLocation(RayGunProjectileEntity pEntity) {
        return TEXTURE;
    }


}
