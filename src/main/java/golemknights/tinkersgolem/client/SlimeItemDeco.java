package golemknights.tinkersgolem.client;

import dev.xkmc.modulargolems.content.item.golem.GolemHolder;
import golemknights.tinkersgolem.entity.SlimeGolemType;
import golemknights.tinkersgolem.register.TGEntities;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.IItemDecorator;

public class SlimeItemDeco implements IItemDecorator {

	@Override
	public boolean render(GuiGraphics g, Font font, ItemStack stack, int x, int y) {
		if (!stack.is(TGEntities.HOLDER_SLIME.get())) return false;
		if (GolemHolder.getMaxHealth(stack) < 0) return false;
		int size = SlimeGolemType.getSize(stack);
		String s = size + "";
		g.pose().pushPose();
		g.pose().translate(0.0F, 0.0F, 250);
		g.drawString(font, s, x + 1, y + 1, -1);
		g.pose().popPose();
		return true;
	}

}
