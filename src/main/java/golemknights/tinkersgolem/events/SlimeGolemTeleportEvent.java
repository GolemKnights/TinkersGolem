package golemknights.tinkersgolem.events;

import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.EntityTeleportEvent;
import net.minecraftforge.eventbus.api.Cancelable;

@Cancelable
public class SlimeGolemTeleportEvent extends EntityTeleportEvent.EnderEntity {
    private final LivingEntity slime;

    public LivingEntity getEntity(){
        return slime;
    }

    public SlimeGolemTeleportEvent(LivingEntity entity, double targetX, double targetY, double targetZ, LivingEntity slime) {
        super(entity, targetX, targetY, targetZ);
        this.slime = slime;
    }

    public boolean isTeleportingSelf() {
        return getEntity() == slime;
    }
}
