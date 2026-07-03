package golemknights.tinkersgolem.modifiers.slime;

import dev.xkmc.modulargolems.content.core.StatFilterType;
import dev.xkmc.modulargolems.content.item.upgrade.IUpgradeItem;
import dev.xkmc.modulargolems.content.item.upgrade.UpgradeItem;
import golemknights.tinkersgolem.data.TGTagGen;

import java.util.List;

public class SlimeSlotModifier extends SlimeModifier {

	public SlimeSlotModifier() {
		super(StatFilterType.MASS, 10);
	}

	@Override
	public int addSlot(List<IUpgradeItem> upgrades, int lv) {
		int count = 0;
		for (var e : upgrades)
			if (e instanceof UpgradeItem item && item.getDefaultInstance().is(TGTagGen.SLIME_UPGRADE))
				count++;
		return Math.min(count, lv);
	}

}
