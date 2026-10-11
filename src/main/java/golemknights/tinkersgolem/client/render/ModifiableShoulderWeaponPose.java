// Source code is decompiled from a .class file using FernFlower decompiler (from Intellij IDEA).
package golemknights.tinkersgolem.client.render;

import org.joml.Quaternionf;

import com.mojang.blaze3d.vertex.PoseStack;

import dev.xkmc.modulargolems.content.client.pose.GolemShoulderPose;
import dev.xkmc.modulargolems.content.entity.metalgolem.MetalGolemEntity;
import dev.xkmc.modulargolems.content.entity.metalgolem.MetalGolemModel;
import dev.xkmc.modulargolems.content.item.ranged.CannonPoseUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public record ModifiableShoulderWeaponPose(CannonPoseUtil transform, String id, float x, float y, float z)
        implements GolemShoulderPose {

    public void setup(MetalGolemEntity entity, MetalGolemModel model, ItemStack stack, InteractionHand hand,
            float pTick) {
    }

    public void render(MetalGolemEntity entity, MetalGolemModel model, ItemStack stack, InteractionHand hand,
            PoseStack pose, MultiBufferSource source, int light, float pTick) {
        pose.pushPose();
        pose.translate(x, y, z);
        if (entity.animState.getStartingAnim() >= 5) {
            float[] angles = this.transform.getAngle(entity, hand);
            float diff = Mth.wrapDegrees(angles[0] * (180F / (float) Math.PI) + entity.yBodyRot);
            if (diff > 15.0F) {
                angles[0] = (15.0F - entity.yBodyRot) * ((float) Math.PI / 180F);
            }
            if (diff < -15.0F) {
                angles[0] = (-15.0F - entity.yBodyRot) * ((float) Math.PI / 180F);
            }

            float yRot = angles[0] + entity.yBodyRot * ((float) Math.PI / 180F);
            float xRot = angles[1];

            pose.mulPose(new Quaternionf().rotationZYX(0.0F, -yRot, xRot));
        } else {
            pose.rotateAround(
                    new Quaternionf().rotationZYX(0F, 0F,
                            (float) -Math.PI / 10 * (4 - entity.animState.getStartingAnim())),
                    0, 0.25f, -0.25f);
        }
        Minecraft.getInstance().getItemRenderer().renderStatic(stack, ItemDisplayContext.NONE,
                light, OverlayTexture.NO_OVERLAY, pose, source, Minecraft.getInstance().level, 0);
        pose.popPose();
    }
}
