package golemknights.tinkersgolem.modifiers.golem;

import dev.xkmc.modulargolems.content.core.StatFilterType;
import dev.xkmc.modulargolems.content.entity.common.AbstractGolemEntity;
import dev.xkmc.modulargolems.content.modifier.base.GolemModifier;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import slimeknights.tconstruct.common.Sounds;

public class OverbonkingModifier extends GolemModifier {
    public OverbonkingModifier() {
		super(StatFilterType.MASS, 5);
	}

    private static double getAngle(LivingEntity living, LivingEntity target){
        RandomSource random = living.getRandom();
        return (living.getX() - target.getX()) + random.nextGaussian() * 0.0075f;
    }

	@Override
    public void onHurtTarget(AbstractGolemEntity<?, ?> entity, LivingHurtEvent event, int level) {
        LivingEntity target = event.getEntity();
        if (target instanceof LivingEntity && entity.getRandom().nextInt(10) < level) {
            Vec3 angle = new Vec3(getAngle(entity, target), getAngle(entity, target), getAngle(entity, target));
            target.push(4, angle.x, angle.z);
            entity.level().playSound(null, entity.getX(), entity.getY(), entity.getZ(), Sounds.BONK.getSound(), entity.getSoundSource(), 1, 0.5f);
        }
    }
}
