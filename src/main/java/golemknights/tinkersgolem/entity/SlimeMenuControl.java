package golemknights.tinkersgolem.entity;

import dev.xkmc.modulargolems.content.core.GolemMenuControl;
import dev.xkmc.modulargolems.content.core.GolemScreenControl;
import dev.xkmc.modulargolems.content.menu.equipment.EquipmentsMenu;
import net.minecraft.world.entity.EquipmentSlot;

import java.util.Optional;

public class SlimeMenuControl extends GolemMenuControl<SlimeGolemEntity> {

	public SlimeMenuControl(EquipmentsMenu menu, SlimeGolemEntity golem) {
		super(menu, golem);
	}

	@Override
	public void fillMenu() {
		this.menu.addSlot("chest", (e) -> this.isValid(EquipmentSlot.HEAD, e));
	}

	@Override
	public EquipmentSlot[] getSlotDefinition() {
		return new EquipmentSlot[]{EquipmentSlot.HEAD};
	}

	@Override
	public Optional<? extends GolemScreenControl<SlimeGolemEntity>> getScreenProvider() {
		return Optional.of(new SlimeScreenControl(this));
	}
}
