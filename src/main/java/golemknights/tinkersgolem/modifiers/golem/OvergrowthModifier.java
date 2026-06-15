package golemknights.tinkersgolem.modifiers.golem;

import dev.xkmc.modulargolems.content.core.StatFilterType;
import dev.xkmc.modulargolems.content.entity.common.AbstractGolemEntity;
import dev.xkmc.modulargolems.content.modifier.base.GolemModifier;
import golemknights.tinkersgolem.events.GolemOverslimeEvents;

import java.util.Random;

public class OvergrowthModifier extends GolemModifier {
    public OvergrowthModifier(StatFilterType type, int maxLevel) {
        super(type, maxLevel);
    }

    @Override
    public void onAiStep(AbstractGolemEntity<?, ?> golem, int level) {
        if (golem.tickCount % 20 == 0) {
            if (new Random().nextFloat() < 0.05 * level) {
                GolemOverslimeEvents.addOverslime(golem, 1);
            }
        }
    }
}
