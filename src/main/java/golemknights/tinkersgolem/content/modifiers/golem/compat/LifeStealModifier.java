package golemknights.tinkersgolem.content.modifiers.golem.compat;

import dev.xkmc.modulargolems.content.core.StatFilterType;
import dev.xkmc.modulargolems.content.entity.common.AbstractGolemEntity;
import dev.xkmc.modulargolems.content.modifier.base.GolemModifier;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

import java.util.List;

public class LifeStealModifier extends GolemModifier {
    public LifeStealModifier() {
        super(StatFilterType.MASS, 10);
    }
    @Override
    public void onHurtTarget(AbstractGolemEntity<?, ?> entity, LivingHurtEvent event, int level) {
        entity.heal(event.getAmount() * getPercent() * level);
    }

    private float getPercent() {
        return 0.05f;
    }

    @Override
    public List<MutableComponent> getDetail(int v) {
        int val = Math.round(getPercent() * 100 * v);
        return List.of(Component.translatable(this.getDescriptionId() + ".desc", val).withStyle(ChatFormatting.GREEN));
    }
}
