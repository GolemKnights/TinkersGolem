package golemknights.tinkersgolem.register;

import golemknights.tinkersgolem.TinkersGolem;
import golemknights.tinkersgolem.content.modifiers.tool.MetalGolemAmmoFinderModule;
import golemknights.tinkersgolem.content.modifiers.tool.SpewingModule;
import golemknights.tinkersgolem.content.modifiers.tool.SpotlightModule;
import golemknights.tinkersgolem.library.hooks.ShoulderWeaponModifierHook;
import net.minecraft.core.registries.Registries;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.RegisterEvent;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.ModifierId;
import slimeknights.tconstruct.library.modifiers.modules.ModifierModule;
import slimeknights.tconstruct.library.module.ModuleHook;

import static golemknights.tinkersgolem.TinkersGolem.getResource;

public class TGTinkersModifiers {
    @SubscribeEvent
    void registerSerializers(RegisterEvent event) {
        if (event.getRegistryKey() == Registries.RECIPE_SERIALIZER) {
            ModifierModule.LOADER.register(TinkersGolem.getResource("spewing"), SpewingModule.LOADER);
            ModifierModule.LOADER.register(TinkersGolem.getResource("spotlight"), SpotlightModule.LOADER);
            ModifierModule.LOADER.register(TinkersGolem.getResource("golem_ammo_finder"), MetalGolemAmmoFinderModule.LOADER);
        }
    }
    // Hooks
    public static final ModuleHook<ShoulderWeaponModifierHook> SHOLDER_WEAPON_MODIFIER_HOOK = ModifierHooks.register(
            getResource("shoulder_weapon"), ShoulderWeaponModifierHook.class, ShoulderWeaponModifierHook.AllMerger::new,
            (t, m, g, i, h) -> {});
    // Modifierid
    public static final ModifierId spewing = id("spewing");
    public static final ModifierId ammoFinder= id("golem_ammo_finder");
    public static final ModifierId spotlight = id("spotlight");
    private static ModifierId id(String name) {
        return new ModifierId(TinkersGolem.MODID, name);
    }

    public static void registers(IEventBus bus) {
        bus.register(new TGTinkersModifiers());
    }
}
