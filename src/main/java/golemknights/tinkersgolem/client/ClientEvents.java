package golemknights.tinkersgolem.client;

import golemknights.tinkersgolem.TinkersGolem;
import golemknights.tinkersgolem.register.TGParticles;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = TinkersGolem.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientEvents {
	@SubscribeEvent
	static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
	}
    @SubscribeEvent
    public static void registerParticleFactories(RegisterParticleProvidersEvent event) {
        event.registerSpecial(TGParticles.DynamicItemBreak.get(), new DynamicItemBreakParticle.Provider());
    }
}
