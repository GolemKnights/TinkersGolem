package golemknights.tinkersgolem.content.modifiers.golem;

import dev.xkmc.modulargolems.content.core.StatFilterType;
import dev.xkmc.modulargolems.content.entity.common.AbstractGolemEntity;
import dev.xkmc.modulargolems.content.modifier.base.GolemModifier;
import dev.xkmc.modulargolems.init.data.MGLangData;
import golemknights.tinkersgolem.content.modifiers.TGModifierUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffectUtil;
import net.minecraft.world.effect.MobEffects;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

import java.util.List;

public class LightlyAttackModifier extends GolemModifier {
    public LightlyAttackModifier() {
        super(StatFilterType.MASS, 5);
    }

    @Override
    public void onHurtTarget(AbstractGolemEntity<?, ?> entity, LivingHurtEvent event, int level) {
        TGModifierUtil.applyEffect(entity, new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 200, level - 1, false, false));
    }

    @Override
    public List<MutableComponent> getDetail(int v) {
        MobEffectInstance ins = new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 200, v - 1, false, false);
        MutableComponent lang = Component.translatable(ins.getDescriptionId());
        MobEffect mobeffect = ins.getEffect();
        if (ins.getAmplifier() > 0) {
            lang = Component.translatable("potion.withAmplifier", lang, Component.translatable("potion.potency." + ins.getAmplifier()));
        }

        if (ins.getDuration() >= 20) {
            lang = Component.translatable("potion.withDuration", lang, MobEffectUtil.formatDuration(ins, 1.0F));
        }

        lang = lang.withStyle(mobeffect.getCategory().getTooltipFormatting());
        return List.of(MGLangData.POTION_ATTACK.get(new Object[]{lang}).withStyle(ChatFormatting.GREEN));
    }
}
