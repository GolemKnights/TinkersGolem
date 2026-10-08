package golemknights.tinkersgolem.content.modifiers.tool;

import dev.xkmc.modulargolems.content.entity.metalgolem.MetalGolemEntity;
import dev.xkmc.modulargolems.content.item.ranged.CannonPoseUtil;
import dev.xkmc.modulargolems.init.registrate.GolemMiscEntities;
import golemknights.tinkersgolem.content.entity.ModifiableLaserEntity;
import golemknights.tinkersgolem.library.hooks.ShoulderWeaponModifierHook;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

public enum SpotlightModule implements ShoulderWeaponModifierHook.Delayed {
    INSTANCE;

    @Override
    public void onShoot(IToolStackView tool, ModifierEntry modifier, MetalGolemEntity entity, ItemStack toolItem, InteractionHand hand) {
        if (CannonPoseUtil.BEACON_CANNON.isOutOfRange(entity, hand)) {
            return;
        }
        ModifiableLaserEntity laser = new ModifiableLaserEntity(GolemMiscEntities.LASER.get(), entity.level(), entity, 10, hand == InteractionHand.MAIN_HAND, tool, toolItem);
        entity.level().addFreshEntity(laser);
        if (!entity.isSilent()) {
            entity.level().playSound(null, entity.blockPosition(), SoundEvents.BEACON_DEACTIVATE, SoundSource.NEUTRAL, 2.0F, 1.5F);
        }
    }
}
