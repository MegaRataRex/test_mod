package net.megarata.xdmod.mixin;


import net.megarata.xdmod.effect.ModEffects;
import net.megarata.xdmod.network.ModMessages;
import net.megarata.xdmod.network.packet.SlideC2SPacket;
import net.megarata.xdmod.particle.ModParticles;
import net.minecraft.core.GlobalPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import org.checkerframework.checker.units.qual.A;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;

@Mixin(Player.class)
public abstract class PlayerMixin {


    private int explodeCooldown = 60;

    private Player self(){
        return (Player) ((Object)this);
    }

    @Inject(method = "tick",at = @At("TAIL"))
    private void tick_tail(CallbackInfo ci){
        if(explodeCooldown <= 60){
            explodeCooldown ++;
        }
    }

    @Inject(method = "touch",at = @At("HEAD"))
    private void touch_head(Entity pEntity, CallbackInfo ci){
        if(explodeCooldown >= 60){
            if(self().hasEffect(ModEffects.PHD_FLOPPER.get()) && (self().getDeltaMovement().x > 0.24  || self().getDeltaMovement().z > 0.24)){
                self().level().addParticle(ModParticles.PHD_EXPLOSION_PARTICLE.get(), true, self().getX(), self().getY(), self().getZ(),
                        0, 0, 0);
                ModMessages.Send2Server(new SlideC2SPacket());
                explodeCooldown = 0;
            }
        }
    }
}
