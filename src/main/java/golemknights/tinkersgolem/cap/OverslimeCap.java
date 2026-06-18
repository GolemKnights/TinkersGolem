package golemknights.tinkersgolem.cap;

import dev.xkmc.l2library.capability.entity.GeneralCapabilityHolder;
import dev.xkmc.l2library.capability.entity.GeneralCapabilityTemplate;
import dev.xkmc.l2serial.serialization.SerialClass;
import dev.xkmc.modulargolems.content.entity.common.GuardedEntity;
import golemknights.tinkersgolem.TinkersGolem;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;

@SerialClass
public class OverslimeCap extends GeneralCapabilityTemplate<GuardedEntity, OverslimeCap> {

	public static final Capability<OverslimeCap> CAPABILITY = CapabilityManager.get(new CapabilityToken<>() {
	});
	public static final GeneralCapabilityHolder<GuardedEntity, OverslimeCap> HOLDER =
			new GeneralCapabilityHolder<>(TinkersGolem.getResource("overslime"), CAPABILITY,
					OverslimeCap.class, OverslimeCap::new, GuardedEntity.class, (e) -> true);

	@SerialClass.SerialField
	public float overslime;

	public OverslimeCap() {
	}

	public void sync(GuardedEntity e) {
		TinkersGolem.HANDLER.toTrackingPlayers(new OverslimeSyncPacket(e, overslime), e);
	}

	public void syncToPlayer(GuardedEntity e, ServerPlayer player) {
		TinkersGolem.HANDLER.toClientPlayer(new OverslimeSyncPacket(e, overslime), player);
	}

	public static void register() {
	}

}
