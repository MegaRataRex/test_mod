package net.megarata.xdmod.block.entity;

import net.megarata.xdmod.XdMod;
import net.megarata.xdmod.block.custom.MysteryBoxBlock;
import net.megarata.xdmod.sound.ModSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Clearable;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.Containers;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import software.bernie.geckolib.animatable.GeoBlockEntity;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.core.animation.*;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.RenderUtils;

import javax.annotation.Nullable;
import java.util.List;


public class MysteryBoxBlockEntity extends BlockEntity implements Clearable, GeoBlockEntity {

    private AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);
    private int progress = 0;
    private final int  maxProgress = 300;
    public final NonNullList<ItemStack> items = NonNullList.withSize(1, ItemStack.EMPTY);

    LootTable lootTable;
    LootParams.Builder builder;







    public MysteryBoxBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntities.MYSTERY_BOX_BE.get(), pPos, pBlockState);

    }

    public NonNullList<ItemStack> getItems() {
        return items;
    }


    protected void saveAdditional(CompoundTag pTag) {
        pTag.putInt("mystery_box.progress", this.progress);
        ContainerHelper.saveAllItems(pTag, this.items, true);

        super.saveAdditional(pTag);
    }

    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);
        this.items.clear();
        ContainerHelper.loadAllItems(pTag, this.items);
        this.progress = pTag.getInt("mystery_box.progress");
    }

    public CompoundTag getUpdateTag() {
        CompoundTag tag = new CompoundTag();
        tag.putInt("mystery_box.progress",this.progress);
        ContainerHelper.saveAllItems(tag,this.items);
        return tag;
    }

    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }


    private void markUpdated() {
        this.setChanged();
        this.getLevel().sendBlockUpdated(this.getBlockPos(), this.getBlockState(), this.getBlockState(), 3);
    }

    @Override
    public void clearContent() {
        this.items.clear();
    }

    public void tick(Level level, BlockPos pPos, BlockState pState) {
        if(this.level == null || this.level.isClientSide())
            return;
        if(!hasItem()){
            closeBox();
            resetProgress();
            
        }
        if(hasItem()) {
            increaseUseProgress();
            if(this.progress == 10){
                level.playSound(null,getBlockPos(), ModSounds.MYSTERY_BOX_SPIN.get(), SoundSource.BLOCKS);
            }
            if(willItemChange() && !hasBoxFinished()){changeItem();}
            if(hasBoxFinished()){
                if(isReady()){setToReady();}
                if(hasWeaponDisappeared()){
                    resetProgress();
                    closeBox();
                    deleteItemStack();
                    }
                }
            setChanged(level,pPos,pState);
            }
        this.level.sendBlockUpdated(this.worldPosition,getBlockState(),getBlockState(),
                Block.UPDATE_ALL);
    }

    private boolean isReady() {
        return this.progress == 100;
    }

    private void closeBox() {
        this.getLevel().setBlock(this.getBlockPos()
                ,this.getBlockState().setValue(MysteryBoxBlock.USING,false)
                        .setValue(MysteryBoxBlock.READY,false),3);
        markUpdated();
    }

    public void deleteItemStack() {
        this.items.set(0,ItemStack.EMPTY);
    }
    private void resetProgress(){
        this.progress = 0;
    }

    private void setToReady(){
        this.getLevel().setBlock(this.getBlockPos()
                ,this.getBlockState().setValue(MysteryBoxBlock.READY,true),3);
    }

    private boolean willItemChange() {
        return this.progress % 2 == 0;
    }

    public void changeItem() {
        if(this.level == null) {
            return;
        }
        if (lootTable == null) {
            lootTable = level.getServer().getLootData().getLootTable(new ResourceLocation(
                    "xdmod:blocks/mystery_box_loot"));
        }
        if(builder == null){
           builder = new LootParams.Builder((ServerLevel) level).
                    withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf(getBlockPos()))
                    .withParameter(LootContextParams.BLOCK_STATE, getBlockState())
                    .withParameter(LootContextParams.TOOL, ItemStack.EMPTY);
        }

        List<ItemStack> lootPool = lootTable.getRandomItems(builder.create(LootContextParamSets.BLOCK));
        items.set(0, lootPool.isEmpty() ? ItemStack.EMPTY : lootPool.get(0));

    }


    public ItemStack getRenderStack() {
        ItemStack stack = this.getItems().get(0);

        return stack;
    }


    private boolean hasWeaponDisappeared(){
        return this.progress >= maxProgress;
    }

    private boolean hasBoxFinished(){
        return this.progress >= 100;
    }

    private void increaseUseProgress(){
        this.progress++;
    }

    public boolean hasItem(){
        return !(this.items.get(0) == ItemStack.EMPTY);
    }

    public void dropItemStack(BlockPos pPos){
        Containers.dropItemStack(this.getLevel(),pPos.getX(),pPos.getY()+1,pPos.getZ(),this.items.get(0));
    }

    private boolean isMiddleBlock(){
        return getBlockState().getValue(MysteryBoxBlock.PART) == MysteryBoxBlock.MysteryBoxPart.MIDDLE;
    }
    public boolean isBeingUsed(){
        return getBlockState().getValue(MysteryBoxBlock.USING);
    }

    public int getProgress() {
        return progress;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "controller", 0, this::predicate));
    }


    private <T extends GeoAnimatable> PlayState predicate(AnimationState<T> tAnimationState) {
        if(isMiddleBlock() && isBeingUsed()) {
            tAnimationState.getController().setAnimation(RawAnimation.begin().then("open", Animation.LoopType.HOLD_ON_LAST_FRAME));
        }
        else if(hasWeaponDisappeared() || !isBeingUsed()){
            tAnimationState.getController().setAnimation(RawAnimation.begin().then("close", Animation.LoopType.HOLD_ON_LAST_FRAME));
        }
        return PlayState.CONTINUE;
    }

    public void placeItem(@Nullable Entity pEntity, ItemStack pStack){
        this.items.set(0, pStack.split(1));
        this.level.gameEvent(GameEvent.BLOCK_CHANGE, this.getBlockPos(), GameEvent.Context.of(pEntity, this.getBlockState()));
        this.markUpdated();

    }
    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    @Override
    public double getTick(Object blockEntity) {
        return RenderUtils.getCurrentTick();

    }
    @Override
    public boolean shouldPlayAnimsWhileGamePaused() {
        return false;
    }

}