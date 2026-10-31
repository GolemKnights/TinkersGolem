package golemknights.tinkersgolem.content.modifiers.golem;

import dev.xkmc.modulargolems.content.core.StatFilterType;
import dev.xkmc.modulargolems.content.entity.common.AbstractGolemEntity;
import dev.xkmc.modulargolems.content.modifier.base.GolemModifier;
import golemknights.tinkersgolem.events.GolemOverslimeEvents;
import golemknights.tinkersgolem.register.TGAttributes;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.living.LivingDeathEvent;

import java.util.List;

public class OvereatModifier extends GolemModifier {
    public OvereatModifier() {
        super(StatFilterType.MASS, 5);
    }

    @Override
    public void onKillTarget(AbstractGolemEntity<?, ?> golem, LivingEntity entity, LivingDeathEvent event, int level) {
        float origin = entity.getMaxHealth() * getPercent() * level;
        float amount = GolemOverslimeEvents.addOverslime(golem, origin);
        var ins = entity.getAttribute(TGAttributes.OVERSLIME_RECOVERY.get());
        if (ins != null)
            amount /= (float) ins.getValue();
        if (origin > amount)
            golem.heal(origin - amount);
    }

    private float getPercent() {
        return 0.2f;
    }

    @Override
    public List<MutableComponent> getDetail(int v) {
        int val = Math.round(getPercent() * 100 * v);
        return List.of(Component.translatable(this.getDescriptionId() + ".desc", val).withStyle(ChatFormatting.GREEN));
    }
}
