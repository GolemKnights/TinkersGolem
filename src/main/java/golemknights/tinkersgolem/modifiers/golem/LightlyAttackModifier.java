package golemknights.tinkersgolem.modifiers.golem;

import dev.xkmc.modulargolems.content.core.StatFilterType;
import dev.xkmc.modulargolems.content.entity.common.AbstractGolemEntity;
import dev.xkmc.modulargolems.content.modifier.base.GolemModifier;
import golemknights.tinkersgolem.modifiers.TGModifierUtil;
import net.minecraft.world.effect.MobEffects;
import net.minecraftforge.event.entity.living.LivingAttackEvent;

public class LightlyAttackModifier extends GolemModifier {
    public LightlyAttackModifier() {
        super(StatFilterType.MASS, 5);
    }

    @Override
    public void onAttackTarget(AbstractGolemEntity<?, ?> entity, LivingAttackEvent event, int level) {
        TGModifierUtil.updateEffect(entity, MobEffects.MOVEMENT_SPEED, 1, level + 1, 200);
    }
}
