package golemknights.tinkersgolem.content.modifiers.golem;

import dev.xkmc.modulargolems.content.core.StatFilterType;
import dev.xkmc.modulargolems.content.entity.common.AbstractGolemEntity;
import dev.xkmc.modulargolems.content.modifier.base.GolemModifier;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.living.LivingDeathEvent;

public class SwallowModifier extends GolemModifier {
    public SwallowModifier() {
        super(StatFilterType.MASS, 5);
    }

    @Override
    public void onKillTarget(AbstractGolemEntity<?, ?> golem, LivingEntity entity, LivingDeathEvent event, int level) {
        golem.heal(entity.getMaxHealth() * 0.2f * level);
    }
}
