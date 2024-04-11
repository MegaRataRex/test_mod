package net.megarata.xdmod.particle.custom;

import net.minecraft.client.multiplayer.ClientLevel;

public class PhdSliderParticleSmall extends PhdSliderExplosionParticle{

    protected PhdSliderParticleSmall(ClientLevel pLevel, double pX, double pY, double pZ) {
        super(pLevel, pX, pY, pZ);
        this.scale = 0.5f;
    }
}
