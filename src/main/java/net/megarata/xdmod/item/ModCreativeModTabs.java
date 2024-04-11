package net.megarata.xdmod.item;

import net.megarata.xdmod.XdMod;
import net.megarata.xdmod.block.ModBlocks;
import net.megarata.xdmod.sound.ModSounds;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ModCreativeModTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, XdMod.MODID);

    public static final RegistryObject<CreativeModeTab> TUTORIAL_TAB = CREATIVE_MODE_TABS.register("tutorial_tab",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(ModItems.WEEZER.get()))
                    .title(Component.translatable("creativetab.tutorial_tab"))
                    .displayItems((pParameters, pOutput) -> {
                        pOutput.accept(ModItems.WEEZER.get());
                        pOutput.accept(ModItems.QSTONEAGE.get());
                        pOutput.accept(ModItems.STRONGER_THAN_YOU_DISC.get());
                        pOutput.accept(ModItems.RAY_GUN.get());
                        pOutput.accept(ModBlocks.MYSTERY_BOX_BLOCK.get());
                        pOutput.accept(ModItems.ELEMENT_115.get());
                        pOutput.accept(ModBlocks.ELEMENT_115_ORE.get());
                        pOutput.accept(ModBlocks.ELEMENT_115_DEEPSLATE_ORE.get());
                        pOutput.accept(ModItems.PHD_SLIDER.get());
                        pOutput.accept(ModItems.JUGGERNOG.get());
                        pOutput.accept(ModItems.EMPTY_PERK_BOTTLE.get());
                    })
                    .build());


    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }
}
