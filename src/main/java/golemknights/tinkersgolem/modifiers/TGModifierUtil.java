package golemknights.tinkersgolem.modifiers;

import dev.xkmc.l2library.base.effects.EffectUtil;
import dev.xkmc.modulargolems.content.entity.common.AbstractGolemEntity;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;

public class TGModifierUtil {
    public static void updateEffect(LivingEntity entity, MobEffect effect, int addLevel, int maxLevel, int time){
        updateEffect(entity, entity, effect, addLevel, maxLevel, time);
    }
    public static void updateEffect(LivingEntity entity, LivingEntity target, MobEffect effect, int addLevel, int maxLevel, int time){
        MobEffectInstance mobEffect = entity.getEffect(effect);
        MobEffectInstance newEffect;
        if (mobEffect != null){
            newEffect = new MobEffectInstance(effect, time, Math.min(mobEffect.getAmplifier() + addLevel, maxLevel - 1));
        } else {
            newEffect = new MobEffectInstance(effect, time, Math.min(addLevel - 1, maxLevel - 1));
        }
        applyEffect(entity, target, newEffect);
    }

    public static void applyEffect(LivingEntity entity, MobEffectInstance effect) {
        applyEffect(entity, entity, effect);
    }
    public static void applyEffect(LivingEntity entity, LivingEntity target, MobEffectInstance effect) {
        if (!target.level().isClientSide()) {
            EffectUtil.addEffect(target, effect, EffectUtil.AddReason.NONE, entity);
        }
    }
}
