package golemknights.tinkersgolem.entity;

import dev.xkmc.modulargolems.content.client.overlay.GolemStatusOverlay;
import dev.xkmc.modulargolems.content.core.GolemOverlayControl;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.entity.EquipmentSlot;

public class SlimeOverlayControl extends GolemOverlayControl<SlimeGolemEntity> {

	public SlimeOverlayControl(SlimeGolemEntity golem) {
		super(golem);
	}

	@Override
	public void renderImage(GolemStatusOverlay.GolemEquipmentTooltip tooltip, Font font, int mx, int my, GuiGraphics g) {
		tooltip.renderSlot(g, mx, my, golem.getItemBySlot(EquipmentSlot.HEAD), "altas_helmet");
	}

	@Override
	public int getHeight() {
		return 20;
	}

	@Override
	public int getWidth(Font font) {
		return 18;
	}

}
