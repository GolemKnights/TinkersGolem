package golemknights.tinkersgolem.item.misc;

import dev.xkmc.modulargolems.content.core.GolemType;
import dev.xkmc.modulargolems.content.item.upgrade.IUpgradeItem;
import dev.xkmc.modulargolems.content.modifier.base.GolemModifier;
import dev.xkmc.modulargolems.content.modifier.base.ModifierInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Supplier;

public class SpecialUpgradeItem extends Item implements IUpgradeItem {

	private final Supplier<GolemModifier> mod;

	public SpecialUpgradeItem(Item.Properties props, Supplier<GolemModifier> mod) {
		super(props);
		this.mod = mod;
	}

	@Override
	public List<ModifierInstance> get() {
		return List.of(new ModifierInstance(mod.get(), 1));
	}

	public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> list, TooltipFlag flag) {
		list.add(mod.get().getTooltip(1));
		list.addAll(mod.get().getDetail(1));
	}

	public boolean fitsOn(GolemType<?, ?> type) {
		return mod.get().fitsOn(type);
	}

}