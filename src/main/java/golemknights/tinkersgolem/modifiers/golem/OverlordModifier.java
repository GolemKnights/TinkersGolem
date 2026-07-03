package golemknights.tinkersgolem.modifiers.golem;

import dev.xkmc.l2damagetracker.contents.attack.AttackCache;
import dev.xkmc.modulargolems.content.core.StatFilterType;
import dev.xkmc.modulargolems.content.entity.common.AbstractGolemEntity;
import dev.xkmc.modulargolems.content.modifier.base.GolemModifier;
import golemknights.tinkersgolem.data.TGConfig;
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
		GolemOverslimeEvents.addOverslime(golem, cache.getDamageDealt() * TGConfig.COMMON.overlordFactor.get().floatValue() * value);
	}

	@Override
	public List<MutableComponent> getDetail(int v) {
		int val = Math.round(TGConfig.COMMON.overlordFactor.get().floatValue() * 100 * v);
		return List.of(Component.translatable(this.getDescriptionId() + ".desc", val).withStyle(ChatFormatting.GREEN));
	}

}
