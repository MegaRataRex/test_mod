package net.megarata.xdmod.item;

import net.megarata.xdmod.XdMod;
import net.megarata.xdmod.effect.ModEffects;
import net.megarata.xdmod.item.custom.PerkItem;
import net.megarata.xdmod.item.custom.RayGunModItem;
import net.megarata.xdmod.sound.ModSounds;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.RecordItem;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, XdMod.MODID);

    public static final RegistryObject<Item> WEEZER = ITEMS.register("weezer",
            () -> new RecordItem(6, ModSounds.BUDDY_HOLLY, new Item.Properties().stacksTo(1), 3140));

    public static final RegistryObject<Item> QSTONEAGE = ITEMS.register("go_flow_disc",
            () -> new RecordItem(6, ModSounds.GO_FLOW, new Item.Properties().stacksTo(1), 3760));

    public static final RegistryObject<Item> STRONGER_THAN_YOU_DISC = ITEMS.register("stronger_than_you_disc",
            () -> new RecordItem(6, ModSounds.SHADOW_FREDDY, new Item.Properties().stacksTo(1), 3440));
    public static final RegistryObject<Item> RAY_GUN = ITEMS.register("ray_gun",
            () -> new RayGunModItem( new Item.Properties().stacksTo(1)));
    public  static  final RegistryObject<Item> ELEMENT_115 = ITEMS.register("element_115",
            ()-> new Item(new Item.Properties()));

    public static final RegistryObject <Item> PHD_SLIDER = ITEMS.register("phd_slider_bottle", () -> new PerkItem(new Item.Properties().
            stacksTo(1), ModEffects.PHD_FLOPPER.get()));

    public static final RegistryObject <Item> JUGGERNOG = ITEMS.register("juggernog_bottle", () -> new PerkItem(new Item.Properties().
            stacksTo(1), ModEffects.JUGGERNOG.get()));


    public  static  final RegistryObject<Item> EMPTY_PERK_BOTTLE = ITEMS.register("perk_bottle",
            ()-> new Item(new Item.Properties()));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    } 

}
