package golemknights.tinkersgolem.modifiers.golem;

import dev.xkmc.modulargolems.content.core.StatFilterType;
import dev.xkmc.modulargolems.content.entity.common.AbstractGolemEntity;
import dev.xkmc.modulargolems.content.modifier.base.GolemModifier;
import net.minecraftforge.event.entity.living.LivingDamageEvent;

public class DuritaeModifier extends GolemModifier {
    public DuritaeModifier() {
        super(StatFilterType.MASS, 5);
    }
    @Override
    public void onDamaged(AbstractGolemEntity<?, ?> entity, LivingDamageEvent event, int level) {
        for (int i = 0; i < level; ++i) {
            float chance = entity.getRandom().nextFloat();
            if (chance < 0.35){
                event.setAmount(event.getAmount() * 0.5f);
            } else if (chance < 0.5) {
                event.setAmount(event.getAmount() * 1.5f);
            }
        }
    }
}
