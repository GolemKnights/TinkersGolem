package golemknights.tinkersgolem.mixinhelper;

import net.minecraft.world.item.crafting.Ingredient;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.fluid.entity.DamageFluidEffect.DamageTypePair;
import java.util.List;

public interface FireballTypeAccessor {

    Ingredient match();

    DamageTypePair damageType();

    float damageMultiplier();

    List<ModifierEntry> ammoModifiers();

    DamageTypePair damageType(DamageTypePair fallback);
}