package golemknights.tinkersgolem.mixin;

import dev.xkmc.modulargolems.init.data.MGDamageTypes;
import golemknights.tinkersgolem.content.item.weapon.ModifiableShoulderCannonItem;
import golemknights.tinkersgolem.register.TGTinkersModifiers;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.helper.ToolAttackUtil;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

@Mixin(value = ToolAttackUtil.class, remap = false)
public abstract class MakeLaserMagicMixin {
    @Redirect(
            method = "performAttack",
            at = @At(
                    value = "INVOKE",
                    target = "Lslimeknights/tconstruct/library/tools/context/ToolAttackContext;makeDamageSource()Lnet/minecraft/world/damagesource/DamageSource;"
            )
    )
    private static DamageSource makeDamageSource(ToolAttackContext context, IToolStackView tool) {
        LivingEntity attacker = context.getAttacker();
        if (tool.getModifierLevel(TGTinkersModifiers.spotlight) > 0 && tool.getItem() instanceof ModifiableShoulderCannonItem) {
            return new DamageSource(attacker.level().registryAccess().lookupOrThrow(Registries.DAMAGE_TYPE).getOrThrow(MGDamageTypes.BEACON), attacker);
        }
        return context.makeDamageSource();
    }
}
