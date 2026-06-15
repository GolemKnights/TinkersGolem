package golemknights.tinkersgolem.register;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.xkmc.modulargolems.content.core.IGolemPart;
import dev.xkmc.modulargolems.content.item.golem.GolemPart;
import dev.xkmc.modulargolems.init.registrate.GolemItems;
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
        return Component.translatable("golem_part.dog_golem." + this.name().toLowerCase(Locale.ROOT), new Object[]{desc}).withStyle(ChatFormatting.GREEN);
    }

    public GolemPart<?, SlimeGolemPartType> toItem() {
        GolemPart var10000;
        switch (this) {
            case INNER -> var10000 = GolemItems.DOG_BODY.get();
            case OUTER -> var10000 = GolemItems.DOG_LEGS.get();
            default -> throw new IncompatibleClassChangeError();
        }

        return var10000;
    }

    @Override
    public void setupItemRender(PoseStack stack, ItemDisplayContext transform, @Nullable SlimeGolemPartType part) {

    }
}

