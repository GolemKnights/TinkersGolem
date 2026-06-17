package golemknights.tinkersgolem.modifiers.slime;

import dev.xkmc.modulargolems.content.core.StatFilterType;
import dev.xkmc.modulargolems.content.item.golem.GolemPart;
import dev.xkmc.modulargolems.content.modifier.base.GolemModifier;
import golemknights.tinkersgolem.register.TGEntities;

public class SlimeModifier extends GolemModifier {

	public SlimeModifier(StatFilterType type, int maxLevel) {
		super(type, maxLevel);
	}

	@Override
	public boolean canExistOn(GolemPart<?, ?> part) {
		return part.getEntityType() == TGEntities.TYPE_SLIME.get();
	}

}
