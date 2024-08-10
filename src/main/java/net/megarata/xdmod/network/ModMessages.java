package net.megarata.xdmod.network;

import net.megarata.xdmod.XdMod;
import net.megarata.xdmod.network.packet.SlideC2SPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

public class ModMessages {
    private static ResourceLocation SLIDE_ID = new ResourceLocation(XdMod.MODID,"sliding");
    private static final String PROTOCOL_VERSION = "1";
    private static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
            SLIDE_ID, () -> PROTOCOL_VERSION, PROTOCOL_VERSION::equals, PROTOCOL_VERSION ::equals);

    public static void registerMessages(){
        INSTANCE.messageBuilder(SlideC2SPacket.class, 1, NetworkDirection.PLAY_TO_SERVER)
                .encoder(SlideC2SPacket::encode)
                .decoder(SlideC2SPacket::new)
                .consumerMainThread(SlideC2SPacket::handle)
                .add();
    }

    public static void Send2Server(Object msg){
        INSTANCE.send(PacketDistributor.SERVER.noArg(),msg);
    }


    public static void Server2Client(Object msg){
        INSTANCE.send(PacketDistributor.ALL.noArg(),msg);
    }

}
