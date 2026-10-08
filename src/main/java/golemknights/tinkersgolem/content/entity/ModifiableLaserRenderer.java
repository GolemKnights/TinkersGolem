package golemknights.tinkersgolem.content.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import dev.xkmc.modulargolems.content.entity.metalgolem.BeaconRenderer;
import dev.xkmc.modulargolems.content.entity.metalgolem.MetalGolemEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.DyeColor;

public class ModifiableLaserRenderer extends EntityRenderer<ModifiableLaserEntity> {
    public ModifiableLaserRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    public boolean shouldRender(ModifiableLaserEntity p_114491_, Frustum p_114492_, double p_114493_, double p_114494_, double p_114495_) {
        return true;
    }

    public void render(ModifiableLaserEntity e, float yrot, float pTick, PoseStack pose, MultiBufferSource source, int light) {
        LivingEntity var8 = e.getOwner();
        if (var8 instanceof MetalGolemEntity golem) {
            pose.pushPose();
            pose.mulPose(Axis.YP.rotationDegrees(e.getYRot()));
            pose.mulPose(Axis.XP.rotationDegrees(e.getXRot() + 90.0F));
            float perc = Math.max(0.0F, 1.0F - e.tickCount / (float)e.life);
            float r = golem.getScale() * 0.5F * perc * perc;
            pose.scale(r, 1.0F, r);
            BeaconRenderer.renderBeam(pose, source, 0.0F, 1.0F, e.len, DyeColor.WHITE.getTextureDiffuseColors());
            pose.popPose();
        }
    }

    public ResourceLocation getTextureLocation(ModifiableLaserEntity e) {
        return BeaconRenderer.BEAM_LOCATION;
    }
}
