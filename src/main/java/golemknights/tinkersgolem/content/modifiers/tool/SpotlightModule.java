package golemknights.tinkersgolem.content.modifiers.tool;

import dev.xkmc.modulargolems.content.entity.metalgolem.MetalGolemEntity;
import dev.xkmc.modulargolems.content.item.ranged.CannonPoseUtil;
import golemknights.tinkersgolem.content.entity.ModifiableLaserEntity;
import golemknights.tinkersgolem.library.hooks.ShoulderWeaponModifierHook;
import golemknights.tinkersgolem.register.TGEntities;
import golemknights.tinkersgolem.register.TGStats;
import golemknights.tinkersgolem.register.TGTinkersModifiers;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import slimeknights.mantle.data.loadable.record.RecordLoadable;
import slimeknights.mantle.data.loadable.record.SingletonLoader;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.hook.build.ConditionalStatModifierHook;
import slimeknights.tconstruct.library.modifiers.modules.ModifierModule;
import slimeknights.tconstruct.library.module.HookProvider;
import slimeknights.tconstruct.library.module.ModuleHook;
import slimeknights.tconstruct.library.tools.helper.ToolDamageUtil;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

import java.util.List;

public enum SpotlightModule implements ModifierModule, ShoulderWeaponModifierHook.Delayed {
    INSTANCE;

    public static final RecordLoadable<SpotlightModule> LOADER = new SingletonLoader<>(INSTANCE);

    @Override
    public RecordLoadable<? extends ModifierModule> getLoader() {
        return LOADER;
    }

    @Override
    public List<ModuleHook<?>> getDefaultHooks() {
        return HookProvider.defaultHooks(TGTinkersModifiers.SHOLDER_WEAPON_MODIFIER_HOOK);
    }

    @Override
    public void onShoot(IToolStackView tool, ModifierEntry modifier, MetalGolemEntity entity, ItemStack toolItem, InteractionHand hand) {
        if (CannonPoseUtil.BEACON_CANNON.isOutOfRange(entity, hand, ConditionalStatModifierHook.getModifiedStat(tool, entity, TGStats.CANNON_RANGE))) {
            return;
        }
        ModifiableLaserEntity laser = new ModifiableLaserEntity(TGEntities.ENTITY_LASER.get(), entity.level(), entity, 10, hand == InteractionHand.MAIN_HAND, tool, toolItem);
        if (entity.level().addFreshEntity(laser)) {
            ToolDamageUtil.damageAnimated(tool, 1, entity);
            if (!entity.isSilent()) {
                entity.level().playSound(null, entity.blockPosition(), SoundEvents.BEACON_DEACTIVATE, SoundSource.NEUTRAL, 2.0F, 1.5F);
            }
        }
    }
}
