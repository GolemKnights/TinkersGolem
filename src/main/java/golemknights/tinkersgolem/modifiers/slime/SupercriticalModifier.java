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
		if (golem.tickCount % 20 != 0) return;
		if (!(GolemOverslimeEvents.getOverslime(golem) >= golem.getMaxHealth())) return;
		if (golem.getGuardedDataImpl() < golem.getMaxHealth()) {
			GolemOverslimeEvents.removeOverslime(golem, golem.getMaxHealth() - golem.getGuardedDataImpl());
			golem.setGuardedDataImpl(golem.getMaxHealth(), false, false);
		} else if (slime.getSize() < 8) {
			GolemOverslimeEvents.removeOverslime(golem, golem.getMaxHealth());
			slime.setSize(slime.getSize() + 1, false);
			slime.addTag("NoSplit");
		}
	}

}
