package net.megarata.xdmod;

import com.mojang.logging.LogUtils;
import net.megarata.xdmod.block.ModBlocks;
import net.megarata.xdmod.block.entity.ModBlockEntities;
import net.megarata.xdmod.effect.ModEffects;
import net.megarata.xdmod.entity.ModEntities;
import net.megarata.xdmod.entity.client.MysteryBoxRenderer;
import net.megarata.xdmod.entity.client.RayGunProjectileRenderer;
import net.megarata.xdmod.particle.ModParticles;
import net.megarata.xdmod.sound.ModSounds;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.util.Mth;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;
import net.megarata.xdmod.item.ModCreativeModTabs;
import net.megarata.xdmod.item.ModItems;
import software.bernie.geckolib.GeckoLib;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(XdMod.MODID)
public class XdMod
{
    // Define mod id in a common place for everything to reference
    public static final String MODID = "xdmod";
    // Directly reference a slf4j logger
    private static final Logger LOGGER = LogUtils.getLogger();

    public static HumanoidModel.ArmPose RAY_GUN_HOLD;

    public XdMod()
    {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        ModCreativeModTabs.register(modEventBus);
        ModItems.register(modEventBus);
        ModSounds.register(modEventBus);
        ModParticles.register(modEventBus);
        ModEntities.register(modEventBus);
        ModBlocks.register(modEventBus);
        ModBlockEntities.register(modEventBus);
        ModEffects.register(modEventBus);
        GeckoLib.initialize();
        modEventBus.addListener(this::commonSetup);
        MinecraftForge.EVENT_BUS.register(this);
        modEventBus.addListener(this::addCreative);




    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {
    }

    // Add the example block item to the building blocks tab
    private void addCreative(BuildCreativeModeTabContentsEvent event)
    {

    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event)
    {
    }

    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents
    {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event)
        {

            EntityRenderers.register(ModEntities.RAY_GUN_PROJECTILE.get(), RayGunProjectileRenderer::new);
            BlockEntityRenderers.register(ModBlockEntities.MYSTERY_BOX_BE.get(), MysteryBoxRenderer::new);
            event.enqueueWork(() -> {
                RAY_GUN_HOLD = HumanoidModel.ArmPose.create("ICON_HOLD", false, (model, entity, arm) -> {
                    if (arm.getId() == 0) {
                        model.leftArm.xRot = Mth.DEG_TO_RAD * -90;
                        model.leftArm.yRot = Mth.DEG_TO_RAD * 10;
                        model.leftArm.zRot = Mth.DEG_TO_RAD * -10;
                    } else {
                        model.rightArm.xRot = Mth.DEG_TO_RAD * -90;
                        model.rightArm.yRot = Mth.DEG_TO_RAD * -10;
                        model.rightArm.zRot = Mth.DEG_TO_RAD * 10;
                    }
                });
            });
        }
    }
}
