package golemknights.tinkersgolem.modifiers.golem;

import dev.xkmc.modulargolems.content.modifier.base.AttributeGolemModifier;
import golemknights.tinkersgolem.register.TGAttributes;

import java.util.function.DoubleSupplier;

public class OverforcedModifier extends AttributeGolemModifier {
    public OverforcedModifier() {
        super(5, new AttrEntry(TGAttributes.STAT_OVERSLIME, () -> 20));
    }
}
