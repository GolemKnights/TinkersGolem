package golemknights.tinkersgolem.modifiers.golem;

import dev.xkmc.modulargolems.content.core.StatFilterType;
import dev.xkmc.modulargolems.content.entity.common.AbstractGolemEntity;
import dev.xkmc.modulargolems.content.modifier.base.GolemModifier;
import golemknights.tinkersgolem.data.TGConfig;
import golemknights.tinkersgolem.events.GolemOverslimeEvents;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

import java.util.List;

public class OvergrowthModifier extends GolemModifier {
    public OvergrowthModifier() {
        super(StatFilterType.MASS, 5);
    }

    @Override
    public void onAiStep(AbstractGolemEntity<?, ?> golem, int level) {
        if (golem.tickCount % 20 == 0) {
            GolemOverslimeEvents.addOverslime(golem, TGConfig.COMMON.overgrowthRate.get().floatValue() * level);
        }
    }

    @Override
    public List<MutableComponent> getDetail(int v) {
        return List.of(Component.translatable(this.getDescriptionId() + ".desc", TGConfig.COMMON.overgrowthRate.get().floatValue() * v).withStyle(ChatFormatting.GREEN));
    }

}
