package golemknights.tinkersgolem.modifiers.slime;

import dev.xkmc.modulargolems.content.core.StatFilterType;
import dev.xkmc.modulargolems.content.entity.common.AbstractGolemEntity;
import golemknights.tinkersgolem.data.TGConfig;
import golemknights.tinkersgolem.entity.SlimeGolemEntity;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;

import java.util.List;

public class OvershockModifier extends SlimeModifier {

	public OvershockModifier() {
		super(StatFilterType.MASS, 5);
	}

	@Override
	public void onAiStep(AbstractGolemEntity<?, ?> golem, int level) {
		double factor = TGConfig.COMMON.overshockFactor.get();
		if (golem instanceof SlimeGolemEntity slime && slime.onGround() && !slime.wasOnGround()) {
			List<LivingEntity> list = golem.level().getEntitiesOfClass(LivingEntity.class, golem.getBoundingBox().inflate(1.5, 1, 1.5));
			list.remove(golem.getOwner());
			for (LivingEntity living : list) {
				if (!slime.predicateTarget(living)) continue;
				if (living.hurt(slime.damageSources().mobAttack(slime), (float) (slime.getAttributeValue(Attributes.ATTACK_DAMAGE) * factor))) {
					slime.doEnchantDamageEffects(slime, living);
				}
			}
		}
	}

	@Override
	public List<MutableComponent> getDetail(int v) {
		double factor = TGConfig.COMMON.overshockFactor.get();
		int val = (int) Math.round(factor * 100 * v);
		return List.of(Component.translatable(this.getDescriptionId() + ".desc", val).withStyle(ChatFormatting.GREEN));
	}
}
