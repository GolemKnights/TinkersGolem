package golemknights.tinkersgolem.events;

import com.tterrag.registrate.util.entry.RegistryEntry;
import dev.xkmc.modulargolems.content.core.GolemType;
import golemknights.tinkersgolem.SlimeGolemEntity;
import golemknights.tinkersgolem.TinkersGolem;
import golemknights.tinkersgolem.register.SlimeGolemPartType;
import golemknights.tinkersgolem.register.TGEntities;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = TinkersGolem.MODID, bus= Mod.EventBusSubscriber.Bus.MOD)
public class EntityEvents {
    public static RegistryEntry<GolemType<SlimeGolemEntity, SlimeGolemPartType>> SLIME_GOLEM_TYPE;

    @SubscribeEvent
    static void entityAttributes(EntityAttributeCreationEvent event) {
        event.put(TGEntities.slimeGolemEntity.get(), SlimeGolemEntity.createMonsterAttributes().build());
    }
}
