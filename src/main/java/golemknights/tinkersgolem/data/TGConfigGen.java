package golemknights.tinkersgolem.data;

import dev.xkmc.l2library.serial.config.ConfigDataProvider;
import dev.xkmc.modulargolems.content.config.GolemMaterialConfig;
import dev.xkmc.modulargolems.init.ModularGolems;
import dev.xkmc.modulargolems.init.registrate.GolemModifiers;
import dev.xkmc.modulargolems.init.registrate.GolemTypes;
import golemknights.tinkersgolem.register.TGAttributes;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;
import slimeknights.tconstruct.TConstruct;
import slimeknights.tconstruct.shared.TinkerMaterials;

public class TGConfigGen extends ConfigDataProvider {

	public TGConfigGen(DataGenerator generator) {
		super(generator, "Golem Config");
	}

	@Override
	public void add(Collector map) {


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