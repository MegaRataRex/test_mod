package net.megarata.xdmod.network.packet;

import dev.kosmx.playerAnim.core.util.Vec3f;
import net.megarata.xdmod.effect.ModEffects;
import net.megarata.xdmod.util.ModDamageCalculator;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkEvent;
import org.joml.Vector3f;

import java.util.function.Supplier;

public class SlideC2SPacket {


    public SlideC2SPacket() {
    }

    public SlideC2SPacket(FriendlyByteBuf buf) {
    }

    public void encode(FriendlyByteBuf buf){
    }

    public void handle(Supplier<NetworkEvent.Context> contextSupplier) {
        contextSupplier.get().enqueueWork(()-> {
            ServerPlayer sender = contextSupplier.get().getSender();
            ServerLevel level = sender.serverLevel();
            level.playSound(null, sender.blockPosition(), SoundEvents.DRAGON_FIREBALL_EXPLODE, SoundSource.BLOCKS,
                    1.0f, 1.3f);
            level.explode(sender, null, new ModDamageCalculator(),
                    sender.getX(),sender.getY(),sender.getZ(),4f,false, Level.ExplosionInteraction.MOB,true);
        });
        contextSupplier.get().setPacketHandled(true);
    }


}
