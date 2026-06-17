package golemknights.tinkersgolem.entity;

import dev.xkmc.l2library.base.menu.base.MenuLayoutConfig;
import dev.xkmc.modulargolems.content.core.GolemMenuControl;
import dev.xkmc.modulargolems.content.core.GolemScreenControl;
import net.minecraft.client.gui.GuiGraphics;

public class SlimeScreenControl extends GolemScreenControl<SlimeGolemEntity> {

	public SlimeScreenControl(GolemMenuControl<SlimeGolemEntity> ctrl) {
		super(ctrl);
	}

	@Override
	public void render(MenuLayoutConfig.ScreenRenderer sr, GuiGraphics g, float v) {
		sr.draw(g, "chest", "slot", -1, -1);
		if (this.menu.getAsPredSlot("chest", 0, 0).getItem().isEmpty()) {
			sr.draw(g, "chest", "altas_helmet", -1, -1);
		}
	}

}
