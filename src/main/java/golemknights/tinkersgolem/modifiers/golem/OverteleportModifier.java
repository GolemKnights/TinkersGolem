package golemknights.tinkersgolem.modifiers.golem;

import dev.xkmc.l2damagetracker.contents.attack.AttackCache;
import dev.xkmc.modulargolems.content.core.StatFilterType;
import dev.xkmc.modulargolems.content.entity.common.AbstractGolemEntity;
import dev.xkmc.modulargolems.content.modifier.base.GolemModifier;
import golemknights.tinkersgolem.data.TGConfig;
import golemknights.tinkersgolem.events.SlimeGolemTeleportEvent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import slimeknights.tconstruct.library.utils.TeleportHelper;

public class OverteleportModifier extends GolemModifier {
	public OverteleportModifier() {
		super(StatFilterType.MASS, 5);
	}

	private static void tp(LivingEntity living, int level) {
		int d = TGConfig.COMMON.overteleportRange.get() + (level - 1) * TGConfig.COMMON.overteleportRangePerLevel.get();
		TeleportHelper.ITeleportEventFactory teleportPredicate = (entity, x, y, z) -> new SlimeGolemTeleportEvent(entity, x, y, z, living);
		TeleportHelper.randomNearbyTeleport(living, teleportPredicate, d, d);
	}

	@Override
	public void onHurtTarget(AbstractGolemEntity<?, ?> entity, LivingHurtEvent event, int level) {
		LivingEntity target = event.getEntity();
		if (target instanceof LivingEntity) {
			tp(target, level);
		}
	}

	@Override
	public void onDamageMax(AttackCache cache, AbstractGolemEntity<?, ?> golem, int level) {
		if (golem.isAlive()) {
			tp(golem, level);
		}
	}
}
