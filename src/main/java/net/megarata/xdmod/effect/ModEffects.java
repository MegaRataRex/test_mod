package net.megarata.xdmod.effect;

import jdk.jshell.execution.FailOverExecutionControlProvider;
import net.megarata.xdmod.XdMod;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import javax.print.attribute.Attribute;

public class ModEffects {
    public static final DeferredRegister<MobEffect> MOB_EFFECTS = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS,
            XdMod.MODID);

    public static final RegistryObject<MobEffect> PHD_FLOPPER = MOB_EFFECTS.register("phd", () -> new
            PhdFlopperEffect(MobEffectCategory.BENEFICIAL, 0xb300e4));

    public static final  RegistryObject<MobEffect> JUGGERNOG = MOB_EFFECTS.register("juggernog", ()-> new
            JuggernogEffect(MobEffectCategory.BENEFICIAL, 0xd10006).addAttributeModifier(Attributes.MAX_HEALTH,
            "7107DE5E-7CE8-4030-940E-514C1F160890",10.00D, AttributeModifier.Operation.ADDITION).addAttributeModifier
            (Attributes.ARMOR,"7107DE5E-7CE8-4030-940E-514C1F160890",4, AttributeModifier.Operation.ADDITION).
            addAttributeModifier(Attributes.ARMOR_TOUGHNESS,"7107DE5E-7CE8-4030-940E-514C1F160890",1,
                    AttributeModifier.Operation.ADDITION));

    public static void register(IEventBus eventBus){
        MOB_EFFECTS.register(eventBus);
    }
}
