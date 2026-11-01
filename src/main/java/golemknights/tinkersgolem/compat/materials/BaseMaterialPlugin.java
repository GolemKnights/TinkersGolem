package golemknights.tinkersgolem.compat.materials;

import com.tterrag.registrate.providers.RegistrateLangProvider;
import com.tterrag.registrate.providers.RegistrateRecipeProvider;
import dev.xkmc.l2library.base.L2Registrate;
import dev.xkmc.l2library.serial.config.ConfigDataProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.fml.ModList;

@SuppressWarnings("removal")
public abstract class BaseMaterialPlugin {
    public static void addGenerators(L2Registrate registrate, GatherDataEvent event) {
        //TinkersThinkingPlugin.addGenerator(registrate, event);
        L2CPlugin.addGenerator(registrate, event);
        L2HPlugin.addGenerator(registrate, event);
    }
    public final boolean isLoaded(){
        return ModList.get().isLoaded(this.getModId());
    }
    public ResourceLocation getResource(String id) {
        return new ResourceLocation(this.getModId(), id);
    }
    public String getGolemMaterialsKey(String id) {
        return "golem_material." + this.getModId() + "." + id;
    }
    protected abstract String getModId();
    public abstract void addGolemMaterials(ConfigDataProvider.Collector map);
    public abstract void genLang(RegistrateLangProvider pvd);
    public abstract void genRecipe(RegistrateRecipeProvider pvd);
    public class golemMaterialGen extends ConfigDataProvider {
        public golemMaterialGen(DataGenerator generator) {
            super(generator, "Golem Config of " + BaseMaterialPlugin.this.getModId());
        }

        @Override
        public void add(Collector map) {
            addGolemMaterials(map);
        }
    }
}
