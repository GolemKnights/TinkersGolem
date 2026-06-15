package golemknights.tinkersgolem.events;

import golemknights.tinkersgolem.TinkersGolem;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = TinkersGolem.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class EntityEvents {

	@SubscribeEvent
	static void entityAttributes(EntityAttributeCreationEvent event) {
	}

}
