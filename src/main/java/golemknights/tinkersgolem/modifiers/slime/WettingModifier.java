package golemknights.tinkersgolem.modifiers.slime;

import dev.xkmc.l2damagetracker.contents.attack.AttackCache;
import dev.xkmc.modulargolems.content.core.StatFilterType;
import dev.xkmc.modulargolems.content.entity.common.AbstractGolemEntity;
import golemknights.tinkersgolem.entity.SlimeGolemEntity;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import slimeknights.tconstruct.library.modifiers.fluid.FluidEffectContext;
import slimeknights.tconstruct.library.modifiers.fluid.FluidEffectManager;
import slimeknights.tconstruct.library.modifiers.fluid.FluidEffects;

import static slimeknights.tconstruct.tools.modifiers.ability.fluid.UseFluidOnHitModifier.spawnParticles;

public class WettingModifier extends SlimeModifier {

	public WettingModifier() {
		super(StatFilterType.MASS, 3);
	}

	@Override
	public void onDamageMax(AttackCache cache, AbstractGolemEntity<?, ?> golem, int lv) {
		if (!(golem instanceof SlimeGolemEntity slime)) return;
		FluidStack fluid = slime.getFluid();
		if (fluid.isEmpty()) return;
		FluidEffects recipe = FluidEffectManager.INSTANCE.find(fluid.getFluid());
		if (!recipe.hasEntityEffects()) return;
		int consumed = recipe.applyToEntity(fluid, lv * slime.getSize(), FluidEffectContext.builder(golem.level()).user(slime, null).target(slime, slime), IFluidHandler.FluidAction.EXECUTE);
		if (consumed <= 0) return;
		spawnParticles(golem, fluid);
		fluid.shrink(consumed);
		slime.setFluid(fluid);
	}

}
