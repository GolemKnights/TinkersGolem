package golemknights.tinkersgolem.mixin;

import golemknights.tinkersgolem.mixinhelper.FireballTypeAccessor;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(targets = "slimeknights.tconstruct.tools.modules.interaction.FireballModule$FireballType", remap = false)
public abstract class FireballTypeMixin implements FireballTypeAccessor {}