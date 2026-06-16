package golemknights.tinkersgolem.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.xkmc.modulargolems.content.entity.common.AbstractGolemRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.util.Mth;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class SlimeGolemRenderer extends AbstractGolemRenderer<SlimeGolemEntity, SlimeGolemPartType, SlimeGolemModel> {

	public SlimeGolemRenderer(EntityRendererProvider.Context set) {
		super(set, new SlimeGolemModel(set.getModelSet()), 0.25F, SlimeGolemPartType::values);
	}

	@Override
	public void render(SlimeGolemEntity entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
		this.shadowRadius = 0.25F * (float) entity.getSize();
		super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
	}

	@Override
	protected void scale(SlimeGolemEntity e, PoseStack poseStack, float partialTickTime) {
		poseStack.scale(0.999F, 0.999F, 0.999F);
		poseStack.translate(0.0F, 0.001F, 0.0F);
		float f1 = e.isAddedToWorld() ? e.getSize() : 1;
		float f2 = Mth.lerp(partialTickTime, e.oSquish, e.squish) / (f1 * 0.5F + 1.0F);
		float f3 = 1.0F / (f2 + 1.0F);
		poseStack.scale(f3 * f1, 1.0F / f3 * f1, f3 * f1);
	}

}
