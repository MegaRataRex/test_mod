package net.megarata.xdmod.mixin;

import com.mojang.authlib.GameProfile;
import dev.kosmx.playerAnim.api.layered.KeyframeAnimationPlayer;
import dev.kosmx.playerAnim.api.layered.ModifierLayer;
import dev.kosmx.playerAnim.api.layered.modifier.AbstractFadeModifier;
import dev.kosmx.playerAnim.core.data.KeyframeAnimation;
import dev.kosmx.playerAnim.core.util.Ease;
import dev.kosmx.playerAnim.impl.IAnimatedPlayer;
import net.megarata.xdmod.client.anim.AnimationRegistry;
import net.megarata.xdmod.client.anim.IPlaySlideAnim;

import net.minecraft.client.multiplayer.ClientLevel;

import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.core.BlockPos;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;

import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(AbstractClientPlayer.class)
public abstract class AbstractClientPlayerMixin extends Player implements IPlaySlideAnim {

    public AbstractClientPlayerMixin(Level pLevel, BlockPos pPos, float pYRot, GameProfile pGameProfile) {
        super(pLevel, pPos, pYRot, pGameProfile);
    }
    private final ModifierLayer base = new ModifierLayer<>(null);

    @Inject(method = "<init>", at = @At("TAIL"))
    private void postInit(ClientLevel world, GameProfile profile, CallbackInfo ci) {
        var stack = ((IAnimatedPlayer) this).getAnimationStack();
        stack.addAnimLayer(1000, base);
    }

    public void playSlideAnim(Vec3 direction) {
        try {
            KeyframeAnimation animation = AnimationRegistry.animations.get("xdmod:emote");
            if(animation == null){
            }
            var copy = animation.mutableCopy();

            var fadeIn = copy.beginTick;
            float length = copy.endTick;
            base.replaceAnimationWithFade(
                    AbstractFadeModifier.standardFadeIn(fadeIn, Ease.INOUTSINE),
                    new KeyframeAnimationPlayer(copy.build(), 0));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
