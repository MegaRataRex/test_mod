package net.megarata.xdmod.sound;

import net.megarata.xdmod.XdMod;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModSounds {
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS =
            DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, XdMod.MODID);

    public static final RegistryObject<SoundEvent> BUDDY_HOLLY = registerSoundEvents("buddy_holly");

    public static final RegistryObject<SoundEvent> GO_FLOW = registerSoundEvents("go_flow");

    public static final  RegistryObject<SoundEvent> SHADOW_FREDDY = registerSoundEvents("stronger_than_you");

    public static final  RegistryObject<SoundEvent> RAYGUN_SHOOT = registerSoundEvents("ray_gun_shoot_sound");

    public static final  RegistryObject<SoundEvent> RAYGUN_BEAM = registerSoundEvents("ray_gun_beam");

    public static final  RegistryObject<SoundEvent> MYSTERY_BOX_SPIN = registerSoundEvents("mystery_box_spin");

    public  static final RegistryObject<SoundEvent> MYSTERY_BOX_OPEN = registerSoundEvents("mystery_box_open");

    private static RegistryObject<SoundEvent> registerSoundEvents(String name){
        return SOUND_EVENTS.register(name, () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(XdMod.MODID, name)));

    }

    public static void register(IEventBus eventBus) {
        SOUND_EVENTS.register(eventBus);
    }
}
