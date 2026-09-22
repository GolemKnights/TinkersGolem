package golemknights.tinkersgolem.register;

import golemknights.tinkersgolem.hooks.ShoulderWeaponModifierHook;
import net.minecraft.core.registries.Registries;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.RegisterEvent;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.module.ModuleHook;

import static golemknights.tinkersgolem.TinkersGolem.getResource;

public class TGTinkersModifiers {
    @SubscribeEvent
    void registerSerializers(RegisterEvent event) {
        if (event.getRegistryKey() == Registries.RECIPE_SERIALIZER) {
        }
    }

    public static final ModuleHook<ShoulderWeaponModifierHook> SHOLDER_WEAPON_MODIFIER_HOOK = ModifierHooks.register(
            getResource("shoulder_weapon"), ShoulderWeaponModifierHook.class, ShoulderWeaponModifierHook.AllMerger::new,
            (t, m, g, i, h) -> {});

    public static void registers(IEventBus bus) {
        bus.register(new TGTinkersModifiers());
    }
}
