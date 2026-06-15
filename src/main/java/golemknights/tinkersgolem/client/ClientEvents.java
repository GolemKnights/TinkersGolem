package golemknights.tinkersgolem.client;

import golemknights.tinkersgolem.TinkersGolem;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = TinkersGolem.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientEvents {
	@SubscribeEvent
	static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
	}
}
