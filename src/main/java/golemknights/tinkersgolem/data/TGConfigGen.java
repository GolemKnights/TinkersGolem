package golemknights.tinkersgolem.data;

import dev.xkmc.l2library.serial.config.ConfigDataProvider;
import dev.xkmc.modulargolems.content.config.GolemMaterialConfig;
import dev.xkmc.modulargolems.content.config.GolemPartConfig;
import dev.xkmc.modulargolems.content.core.StatFilterType;
import dev.xkmc.modulargolems.init.ModularGolems;
import dev.xkmc.modulargolems.init.registrate.GolemModifiers;
import dev.xkmc.modulargolems.init.registrate.GolemTypes;
import golemknights.tinkersgolem.TinkersGolem;
import golemknights.tinkersgolem.register.TGAttributes;
import golemknights.tinkersgolem.register.TGEntities;
import golemknights.tinkersgolem.register.TGGolemModifiers;
import net.minecraft.data.DataGenerator;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import slimeknights.tconstruct.TConstruct;
import slimeknights.tconstruct.shared.TinkerCommons;
import slimeknights.tconstruct.shared.TinkerMaterials;
import slimeknights.tconstruct.shared.block.SlimeType;

import java.util.List;

public class TGConfigGen extends ConfigDataProvider {

	public TGConfigGen(DataGenerator generator) {
		super(generator, "Golem Config");
	}

	@Override
	public void add(Collector map) {

		map.add(ModularGolems.PARTS, TinkersGolem.getResource("slime"), new GolemPartConfig()
				.addPart(TGEntities.SLIME_CORE.get())
				.addFilter(StatFilterType.HEALTH, 0.5)
				.addFilter(StatFilterType.ATTACK, 1)
				.addFilter(StatFilterType.MOVEMENT, 0)
				.addFilter(StatFilterType.MASS, 0.5)
				.addFilter(StatFilterType.HEAD, 1)
				.end()

				.addPart(TGEntities.SLIME_SHELL.get())
				.addFilter(StatFilterType.HEALTH, 0.5)
				.addFilter(StatFilterType.ATTACK, 0)
				.addFilter(StatFilterType.MOVEMENT, 1)
				.addFilter(StatFilterType.MASS, 0.5)
				.addFilter(StatFilterType.HEAD, 0)
				.end()

				.addEntity(TGEntities.TYPE_SLIME.get())
				.addFilter(GolemTypes.STAT_HEALTH.get(), 0.8)
				.addFilter(GolemTypes.STAT_ATTACK.get(), 0.4)
				.addFilter(GolemTypes.STAT_REGEN.get(), 1)
				.addFilter(GolemTypes.STAT_SWEEP.get(), 0)
				.end()
		);

		Item[] slimeParts = new Item[]{TGEntities.SLIME_CORE.get(), TGEntities.SLIME_SHELL.get()};

		map.add(ModularGolems.MATERIALS, TinkersGolem.getResource("slime"), new GolemMaterialConfig()
				.addMaterial(TinkersGolem.getResource("earthslime"), Ingredient.of(Items.SLIME_BALL))
				.onlyFor(slimeParts)
				.addStat(GolemTypes.STAT_HEALTH.get(), 50)
				.addStat(GolemTypes.STAT_ATTACK.get(), 15)
                .addStat(GolemTypes.STAT_JUMP.get(), 1)
                .addStat(GolemTypes.STAT_SPEED.get(), 0.2)
				.addStat(TGAttributes.STAT_OVERSLIME.get(), 70)
				.end()

				.addMaterial(TinkersGolem.getResource("skyslime"), Ingredient.of(TinkerCommons.slimeball.get(SlimeType.SKY)))
				.onlyFor(slimeParts)
				.addStat(GolemTypes.STAT_HEALTH.get(), 40)
				.addStat(GolemTypes.STAT_ATTACK.get(), 10)
                .addStat(GolemTypes.STAT_JUMP.get(), 2)
				.addStat(TGAttributes.STAT_OVERSLIME.get(), 50)
                .addModifier(TGGolemModifiers.OVERSHOCK.get(), 1)
				.end()

				.addMaterial(TinkersGolem.getResource("ichor"), Ingredient.of(TinkerCommons.slimeball.get(SlimeType.ICHOR)))
				.onlyFor(slimeParts)
				.addStat(GolemTypes.STAT_HEALTH.get(), 40)
				.addStat(GolemTypes.STAT_ATTACK.get(), 10)
                .addStat(GolemTypes.STAT_SPEED.get(), 0.6)
				.addStat(TGAttributes.STAT_OVERSLIME.get(), 50)
                .addModifier(TGGolemModifiers.OVERBOKING.get(), 1)
				.addModifier(GolemModifiers.FIRE_IMMUNE.get(), 1)
				.end()

				.addMaterial(TinkersGolem.getResource("enderslime"), Ingredient.of(TinkerCommons.slimeball.get(SlimeType.ENDER)))
				.onlyFor(slimeParts)
				.addStat(GolemTypes.STAT_HEALTH.get(), 30)
				.addStat(GolemTypes.STAT_ATTACK.get(), 10)
				.addStat(GolemTypes.STAT_RANGE.get(), 1)
				.addStat(TGAttributes.STAT_OVERSLIME.get(), 50)
				.addStat(TGAttributes.STAT_TANK_CAPACITY.get(), 8)
                .addModifier(TGGolemModifiers.OVERTELEPORT.get(), 1)
				.end()

				.supportsDefaultAnd(List.of(TGEntities.SLIME_CORE.get()),
						ModularGolems.loc("copper"),
						ModularGolems.loc("iron"),
						ModularGolems.loc("gold"),
						ModularGolems.loc("netherite"),
						TConstruct.getResource("amethyst_bronze"),
						TConstruct.getResource("manyullyn"),
						TConstruct.getResource("hepatizon"),
						TConstruct.getResource("cobalt"),
						TConstruct.getResource("rose_gold"),
						TConstruct.getResource("slimesteel"),
						TConstruct.getResource("queens_slime"),
						TConstruct.getResource("cinderslime")
				)

		);

		map.add(ModularGolems.MATERIALS, TConstruct.getResource("extra"), new GolemMaterialConfig()
				.addMaterial(TConstruct.getResource("slimesteel"), Ingredient.of(TinkerMaterials.slimesteel.getIngotTag()))
				.addStat(GolemTypes.STAT_HEALTH.get(), 200)
				.addStat(GolemTypes.STAT_ATTACK.get(), 20)
				.addStat(TGAttributes.STAT_OVERSLIME.get(), 50)
				.addStat(TGAttributes.STAT_OVERSLIME_BONUS.get(), 1)
				.end()

				.addMaterial(TConstruct.getResource("queens_slime"), Ingredient.of(TinkerMaterials.queensSlime.getIngotTag()))
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
                .addModifier(TGGolemModifiers.OVERBURN.get(), 1)
                .addModifier(TGGolemModifiers.OVERSMELT.get(), 1)
				.end()
		);

	}

}