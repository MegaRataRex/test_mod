package net.megarata.xdmod.block;

import net.megarata.xdmod.XdMod;
import net.megarata.xdmod.block.custom.Element115OreBlock;
import net.megarata.xdmod.block.custom.MysteryBoxBlock;
import net.megarata.xdmod.item.ModItems;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DropExperienceBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;
import java.util.function.ToIntFunction;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(ForgeRegistries.BLOCKS, XdMod.MODID);

    public static final RegistryObject<Block> MYSTERY_BOX_BLOCK = registerBlock("mystery_box",
            () -> new MysteryBoxBlock(BlockBehaviour.Properties.copy(Blocks.CHEST).noOcclusion()
                    .lightLevel(state -> state.getValue(MysteryBoxBlock.USING) ? 10 : 3)));


    public static final RegistryObject<Block> ELEMENT_115_ORE = registerBlock("element_115_ore",
            () -> new Element115OreBlock(BlockBehaviour.Properties.copy(Blocks.STONE).strength(2.5f).
                    lightLevel(state -> state.getValue(Element115OreBlock.EMIT_LIGHT) ? 8 : 0).
                    requiresCorrectToolForDrops(),UniformInt.of(3,7)));

    public static final RegistryObject<Block> ELEMENT_115_DEEPSLATE_ORE = registerBlock("element_115_deepslate_ore",
            () -> new Element115OreBlock(BlockBehaviour.Properties.copy(Blocks.DEEPSLATE).sound(SoundType.DEEPSLATE).strength(3.5f,6.0f)
                    .lightLevel(state -> state.getValue(Element115OreBlock.EMIT_LIGHT) ? 8 : 0).
                    requiresCorrectToolForDrops(),UniformInt.of(3,7)));

    private static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block) {
        RegistryObject<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }

    private static <T extends Block> RegistryObject<Item> registerBlockItem(String name, RegistryObject<T> block) {
        return ModItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}
