package golemknights.tinkersgolem.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.xkmc.modulargolems.content.client.armor.GolemModelPath;
import dev.xkmc.modulargolems.content.entity.metalgolem.GolemEquipmentRenderer;
import dev.xkmc.modulargolems.content.entity.metalgolem.MetalGolemEntity;
import dev.xkmc.modulargolems.content.entity.metalgolem.MetalGolemModel;
import dev.xkmc.modulargolems.content.item.equipments.GolemItemSpecialRenderer;
import golemknights.tinkersgolem.content.item.armor.ModifiableMetalGolemArmorItem;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.item.ItemStack;
import slimeknights.tconstruct.library.client.armor.ArmorModelManager.ArmorModel;
import slimeknights.tconstruct.library.client.armor.texture.ArmorTextureSupplier;
import slimeknights.tconstruct.library.client.armor.texture.ArmorTextureSupplier.ArmorTexture;
import java.util.List;

import static golemknights.tinkersgolem.TinkersGolem.LOGGER;

public class ModifiableMetalGolemArmorRender implements GolemItemSpecialRenderer {

    public static final ModifiableMetalGolemArmorRender INSTANCE = new ModifiableMetalGolemArmorRender();
    private final Model bridge;
    private transient GolemModelPath gmpath;
    private transient MetalGolemModel model;

    public ModifiableMetalGolemArmorRender() {
        bridge = new Model(RenderType::armorCutoutNoCull) {
            @Override
            public void renderToBuffer(PoseStack pose, VertexConsumer buffer, int packedLightIn, int packedOverlayIn,
                    float red, float green, float blue, float alpha) {
                renderModel(model, gmpath, pose, buffer, packedLightIn, red, green, blue, alpha);
            }
        };
    }

    @Override

    public void render(MetalGolemEntity entity, ItemStack stack, PoseStack pose, MultiBufferSource source, int light,
            float pTick, GolemEquipmentRenderer renderer) {
        if (stack.getItem() instanceof ModifiableMetalGolemArmorItem mgaitem) {
            this.gmpath = GolemModelPath.get(mgaitem.getModelPath());
            this.model = renderer.map.get(this.gmpath.models());
            this.model.copyFrom(renderer.getParentModel());
            ArmorModel armorModel = mgaitem.getModel(stack);
            var registryAccess = entity.level().registryAccess();
            for (ArmorTextureSupplier textureSupplier : armorModel.layers()) {
                ArmorTexture texture = textureSupplier.getArmorTexture(stack, ArmorTextureSupplier.TextureType.ARMOR,
                        registryAccess);
                if (texture != ArmorTexture.EMPTY) {
                    texture.renderTexture(bridge, pose, source, light, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1, false);
                }
            }
            if (stack.hasFoil()) {
                VertexConsumer buffer = source.getBuffer(RenderType.armorEntityGlint());
                renderModel(this.model, this.gmpath, pose, buffer, light, 1, 1, 1, 1);
            }
        } else {
            LOGGER.error("ModifiableMetalGolemArmorRender render error, item is not ModifiableMetalGolemArmorItem");
        }
    }

    public void renderModel(MetalGolemModel model, GolemModelPath gmpath, PoseStack pose, VertexConsumer buffer,
            int light, float red, float green, float blue, float alpha) {
        for (List<String> ls : gmpath.paths()) {
            ModelPart gemr = model.root();
            pose.pushPose();

            for (String s : ls) {
                gemr.translateAndRotate(pose);
                gemr = gemr.getChild(s);
            }

            gemr.render(pose, buffer, light, OverlayTexture.NO_OVERLAY, red, green, blue, alpha);
            pose.popPose();
        }
    }
}