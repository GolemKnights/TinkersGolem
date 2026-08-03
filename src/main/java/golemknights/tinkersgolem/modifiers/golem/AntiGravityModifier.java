package golemknights.tinkersgolem.modifiers.golem;

import dev.xkmc.modulargolems.content.core.StatFilterType;
import dev.xkmc.modulargolems.content.entity.common.AbstractGolemEntity;
import dev.xkmc.modulargolems.content.modifier.base.GolemModifier;
import net.minecraft.world.effect.MobEffectInstance;
import slimeknights.tconstruct.shared.TinkerEffects;

public class AntiGravityModifier extends GolemModifier {
    public AntiGravityModifier() {
        super(StatFilterType.MASS, 1);
    }

    @Override
    public void onAiStep(AbstractGolemEntity<?, ?> golem, int level) {
        golem.addEffect(new MobEffectInstance(TinkerEffects.antigravity.get(), 100, 0, false, false));
    }
}
