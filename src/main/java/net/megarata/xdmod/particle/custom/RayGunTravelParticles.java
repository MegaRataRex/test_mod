package net.megarata.xdmod.particle.custom;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class RayGunTravelParticles extends TextureSheetParticle {
    private final SpriteSet spriteSet;

    protected RayGunTravelParticles(ClientLevel pLevel, double pX, double pY, double pZ,
                              SpriteSet spriteSet, double pXSpeed, double pYSpeed, double pZSpeed) {
        super(pLevel, pX, pY, pZ, pXSpeed, pYSpeed, pZSpeed);

        this.friction = 0.8f;
        this.xd = pXSpeed;
        this.yd = pYSpeed;
        this.zd = pZSpeed;
        this.spriteSet = spriteSet;


        this.quadSize *= 1.5f;
        this.lifetime = 25;
        this.setSpriteFromAge(spriteSet);
        this.alpha = 1.0f;

        this.rCol = 1f;
        this.gCol = 1f;
        this.bCol = 1f;

    }

    @Override
    public void tick() {
        super.tick();
        if (this.age >= this.lifetime) {
            this.remove();
        } else {
            this.alpha -= 0.04;
            this.scale(1.1f);
        }
    }

    @Override
    protected int getLightColor(float pPartialTick) {
        return 240;
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }



    public static class Provider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet spriteSet;
        public  Provider(SpriteSet spriteSet){
            this.spriteSet = spriteSet;

        }
        public Particle createParticle(SimpleParticleType particleType,ClientLevel level, double pX, double pY, double pZ,
                                       double pXSpeed, double pYSpeed, double pZSpeed){
            return new RayGunTravelParticles(level, pX, pY, pZ,this.spriteSet,pXSpeed,pYSpeed,pZSpeed);
        }
    }
}