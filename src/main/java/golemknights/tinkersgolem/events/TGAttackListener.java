package golemknights.tinkersgolem.events;

import dev.xkmc.l2damagetracker.contents.attack.AttackCache;
import dev.xkmc.l2damagetracker.contents.attack.AttackListener;
import dev.xkmc.l2damagetracker.contents.attack.CreateSourceEvent;
import dev.xkmc.l2damagetracker.contents.attack.DamageModifier;
import dev.xkmc.modulargolems.content.entity.common.AbstractGolemEntity;
import net.minecraft.world.item.ItemStack;

public class TGAttackListener implements AttackListener {

	// 伤害来源修改
	@Override
	public void onCreateSource(CreateSourceEvent event) {
	}

	// 造成伤害阶段
	@Override
	public void onHurt(AttackCache cache, ItemStack weapon) {
	}

	// 受到伤害阶段
	@Override
	public void onDamage(AttackCache cache, ItemStack weapon) {
		if (cache.getAttackTarget() instanceof AbstractGolemEntity<?, ?> golem) {
			// dealt modifier 为受到伤害时的减伤计算，nonLinearMiddle处于计算后端，其中优先级200在大部分增伤之后
			cache.addDealtModifier(DamageModifier.nonlinearMiddle(200, v -> GolemOverslimeEvents.onGolemHurt(golem, v)));
		}
	}

}
