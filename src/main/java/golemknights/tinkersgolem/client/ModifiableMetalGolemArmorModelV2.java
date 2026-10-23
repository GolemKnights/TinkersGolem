package golemknights.tinkersgolem.client;

import java.util.List;

import dev.xkmc.modulargolems.content.client.armor.GolemModelPath;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;

public class ModifiableMetalGolemArmorModelV2 {
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(TGModelPaths.METAL_GOLEM_ARMOR_V2,
			"main");

	public static final GolemModelPath PATH = GolemModelPath.register(TGModelPaths.METAL_GOLEM_ARMOR_V2,
			new GolemModelPath(LAYER_LOCATION,
					List.of(List.of("body", "body1"),
							List.of("head", "head1"),
							List.of("head", "head2"),
							List.of("head", "head3"),
							List.of("right_arm", "body1_r"),
							List.of("left_arm", "body1_l"),
							List.of("right_leg", "legs_1l"),
							List.of("right_leg", "legs3_l"),
							List.of("right_leg", "boot1_l"),
							List.of("right_leg", "boot2_l"),
							List.of("right_leg", "boot3_l"),
							List.of("left_leg", "legs1"),
							List.of("left_leg", "legs2"),
							List.of("left_leg", "legs3"),
							List.of("left_leg", "boot1"),
							List.of("left_leg", "boot2"),
							List.of("left_leg", "boot3"))));

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition head = partdefinition.addOrReplaceChild("head",
				CubeListBuilder.create().texOffs(0, 0)
						.addBox(-4.0F, -10.0F, -5.5F, 8.0F, 10.0F, 8.0F, new CubeDeformation(0.0F))
						.texOffs(24, 0).addBox(-1.0F, -3.0F, -7.5F, 2.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)),
				PartPose.offset(0.0F, -9.0F, -2.0F));

		PartDefinition head1 = head.addOrReplaceChild("head1",
				CubeListBuilder.create().texOffs(0, 112)
						.addBox(-5.0F, -47.0F, -9.25F, 4.0F, 6.0F, 3.0F, new CubeDeformation(0.0F))
						.texOffs(15, 112).addBox(1.0F, -47.0F, -9.25F, 4.0F, 6.0F, 3.0F, new CubeDeformation(0.0F))
						.texOffs(0, 121).addBox(-1.0F, -45.0F, -9.25F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
						.texOffs(0, 84).addBox(-5.0F, -47.0F, -6.25F, 10.0F, 1.0F, 8.0F, new CubeDeformation(0.0F))
						.texOffs(0, 93).addBox(-5.0F, -46.0F, -6.25F, 10.0F, 9.0F, 8.0F, new CubeDeformation(0.0F))
						.texOffs(28, 82).addBox(-1.0F, -43.0F, -8.25F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
						.texOffs(9, 121).addBox(3.0F, -39.0F, -9.25F, 2.0F, 3.0F, 3.0F, new CubeDeformation(0.0F))
						.texOffs(16, 121).addBox(-5.0F, -39.0F, -9.25F, 2.0F, 3.0F, 3.0F, new CubeDeformation(0.0F))
						.texOffs(28, 103).addBox(-6.0F, -38.5F, -6.25F, 12.0F, 3.0F, 9.0F, new CubeDeformation(0.0F)),
				PartPose.offset(0.0F, 35.0F, 1.75F));

		PartDefinition head2 = head.addOrReplaceChild("head2",
				CubeListBuilder.create().texOffs(8, 51)
						.addBox(-6.0F, -3.0F, 0.0F, 8.0F, 9.0F, 7.0F, new CubeDeformation(0.0F))
						.texOffs(29, 39).addBox(-7.0F, -3.0F, -4.0F, 10.0F, 9.0F, 10.0F, new CubeDeformation(0.0F)),
				PartPose.offset(2.0F, -8.0F, -3.5F));

		PartDefinition head3 = head.addOrReplaceChild("head3", CubeListBuilder.create().texOffs(28, 85).addBox(-8.0F,
				1.0F, -2.25F, 8.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(4.0F, -8.0F, -3.5F));

		PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(98, 69)
				.addBox(-3.0F, -5.0F, -0.5F, 6.0F, 6.0F, 9.0F, new CubeDeformation(0.0F))
				.texOffs(0, 35).addBox(-5.5F, -6.0F, 0.0F, 11.0F, 6.0F, 8.0F, new CubeDeformation(0.0F)),
				PartPose.offset(0.0F, -7.0F, 0.0F));

		PartDefinition body1 = body.addOrReplaceChild("body1",
				CubeListBuilder.create().texOffs(68, 84)
						.addBox(-7.0F, -26.0F, -7.0F, 15.0F, 5.0F, 12.0F, new CubeDeformation(0.0F))
						.texOffs(66, 103).addBox(-9.0F, -34.0F, -7.0F, 19.0F, 13.0F, 12.0F, new CubeDeformation(0.0F))
						.texOffs(0, 49).addBox(-8.0F, -32.0F, -7.0F, 17.0F, 8.0F, 12.0F, new CubeDeformation(0.0F)),
				PartPose.offset(-0.5F, 31.5F, 0.5F));

		PartDefinition right_arm = partdefinition.addOrReplaceChild("right_arm", CubeListBuilder.create(),
				PartPose.offset(-9.0F, -7.0F, 0.0F));

		PartDefinition body1_r = right_arm.addOrReplaceChild("body1_r",
				CubeListBuilder.create().texOffs(80, 37).mirror()
						.addBox(0.0F, -2.0F, -1.0F, 5.0F, 10.0F, 7.0F, new CubeDeformation(0.0F)).mirror(false)
						.texOffs(104, 37).mirror()
						.addBox(0.0F, -4.0F, -1.0F, 5.0F, 2.0F, 7.0F, new CubeDeformation(0.0F)).mirror(false),
				PartPose.offset(-4.5F, -1.5F, -2.5F));

		PartDefinition left_arm = partdefinition.addOrReplaceChild("left_arm", CubeListBuilder.create(),
				PartPose.offset(9.0F, -7.0F, 0.0F));

		PartDefinition body1_l = left_arm.addOrReplaceChild("body1_l",
				CubeListBuilder.create().texOffs(80, 20)
						.addBox(-5.0F, -2.0F, -1.0F, 5.0F, 10.0F, 7.0F, new CubeDeformation(0.0F))
						.texOffs(104, 20).addBox(-5.0F, -4.0F, -1.0F, 5.0F, 2.0F, 7.0F, new CubeDeformation(0.0F)),
				PartPose.offset(4.5F, -1.5F, -2.5F));

		PartDefinition body3_l = left_arm.addOrReplaceChild("body3_l", CubeListBuilder.create().texOffs(2, 20).addBox(
				-5.0F, -2.0F, -4.0F, 5.0F, 5.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offset(4.5F, 23.5F, 0.5F));

		PartDefinition left_leg = partdefinition.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(60, 0)
				.addBox(-3.5F, -1.0F, -3.0F, 6.0F, 16.0F, 5.0F, new CubeDeformation(0.0F)),
				PartPose.offset(5.0F, 9.0F, 0.0F));

		PartDefinition legs1 = left_leg.addOrReplaceChild("legs1",
				CubeListBuilder.create()
						.texOffs(0, 3).addBox(1.0F, -2.0F, 0.5F, 7.0F, 4.0F, 6.0F, new CubeDeformation(0.0F)),
				PartPose.offset(-5.0F, 0.5F, -4.0F));

		PartDefinition legs2 = left_leg.addOrReplaceChild("legs2",
				CubeListBuilder.create().texOffs(0, 21)
						.addBox(-9.0F, -5.0F, -1.0F, 11.0F, 6.0F, 8.0F, new CubeDeformation(0.0F))
						.texOffs(81, 65).addBox(-5.5F, -3.0F, -1.5F, 4.0F, 4.0F, 9.0F, new CubeDeformation(0.0F)),
				PartPose.offset(-1.5F, -0.5F, -3.0F));

		PartDefinition legs3 = left_leg.addOrReplaceChild("legs3", CubeListBuilder.create().texOffs(0, 7).addBox(1.0F,
				-14.5F, -3.5F, 7.0F, 7.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(-5.0F, 15.0F, 0.0F));

		PartDefinition boot1 = left_leg.addOrReplaceChild("boot1", CubeListBuilder.create().texOffs(26, 4).addBox(1.0F,
				-16.5F, -3.5F, 7.0F, 8.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(-5.0F, 22.0F, 0.0F));

		PartDefinition boot2 = left_leg.addOrReplaceChild("boot2", CubeListBuilder.create().texOffs(53, 6).addBox(1.0F,
				-15.5F, -3.5F, 7.0F, 8.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(-5.0F, 22.0F, 0.0F));

		PartDefinition boot3 = left_leg.addOrReplaceChild("boot3", CubeListBuilder.create().texOffs(53, 20).addBox(1.0F,
				-8.5F, -3.5F, 7.0F, 2.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(-5.0F, 22.0F, 0.0F));

		PartDefinition right_leg = partdefinition.addOrReplaceChild("right_leg", CubeListBuilder.create().texOffs(37, 0)
				.addBox(-3.5F, -1.0F, -3.0F, 6.0F, 16.0F, 5.0F, new CubeDeformation(0.0F)),
				PartPose.offset(-4.0F, 9.0F, 0.0F));

		PartDefinition legs_1l = right_leg.addOrReplaceChild("legs_1l",
				CubeListBuilder.create().texOffs(0, 3).mirror()
						.addBox(-8.0F, -14.5F, -3.5F, 7.0F, 4.0F, 6.0F, new CubeDeformation(0.0F)).mirror(false),
				PartPose.offset(4.0F, 13.0F, 0.0F));

		PartDefinition legs3_l = right_leg.addOrReplaceChild("legs3_l",
				CubeListBuilder.create().texOffs(0, 7).mirror()
						.addBox(-8.0F, -14.5F, -3.5F, 7.0F, 7.0F, 6.0F, new CubeDeformation(0.0F)).mirror(false),
				PartPose.offset(4.0F, 15.0F, 0.0F));

		PartDefinition boot1_l = right_leg.addOrReplaceChild("boot1_l",
				CubeListBuilder.create().texOffs(26, 4).mirror()
						.addBox(-8.0F, -17.5F, -3.5F, 7.0F, 10.0F, 6.0F, new CubeDeformation(0.0F)).mirror(false),
				PartPose.offset(4.0F, 23.0F, 0.0F));

		PartDefinition boot2_l = right_leg.addOrReplaceChild("boot2_l",
				CubeListBuilder.create().texOffs(53, 6).mirror()
						.addBox(-8.0F, -15.5F, -3.5F, 7.0F, 8.0F, 6.0F, new CubeDeformation(0.0F)).mirror(false),
				PartPose.offset(4.0F, 22.0F, 0.0F));

		PartDefinition boot3_l = right_leg.addOrReplaceChild("boot3_l",
				CubeListBuilder.create().texOffs(53, 20).mirror()
						.addBox(-8.0F, -8.5F, -3.5F, 7.0F, 2.0F, 6.0F, new CubeDeformation(0.0F)).mirror(false),
				PartPose.offset(4.0F, 22.0F, 0.0F));

		left_arm.addOrReplaceChild("left_forearm", CubeListBuilder.create(), PartPose.ZERO);
		right_arm.addOrReplaceChild("right_forearm", CubeListBuilder.create(), PartPose.ZERO);
		return LayerDefinition.create(meshdefinition, 128, 128);
	}
}