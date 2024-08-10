package net.megarata.xdmod.mixin;
import net.megarata.xdmod.client.SlideBehaviour;
import net.megarata.xdmod.client.SlideBehaviourExtension;
import net.megarata.xdmod.client.anim.IPlaySlideAnim;
import net.megarata.xdmod.effect.ModEffects;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.swing.text.html.parser.Entity;
import java.util.logging.Level;

@Mixin(LocalPlayer.class)
public abstract class LocalPlayerMixin implements SlideBehaviourExtension {


    @Shadow public abstract boolean isShiftKeyDown();
    @Shadow private boolean crouching;

    @Override
    public SlideBehaviour getSlideBehaviour() {
        return new SlideBehaviour();
    }

    private SlideBehaviour slideBehaviour = getSlideBehaviour();
    private AbstractClientPlayer player = (AbstractClientPlayer) ((Object)this);
    private IPlaySlideAnim playSlideAnim = (IPlaySlideAnim) (player);


    @Inject(method = "isMovingSlowly", at = @At("RETURN"), cancellable = true)
    public void isMovingSlowly_head(CallbackInfoReturnable<Boolean> cir){
        if(slideBehaviour.isSliding()){
            cir.setReturnValue(false);
        }
    }

    @Inject(method = "tick",at = @At("TAIL"))
    public void tick_tail(CallbackInfo ci){
        slideBehaviour.tick();
        if(player != null && player.isSprinting()){
            slideBehaviour.sprintTick();
        }
        if(player != null && slideBehaviour.timeSinceLastSprint < 3 && isShiftKeyDown()){
            trySliding();
        }
    }

    public void trySliding() {
        if (!slideBehaviour.isSlideAvailable(player)) {
            return;
        }
        if (player.getVehicle() != null) {
            return;
        }
        if (player.isUsingItem() || player.isBlocking()) {
            return;
        }
        if (player.isInWater()) {
            return;
        }
        if(player.isFallFlying()){
            return;
        }
        if(!player.onGround()){
            return;
        }
        if(slideBehaviour.isSlideAvailable(player)) {
            Vec3 direction;
            direction = player.getDeltaMovement().normalize();
            float distance = 2.1f;
            if(player.hasEffect(ModEffects.PHD_FLOPPER.get())){
                distance = 3.5f;
            }
            direction = direction.scale(distance);
            player.addDeltaMovement(direction);

            slideBehaviour.onSlide();
            playSlideAnim.playSlideAnim(direction);
        }

    }


    @Inject(method = "isCrouching",at = @At("RETURN"),cancellable = true)
    public void isCrouching_return(CallbackInfoReturnable<Boolean> cir){
        cir.setReturnValue(this.crouching && !slideBehaviour.isSliding());
    }
}
