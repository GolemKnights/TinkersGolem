package golemknights.tinkersgolem.content.modifiers.golem;

import dev.xkmc.modulargolems.content.core.StatFilterType;
import dev.xkmc.modulargolems.content.entity.common.AbstractGolemEntity;
import dev.xkmc.modulargolems.content.modifier.base.GolemModifier;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;

import java.util.List;

public class WardedModifier extends GolemModifier {
    public WardedModifier() {
        super(StatFilterType.HEALTH, 4);
    }

    public void onDamaged(AbstractGolemEntity<?, ?> entity, LivingDamageEvent event, int level) {
        if (event.getAmount() > 1) {
            if (entity.getHealth() >= entity.getMaxHealth()) {
                event.setAmount(Math.max(1, event.getAmount() - level * getAmount()));
            }
        }
    }

    private float getAmount() {
        return 2.5f;
    }

    public List<MutableComponent> getDetail(int v) {
        double reduce = v * getAmount();
        return List.of(Component.translatable(this.getDescriptionId() + ".desc", reduce).withStyle(ChatFormatting.GREEN));
    }
}