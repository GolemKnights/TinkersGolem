package golemknights.tinkersgolem.mixin;

import java.util.Arrays;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import golemknights.tinkersgolem.mixinhelper.FireballModule_ShoulderWeaponModifierHook;
import golemknights.tinkersgolem.register.TGTinkersModifiers;
import slimeknights.tconstruct.library.module.ModuleHook;
import slimeknights.tconstruct.tools.modules.interaction.FireballModule;

@Mixin(value = FireballModule.class, remap = false)
public abstract class FireballModuleMixin implements FireballModule_ShoulderWeaponModifierHook {

    @ModifyArg(method = "<clinit>", at = @At(value = "INVOKE", target = "Lslimeknights/tconstruct/library/module/HookProvider;defaultHooks([Lslimeknights/tconstruct/library/module/ModuleHook;)Ljava/util/List;"), index = 0)
    private static ModuleHook<?>[] modifyHooks(ModuleHook<?>[] hooks) {
        ModuleHook<?>[] newHooks = Arrays.copyOf(hooks, hooks.length + 1);
        newHooks[hooks.length] = TGTinkersModifiers.SHOLDER_WEAPON_MODIFIER_HOOK;
        return newHooks;
    }
}
