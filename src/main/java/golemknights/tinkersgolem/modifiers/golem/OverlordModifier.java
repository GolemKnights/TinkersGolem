package golemknights.tinkersgolem.modifiers.golem;

import dev.xkmc.l2damagetracker.contents.attack.AttackCache;
import dev.xkmc.modulargolems.content.core.StatFilterType;
import dev.xkmc.modulargolems.content.entity.common.AbstractGolemEntity;
import dev.xkmc.modulargolems.content.modifier.base.GolemModifier;
import golemknights.tinkersgolem.events.GolemOverslimeEvents;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

import java.util.List;

public class OverlordModifier extends GolemModifier {
	public OverlordModifier() {
		super(StatFilterType.HEALTH, 5);
	}

	@Override
	public void onDamageMax(AttackCache cache, AbstractGolemEntity<?, ?> golem, int value) {
		GolemOverslimeEvents.addOverslime(golem, cache.getDamageDealt() * 0.1f * value);
	}

	@Override
	public List<MutableComponent> getDetail(int v) {
		int val = Math.round(0.1f * 100 * v);
		return List.of(Component.translatable(this.getDescriptionId() + ".desc", val).withStyle(ChatFormatting.GREEN));
	}

}
