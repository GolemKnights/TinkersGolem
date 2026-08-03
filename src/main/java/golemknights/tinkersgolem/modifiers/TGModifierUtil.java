package golemknights.tinkersgolem.modifiers;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;

public class TGModifierUtil {
    public static void updateEffect(LivingEntity entity, MobEffect effect, int addLevel, int maxLevel, int time){
        MobEffectInstance mobeffect = entity.getEffect(effect);
        if (mobeffect != null){
            entity.addEffect(new MobEffectInstance(effect, time, Math.min(mobeffect.getAmplifier() + addLevel, maxLevel - 1)));
        }else {
            entity.addEffect(new MobEffectInstance(effect, time, Math.min(addLevel - 1, maxLevel - 1)));
        }
    }
}
