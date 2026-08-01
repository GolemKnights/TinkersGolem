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
                .addStat(GolemTypes.STAT_SPEED.get(), -0.1)
                .addModifier(TGGolemModifiers.CRAZYBONE.get(), 1)
                .end()
        );
    }

    @Override
    public void genLang(RegistrateLangProvider pvd) {
        pvd.add(this.getGolemMaterialsKey("ardite"), "Ardite");
    }

    @Override
    public void genRecipe(RegistrateRecipeProvider pvd) {
        TGRecipeGen.genMetal(pvd, this.getResource("ardite"), ModFluids.molten_ardite, 90);
    }
}
