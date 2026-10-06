package golemknights.tinkersgolem.library.hooks;

import dev.xkmc.modulargolems.content.entity.metalgolem.MetalGolemEntity;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

import java.util.Collection;

public interface ShoulderWeaponModifierHook {
    void onTick(IToolStackView tool, ModifierEntry modifier, MetalGolemEntity golem, ItemStack toolItem,
            InteractionHand hand);

    record AllMerger(Collection<ShoulderWeaponModifierHook> modules) implements ShoulderWeaponModifierHook {
        @Override
        public void onTick(IToolStackView tool, ModifierEntry modifier, MetalGolemEntity golem, ItemStack toolItem,
                InteractionHand hand) {
            modules.forEach(hook -> hook.onTick(tool, modifier, golem, toolItem, hand));
        }
    }
}
