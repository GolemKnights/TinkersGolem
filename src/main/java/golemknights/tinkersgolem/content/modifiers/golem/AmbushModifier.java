package golemknights.tinkersgolem.content.modifiers.golem;

import dev.xkmc.l2damagetracker.contents.attack.AttackCache;
import dev.xkmc.l2damagetracker.contents.attack.DamageModifier;
import dev.xkmc.modulargolems.content.core.StatFilterType;
import dev.xkmc.modulargolems.content.entity.common.AbstractGolemEntity;
import dev.xkmc.modulargolems.content.modifier.base.GolemModifier;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.LivingEntity;

import java.util.List;

public class AmbushModifier extends GolemModifier {
    public AmbushModifier() {
        super(StatFilterType.ATTACK, 4);
    }

    @Override
    public void modifyDamage(AttackCache cache, AbstractGolemEntity<?, ?> entity, int level) {
        float percent = 0;
        if (entity.getHealth() >= entity.getMaxHealth()) {
            percent += getPercent1();
        }
        LivingEntity target = cache.getAttackTarget();
        if (target.getHealth() >= target.getMaxHealth()) {
            percent += getPercent2();
        }
        if (percent > 0) {
            cache.addHurtModifier(DamageModifier.multTotal(1 + percent * level));
        }
    }

    private float getPercent1() {
        return 0.25f;
    }
    private float getPercent2() {
        return 0.15f;
    }

    @Override
    public List<MutableComponent> getDetail(int v) {
        int val1 = Math.round(getPercent1() * 100 * v);
        int val2 = Math.round(getPercent2() * 100 * v);
        return List.of(Component.translatable(this.getDescriptionId() + ".desc", val1, val2).withStyle(ChatFormatting.GREEN));
    }
}