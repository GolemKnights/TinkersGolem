package golemknights.tinkersgolem.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import dev.xkmc.modulargolems.content.client.armor.GolemModelPath;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import dev.xkmc.modulargolems.content.entity.metalgolem.MetalGolemModel;
import static golemknights.tinkersgolem.TinkersGolem.getResource;

import java.util.List;

@SuppressWarnings({"unused","null"})
public class ModifiableMetalGolemArmorModel {
    public static final ResourceLocation LOC = getResource("metal_golem_armor");
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(LOC, "main");

    public static final GolemModelPath PATH = GolemModelPath.register(LOC,
            new GolemModelPath(LAYER_LOCATION,
                    List.of(List.of("body", "body1"),
                            List.of("body", "legs1"),
                            List.of("head", "head1"),
                            List.of("right_arm", "body2"),
                            List.of("left_arm", "body3"),
                            List.of("right_leg", "legs2"),
                            List.of("right_leg", "boot1"),
                            List.of("left_leg", "legs3"),
                            List.of("left_leg", "boot2"))));

    private final ModelPart body;
    private final ModelPart body1;
    private final ModelPart legs1;
    private final ModelPart head;
    private final ModelPart head1;
    private final ModelPart right_arm;
    private final ModelPart body2;
    private final ModelPart left_arm;
    private final ModelPart body3;
    private final ModelPart right_leg;
    private final ModelPart legs2;
    private final ModelPart boot1;
    private final ModelPart left_leg;
    private final ModelPart legs3;
    private final ModelPart boot2;

    public ModifiableMetalGolemArmorModel(ModelPart root) {
        this.body = root.getChild("body");
        this.body1 = this.body.getChild("body1");
        this.legs1 = this.body.getChild("legs1");
        this.head = root.getChild("head");
        this.head1 = this.head.getChild("head1");
        this.right_arm = root.getChild("right_arm");
        this.body2 = this.right_arm.getChild("body2");
        this.left_arm = root.getChild("left_arm");
        this.body3 = this.left_arm.getChild("body3");
        this.right_leg = root.getChild("right_leg");
        this.legs2 = this.right_leg.getChild("legs2");
        this.boot1 = this.right_leg.getChild("boot1");
        this.left_leg = root.getChild("left_leg");
        this.legs3 = this.left_leg.getChild("legs3");
        this.boot2 = this.left_leg.getChild("boot2");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition body = partdefinition.addOrReplaceChild("body",
                CubeListBuilder.create().texOffs(0, 40)
                        .addBox(-9.0F, -2.0F, -6.0F, 18.0F, 12.0F, 11.0F,
                                new CubeDeformation(0.0F))
                        .texOffs(0, 70).addBox(-4.5F, 10.0F, -3.0F, 9.0F, 5.0F, 6.0F,
                                new CubeDeformation(0.5F)),
                PartPose.offset(0.0F, -7.0F, 0.0F));

        PartDefinition body1 = body.addOrReplaceChild("body1",
                CubeListBuilder.create().texOffs(0, 20)
                        .addBox(-9.5F, -26.5F, -6.5F, 19.0F, 6.0F, 12.0F,
                                new CubeDeformation(0.0F))
                        .texOffs(0, 0).addBox(-10.0F, -33.5F, -7.0F, 20.0F, 7.0F, 13.0F,
                                new CubeDeformation(0.0F)),
                PartPose.offset(0.0F, 31.0F, 0.0F));

        PartDefinition legs1 = body.addOrReplaceChild("legs1",
                CubeListBuilder.create().texOffs(62, 20).addBox(-5.5F,
                        -20.0F, -4.0F, 11.0F, 4.0F, 8.0F, new CubeDeformation(0.0F)),
                PartPose.offset(0.0F, 31.0F, 0.0F));

        PartDefinition head = partdefinition.addOrReplaceChild("head",
                CubeListBuilder.create().texOffs(0, 0)
                        .addBox(-4.0F, -12.0F, -5.5F, 8.0F, 10.0F, 8.0F,
                                new CubeDeformation(0.0F))
                        .texOffs(24, 0).addBox(-1.0F, -5.0F, -7.5F, 2.0F, 4.0F, 2.0F,
                                new CubeDeformation(0.0F)),
                PartPose.offset(0.0F, -7.0F, -2.0F));

        PartDefinition head1 = head.addOrReplaceChild("head1",
                CubeListBuilder.create().texOffs(0, 38).addBox(-4.5F,
                        -44.0F, -8.0F, 9.0F, 11.0F, 9.0F, new CubeDeformation(0.0F)),
                PartPose.offset(0.0F, 31.0F, 2.0F));

        PartDefinition cube_r1 = head1.addOrReplaceChild("cube_r1",
                CubeListBuilder.create().texOffs(48, 103).addBox(-4.0F, -1.0F, 0.0F, 8.0F, 5.0F, 1.0F,
                        new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, -37.0F, 1.0F, 0.1309F, 0.0F, 0.0F));

        PartDefinition cube_r2 = head1.addOrReplaceChild("cube_r2",
                CubeListBuilder.create().texOffs(100, 79).addBox(-1.0F, -2.0F, -3.0F, 2.0F, 2.0F, 8.0F,
                        new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, -43.0F, -4.0F, 0.2182F, 0.0F, 0.0F));

        PartDefinition cube_r3 = head1.addOrReplaceChild("cube_r3",
                CubeListBuilder.create().texOffs(26, 74).addBox(-2.0F, -3.0F, -8.3F, 3.0F, 4.0F, 10.0F,
                        new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(4.0F, -34.0F, 0.0F, 0.0F, 0.0F, -0.1309F));

        PartDefinition cube_r4 = head1.addOrReplaceChild("cube_r4",
                CubeListBuilder.create().texOffs(0, 74).addBox(-1.0F, -3.0F, -8.3F, 3.0F, 4.0F, 10.0F,
                        new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(-4.0F, -34.0F, 0.0F, 0.0F, 0.0F, 0.1309F));

        PartDefinition cube_r5 = head1.addOrReplaceChild("cube_r5",
                CubeListBuilder.create().texOffs(68, 32).addBox(-5.0F, -1.0F, -4.6F, 10.0F, 5.0F, 6.0F,
                        new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, -42.0F, -4.0F, -0.3054F, 0.0F, 0.0F));

        PartDefinition right_arm = partdefinition.addOrReplaceChild("right_arm", CubeListBuilder.create()
                .texOffs(60, 21)
                .addBox(-13.0F, -2.5F, -3.0F, 4.0F, 30.0F, 6.0F, new CubeDeformation(0.0F)),
                PartPose.offset(0.0F, -7.0F, 0.0F));

        PartDefinition body2 = right_arm.addOrReplaceChild("body2",
                CubeListBuilder.create().texOffs(0, 58)
                        .addBox(-14.0F, -34.0F, -4.0F, 6.0F, 8.0F, 8.0F,
                                new CubeDeformation(0.0F))
                        .texOffs(94, 56)
                        .addBox(-13.5F, -26.0F, -3.5F, 5.0F, 5.0F, 7.0F,
                                new CubeDeformation(0.0F))
                        .texOffs(52, 75)
                        .addBox(-13.5F, -15.0F, -3.5F, 5.0F, 9.0F, 7.0F,
                                new CubeDeformation(0.0F))
                        .texOffs(94, 0)
                        .addBox(-13.5F, -20.0F, -3.5F, 5.0F, 6.0F, 7.0F,
                                new CubeDeformation(-0.3F))
                        .texOffs(24, 100).addBox(-13.5F, -7.0F, -3.5F, 5.0F, 4.0F, 7.0F,
                                new CubeDeformation(-0.2F)),
                PartPose.offset(0.0F, 31.0F, 0.0F));

        PartDefinition cube_r6 = body2.addOrReplaceChild("cube_r6",
                CubeListBuilder.create().texOffs(100, 13).addBox(-2.0F, -2.0F, -4.0F, 2.0F, 7.0F, 8.0F,
                        new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(-13.0F, -15.0F, 0.0F, 0.0F, 0.0F, -0.2182F));

        PartDefinition cube_r7 = body2.addOrReplaceChild("cube_r7",
                CubeListBuilder.create().texOffs(36, 38).addBox(-3.2F, -4.0F, -5.0F, 6.0F, 8.0F, 10.0F,
                        new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(-12.0F, -31.0F, 0.0F, 0.0F, 0.0F, 0.48F));

        PartDefinition left_arm = partdefinition.addOrReplaceChild("left_arm",
                CubeListBuilder.create().texOffs(60, 58)
                        .addBox(9.0F, -2.5F, -3.0F, 4.0F, 30.0F, 6.0F,
                                new CubeDeformation(0.0F)),
                PartPose.offset(0.0F, -7.0F, 0.0F));

        PartDefinition body3 = left_arm.addOrReplaceChild("body3",
                CubeListBuilder.create().texOffs(66, 0)
                        .addBox(-14.0F, -34.0F, -4.0F, 6.0F, 8.0F, 8.0F,
                                new CubeDeformation(0.0F))
                        .texOffs(0, 100)
                        .addBox(-13.5F, -26.0F, -3.5F, 5.0F, 5.0F, 7.0F,
                                new CubeDeformation(0.0F))
                        .texOffs(76, 75)
                        .addBox(-13.5F, -15.0F, -3.5F, 5.0F, 9.0F, 7.0F,
                                new CubeDeformation(0.0F))
                        .texOffs(94, 43)
                        .addBox(-13.5F, -20.0F, -3.5F, 5.0F, 6.0F, 7.0F,
                                new CubeDeformation(-0.3F))
                        .texOffs(100, 68).addBox(-13.5F, -7.0F, -3.5F, 5.0F, 4.0F, 7.0F,
                                new CubeDeformation(-0.2F)),
                PartPose.offset(22.0F, 31.0F, 0.0F));

        PartDefinition cube_r8 = body3.addOrReplaceChild("cube_r8",
                CubeListBuilder.create().texOffs(100, 28).addBox(0.0F, -2.0F, -4.0F, 2.0F, 7.0F, 8.0F,
                        new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(-9.0F, -15.0F, 0.0F, 0.0F, 0.0F, 0.2182F));

        PartDefinition cube_r9 = body3.addOrReplaceChild("cube_r9",
                CubeListBuilder.create().texOffs(36, 56).addBox(-2.8F, -4.0F, -5.0F, 6.0F, 8.0F, 10.0F,
                        new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(-10.0F, -31.0F, 0.0F, 0.0F, 0.0F, -0.48F));

        PartDefinition right_leg = partdefinition.addOrReplaceChild("right_leg",
                CubeListBuilder.create().texOffs(37, 0)
                        .addBox(-3.5F, -3.0F, -3.0F, 6.0F, 16.0F, 5.0F,
                                new CubeDeformation(0.0F)),
                PartPose.offset(-4.0F, 11.0F, 0.0F));

        PartDefinition legs2 = right_leg.addOrReplaceChild("legs2",
                CubeListBuilder.create().texOffs(0, 88)
                        .addBox(-8.0F, -15.0F, -3.5F, 7.0F, 6.0F, 6.0F,
                                new CubeDeformation(0.0F))
                        .texOffs(68, 43).addBox(-8.0F, -16.5F, -3.5F, 7.0F, 10.0F, 6.0F,
                                new CubeDeformation(-0.3F)),
                PartPose.offset(4.0F, 13.0F, 0.0F));

        PartDefinition boot1 = right_leg.addOrReplaceChild("boot1",
                CubeListBuilder.create().texOffs(52, 91).addBox(
                        -8.0F, -5.6F, -3.5F, 7.0F, 6.0F, 6.0F, new CubeDeformation(0.0F)),
                PartPose.offset(4.0F, 13.0F, 0.0F));

        PartDefinition left_leg = partdefinition.addOrReplaceChild("left_leg",
                CubeListBuilder.create().texOffs(60, 0)
                        .addBox(-3.5F, -3.0F, -3.0F, 6.0F, 16.0F, 5.0F,
                                new CubeDeformation(0.0F)),
                PartPose.offset(5.0F, 11.0F, 0.0F));

        PartDefinition legs3 = left_leg.addOrReplaceChild("legs3",
                CubeListBuilder.create().texOffs(26, 88)
                        .addBox(-8.0F, -15.0F, -3.5F, 7.0F, 6.0F, 6.0F,
                                new CubeDeformation(0.0F))
                        .texOffs(68, 59).addBox(-8.0F, -16.5F, -3.5F, 7.0F, 10.0F, 6.0F,
                                new CubeDeformation(-0.3F)),
                PartPose.offset(4.0F, 13.0F, 0.0F));

        PartDefinition boot2 = left_leg.addOrReplaceChild("boot2",
                CubeListBuilder.create().texOffs(78, 91).addBox(
                        -8.0F, -5.6F, -3.5F, 7.0F, 6.0F, 6.0F, new CubeDeformation(0.0F)),
                PartPose.offset(4.0F, 13.0F, 0.0F));

        left_arm.addOrReplaceChild("left_forearm", CubeListBuilder.create(), PartPose.ZERO);
        right_arm.addOrReplaceChild("right_forearm", CubeListBuilder.create(), PartPose.ZERO);
        return LayerDefinition.create(meshdefinition, 128, 128);
    }
}