package golemknights.tinkersgolem.compat.materials;

import com.tterrag.registrate.providers.ProviderType;
import com.tterrag.registrate.providers.RegistrateLangProvider;
import com.tterrag.registrate.providers.RegistrateRecipeProvider;
import dev.xkmc.l2library.base.L2Registrate;
import dev.xkmc.l2library.serial.config.ConfigDataProvider;
import dev.xkmc.modulargolems.content.config.GolemMaterialConfig;
import dev.xkmc.modulargolems.init.ModularGolems;
import golemknights.tinkersgolem.compat.materials.tinkers.TinkersThinkingPlugin;
import golemknights.tinkersgolem.register.TGEntities;
import net.minecraftforge.data.event.GatherDataEvent;

import java.util.List;

public class L2CPlugin extends BaseMaterialPlugin{
    public static void addGenerator(L2Registrate registrate, GatherDataEvent event) {
        //registrate.addDataGenerator(ProviderType.LANG, new L2CPlugin()::genLang);
        //registrate.addDataGenerator(ProviderType.RECIPE, new L2CPlugin()::genRecipe);
        var gen = event.getGenerator();
        var output = gen.getPackOutput();
        var pvd = event.getLookupProvider();
        var helper = event.getExistingFileHelper();
        var server = event.includeServer();
        var client = event.includeClient();
        gen.addProvider(server, new L2CPlugin().new golemMaterialGen(gen));
    }
    @Override
    protected String getModId() {
        return "l2complements";
    }

    @Override
    public void addGolemMaterials(ConfigDataProvider.Collector map) {
        map.add(ModularGolems.MATERIALS, this.getResource("slimecore_support"), new GolemMaterialConfig()
                .supportsDefaultAnd(List.of(TGEntities.SLIME_CORE.get()),
                        this.getResource("totemic_gold"),
                        this.getResource("poseidite"),
                        this.getResource("shulkerate"),
                        this.getResource("eternium")
                )
        );
    }

    @Override
    public void genLang(RegistrateLangProvider pvd) {
    }

    @Override
    public void genRecipe(RegistrateRecipeProvider pvd) {
    }
}
