package golemknights.tinkersgolem.modifiers.golem;

import dev.xkmc.modulargolems.content.core.StatFilterType;
import dev.xkmc.modulargolems.content.entity.common.AbstractGolemEntity;
import dev.xkmc.modulargolems.content.modifier.base.GolemModifier;
import golemknights.tinkersgolem.events.GolemOverslimeEvents;

public class OvergrowthModifier extends GolemModifier {
	public OvergrowthModifier() {
		super(StatFilterType.MASS, 5);
	}

	@Override
	public void onAiStep(AbstractGolemEntity<?, ?> golem, int level) {
		if (golem.tickCount % 20 == 0) {
			float rate = 0.05f * level;
			int recover = (int) rate;
			if (golem.getRandom().nextFloat() < rate - recover) recover++;
			if (recover > 0)
				GolemOverslimeEvents.addOverslime(golem, recover);
		}
	}

}
