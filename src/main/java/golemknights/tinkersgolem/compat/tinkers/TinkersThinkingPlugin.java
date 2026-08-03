package golemknights.tinkersgolem.compat.tinkers;

import com.creeping_creeper.tinkers_thinking.common.register.ModCommonItems;
import com.creeping_creeper.tinkers_thinking.common.register.ModFluids;
import com.tterrag.registrate.providers.RegistrateLangProvider;
import com.tterrag.registrate.providers.RegistrateRecipeProvider;
import dev.xkmc.l2library.serial.config.ConfigDataProvider;
import dev.xkmc.modulargolems.content.config.GolemMaterialConfig;
import dev.xkmc.modulargolems.init.ModularGolems;
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
                .addStat(GolemTypes.STAT_WEIGHT.get(), -0.2)
                .addModifier(TGGolemModifiers.CRAZY_METAL.get(), 1)
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
                .addStat(GolemTypes.STAT_WEIGHT.get(), 0.3)
                .addModifier(TGGolemModifiers.SYMBIONIC.get(), 1)
                .end()

                .addMaterial(this.getResource("lightite"), Ingredient.of(ModCommonItems.lightite.getIngotTag()))
                .addStat(GolemTypes.STAT_HEALTH.get(), 100)
                .addStat(GolemTypes.STAT_ATTACK.get(), 22)
                .addStat(TGAttributes.STAT_GRAVITY.get(), -0.2)
                .addStat(GolemTypes.STAT_WEIGHT.get(), 0.6)
                .addModifier(TGGolemModifiers.LIGHTLY_ATTACK.get(), 1)
                .end()

                .addMaterial(this.getResource("obsidian_bronze"), Ingredient.of(ModCommonItems.obsidian_bronze.getIngotTag()))
                .addStat(GolemTypes.STAT_HEALTH.get(), 185)
                .addStat(GolemTypes.STAT_ATTACK.get(), 24)
                .addStat(GolemTypes.STAT_SPEED.get(), -0.1)
                .addModifier(TGGolemModifiers.DURITAE.get(), 1)
                .end()

                .addMaterial(this.getResource("electrical_steel"), Ingredient.of(ModCommonItems.electrical_steel.getIngotTag()))
                .addStat(GolemTypes.STAT_HEALTH.get(), 200)
                .addStat(GolemTypes.STAT_ATTACK.get(), 22)
                .addStat(GolemTypes.STAT_SIZE.get(), 1)
                .addModifier(TGGolemModifiers.REPULSIVE.get(), 1)
                .end()
        );
    }

    @Override
    public void genLang(RegistrateLangProvider pvd) {
        pvd.add(this.getGolemMaterialsKey("ardite"), "Ardite");
        pvd.add(this.getGolemMaterialsKey("tinkers_bronze"), "Tinkers' Bronze");
        pvd.add(this.getGolemMaterialsKey("chlorophyte"), "Chlorophyte");
        pvd.add(this.getGolemMaterialsKey("lightite"), "Lightite");
        pvd.add(this.getGolemMaterialsKey("obsidian_bronze"), "Obsidian Bronze");
        pvd.add(this.getGolemMaterialsKey("electrical_steel"), "Electrical Steel");
    }

    @Override
    public void genRecipe(RegistrateRecipeProvider pvd) {
        TGRecipeGen.genMetal(pvd, this.getResource("ardite"), ModFluids.molten_ardite, 90);
        TGRecipeGen.genMetal(pvd, this.getResource("tinkers_bronze"), ModFluids.molten_tinkers_bronze, 90);
        TGRecipeGen.genMetal(pvd, this.getResource("chlorophyte"), ModFluids.molten_chlorophyte, 90);
        TGRecipeGen.genMetal(pvd, this.getResource("lightite"), ModFluids.molten_lightite, 90);
        TGRecipeGen.genMetal(pvd, this.getResource("obsidian_bronze"), ModFluids.molten_obsidian_bronze, 90);
        TGRecipeGen.genMetal(pvd, this.getResource("electrical_steel"), ModFluids.molten_electrical_steel, 90);
    }
}
