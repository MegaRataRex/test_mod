package net.megarata.xdmod.util;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.ExplosionDamageCalculator;
import net.minecraft.world.level.block.state.BlockState;

public class ModDamageCalculator extends ExplosionDamageCalculator {


    @Override
    public boolean shouldBlockExplode(Explosion pExplosion, BlockGetter pReader, BlockPos pPos, BlockState pState, float pPower) {
        return false;
    }
}
