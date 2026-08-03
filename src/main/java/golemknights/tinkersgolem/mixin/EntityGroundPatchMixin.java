package golemknights.tinkersgolem.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import slimeknights.tconstruct.shared.TinkerEffects;

@Mixin(Entity.class)
public abstract class EntityGroundPatchMixin {
    @Inject(method = "onGround", at = @At("RETURN"), cancellable = true)
    private void patchOnGround(CallbackInfoReturnable<Boolean> cir){
        if ((Entity)(Object)this instanceof LivingEntity living){
            if (living.hasEffect(TinkerEffects.antigravity.get())){
                BlockPos headPos = living.blockPosition().above();
                BlockState headState = living.level().getBlockState(headPos);
                //VoxelShape shape = headState.getCollisionShape(living.level(), headPos);
                cir.setReturnValue(/*!shape.isEmpty()*/headState.isSolid());
            }
        }
    }
}
