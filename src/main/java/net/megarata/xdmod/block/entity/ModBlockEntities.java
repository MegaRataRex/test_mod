package net.megarata.xdmod.block.entity;



import net.megarata.xdmod.XdMod;
import net.megarata.xdmod.block.ModBlocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, XdMod.MODID);

    public static final RegistryObject<BlockEntityType<MysteryBoxBlockEntity>> MYSTERY_BOX_BE =
            BLOCK_ENTITIES.register("mystery_box_block_entity", () ->
                    BlockEntityType.Builder.of(MysteryBoxBlockEntity::new,
                            ModBlocks.MYSTERY_BOX_BLOCK.get()).build(null));
    public static void register(IEventBus eventBus){
        BLOCK_ENTITIES.register(eventBus);
    }
}
