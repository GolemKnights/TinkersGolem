package golemknights.tinkersgolem.content.modifiers.golem;

import dev.xkmc.modulargolems.content.core.StatFilterType;
import dev.xkmc.modulargolems.content.entity.common.AbstractGolemEntity;
import dev.xkmc.modulargolems.content.modifier.base.GolemModifier;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

public class CrazyMetalModifier extends GolemModifier {
    public CrazyMetalModifier() {
        super(StatFilterType.MASS, 5);
    }
    @Override
    public void onHurtTarget(AbstractGolemEntity<?, ?> entity, LivingHurtEvent event, int level) {
        if (entity.getRandom().nextFloat() < 0.1 * level){
            entity.heal(event.getAmount() * 5);
        }
    }
}
