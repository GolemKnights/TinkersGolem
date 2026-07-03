package golemknights.tinkersgolem.entity;

import com.tterrag.registrate.util.entry.EntityEntry;
import dev.xkmc.modulargolems.content.core.GolemMenuControl;
import dev.xkmc.modulargolems.content.core.GolemOverlayControl;
import dev.xkmc.modulargolems.content.core.GolemType;
import dev.xkmc.modulargolems.content.core.ModelProvider;
import dev.xkmc.modulargolems.content.menu.equipment.EquipmentsMenu;
import golemknights.tinkersgolem.TinkersGolem;
import golemknights.tinkersgolem.data.TGConfig;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.Optional;
import java.util.function.Supplier;

public class SlimeGolemType extends GolemType<SlimeGolemEntity, SlimeGolemPartType> {

	public static int getSize(ItemStack stack) {
		return Optional.ofNullable(stack.getTag()).filter((e) -> e.contains("golem_entity")).map((e) -> e.getCompound("golem_entity")).map((e) -> e.getInt("Size") + 1).orElse(4);
	}

	public SlimeGolemType(EntityEntry<SlimeGolemEntity> type, Supplier<ModelProvider<SlimeGolemEntity, SlimeGolemPartType>> model) {
		super(type, SlimeGolemPartType::values, SlimeGolemPartType.CORE, model);
	}

	@Override
	public GolemMenuControl<SlimeGolemEntity> menuControl(EquipmentsMenu menu, SlimeGolemEntity e) {
		return new SlimeMenuControl(menu, e);
	}

	@Override
	public Supplier<Supplier<GolemOverlayControl<SlimeGolemEntity>>> overlayControl(SlimeGolemEntity e) {
		return () -> () -> new SlimeOverlayControl(e);
	}

	@Override
	public ResourceLocation defaultMaterial() {
		return TinkersGolem.getResource("earth_slime");
	}

	@Override
	public ItemStack getMenuIcon(SlimeGolemEntity golem) {
		return Items.DIAMOND_HELMET.getDefaultInstance();
	}

	@Override
	public boolean mayEdit(ItemStack stack) {
		return getSize(stack) == 4;
	}

	@Override
	public int getUpgradeSlots() {
		return TGConfig.COMMON.slimeGolemSlot.get();
	}

}
