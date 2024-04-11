package net.megarata.xdmod.recipe;

import net.megarata.xdmod.XdMod;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import javax.swing.plaf.PanelUI;

public class ModRecipes {
    public static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS =
            DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, XdMod.MODID);

    public static final RegistryObject<RecipeSerializer<MysteryBoxRecipe>> MYSTERY_BOX_SERIALIZER =
            SERIALIZERS.register("mystery_box",() -> MysteryBoxRecipe.Serializer.INSTANCE);

    public static void register(IEventBus eventBus){
        SERIALIZERS.register(eventBus);
    }
}

