package golemknights.tinkersgolem.compat.tinkers;

import com.creeping_creeper.tinkers_thinking.common.register.ModCommonItems;
import com.creeping_creeper.tinkers_thinking.common.register.ModFluids;
import com.tterrag.registrate.providers.RegistrateLangProvider;
import com.tterrag.registrate.providers.RegistrateRecipeProvider;
import dev.xkmc.l2library.serial.config.ConfigDataProvider;
import dev.xkmc.modulargolems.content.config.GolemMaterialConfig;
import dev.xkmc.modulargolems.init.ModularGolems;
import dev.xkmc.modulargolems.init.registrate.GolemModifiers;
import dev.xkmc.modulargolems.init.registrate.GolemTypes;
import golemknights.tinkersgolem.data.TGRecipeGen;
import golemknights.tinkersgolem.register.TGAttributes;
import golemknights.tinkersgolem.register.TGGolemModifiers;
import net.minecraft.world.item.crafting.Ingredient;

public final class TinkersThinkingPlugin extends BaseTinkersPlugin{
    @Override
    protected String getModId() {
        return "tinkers_thinking";
    }

    @Override
    public void addGolemMaterials(ConfigDataProvider.Collector map) {
        map.add(ModularGolems.MATERIALS, this.getResource("common"), new GolemMaterialConfig()
                .addMaterial(this.getResource("ardite"), Ingredient.of(ModCommonItems.ardite.getIngotTag()))
                .addStat(GolemTypes.STAT_HEALTH.get(), 240)
                .addStat(GolemTypes.STAT_ATTACK.get(), 16)
                .addStat(GolemTypes.STAT_WEIGHT.get(), 0.2)
                .addModifier(TGGolemModifiers.CRAZYMETAL.get(), 1)
                .end()

                .addMaterial(this.getResource("tinkers_bronze"), Ingredient.of(ModCommonItems.tinkers_bronze.getIngotTag()))
                .addStat(GolemTypes.STAT_HEALTH.get(), 150)
                .addStat(GolemTypes.STAT_ATTACK.get(), 22)
                .addStat(GolemTypes.STAT_SPEED.get(), -0.15)
                .addStat(TGAttributes.STAT_ATTACK_P.get(), 0.3)
                .addModifier(TGGolemModifiers.COUNTERMEASURES.get(), 1)
                .end()

                .addMaterial(this.getResource("chlorophyte"), Ingredient.of(ModCommonItems.chlorophyte.getIngotTag()))
                .addStat(GolemTypes.STAT_HEALTH.get(), 225)
                .addStat(GolemTypes.STAT_ATTACK.get(), 14)
                .addStat(GolemTypes.STAT_REGEN.get(), 2)
                .addStat(GolemTypes.STAT_WEIGHT.get(), -0.3)
                .addModifier(TGGolemModifiers.SYMBIONIC.get(), 1)
                .end()
        );
    }

    @Override
    public void genLang(RegistrateLangProvider pvd) {
        pvd.add(this.getGolemMaterialsKey("ardite"), "Ardite");
        pvd.add(this.getGolemMaterialsKey("tinkers_bronze"), "Tinkers' Bronze");
        pvd.add(this.getGolemMaterialsKey("chlorophyte"), "Chlorophyte");
    }

    @Override
    public void genRecipe(RegistrateRecipeProvider pvd) {
        TGRecipeGen.genMetal(pvd, this.getResource("ardite"), ModFluids.molten_ardite, 90);
        TGRecipeGen.genMetal(pvd, this.getResource("tinkers_bronze"), ModFluids.molten_tinkers_bronze, 90);
        TGRecipeGen.genMetal(pvd, this.getResource("chlorophyte"), ModFluids.molten_chlorophyte, 90);
    }
}
