package golemknights.tinkersgolem.events;

import dev.xkmc.modulargolems.events.event.GolemInfoEvent;
import golemknights.tinkersgolem.cap.OverslimeCap;
import golemknights.tinkersgolem.data.TGLang;
import golemknights.tinkersgolem.register.TGAttributes;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.util.Mth;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static golemknights.tinkersgolem.TinkersGolem.MODID;

@Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class TGClientGolemEventHandlers {

	@SubscribeEvent(priority = EventPriority.HIGH)
	public static void onGolemEquipmentTest(GolemInfoEvent event) {
		var attr = event.getGolem().getAttribute(TGAttributes.MAX_OVERSLIME.get());
		if (attr == null || attr.getValue() == 0) return;
		int max = (int) attr.getValue();
		var val = (int) OverslimeCap.HOLDER.get(event.getGolem()).overslime;
		event.addLine(TGLang.OVERSLIME_INFO.get(hsbText(val, max), max).withStyle(ChatFormatting.GREEN));
	}


	private static MutableComponent hsbText(float v, float max) {
		float perc = Mth.clamp(v / max, 0f, 1f);
		int col = Mth.hsvToRgb(perc / 3F, 1F, 1F);
		return Component.literal("" + Math.round(v)).setStyle(Style.EMPTY.withColor(col));
	}

}
