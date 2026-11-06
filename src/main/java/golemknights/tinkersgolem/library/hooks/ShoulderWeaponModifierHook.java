package golemknights.tinkersgolem.library.hooks;

import dev.xkmc.modulargolems.content.entity.common.AbstractGolemEntity;
import net.minecraft.world.item.ItemStack;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.stat.ToolStats;

import java.util.Collection;

public interface ShoulderWeaponModifierHook {
    void onTick(IToolStackView tool, ModifierEntry modifier, AbstractGolemEntity<?, ?> golem, ItemStack toolItem,
                int slot);

    interface Delayed extends ShoulderWeaponModifierHook {
        default int getDelay(IToolStackView tool, ModifierEntry modifier, AbstractGolemEntity<?, ?> golem, ItemStack toolItem,
                             int hand) {
            return (int) (40 / tool.getStats().get(ToolStats.DRAW_SPEED));
        }

        void onShoot(IToolStackView tool, ModifierEntry modifier, AbstractGolemEntity<?, ?> golem, ItemStack toolItem,
                     int slot);

        default void onTick(IToolStackView tool, ModifierEntry modifier, AbstractGolemEntity<?, ?> golem, ItemStack toolItem,
                            int slot) {
            int delay = Math.max(1, getDelay(tool, modifier, golem, toolItem, slot));
            if (golem.tickCount % delay == (slot < 0 ? delay / 4 : delay / 4 * 3))
                onShoot(tool, modifier, golem, toolItem, slot);
        }
    }

    record AllMerger(Collection<ShoulderWeaponModifierHook> modules) implements ShoulderWeaponModifierHook {
        @Override
        public void onTick(IToolStackView tool, ModifierEntry modifier, AbstractGolemEntity<?, ?> golem, ItemStack toolItem,
                           int slot) {
            modules.forEach(hook -> hook.onTick(tool, modifier, golem, toolItem, slot));
        }
    }
}
