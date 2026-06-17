package golemknights.tinkersgolem.events;

import dev.xkmc.modulargolems.events.event.GolemEquipItemEvent;
import golemknights.tinkersgolem.entity.SlimeGolemEntity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static golemknights.tinkersgolem.TinkersGolem.MODID;

@Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class TGGolemEventHandlers {

	@SubscribeEvent
	public static void onGolemEquipmentTest(GolemEquipItemEvent event) {
		if (event.getEntity() instanceof SlimeGolemEntity e) {
			if (LivingEntity.getEquipmentSlotForItem(event.getStack()) == EquipmentSlot.HEAD) {
				if (event.getStack().canEquip(EquipmentSlot.HEAD, e)) {
					event.setSlot(1, EquipmentSlot.HEAD);
				}
			}
		}
	}

}
