package golemknights.tinkersgolem.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.xkmc.modulargolems.content.core.IGolemPart;
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
			default -> throw new IncompatibleClassChangeError();
		};
	}

	@Override
	public void setupItemRender(PoseStack stack, ItemDisplayContext transform, @Nullable SlimeGolemPartType part) {

	}

}

