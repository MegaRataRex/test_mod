package net.megarata.xdmod.util;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import net.megarata.xdmod.block.custom.MysteryBoxBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageSources;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.item.enchantment.ProtectionEnchantment;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class ModUtils {

    static Logger logger = LogManager.getLogger();
    public static ImmutableMap<BlockState, VoxelShape> generateShapeForEach(ImmutableList<BlockState> states) {
        ImmutableMap.Builder<BlockState, VoxelShape> builder = new ImmutableMap.Builder<>();

        for (BlockState state : states) {
            VoxelShape shape;
            Direction direction = state.getValue(MysteryBoxBlock.FACING);
            MysteryBoxBlock.MysteryBoxPart part = state.getValue(MysteryBoxBlock.PART);
            boolean isRight = part == MysteryBoxBlock.MysteryBoxPart.RIGHT;
            boolean isNorthSouth = direction == Direction.NORTH || direction == Direction.SOUTH;
            VoxelShape baseShape;
            if (isNorthSouth) {
                baseShape = Block.box(-16, 0, 0, 32, 13, 16); // Base shape for north-south direction
            } else {
                baseShape = Block.box(0, 0, -16, 16, 13, 32); // Base shape for east-west direction
            }
            if (!part.equals(MysteryBoxBlock.MysteryBoxPart.MIDDLE)) {
                switch (direction) {
                    case NORTH:
                        baseShape = isRight ? Block.box(0, 0, 0, 48, 13, 16) :
                                Block.box(-32, 0, 0, 16, 13, 16);
                        break;
                    case SOUTH:
                        baseShape = isRight ? Block.box(-32, 0, 0, 16, 13, 16) :
                                Block.box(0, 0, 0, 48, 13, 16);
                        break;
                    case EAST:
                        baseShape = isRight ? Block.box(0, 0, 0, 16, 13, 48) :
                                Block.box(0, 0, -32, 16, 13, 16);
                        break;
                    case WEST:
                        baseShape = isRight ? Block.box(0, 0, -32, 16, 13, 16) :
                                Block.box(0, 0, 0, 16, 13, 48);
                        break;
                }
            }
            builder.put(state, baseShape);
        }
        return builder.build();
    }

    public static List<BlockPos> getNeighboringBlocks(BlockState pState, BlockPos pPos){
        BlockPos newPos = new BlockPos(pPos.getX(),pPos.getY(),pPos.getZ());
        MysteryBoxBlock.MysteryBoxPart part = pState.getValue(MysteryBoxBlock.PART);
        Direction direction = pState.getValue(MysteryBoxBlock.FACING);
        List<BlockPos> blockPosList = new ArrayList<>();
        switch (part) {
            case LEFT:
                blockPosList.add(newPos.relative(direction.getCounterClockWise()));
                break;
            case MIDDLE:
                blockPosList.add(newPos.relative(direction.getCounterClockWise()));
                blockPosList.add(newPos.relative(direction.getClockWise()));
                break;
            case RIGHT:
                blockPosList.add(newPos.relative(direction.getClockWise()));
                break;
            default:
                break;
        }
        return blockPosList;
    }

    public static List<Entity> getEntitiesInRadius(Level world, double x, double y, double z, double radius) {
        AABB boundingBox = new AABB(x - radius, y - radius, z - radius, x + radius, y + radius, z + radius);
        List<Entity> nearbyEntities = world.getEntities(null,boundingBox);
        return nearbyEntities;
    }

    public static void doCustomExplosionDamage(Vec3 vec3, double radius, Entity entity, @Nullable Entity causingEntity, double amount){
        double normalizedVector = Math.sqrt(entity.distanceToSqr(vec3))/ radius;
        logger.debug(normalizedVector);
        if(normalizedVector<=1.00){
            double dx = entity.getX() - vec3.get(Direction.Axis.X);
            double dy = (entity instanceof PrimedTnt ? entity.getY() : entity.getEyeY())-vec3.get(Direction.Axis.Y);
            double dz = entity.getZ() - vec3.get(Direction.Axis.Z);
            double distanceSquared = dx*dx + dy*dy + dz*dz;
            if (distanceSquared > 0){
                distanceSquared = Math.sqrt(distanceSquared);
                dx/=distanceSquared;
                dy/=distanceSquared;
                dz/=distanceSquared;
                double seen = getSeenPercent(vec3,entity);
                logger.debug(seen);
                double dmgMultiplier = (1.0-Mth.clamp(normalizedVector,0,0.6)) * seen;;
                double damage = amount * dmgMultiplier;
                logger.debug(damage);
                double knockback;
                entity.hurt(entity.damageSources().explosion(causingEntity,causingEntity),(float) damage);
                if (entity instanceof LivingEntity livingEntity) {
                    knockback = ProtectionEnchantment.getExplosionKnockbackAfterDampener(livingEntity,dmgMultiplier);
                    logger.debug(knockback);
                } else {
                    knockback = dmgMultiplier;
                }
                dx *= knockback;
                dy *= knockback;
                dz *= knockback;
                Vec3 vec31 = new Vec3(dx,dy,dz);
                logger.debug(vec31);
                entity.setDeltaMovement(entity.getDeltaMovement().add(vec31));
            }
        }
    }

    private static double getSeenPercent(Vec3 vec3, Entity entity){
        Vec3 entityPos = new Vec3(entity.getX(),entity.getY(),entity.getZ());
        Vec3 direction = entityPos.subtract(vec3).normalize();

        ClipContext clipContext = new ClipContext(vec3,entityPos, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE,entity);

        BlockHitResult result = entity.level().clip(clipContext);

        if(result.getType() == HitResult.Type.BLOCK){
            double distance = vec3.distanceTo(entityPos);
            double distanceObstacle = vec3.distanceTo(result.getLocation());
            double percent = distanceObstacle / distance;
            return  Math.min(1.0,Math.max(0.0,1.0-percent));
        }
        return 1.0;
    }
}
