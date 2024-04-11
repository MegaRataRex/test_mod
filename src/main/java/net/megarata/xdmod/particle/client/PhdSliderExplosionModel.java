package net.megarata.xdmod.particle.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.megarata.xdmod.XdMod;
import net.megarata.xdmod.particle.custom.PhdSliderExplosionParticle;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.client.extensions.IForgeBakedModel;
import net.minecraftforge.fml.common.Mod;
import software.bernie.geckolib.model.GeoModel;

import java.util.ArrayList;
import java.util.List;

public class PhdSliderExplosionModel extends HierarchicalModel {


    private final ModelPart root;

    private final ModelPart foot;

    private final ModelPart headnbody;

    public PhdSliderExplosionModel(ModelPart root) {
        this.root = root.getChild("root");
        this.foot = this.root.getChild("cloud_foot");
        this.headnbody = this.root.getChild("bone");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition cloud_foot = root.addOrReplaceChild("cloud_foot", CubeListBuilder.create().texOffs(0, 34).addBox(-6.0F, -2.0F, -6.0F, 12.0F, 2.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition cube_r1 = cloud_foot.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(40, 21).addBox(-9.0F, -2.0F, 0.0F, 18.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -2.0F, -0.0711F, 0.0F, 0.7854F, 0.0F));

        PartDefinition cube_r2 = cloud_foot.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(40, 21).addBox(-9.0F, -2.0F, 0.0F, 18.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -2.0F, -0.0711F, 0.0F, 2.3562F, 0.0F));

        PartDefinition bone = root.addOrReplaceChild("bone", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition cloud_head = bone.addOrReplaceChild("cloud_head", CubeListBuilder.create().texOffs(0, 21).addBox(-6.0F, -11.75F, -6.0F, 12.0F, 1.0F, 12.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(-7.0F, -10.85F, -7.0F, 14.0F, 7.0F, 14.0F, new CubeDeformation(0.0F))
                .texOffs(36, 36).addBox(-8.0F, -9.85F, -6.0F, 1.0F, 5.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -10.15F, 0.0F));

        PartDefinition cube_r3 = cloud_head.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(36, 36).addBox(1.5F, -2.5F, -6.0F, 1.0F, 5.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -7.35F, -9.5F, 0.0F, -1.5708F, 0.0F));

        PartDefinition cube_r4 = cloud_head.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(36, 36).addBox(0.5F, -2.5F, -6.0F, 1.0F, 5.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(8.5F, -7.35F, 0.0F, 0.0F, 3.1416F, 0.0F));

        PartDefinition cube_r5 = cloud_head.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(36, 36).addBox(-8.0F, -20.0F, -6.0F, 1.0F, 5.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 10.15F, 0.0F, 0.0F, 1.5708F, 0.0F));

        PartDefinition cloud_body = bone.addOrReplaceChild("cloud_body", CubeListBuilder.create().texOffs(46, 0).addBox(-2.0F, -7.0F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(0, 48).addBox(-2.0F, -8.0F, -2.0F, 4.0F, 3.0F, 4.0F, new CubeDeformation(0.3F))
                .texOffs(48, 31).addBox(-2.0F, -9.0F, -2.0F, 4.0F, 1.0F, 4.0F, new CubeDeformation(0.55F))
                .texOffs(16, 48).addBox(-2.0F, -13.0F, -2.0F, 4.0F, 2.0F, 4.0F, new CubeDeformation(1.45F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 128, 128);
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        root.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    }

    @Override
    public ModelPart root() {
        return root;
    }

    @Override
    public void setupAnim(Entity pEntity, float pLimbSwing, float pLimbSwingAmount, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {
        this.root.getAllParts().forEach(ModelPart::resetPose);
    }

    public void actuallyAnimate(float life, float tickDelta){
        this.root.getAllParts().forEach(ModelPart::resetPose);

    }



}
