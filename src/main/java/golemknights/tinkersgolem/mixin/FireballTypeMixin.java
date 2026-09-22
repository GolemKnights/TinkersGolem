package golemknights.tinkersgolem.mixin;

import org.spongepowered.asm.mixin.Mixin;
import golemknights.tinkersgolem.mixinhelper.FireballTypeAccessor;

@Mixin(targets = "slimeknights.tconstruct.tools.modules.interaction.FireballModule$FireballType", remap = false)
public abstract class FireballTypeMixin implements FireballTypeAccessor {}