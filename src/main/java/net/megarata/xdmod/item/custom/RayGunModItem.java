package net.megarata.xdmod.item.custom;

import net.megarata.xdmod.XdMod;
import net.megarata.xdmod.entity.custom.RayGunProjectileEntity;
import net.megarata.xdmod.item.ModItems;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.megarata.xdmod.sound.ModSounds;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public class RayGunModItem extends Item {

    public RayGunModItem(Properties pProperties) {
        super(pProperties);
    }



    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {

        ItemStack itemstack = pPlayer.getItemInHand(pUsedHand);
        pLevel.playSound(null, pPlayer.getX(), pPlayer.getY(), pPlayer.getZ(), ModSounds.RAYGUN_SHOOT.get(), SoundSource.NEUTRAL,
                1.3F, 1F);
        pLevel.playSound(null, pPlayer.getX(), pPlayer.getY(), pPlayer.getZ(), ModSounds.RAYGUN_BEAM.get(), SoundSource.NEUTRAL,
                0.2f, 1F);
        pPlayer.getCooldowns().addCooldown(this, 8);

        if(!pLevel.isClientSide()) {
            RayGunProjectileEntity rayGunProjectile = new RayGunProjectileEntity(pLevel, pPlayer);
            rayGunProjectile.shootFromRotation(pPlayer, pPlayer.getXRot(), pPlayer.getYRot(), 0.0F, 1.5F, 0.25F);
            pLevel.addFreshEntity(rayGunProjectile);
        }

        pPlayer.awardStat(Stats.ITEM_USED.get(this));
        if (!pPlayer.getAbilities().instabuild) {
            itemstack.hurtAndBreak(1, pPlayer, p -> p.broadcastBreakEvent(pUsedHand));

        }

        return InteractionResultHolder.consume(itemstack);

    }

    @Override
    public UseAnim getUseAnimation(ItemStack pStack) {
        return UseAnim.NONE;

    }


    //author
    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            @Override
            public HumanoidModel.@Nullable ArmPose getArmPose(LivingEntity entityLiving, InteractionHand hand, ItemStack itemStack) {
                return XdMod.RAY_GUN_HOLD;
            }
        });
    }

    public int getUseDuration(ItemStack stack) {
        return 72000;
    }



}
