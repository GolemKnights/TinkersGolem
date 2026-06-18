package golemknights.tinkersgolem.cap;

import dev.xkmc.l2serial.network.SerialPacketBase;
import dev.xkmc.l2serial.serialization.SerialClass;
import dev.xkmc.modulargolems.content.entity.common.AbstractGolemEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.network.NetworkEvent;

@SerialClass
public class OverslimeSyncPacket extends SerialPacketBase {

	@SerialClass.SerialField
	public int id;

	@SerialClass.SerialField
	public float overslime;

	public OverslimeSyncPacket() {
		super();
	}

	public OverslimeSyncPacket(LivingEntity e, float data) {
		super();
		id = e.getId();
		this.overslime = data;
	}

	@Override
	public void handle(NetworkEvent.Context context) {
		ClientHandler.handle(id, overslime);
	}

	public static class ClientHandler {

		public static void handle(int id, float overslime) {
			var level = Minecraft.getInstance().level;
			if (level == null) return;
			if (level.getEntity(id) instanceof AbstractGolemEntity<?, ?> e) {
				OverslimeCap.HOLDER.get(e).overslime = overslime;
			}
		}

	}

}
