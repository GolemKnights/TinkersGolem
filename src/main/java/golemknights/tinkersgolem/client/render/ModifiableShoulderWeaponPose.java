package golemknights.tinkersgolem.client.render;

import org.joml.Quaternionf;

import com.mojang.blaze3d.vertex.PoseStack;

import dev.xkmc.modulargolems.content.client.pose.GolemShoulderPose;
import dev.xkmc.modulargolems.content.entity.common.AbstractGolemEntity;
import dev.xkmc.modulargolems.content.entity.common.IGolemModel;
import dev.xkmc.modulargolems.content.entity.metalgolem.MetalGolemEntity;
import dev.xkmc.modulargolems.content.item.ranged.CannonPoseUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public record ModifiableShoulderWeaponPose(CannonPoseUtil transform, String id, float x, float y, float z)
        implements GolemShoulderPose {

    public void render(AbstractGolemEntity<?, ?> entity, IGolemModel<?, ?, ?> model, ItemStack stack, int slot,
            PoseStack pose, MultiBufferSource source, int light, float pTick) {
        pose.pushPose();
        try {
            pose.mulPose(new Quaternionf().rotationZYX((float) Math.PI, 0f, 0f));
            pose.translate(x, y, z);
            if (!(entity instanceof MetalGolemEntity metal) || metal.animState.getStartingAnim() >= 5) {
                float[] angles = this.transform.getAngle(entity, slot);
                float diff = Mth.wrapDegrees(angles[0] * (180F / (float) Math.PI) + entity.yBodyRot);

                if (diff > 15.0F) {
                    angles[0] = (15.0F - entity.yBodyRot) * ((float) Math.PI / 180F);
                }
                if (diff < -15.0F) {
                    angles[0] = (-15.0F - entity.yBodyRot) * ((float) Math.PI / 180F);
                }

                float yRot = angles[0] + entity.yBodyRot * ((float) Math.PI / 180F);
                float xRot = angles[1];

                LivingEntity target = entity.getTarget();
                if (target == null || !target.isAlive())
                    yRot = 0;

                pose.rotateAround(new Quaternionf().rotationZYX(0.0F, yRot, -xRot), 0, -0.5f, -0.1f);
            } else {
                float startingOrEnding = metal.animState.getStartingAnim() + pTick;
                if (startingOrEnding < 0)
                    startingOrEnding = 5 - metal.animState.getEndingAnim() - pTick;
                if (startingOrEnding < 0)
                    startingOrEnding = 0;
                pose.rotateAround(
                        new Quaternionf().rotationZYX(0F, 0F, (float) Math.PI / 10 * (5 - startingOrEnding)),
                        0, -0.7f, -0.25f);
            }
            Minecraft.getInstance().getItemRenderer().renderStatic(stack, ItemDisplayContext.NONE,
                    light, OverlayTexture.NO_OVERLAY, pose, source, Minecraft.getInstance().level, 0);
        } finally {
            pose.popPose();
        }
    }
}
