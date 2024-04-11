package net.megarata.xdmod.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.megarata.xdmod.XdMod;
import net.megarata.xdmod.block.custom.MysteryBoxBlock;
import net.megarata.xdmod.block.entity.MysteryBoxBlockEntity;
import net.megarata.xdmod.entity.layers.ModModelLayers;
import net.megarata.xdmod.item.ModItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.Tickable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoBlockRenderer;

import java.util.Objects;

public class MysteryBoxRenderer extends GeoBlockRenderer<MysteryBoxBlockEntity>  {
    ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();



    public MysteryBoxRenderer(BlockEntityRendererProvider.Context context) {
        super(new MysteryBoxModel());
    }


    @Override
    public void actuallyRender(PoseStack pPoseStack, MysteryBoxBlockEntity pBlockEntity, BakedGeoModel model, RenderType renderType, MultiBufferSource pBufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        super.actuallyRender(pPoseStack, pBlockEntity, model, renderType, pBufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
        if(pBlockEntity.hasItem()){


            //GETTING THE ITEM
            ItemStack itemStack = pBlockEntity.getRenderStack();

            //MATH
            double offset = Math.sqrt(pBlockEntity.getProgress()+partialTick)/10 ;
            offset = offset >= 1 ? 1 + Math.sin((pBlockEntity.getLevel().getDayTime()+partialTick)/8)/4 : offset;

            pPoseStack.pushPose();
            pPoseStack.translate(0f, 0.2f + offset, 0f);
            pPoseStack.scale(1f, 1f, 1f);

            if(itemStack.getItem() == ModItems.RAY_GUN.get()){
                switch(pBlockEntity.getBlockState().getValue(MysteryBoxBlock.FACING)){
                    case NORTH, SOUTH:
                        pPoseStack.mulPose(Axis.YN.rotationDegrees(pBlockEntity.getBlockState().getValue(MysteryBoxBlock.FACING).toYRot()+90));
                        break;
                    case EAST, WEST:
                        pPoseStack.mulPose(Axis.YN.rotationDegrees(pBlockEntity.getBlockState().getValue(MysteryBoxBlock.FACING).toYRot()));
                        break;
                    default:
                        break;
                }

            }

            itemRenderer.renderStatic(itemStack,ItemDisplayContext.GROUND,getLightLevel(pBlockEntity.getLevel(),
                    pBlockEntity.getBlockPos()),packedOverlay,pPoseStack,pBufferSource,pBlockEntity.getLevel(),0);
            pPoseStack.popPose();
        }
    }

    private int getLightLevel(Level level, BlockPos pos) {
        int bLight = level.getBrightness(LightLayer.BLOCK, pos);
        int sLight = level.getBrightness(LightLayer.SKY, pos);
        return LightTexture.pack(bLight, sLight);
    }

    @Override
    public boolean shouldRender(MysteryBoxBlockEntity pBlockEntity, Vec3 pCameraPos) {
        BlockState blockState = pBlockEntity.getBlockState();
        if(blockState.getValue(MysteryBoxBlock.PART) == MysteryBoxBlock.MysteryBoxPart.LEFT
                || blockState.getValue(MysteryBoxBlock.PART) == MysteryBoxBlock.MysteryBoxPart.RIGHT){
            return false;
        }
        return super.shouldRender(pBlockEntity, pCameraPos);
    }




}
