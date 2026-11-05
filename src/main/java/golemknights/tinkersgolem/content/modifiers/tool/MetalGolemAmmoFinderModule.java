package golemknights.tinkersgolem.content.modifiers.tool;

import dev.xkmc.mob_weapon_api.api.ai.ItemWrapper;
import dev.xkmc.modulargolems.content.entity.common.AbstractGolemEntity;
import dev.xkmc.modulargolems.content.entity.common.SweepGolemEntity;
import dev.xkmc.modulargolems.content.item.ranged.BeaconCannonItem;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import slimeknights.mantle.data.loadable.record.RecordLoadable;
import slimeknights.mantle.data.loadable.record.SingletonLoader;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.ranged.BowAmmoModifierHook;
import slimeknights.tconstruct.library.modifiers.modules.ModifierModule;
import slimeknights.tconstruct.library.module.ModuleHook;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;

public enum MetalGolemAmmoFinderModule implements ModifierModule, BowAmmoModifierHook {
    INSTANCE;

    public static final RecordLoadable<MetalGolemAmmoFinderModule> LOADER = new SingletonLoader<>(INSTANCE);
    private static final List<ModuleHook<?>> HOOKS = List.of(ModifierHooks.BOW_AMMO);

    @Override
    public List<ModuleHook<?>> getDefaultHooks() {
        return HOOKS;
    }

    @Override
    public ItemStack findAmmo(IToolStackView tool, ModifierEntry modifier, LivingEntity shooter, ItemStack standardAmmo,
            Predicate<ItemStack> ammoPredicate) {
        if (!tool.getVolatileData().getBoolean(SKIP_INVENTORY_AMMO) && shooter instanceof AbstractGolemEntity golem) {
            return Stream.of(golem.getArrowSlot(), golem.getArrowSlot2()).map(ItemWrapper::getItem)
                    .filter(ammoPredicate).findFirst().orElse(ItemStack.EMPTY);
        }

        return ItemStack.EMPTY;
    }

    @Override
    public RecordLoadable<? extends ModifierModule> getLoader() {
        return LOADER;
    }
}