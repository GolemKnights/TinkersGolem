package golemknights.tinkersgolem.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import dev.xkmc.modulargolems.content.client.armor.GolemModelPath;
import dev.xkmc.modulargolems.content.entity.metalgolem.GolemEquipmentRenderer;
import dev.xkmc.modulargolems.content.entity.metalgolem.MetalGolemEntity;
import dev.xkmc.modulargolems.content.entity.metalgolem.MetalGolemModel;
import dev.xkmc.modulargolems.content.item.equipments.GolemItemSpecialRenderer;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import golemknights.tinkersgolem.item.armor.ModifiableMetalGolemArmorItem;
import slimeknights.tconstruct.library.client.armor.texture.ArmorTextureSupplier;
import slimeknights.tconstruct.library.client.armor.texture.ArmorTextureSupplier.ArmorTexture;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Field;
import java.util.List;

import static golemknights.tinkersgolem.TinkersGolem.LOGGER;

import slimeknights.tconstruct.library.client.armor.ArmorModelManager.ArmorModel;
import slimeknights.tconstruct.library.client.armor.texture.TintedArmorTexture;

public class ModifiableMetalGolemArmorRender implements GolemItemSpecialRenderer {

    public static final ModifiableMetalGolemArmorRender INSTANCE = new ModifiableMetalGolemArmorRender();

    public ModifiableMetalGolemArmorRender() {
    }

    private static final MethodHandle TEXTURE_HANDLE;

    static {
        try {
            Field f = TintedArmorTexture.class.getDeclaredField("texture");
            f.setAccessible(true);
            TEXTURE_HANDLE = MethodHandles.lookup().unreflectGetter(f);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override

    public void render(MetalGolemEntity entity, ItemStack stack, PoseStack pose, MultiBufferSource source, int light,
            float pTick, GolemEquipmentRenderer renderer) {
        if (stack.getItem() instanceof ModifiableMetalGolemArmorItem mgaitem) {
            GolemModelPath gmpath = GolemModelPath.get(mgaitem.getModelPath());
            MetalGolemModel model = (MetalGolemModel) renderer.map.get(gmpath.models());
            model.copyFrom((MetalGolemModel) renderer.getParentModel());
            ArmorModel armorModel = mgaitem.getModel(stack);
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
                            renderModel(model, gmpath, pose, buffer, light, tintedTexture.color());
                            if (stack.hasFoil()) {
                                buffer = source.getBuffer(RenderType.armorEntityGlint());
                                renderModel(model, gmpath, pose, buffer, light, -1);
                            }
                        } catch (Throwable e) {
                            LOGGER.error("ModifiableMetalGolemArmorRender render error, error: {}", e.getMessage());
                        }
                    } else
                        LOGGER.error("Not Support texture type: {}", texture);
                }
            }
        } else {
            LOGGER.error("ModifiableMetalGolemArmorRender render error, item is not ModifiableMetalGolemArmorItem");
        }
    }

    public void renderModel(MetalGolemModel model, GolemModelPath gmpath, PoseStack pose, VertexConsumer buffer,
            int light, int color) {
        for (List<String> ls : gmpath.paths()) {
            ModelPart gemr = model.root();
            pose.pushPose();

            for (String s : ls) {
                gemr.translateAndRotate(pose);
                gemr = gemr.getChild(s);
            }
            /** Renders a colored model */
            float red = 1.0F, green = 1.0F, blue = 1.0F, alpha = 1.0F;
            if (color != -1) {
                alpha *= (float) (color >> 24 & 255) / 255.0F;
                red *= (float) (color >> 16 & 255) / 255.0F;
                green *= (float) (color >> 8 & 255) / 255.0F;
                blue *= (float) (color & 255) / 255.0F;
            }
            gemr.render(pose, buffer, light, OverlayTexture.NO_OVERLAY, red, green, blue, alpha);
            pose.popPose();
        }
    }
}