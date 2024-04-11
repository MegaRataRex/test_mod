package net.megarata.xdmod.particle.custom;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.megarata.xdmod.entity.layers.ModModelLayers;
import net.megarata.xdmod.particle.client.PhdSliderExplosionModel;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.LayerDefinitions;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.network.protocol.game.ClientboundMoveEntityPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.extensions.IForgeBakedModel;
import net.minecraftforge.client.model.renderable.BakedModelRenderable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.core.animation.*;
import software.bernie.geckolib.core.object.PlayState;

public class PhdSliderExplosionParticle extends Particle {

    private static final ResourceLocation TEXTURE = new ResourceLocation("xdmod:textures/particle/phd_explosion_texture.png");
    PhdSliderExplosionModel mushroom;
    protected float scale;




    protected PhdSliderExplosionParticle(ClientLevel pLevel, double pX, double pY, double pZ) {
        super(pLevel, pX, pY, pZ);
        this.mushroom = new PhdSliderExplosionModel(Minecraft.getInstance().getEntityModels().
                bakeLayer(ModModelLayers.PHD_EXPLOSION_LAYER));
        this.lifetime = 25;
        this.scale = 1.00f;

        this.setSize(this.scale*3,this.scale*3);
    }
    @Override
    public void render(VertexConsumer pBuffer, Camera pRenderInfo, float pPartialTicks) {
        float offset = Mth.clamp((age+pPartialTicks)/3f,0.4f,2.3f) * this.scale;
        offset = offset >= 1.3f ? Mth.lerp((age+pPartialTicks)/25f,2.3f,2.5f) * scale : offset;
        PoseStack poseStack = getPoseStackFromCamera(pRenderInfo, pPartialTicks,offset);
        poseStack.pushPose();
        poseStack.scale(offset,-offset,offset);


        MultiBufferSource.BufferSource multiBufferSource = Minecraft.getInstance().renderBuffers().bufferSource();
        VertexConsumer vertexConsumer = multiBufferSource.getBuffer(RenderType.entityTranslucent(TEXTURE));
        mushroom.renderToBuffer(poseStack,vertexConsumer,getLightColor(pPartialTicks), OverlayTexture.NO_OVERLAY
        ,1.0f,1.0f,1.0f,alpha);
        multiBufferSource.endBatch();
        poseStack.popPose();
    }

    @Override
    public void tick() {
        super.tick();
        if (this.age >= this.lifetime) {
            this.remove();
        } else {
            this.alpha -= 0.04;
        }
    }

    private PoseStack getPoseStackFromCamera(Camera camera, float tickDelta, float offset){
        Vec3 vec3 = camera.getPosition();
        float x = (float) (Mth.lerp(tickDelta,this.xo,this.x) - vec3.x);
        float y = (float) (Mth.lerp(tickDelta,this.yo,this.y) - vec3.y);
        float z = (float) (Mth.lerp(tickDelta,this.zo,this.z) - vec3.z);

        PoseStack poseStack = new PoseStack();
        poseStack.translate(x,y+(1.375*offset),z);
        return poseStack;
    }

    @Override
    protected int getLightColor(float pPartialTick) {
        return 240;
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.CUSTOM;
    }

    public static class Provider implements ParticleProvider<SimpleParticleType> {
        public Provider(){

        }
        @Override
        public Particle createParticle(SimpleParticleType particleType,ClientLevel level, double pX, double pY, double pZ,
                                       double pXSpeed, double pYSpeed, double pZSpeed){
            return new PhdSliderExplosionParticle(level, pX, pY,pZ);
        }
    }
}
