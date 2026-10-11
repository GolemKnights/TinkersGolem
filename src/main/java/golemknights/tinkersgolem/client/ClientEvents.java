package golemknights.tinkersgolem.client;

import dev.xkmc.modulargolems.content.client.pose.GolemShoulderPose;
import dev.xkmc.modulargolems.content.item.ranged.CannonPoseUtil;
import golemknights.tinkersgolem.TinkersGolem;
import golemknights.tinkersgolem.client.render.ModifiableShoulderWeaponPose;
import golemknights.tinkersgolem.register.TGEntities;
import golemknights.tinkersgolem.register.TGParticles;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterItemDecorationsEvent;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static dev.xkmc.modulargolems.content.client.armor.GolemEquipmentModels.regAndAdd;

@Mod.EventBusSubscriber(modid = TinkersGolem.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientEvents {
	@SubscribeEvent
	static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
	}

	@SubscribeEvent
	public static void registerParticleFactories(RegisterParticleProvidersEvent event) {
		event.registerSpecial(TGParticles.DynamicItemBreak.get(), new DynamicItemBreakParticle.Provider());
	}

	@SubscribeEvent
	public static void registerItemDeco(RegisterItemDecorationsEvent event) {
		event.register(TGEntities.HOLDER_SLIME.get(), new SlimeItemDeco());
	}

	@SubscribeEvent
	public static void dispatchEntityLayer(EntityRenderersEvent.RegisterLayerDefinitions event) {
		regAndAdd(event, ModifiableMetalGolemArmorModel.LAYER_LOCATION,
				ModifiableMetalGolemArmorModel::createBodyLayer);
		GolemShoulderPose.register(TGModelPaths.CANNON_RIGHT,
				new ModifiableShoulderWeaponPose(CannonPoseUtil.FLAME_THROWER, "cannon", -0.3F, -0.6F, 0.25F));
		GolemShoulderPose.register(TGModelPaths.CANNON_LEFT,
				new ModifiableShoulderWeaponPose(CannonPoseUtil.FLAME_THROWER, "cannon2", 0.3F, -0.6F, 0.25F));
	}

}
