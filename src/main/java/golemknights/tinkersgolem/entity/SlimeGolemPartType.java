package golemknights.tinkersgolem.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import dev.xkmc.modulargolems.content.core.IGolemPart;
import dev.xkmc.modulargolems.content.entity.dog.DogGolemPartType;
import dev.xkmc.modulargolems.content.item.golem.GolemPart;
import golemknights.tinkersgolem.register.TGEntities;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.ItemDisplayContext;
import org.jetbrains.annotations.Nullable;

import java.util.Locale;

public enum SlimeGolemPartType implements IGolemPart<SlimeGolemPartType> {
	INNER,
	OUTER;

	public MutableComponent getDesc(MutableComponent desc) {
		return Component.translatable("golem_part.slime_golem." +
						name().toLowerCase(Locale.ROOT), desc)
				.withStyle(ChatFormatting.GREEN);
	}

	public GolemPart<?, SlimeGolemPartType> toItem() {
		return switch (this) {
			case INNER -> TGEntities.SLIME_INNER.get();
			case OUTER -> TGEntities.SLIME_OUTER.get();
		};
	}

	@Override
	public void setupItemRender(PoseStack stack, ItemDisplayContext transform, @Nullable SlimeGolemPartType part) {
		//TODO 如下渲染从狗傀儡渲染复制。还需调整渲染角度和偏移

		switch (transform) {
			case GUI:
			case FIRST_PERSON_LEFT_HAND:
			case FIRST_PERSON_RIGHT_HAND:
				break;
			case THIRD_PERSON_LEFT_HAND:
			case THIRD_PERSON_RIGHT_HAND: {
				stack.translate(0.25, 0.4, 0.5);
				float size = 0.5F;
				stack.scale(size, size, size);
				break;
			}
			case GROUND: {
				stack.translate(0.25, 0, 0.5);
				float size = 0.5F;
				stack.scale(size, size, size);
				break;
			}
			case NONE:
			case HEAD:
			case FIXED: {
				stack.translate(0.5, 0.5, 0.5);
				float size = 1f;
				stack.scale(size, -size, size);
				stack.translate(0, -0.5, 0);
				return;
			}
			default:
				stack.translate(0.1, 0, 0.5);
				float size = 0.75F;
				stack.scale(size, size, size);
				break;
		}
		stack.mulPose(Axis.ZP.rotationDegrees(135));
		stack.mulPose(Axis.YP.rotationDegrees(-155));
		if (part == null) {
			float size = 1f;
			stack.scale(size, size, size);
			stack.translate(0, -1.9, 0);
		} else if (part == INNER) {
			float size = 1f;
			stack.scale(size, size, size);
			stack.translate(0, -1.9, 0);
		} else if (part == OUTER) {
			float size = 1f;
			stack.scale(size, size, size);
			stack.translate(0, -1.9, 0);
		}
	}

}

