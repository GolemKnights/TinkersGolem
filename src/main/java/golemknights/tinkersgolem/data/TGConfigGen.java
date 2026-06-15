package golemknights.tinkersgolem.data;

import dev.xkmc.l2library.serial.config.ConfigDataProvider;
import dev.xkmc.modulargolems.content.config.GolemMaterialConfig;
import dev.xkmc.modulargolems.content.config.GolemPartConfig;
import dev.xkmc.modulargolems.content.core.StatFilterType;
import dev.xkmc.modulargolems.init.ModularGolems;
import dev.xkmc.modulargolems.init.registrate.GolemItems;
import dev.xkmc.modulargolems.init.registrate.GolemModifiers;
import dev.xkmc.modulargolems.init.registrate.GolemTypes;
import golemknights.tinkersgolem.TinkersGolem;
import golemknights.tinkersgolem.register.TGAttributes;
import golemknights.tinkersgolem.register.TGEntities;
import golemknights.tinkersgolem.register.TGGolemModifiers;
import net.minecraft.data.DataGenerator;
import net.minecraft.world.item.crafting.Ingredient;
import slimeknights.tconstruct.TConstruct;
import slimeknights.tconstruct.shared.TinkerMaterials;

public class TGConfigGen extends ConfigDataProvider {

	public TGConfigGen(DataGenerator generator) {
		super(generator, "Golem Config");
	}

	@Override
	public void add(Collector map) {

		map.add(ModularGolems.PARTS, TinkersGolem.getResource("slime"), new GolemPartConfig()
				.addPart(TGEntities.SLIME_INNER.get())
				.addFilter(StatFilterType.HEALTH, 1)
				.addFilter(StatFilterType.ATTACK, 0)
				.addFilter(StatFilterType.MOVEMENT, 0)
				.addFilter(StatFilterType.MASS, 0.5)
				.addFilter(StatFilterType.HEAD, 1)
				.end()

				.addPart(TGEntities.SLIME_OUTER.get())
				.addFilter(StatFilterType.HEALTH, 0)
				.addFilter(StatFilterType.ATTACK, 1)
				.addFilter(StatFilterType.MOVEMENT, 1)
				.addFilter(StatFilterType.MASS, 0.5)
				.addFilter(StatFilterType.HEAD, 0)
				.end()

				.addEntity(TGEntities.TYPE_SLIME.get())
				.addFilter(GolemTypes.STAT_HEALTH.get(), 0.5)
				.addFilter(GolemTypes.STAT_ATTACK.get(), 0.4)
				.addFilter(GolemTypes.STAT_REGEN.get(), 1)
				.addFilter(GolemTypes.STAT_SWEEP.get(), 0)
				.end()
		);

		map.add(ModularGolems.MATERIALS, TConstruct.getResource("extra"), new GolemMaterialConfig()
				.addMaterial(TConstruct.getResource("slimesteel"), Ingredient.of(TinkerMaterials.slimesteel.getIngotTag()))
				.addStat(GolemTypes.STAT_HEALTH.get(), 200)
				.addStat(GolemTypes.STAT_ATTACK.get(), 20)
				.addStat(TGAttributes.STAT_OVERSLIME.get(), 50)
				.addStat(TGAttributes.STAT_OVERSLIME_BONUS.get(), 0.4)
				.end()

				.addMaterial(TConstruct.getResource("queenslime"), Ingredient.of(TinkerMaterials.queensSlime.getIngotTag()))
				.addStat(GolemTypes.STAT_HEALTH.get(), 200)
				.addStat(GolemTypes.STAT_ATTACK.get(), 20)
				.addStat(TGAttributes.STAT_OVERSLIME.get(), 100)
				.addModifier(TGGolemModifiers.OVERLORD.get(), 2)
				.end()

				.addMaterial(TConstruct.getResource("cinderslime"), Ingredient.of(TinkerMaterials.cinderslime.getIngotTag()))
				.addStat(GolemTypes.STAT_HEALTH.get(), 200)
				.addStat(GolemTypes.STAT_ATTACK.get(), 20)
				.addStat(TGAttributes.STAT_OVERSLIME.get(), 50)
				.addModifier(GolemModifiers.FIRE_IMMUNE.get(), 1)
				.end()
		);

	}

}