package net.megarata.xdmod.block.custom;

import com.google.common.collect.ImmutableMap;
import net.megarata.xdmod.block.ModBlocks;
import net.megarata.xdmod.block.entity.ModBlockEntities;
import net.megarata.xdmod.block.entity.MysteryBoxBlockEntity;
import net.megarata.xdmod.item.ModItems;
import net.megarata.xdmod.sound.ModSounds;
import net.megarata.xdmod.util.ModUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import java.util.List;
public class MysteryBoxBlock extends BaseEntityBlock {
    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
    public  static final EnumProperty<MysteryBoxPart> PART = EnumProperty.create
            ("part",MysteryBoxPart.class,MysteryBoxPart.values());

    public  static  final BooleanProperty USING = BooleanProperty.create("using");

    public  static  final BooleanProperty READY = BooleanProperty.create("ready");


    public final ImmutableMap<BlockState, VoxelShape> SHAPES = ModUtils.generateShapeForEach
            (this.getStateDefinition().getPossibleStates());
    public MysteryBoxBlock(Properties pProperties) {
        super(pProperties);
        this.registerDefaultState(this.getStateDefinition().any().setValue(PART, MysteryBoxPart.MIDDLE).
                setValue(USING,false).setValue(READY,false));

    }

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
            return SHAPES.get(pState);
        }

    @Override
    public VoxelShape getOcclusionShape(BlockState pState, BlockGetter pLevel, BlockPos pPos) {
        return getShape(pState,pLevel,pPos,CollisionContext.empty());
    }


    public BlockState rotate(BlockState state, Rotation rot) {
        return state.setValue(FACING, rot.rotate(state.getValue(FACING)));
    }
    public BlockState mirror(BlockState pState, Mirror pMirror) {
        return pState.rotate(pMirror.getRotation(pState.getValue(FACING).getOpposite()));
    }

    @Override
    public void setPlacedBy(Level pLevel, BlockPos pPos, BlockState pState, @Nullable LivingEntity pPlacer, ItemStack pStack) {
        super.setPlacedBy(pLevel, pPos, pState, pPlacer, pStack);
        Direction direction = pState.getValue(FACING); //N,S,E,O
        BlockPos posRight =  pPos.relative(direction.getClockWise()); // O ->
        BlockPos posLeft = pPos.relative(direction.getCounterClockWise());// <-O
        if(!pLevel.isClientSide) {
            pLevel.setBlockAndUpdate(posLeft,ModBlocks.MYSTERY_BOX_BLOCK.get().defaultBlockState().
                    setValue(FACING,direction).setValue(PART,MysteryBoxPart.RIGHT));
            pLevel.setBlockAndUpdate(posRight, ModBlocks.MYSTERY_BOX_BLOCK.get().defaultBlockState().
                    setValue(FACING,direction).setValue(PART,MysteryBoxPart.LEFT));
        }

    }



    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {

        Direction direction = pContext.getHorizontalDirection();
        BlockPos posRight = pContext.getClickedPos().relative(direction.getClockWise());
        BlockPos posLeft = pContext.getClickedPos().relative(direction.getCounterClockWise());
        boolean isLandRBlocksAir = pContext.getLevel().getBlockState(posRight).isAir() &&
                pContext.getLevel().getBlockState(posLeft).isAir();
        if(isLandRBlocksAir){
            return this.defaultBlockState().setValue(FACING, pContext.getHorizontalDirection());
        }
        return null;
    }

    private void breakBlocks(LevelAccessor level, BlockPos pos,BlockState oldState){
        List<BlockPos> list = ModUtils.getNeighboringBlocks(oldState,pos);
        if(oldState.getBlock() == this){
            for(BlockPos position : list){
                level.getBlockState(position);
                level.destroyBlock(position,false);
            }
        }
    }


    @Override
    public void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pIsMoving) {
        if(!(pNewState.is(this))){
            breakBlocks(pLevel,pPos,pState);
        }
        super.onRemove(pState, pLevel, pPos, pNewState, pIsMoving);
    }

    @Override
    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.ENTITYBLOCK_ANIMATED;
    }

    /* BLOCK ENTITY */

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new MysteryBoxBlockEntity(pPos,pState);
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
        builder.add(PART);
        builder.add(USING);
        builder.add(READY);
    }

    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        BlockPos middlePos = !(pState.getValue(PART) == MysteryBoxPart.MIDDLE) ?
                ModUtils.getNeighboringBlocks(pState,pPos).get(0) : pPos;
        BlockEntity blockEntity = pLevel.getBlockEntity(middlePos);
        boolean hasItem = pPlayer.getItemInHand(InteractionHand.MAIN_HAND).getItem() == ModItems.ELEMENT_115.get();
        boolean isReady = pLevel.getBlockState(middlePos).getValue(READY);
        if(blockEntity instanceof MysteryBoxBlockEntity mysteryBoxBlockEntity){
            if((!pLevel.isClientSide() && pHand == InteractionHand.MAIN_HAND)&& hasItem &&
                    !pLevel.getBlockState(middlePos).getValue(USING)) {
                ItemStack itemstack = pPlayer.getItemInHand(InteractionHand.MAIN_HAND);
                mysteryBoxBlockEntity.placeItem(pPlayer,pPlayer.getAbilities().instabuild ? itemstack.copy() : itemstack);
                pLevel.setBlock(middlePos,pState.setValue(PART,MysteryBoxPart.MIDDLE).setValue(USING,true),3);
                pLevel.playSound(null,middlePos, ModSounds.MYSTERY_BOX_OPEN.get(), SoundSource.BLOCKS);
                return InteractionResult.SUCCESS;
            }
            if(isReady  && !pLevel.isClientSide() && pHand == InteractionHand.MAIN_HAND){
                mysteryBoxBlockEntity.dropItemStack(middlePos);
                mysteryBoxBlockEntity.deleteItemStack();
                return InteractionResult.SUCCESS;
            }
        }
        return InteractionResult.FAIL;
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        if(pLevel.isClientSide){
            return null;
        }
        return createTickerHelper(pBlockEntityType, ModBlockEntities.MYSTERY_BOX_BE.get(), (pLevel1, pPos, pState1, pBlockEntity)
                -> pBlockEntity.tick(pLevel1,pPos,pState1));
    }

    public enum MysteryBoxPart implements StringRepresentable {
        LEFT("leftblock"),
        MIDDLE("middleblock"),
        RIGHT("rightblock");

        private final String name;
        private MysteryBoxPart(String pName) {this.name = pName;}
        public String toString() {
            return this.name;
        }
        public String getSerializedName() {
            return this.name;
        }
    }
}
