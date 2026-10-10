package golemknights.tinkersgolem.content.modifiers.golem;

import dev.xkmc.l2damagetracker.contents.attack.AttackCache;
import dev.xkmc.l2damagetracker.contents.attack.DamageModifier;
import dev.xkmc.modulargolems.content.core.StatFilterType;
import dev.xkmc.modulargolems.content.entity.common.AbstractGolemEntity;
import dev.xkmc.modulargolems.content.modifier.base.GolemModifier;
import golemknights.tinkersgolem.events.GolemOverslimeEvents;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

import java.util.List;

public class OverwieldModifier extends GolemModifier {
    public OverwieldModifier() {
        super(StatFilterType.MASS, 5);
    }

    @Override
    public void modifyDamage(AttackCache cache, AbstractGolemEntity<?, ?> entity, int level) {
        float amount = GolemOverslimeEvents.removeOverslime(entity, level);
        cache.addHurtModifier(DamageModifier.multTotal(1 + getPercent() * amount));
    }

    private float getPercent() {
        return 0.1f;
    }

    @Override
    public List<MutableComponent> getDetail(int v) {
        int val = Math.round(getPercent() * 100);
        return List.of(Component.translatable(this.getDescriptionId() + ".desc", v, val).withStyle(ChatFormatting.GREEN));
    }
}
