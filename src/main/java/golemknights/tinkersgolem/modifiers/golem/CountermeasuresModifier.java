package golemknights.tinkersgolem.modifiers.golem;

import dev.xkmc.l2damagetracker.contents.attack.AttackCache;
import dev.xkmc.modulargolems.content.core.StatFilterType;
import dev.xkmc.modulargolems.content.entity.common.AbstractGolemEntity;
import dev.xkmc.modulargolems.content.modifier.base.GolemModifier;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraftforge.common.Tags;
import net.minecraftforge.event.entity.living.LivingDamageEvent;

public class CountermeasuresModifier extends GolemModifier {
    public CountermeasuresModifier() {
        super(StatFilterType.MASS, 5);
    }

    @Override
    public void onDamaged(AbstractGolemEntity<?, ?> entity, LivingDamageEvent event, int level) {
        if (event.getSource().is(DamageTypeTags.BYPASSES_ARMOR)){
            event.setAmount(event.getAmount() * Math.max(0, 1 - 0.1f * level));
        }
    }
}
