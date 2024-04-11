package net.megarata.xdmod.particle.custom;

import net.minecraft.client.multiplayer.ClientLevel;

public class PhdSliderParticleBig extends PhdSliderExplosionParticle{
    protected PhdSliderParticleBig(ClientLevel pLevel, double pX, double pY, double pZ) {
        super(pLevel, pX, pY, pZ);
        this.scale = 1.5f;
    }
}
