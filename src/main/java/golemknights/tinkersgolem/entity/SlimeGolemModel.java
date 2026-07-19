package golemknights.tinkersgolem.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.xkmc.modulargolems.content.entity.common.IGolemModel;
import dev.xkmc.modulargolems.content.entity.common.IHeadedModel;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class SlimeGolemModel extends HierarchicalModel<SlimeGolemEntity>
		implements IGolemModel<SlimeGolemEntity, SlimeGolemPartType, SlimeGolemModel>, IHeadedModel {

	private final ModelPart root;
	private final ModelPart outer;

	public SlimeGolemModel(EntityModelSet set) {
		root = set.bakeLayer(ModelLayers.SLIME);
		outer = set.bakeLayer(ModelLayers.SLIME_OUTER);
	}

	@Override
	public ModelPart root() {
		return root;
	}

	@Override
	public void setupAnim(SlimeGolemEntity e, float p_102619_, float p_102620_, float p_102621_, float p_102622_,
			float p_102623_) {

	}

	@Override
	public RenderType renderType(SlimeGolemPartType part, ResourceLocation texture) {
		return part == SlimeGolemPartType.SHELL ? RenderType.entityTranslucent(texture)
				: RenderType.entityCutout(texture);
	}

	@Override
	public void renderToBufferInternal(SlimeGolemPartType type, PoseStack stack, VertexConsumer consumer, int i, int j,
			float f1, float f2, float f3, float f4) {
		if (type == SlimeGolemPartType.CORE) {
			root().render(stack, consumer, i, j, f1, f2, f3, f4);
		} else {
			outer.render(stack, consumer, i, j, f1, f2, f3, f4);
		}
	}

	@Override
	public ResourceLocation getTextureLocationInternal(ResourceLocation rl) {
		return rl.withPath(mat -> "textures/entity/slime_golem/" + mat + ".png");
	}

	@Override
	public void translateToHead(PoseStack poseStack) {
		root().translateAndRotate(poseStack);
	}

	@Override
	public ModelPart getHead() {
		return root();
	}

}
