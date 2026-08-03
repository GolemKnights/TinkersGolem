package golemknights.tinkersgolem.mixin;

import dev.xkmc.modulargolems.content.entity.common.AbstractGolemEntity;
import golemknights.tinkersgolem.register.TGGolemModifiers;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import slimeknights.tconstruct.shared.TinkerEffects;

@Mixin(LivingEntityRenderer.class)
public class DinnerBoneRendererMixin {
    @Inject(method = "isEntityUpsideDown", at = @At("RETURN"))
    private static void upsideDown(LivingEntity p_194454_, CallbackInfoReturnable<Boolean> cir){
        if (p_194454_ instanceof AbstractGolemEntity<?,?> golem){
            if (golem.getModifiers().containsKey(TGGolemModifiers.ANTIGRAVITY.get())){
                cir.setReturnValue(true);
            }
        } else if (p_194454_.hasEffect(TinkerEffects.antigravity.get())) {
            cir.setReturnValue(true);
        }
    }
}
