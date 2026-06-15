package golemknights.tinkersgolem.register;

import golemknights.tinkersgolem.SlimeGolemEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import slimeknights.mantle.registration.object.EntityObject;

import static golemknights.tinkersgolem.TinkersGolem.ENTITIES;

public class TGEntities {
    public static void load() {
    }
    public static final EntityObject<SlimeGolemEntity> slimeGolemEntity = ENTITIES.registerWithEgg("slime_golem", () ->
            EntityType.Builder.of(SlimeGolemEntity::new, MobCategory.MONSTER)
                    .setShouldReceiveVelocityUpdates(true)
                    .setTrackingRange(20)
                    .sized(2.04F, 2.04F)
                    .setCustomClientFactory((spawnEntity, world) -> TGEntities.slimeGolemEntity.get().create(world)), 0x4278e6, 0x2a60d7);

}
