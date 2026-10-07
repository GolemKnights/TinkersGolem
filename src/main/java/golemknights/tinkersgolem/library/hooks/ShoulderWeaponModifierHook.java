package golemknights.tinkersgolem.library.hooks;

import dev.xkmc.modulargolems.content.entity.metalgolem.MetalGolemEntity;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.stat.ToolStats;

import java.util.Collection;

public interface ShoulderWeaponModifierHook {
    void onTick(IToolStackView tool, ModifierEntry modifier, MetalGolemEntity golem, ItemStack toolItem,
            InteractionHand hand);

    public static interface Delayed extends ShoulderWeaponModifierHook {
        default int getDelay(IToolStackView tool, ModifierEntry modifier, MetalGolemEntity entity, ItemStack toolItem,
                InteractionHand hand) {
            return (int) (40 / tool.getStats().get(ToolStats.DRAW_SPEED));
        }

        void onShoot(IToolStackView tool, ModifierEntry modifier, MetalGolemEntity entity, ItemStack toolItem,
                InteractionHand hand);

        default void onTick(IToolStackView tool, ModifierEntry modifier, MetalGolemEntity entity, ItemStack toolItem,
                InteractionHand hand) {
            int delay = getDelay(tool, modifier, entity, toolItem, hand);
            if (entity.tickCount % delay == (hand == InteractionHand.MAIN_HAND ? 0 : delay / 2))
                onShoot(tool, modifier, entity, toolItem, hand);
        }
    }

    record AllMerger(Collection<ShoulderWeaponModifierHook> modules) implements ShoulderWeaponModifierHook {
        @Override
        public void onTick(IToolStackView tool, ModifierEntry modifier, MetalGolemEntity golem, ItemStack toolItem,
                InteractionHand hand) {
            modules.forEach(hook -> hook.onTick(tool, modifier, golem, toolItem, hand));
        }
    }
}
