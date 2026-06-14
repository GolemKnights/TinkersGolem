package golemknights.tinkersgolem.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import dev.xkmc.modulargolems.content.entity.dog.DogGolemEntity;
import dev.xkmc.modulargolems.content.entity.dog.DogGolemModel;
import dev.xkmc.modulargolems.content.item.equipments.DogGolemArmorSpecialRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import golemknights.tinkersgolem.item.armor.ModifiableDogGolemArmorItem;
import slimeknights.tconstruct.library.client.armor.texture.ArmorTextureSupplier;
import slimeknights.tconstruct.library.client.armor.texture.ArmorTextureSupplier.ArmorTexture;

import static golemknights.tinkersgolem.TinkersGolem.LOGGER;

import slimeknights.tconstruct.library.client.armor.ArmorModelManager.ArmorModel;
import slimeknights.tconstruct.library.client.armor.texture.TintedArmorTexture;
import static golemknights.tinkersgolem.client.render.ModifiableMetalGolemArmorRender.TEXTURE_HANDLE;

public class ModifiableDogGolemArmorRender implements DogGolemArmorSpecialRenderer {

    public static final ModifiableDogGolemArmorRender INSTANCE = new ModifiableDogGolemArmorRender();

    public ModifiableDogGolemArmorRender() {
    }

    @Override
    public void render(DogGolemEntity entity, ItemStack stack, PoseStack pose, MultiBufferSource source, int light,
            float f3, DogGolemModel model) {
        if (stack.getItem() instanceof ModifiableDogGolemArmorItem item) {
            ArmorModel armorModel = item.getModel(stack);
            var registryAccess = entity.level().registryAccess();
            for (ArmorTextureSupplier textureSupplier : armorModel.layers()) {
                ArmorTexture texture = textureSupplier.getArmorTexture(stack, ArmorTextureSupplier.TextureType.ARMOR,
                        registryAccess);
                if (texture != ArmorTexture.EMPTY) {
                    if (texture instanceof TintedArmorTexture tintedTexture) {
                        try {
                            var rl = (ResourceLocation) TEXTURE_HANDLE.invokeExact(tintedTexture);
                            VertexConsumer buffer = source.getBuffer(RenderType.armorCutoutNoCull(rl));
                            if (tintedTexture.luminosity() > 0) {
                                light = TintedArmorTexture.applyLuminosity(light, tintedTexture.luminosity());
                            }
                            int col = tintedTexture.color();
                            float r = (float) (col >> 16 & 255) / 255.0F;
                            float g = (float) (col >> 8 & 255) / 255.0F;
                            float b = (float) (col & 255) / 255.0F;
                            model.root().render(pose, buffer, light, OverlayTexture.NO_OVERLAY, r, g, b,
                                    1.0F);
                        } catch (Throwable e) {
                            LOGGER.error("ModifiableMetalGolemArmorRender render error, error: {}", e.getMessage());
                        }
                    } else
                        LOGGER.error("Not Support texture type: {}", texture);
                }
            }
        } else {
            LOGGER.error("ModifiableDogGolemArmorRender render error, item is not ModifiableDogGolemArmorItem");
        }
    }
}
