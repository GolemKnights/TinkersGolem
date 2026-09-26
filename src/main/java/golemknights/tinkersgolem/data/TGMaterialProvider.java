package golemknights.tinkersgolem.data;

import golemknights.tinkersgolem.library.materialstats.CannonCoreMaterialStats;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import slimeknights.tconstruct.library.data.material.AbstractMaterialDataProvider;
import slimeknights.tconstruct.library.data.material.AbstractMaterialStatsDataProvider;
import slimeknights.tconstruct.library.data.material.AbstractMaterialTraitDataProvider;
import slimeknights.tconstruct.library.data.recipe.IMaterialRecipeHelper;
import slimeknights.tconstruct.shared.TinkerMaterials;
import slimeknights.tconstruct.tools.data.ModifierIds;

import java.util.function.Consumer;

import static golemknights.tinkersgolem.register.TGMaterials.slimecore;

public class TGMaterialProvider extends AbstractMaterialDataProvider {
    public TGMaterialProvider(PackOutput packOutput) {
        super(packOutput);
    }

    @Override
    protected void addMaterials() {
        material(slimecore).tier(3).sort(1).craftable();
    }

    @Override
    public String getName() {
        return "Tinker's Golem Materials";
    }

    public static class Stats extends AbstractMaterialStatsDataProvider {
        public Stats(PackOutput packOutput, AbstractMaterialDataProvider materials) {
            super(packOutput, materials);
        }

        @Override
        protected void addMaterialStats() {
            addMaterialStats(slimecore, new CannonCoreMaterialStats(0, 0, 15));
        }

        @Override
        public String getName() {
            return "Tinker's Golem Material Stats";
        }
    }

    public static class Traits extends AbstractMaterialTraitDataProvider {
        public Traits(PackOutput packOutput, AbstractMaterialDataProvider materials) {
            super(packOutput, materials);
        }

        @Override
        protected void addMaterialTraits() {
            addDefaultTraits(slimecore, ModifierIds.slimeball);
        }

        @Override
        public String getName() {
            return "Tinker's Golem Material Traits";
        }
    }

    public static class Recipes extends BaseRecipeProvider implements IMaterialRecipeHelper {
        public Recipes(PackOutput packOutput) {
            super(packOutput);
        }


        @Override
        protected void buildRecipes(Consumer<FinishedRecipe> consumer) {
            String folder = "tools/materials/";
            materialRecipe(consumer, slimecore, Ingredient.of(TinkerMaterials.slimesteel.getBlockItemTag()), 1, 1, folder + "slimecore");
        }

        @Override
        public String getName() {
            return "Tinker's Golem Material Recipes";
        }
    }
}
