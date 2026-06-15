package golemknights.tinkersgolem.modifiers.golem;

import dev.xkmc.l2damagetracker.contents.attack.AttackCache;
import dev.xkmc.modulargolems.content.core.StatFilterType;
import dev.xkmc.modulargolems.content.entity.common.AbstractGolemEntity;
import dev.xkmc.modulargolems.content.modifier.base.GolemModifier;
import golemknights.tinkersgolem.events.GolemOverslimeEvents;

public class OverlordModifier extends GolemModifier {
    public OverlordModifier() {
        super(StatFilterType.HEALTH, 5);
    }

    @Override
    public void onDamageMax(AttackCache cache, AbstractGolemEntity<?, ?> golem, int value) {
        GolemOverslimeEvents.addOverslime(golem, cache.getDamageDealt() * 0.1f * value);
    }
}
