package net.megarata.xdmod.block.custom;

import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DropExperienceBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;

public class Element115OreBlock extends DropExperienceBlock {

    public static BooleanProperty EMIT_LIGHT = BooleanProperty.create("emit_light");
    public Element115OreBlock(Properties pProperties, UniformInt uniformInt) {
        super(pProperties,uniformInt);
        this.registerDefaultState(this.getStateDefinition().any().setValue(EMIT_LIGHT, true));
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(EMIT_LIGHT);;
    }


}
