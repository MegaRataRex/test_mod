package net.megarata.xdmod.entity.custom;

import net.megarata.xdmod.entity.ModEntities;
import net.megarata.xdmod.particle.ModParticles;
import net.megarata.xdmod.particle.custom.RayGunParticles;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.ExplosionDamageCalculator;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.phys.*;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.network.NetworkHooks;

import java.util.List;

public class RayGunProjectileEntity extends Projectile {
    private static final EntityDataAccessor<Boolean> HIT =
            SynchedEntityData.defineId(RayGunProjectileEntity.class, EntityDataSerializers.BOOLEAN);
    private int counter = 0;
    public RayGunProjectileEntity(EntityType<? extends Projectile> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    public RayGunProjectileEntity(Level pLevel, Player player) {
        super(ModEntities.RAY_GUN_PROJECTILE.get(), pLevel);
        setOwner(player);
        BlockPos blockPos = player.blockPosition();
        double d0 = (double) blockPos.getX() + 0.5D;
        double d1 = (double) blockPos.getY() + 1.75D;
        double d2 = (double) blockPos.getZ() + 0.5D;
        this.moveTo(d0, d1, d2, this.getYRot(), this.getXRot());

    }

    @Override
    protected void defineSynchedData() {
        this.entityData.define(HIT,false);
    }

    @Override
    public void tick() {
        super.tick();
        if(this.entityData.get(HIT)) {
            if(this.tickCount >= counter) {
                this.discard();
            }
        }

        if (this.tickCount >= 300) {
            this.remove(RemovalReason.DISCARDED);
        }

        Vec3 vec3 = this.getDeltaMovement();
        HitResult hitresult = ProjectileUtil.getHitResultOnMoveVector(this, this::canHitEntity);
        if (hitresult.getType() != HitResult.Type.MISS && !ForgeEventFactory.onProjectileImpact(this, hitresult))
            this.onHit(hitresult);

        double d0 = this.getX() + vec3.x;
        double d1 = this.getY() + vec3.y;
        double d2 = this.getZ() + vec3.z;
        this.updateRotation();

        double d5 = vec3.x;
        double d6 = vec3.y;
        double d7 = vec3.z;

        if(this.tickCount % 3 == 0) {
                this.level().addParticle(ModParticles.RAY_GUN_TRAVEL_PARTICLES.get(), d0 - (d5 * 2), d1 - (d6 * 2), d2 - (d7 * 2),
                        0, 0, 0);
        }

        if (this.level().getBlockStates(this.getBoundingBox()).noneMatch(BlockBehaviour.BlockStateBase::isAir)) {
            this.discard();
        } else if (this.isInWaterOrBubble()) {
            this.discard();
        } else {
            this.setDeltaMovement(vec3.scale(0.99F));
            this.setPos(d0, d1, d2);
        }
    }

    @Override
    protected void onHit(HitResult pResult) {
        super.onHit(pResult);
        this.level().addParticle(ModParticles.RAY_GUN_PARTICLES.get(),this.getX(),this.getY(),this.getZ(),0,0,0);
        List<Entity> entities = getEntitiesInRadius(this.level(),this.getX(),this.getY(),this.getZ(),1);
        for(Entity entity : entities){
            entity.hurt(this.damageSources().explosion(this,null),5f);
        }
        if (this.level().isClientSide()) {
            return;
        }

        if (pResult.getType() == HitResult.Type.ENTITY && pResult instanceof EntityHitResult entityHitResult) {
            Entity hit = entityHitResult.getEntity();
            Entity owner = this.getOwner();
            if (owner != hit) {
                this.entityData.set(HIT, true);
                counter = this.tickCount + 5;
            }
        } else {
            this.entityData.set(HIT, true);
            counter = this.tickCount + 5;
        }


    }



    public List<Entity> getEntitiesInRadius(Level world, double x, double y, double z, double radius) {
        AABB boundingBox = new AABB(x - radius, y - radius, z - radius, x + radius, y + radius, z + radius);
        List<Entity> nearbyEntities = world.getEntities(this,boundingBox);
        return nearbyEntities;
    }

    @Override
    protected void onHitEntity(EntityHitResult pResult) {
        super.onHitEntity(pResult);
        Entity hitEntity = pResult.getEntity();
        Entity owner = this.getOwner();

        if(hitEntity == owner && this.level().isClientSide()) {
            return;
        }

        LivingEntity livingentity = owner instanceof LivingEntity ? (LivingEntity)owner : null;
        float damage = 10f;
        boolean hurt = hitEntity.hurt(this.damageSources().mobProjectile(this,livingentity), damage);



    }



    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }


}

