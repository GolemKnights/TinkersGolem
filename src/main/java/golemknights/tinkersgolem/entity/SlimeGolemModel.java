package golemknights.tinkersgolem.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.xkmc.modulargolems.content.client.armor.GolemEquipmentModels;
import dev.xkmc.modulargolems.content.entity.common.IGolemModel;
import dev.xkmc.modulargolems.content.entity.common.IHeadedModel;
import net.minecraft.client.model.SlimeModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class SlimeGolemModel extends SlimeModel<SlimeGolemEntity> implements IGolemModel<SlimeGolemEntity, SlimeGolemPartType, SlimeGolemModel>, IHeadedModel {
    public SlimeGolemModel(EntityModelSet set) {
        super(set.bakeLayer(GolemEquipmentModels.CHESTPLATE_LAYER));
    }


    @Override
    public void renderToBufferInternal(SlimeGolemPartType slimeGolemPartType, PoseStack poseStack, VertexConsumer vertexConsumer, int i, int i1, float v, float v1, float v2, float v3) {

    }

    @Override
    public ResourceLocation getTextureLocationInternal(ResourceLocation resourceLocation) {
        return null;
    }

    @Override
    public void translateToHead(PoseStack poseStack) {

    }

    @Override
    public ModelPart getHead() {
        return null;
    }
}
