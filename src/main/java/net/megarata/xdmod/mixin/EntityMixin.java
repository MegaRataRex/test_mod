package net.megarata.xdmod.mixin;

import net.megarata.xdmod.effect.ModEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public abstract class EntityMixin {

    Entity entity = (Entity) ((Object)this);

    @Inject(method = "ignoreExplosion",at = @At("HEAD"), cancellable = true)
    public void hurt_head(CallbackInfoReturnable<Boolean> cir){
        if(entity instanceof LivingEntity livingEntity){
            if(livingEntity.hasEffect(ModEffects.PHD_FLOPPER.get())){
                cir.setReturnValue(true);
            }
        }
    }
}
