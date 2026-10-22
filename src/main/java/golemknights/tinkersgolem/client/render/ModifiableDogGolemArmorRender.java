package golemknights.tinkersgolem.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import dev.xkmc.modulargolems.content.entity.dog.DogGolemEntity;
import dev.xkmc.modulargolems.content.entity.dog.DogGolemModel;
import dev.xkmc.modulargolems.content.item.equipments.DogGolemArmorSpecialRenderer;
import golemknights.tinkersgolem.content.item.armor.ModifiableDogGolemArmorItem;
import net.minecraft.client.model.Model;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.item.ItemStack;
import slimeknights.tconstruct.library.client.armor.ArmorModelManager.ArmorModel;
import slimeknights.tconstruct.library.client.armor.texture.ArmorTextureSupplier;
import slimeknights.tconstruct.library.client.armor.texture.ArmorTextureSupplier.ArmorTexture;
import static golemknights.tinkersgolem.TinkersGolem.LOGGER;

public class ModifiableDogGolemArmorRender implements DogGolemArmorSpecialRenderer {

    public static final ModifiableDogGolemArmorRender INSTANCE = new ModifiableDogGolemArmorRender();
    private final Model bridge;
    private transient DogGolemModel model;

    public ModifiableDogGolemArmorRender() {
        bridge = new Model(RenderType::armorCutoutNoCull) {
            @Override
            public void renderToBuffer(PoseStack pose, VertexConsumer buffer, int packedLightIn, int packedOverlayIn,
                    float red, float green, float blue, float alpha) {
                model.root().render(pose, buffer, packedLightIn, OverlayTexture.NO_OVERLAY, red, green, blue, alpha);
            }
        };
    }

    @Override
    public void render(DogGolemEntity entity, ItemStack stack, PoseStack pose, MultiBufferSource source, int light,
            float f3, DogGolemModel model) {
        this.model = model;
        if (stack.getItem() instanceof ModifiableDogGolemArmorItem item) {
            ArmorModel armorModel = item.getModel(stack);
            var registryAccess = entity.level().registryAccess();
            for (ArmorTextureSupplier textureSupplier : armorModel.layers()) {
                ArmorTexture texture = textureSupplier.getArmorTexture(stack, ArmorTextureSupplier.TextureType.ARMOR,
                        registryAccess);
                if (texture != ArmorTexture.EMPTY) {
                    texture.renderTexture(bridge, pose, source, light, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1, false);
                }
            }
        } else {
            LOGGER.error("ModifiableDogGolemArmorRender render error, item is not ModifiableDogGolemArmorItem");
        }
    }
}
