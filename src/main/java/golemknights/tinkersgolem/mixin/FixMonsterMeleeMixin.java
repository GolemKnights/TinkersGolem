package golemknights.tinkersgolem.mixin;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import slimeknights.tconstruct.tools.logic.ToolEvents;

@Mixin(value = ToolEvents.class, remap = false)
public abstract class FixMonsterMeleeMixin {
    @Redirect(
            method = "livingHurt",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/LivingEntity;getAttributeValue(Lnet/minecraft/world/entity/ai/attributes/Attribute;)D")
    )
    private static double getAttributeValue(LivingEntity entity, Attribute attribute, LivingHurtEvent event) {
        if (event.getSource().getEntity() instanceof LivingEntity living) {
            return living.getAttributeValue(attribute);
        }
        return 0;
    }
}
