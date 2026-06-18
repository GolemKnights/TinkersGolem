package golemknights.tinkersgolem.entity;

import dev.xkmc.l2serial.network.SerialPacketBase;
import dev.xkmc.l2serial.serialization.SerialClass;
import dev.xkmc.l2serial.serialization.codec.AliasCollection;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.network.NetworkEvent;

import java.util.ArrayList;
import java.util.List;

@SerialClass
public class SlimeTankSyncPacket extends SerialPacketBase {

	@SerialClass.SerialField
	public int id;
	@SerialClass.SerialField
	public ArrayList<FluidStack> data;

	public SlimeTankSyncPacket() {
		super();
	}

	public SlimeTankSyncPacket(SlimeGolemEntity e, AliasCollection<FluidStack> list) {
		super();
		id = e.getId();
		data = new ArrayList<>(list.getAsList());
	}

	@Override
	public void handle(NetworkEvent.Context context) {
		ClientHandler.handle(id, data);
	}

	public static class ClientHandler {

		public static void handle(int id, List<FluidStack> list) {
			var level = Minecraft.getInstance().level;
			if (level == null) return;
			if (!(level.getEntity(id) instanceof SlimeGolemEntity e)) return;
			for (int i = 0; i < list.size(); i++)
				e.tank.set(list.size(), i, list.get(i));
		}

	}

}
