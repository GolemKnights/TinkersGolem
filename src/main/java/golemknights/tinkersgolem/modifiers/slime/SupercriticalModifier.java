package golemknights.tinkersgolem.modifiers.slime;

import dev.xkmc.modulargolems.content.core.StatFilterType;
import dev.xkmc.modulargolems.content.entity.common.AbstractGolemEntity;
import golemknights.tinkersgolem.entity.SlimeGolemEntity;
import golemknights.tinkersgolem.events.GolemOverslimeEvents;

public class SupercriticalModifier extends SlimeModifier {

	public SupercriticalModifier() {
		super(StatFilterType.MASS, 1);
	}

	@Override
	public void onAiStep(AbstractGolemEntity<?, ?> golem, int level) {
		if (!(golem instanceof SlimeGolemEntity slime)) return;
		if (golem.tickCount % 20 != 4) return;
		if (GolemOverslimeEvents.getOverslime(golem) >= golem.getMaxHealth()) {
			if (golem.getGuardedDataImpl() < golem.getMaxHealth()) {
				GolemOverslimeEvents.addOverslime(golem, golem.getGuardedDataImpl() - golem.getMaxHealth());
				golem.setGuardedDataImpl(golem.getMaxHealth(), false, false);
			} else {
				GolemOverslimeEvents.addOverslime(golem, -golem.getMaxHealth());
				slime.setSize(slime.getSize() + 1, false);
				slime.addTag("NoSplit");
			}
		}
	}

}
