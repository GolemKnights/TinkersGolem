package golemknights.tinkersgolem.content.modifiers.golem;

import dev.xkmc.modulargolems.content.core.StatFilterType;
import dev.xkmc.modulargolems.content.entity.common.AbstractGolemEntity;
import dev.xkmc.modulargolems.content.modifier.base.GolemModifier;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.AreaEffectCloud;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import slimeknights.mantle.util.CombatHelper;
import slimeknights.tconstruct.common.TinkerDamageTypes;
import slimeknights.tconstruct.library.utils.CustomExplosion;

public class FieryExplosionModifier extends GolemModifier {
    public FieryExplosionModifier() {
        super(StatFilterType.MASS, 5);
    }
    private static boolean recursive = false;

    @Override
    public void onHurtTarget(AbstractGolemEntity<?, ?> entity, LivingHurtEvent event, int level) {
        if (!recursive) {
            recursive = true;
            LivingEntity target = event.getEntity();
            if (target.isOnFire()) {
                float damage = event.getAmount();
                Level world = target.level();
                new CustomExplosion(world, target.position(), 3 * level, entity,
                        CustomExplosion.DEFAULT_ENTITY_PREDICATE.and(e -> e != entity), damage,
                        CombatHelper.damageSource(TinkerDamageTypes.MOB_EXPLOSION.melee(), entity), 1,
                        null, true, Explosion.BlockInteraction.KEEP, true
                ).handleServer();
                AreaEffectCloud cloud = new AreaEffectCloud(world, target.getX(), target.getY(), target.getZ());
                cloud.setRadius(3 * level);
                cloud.setDuration(30 * 20);
                cloud.setRadiusPerTick(-0.5f / 10);
                cloud.setOwner(entity);
                for (MobEffectInstance effect: target.getActiveEffects()){
                    cloud.addEffect(new MobEffectInstance(effect));
                }
                target.removeAllEffects();
                world.addFreshEntity(cloud);
            }
            recursive = false;
        }
    }
}
