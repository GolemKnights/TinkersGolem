package golemknights.tinkersgolem.modifiers.slime;

import dev.xkmc.modulargolems.content.core.StatFilterType;
import dev.xkmc.modulargolems.content.item.upgrade.IUpgradeItem;
import golemknights.tinkersgolem.data.TGTagGen;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.Item;

import java.util.List;

public class SlimeSlotModifier extends SlimeModifier {

	public SlimeSlotModifier() {
		super(StatFilterType.MASS, 10);
	}

	@Override
	public int addSlot(List<IUpgradeItem> upgrades, int lv) {
		int count = 0;
		for (var e : upgrades) {
			if (e instanceof Item item && item.getDefaultInstance().is(TGTagGen.SLIME_UPGRADE))
				count++;
		}
		return Math.min(count, lv);
	}

	@Override
	public List<MutableComponent> getDetail(int v) {
		return List.of(Component.translatable(this.getDescriptionId() + ".desc", v).withStyle(ChatFormatting.GREEN));
	}

}
