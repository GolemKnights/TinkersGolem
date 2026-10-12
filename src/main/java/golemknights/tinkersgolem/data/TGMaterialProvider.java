package golemknights.tinkersgolem.data;

import golemknights.tinkersgolem.TinkersGolem;
import golemknights.tinkersgolem.library.materialstats.CannonCoreMaterialStats;
import golemknights.tinkersgolem.register.TGTinkersModifiers;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.data.ExistingFileHelper;
import slimeknights.tconstruct.library.client.data.material.AbstractMaterialRenderInfoProvider;
import slimeknights.tconstruct.library.client.data.material.AbstractMaterialSpriteProvider;
import slimeknights.tconstruct.library.client.data.spritetransformer.OffsettingSpriteTransformer;
import slimeknights.tconstruct.library.data.material.AbstractMaterialDataProvider;
import slimeknights.tconstruct.library.data.material.AbstractMaterialStatsDataProvider;
import slimeknights.tconstruct.library.data.material.AbstractMaterialTraitDataProvider;
import slimeknights.tconstruct.library.data.recipe.IMaterialRecipeHelper;
import slimeknights.tconstruct.library.data.tinkering.AbstractMaterialTagProvider;
import slimeknights.tconstruct.shared.TinkerMaterials;
import slimeknights.tconstruct.smeltery.TinkerSmeltery;
import slimeknights.tconstruct.tools.data.ModifierIds;

import java.util.function.Consumer;

import static golemknights.tinkersgolem.register.TGMaterials.*;

public class TGMaterialProvider extends AbstractMaterialDataProvider {
    public TGMaterialProvider(PackOutput packOutput) {
        super(packOutput);
    }

    @Override
    protected void addMaterials() {
        material(slimecore).tier(3).sort(1).craftable();
        material(fluidcore).tier(2).sort(1).craftable();
        material(beaconcore).tier(4).sort(1).craftable();
        material(arrowcore).tier(2).sort(1).craftable();
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
            addMaterialStats(slimecore, new CannonCoreMaterialStats(0, 0.1f, 15));
            addMaterialStats(fluidcore, new CannonCoreMaterialStats(0, 0, 15));
            addMaterialStats(beaconcore, new CannonCoreMaterialStats(0, 0, 5));
            addMaterialStats(arrowcore, new CannonCoreMaterialStats(-0.1f, 0, 15));
        }

        @Override
        public String getName() {
            return "Tinker's Golem Material Stats";
        }
    }

    public static class Tags extends AbstractMaterialTagProvider {

        public Tags(PackOutput packOutput, ExistingFileHelper existingFileHelper) {
            super(packOutput, TinkersGolem.MODID, existingFileHelper);
        }

        @Override
        public String getName() {
            return "Tinker's Golem Material Tags";
        }

        @Override
        protected void addTags() {
            tag(cannoncore).add(arrowcore, slimecore, fluidcore, beaconcore);
        }

    }

    public static class Traits extends AbstractMaterialTraitDataProvider {
        public Traits(PackOutput packOutput, AbstractMaterialDataProvider materials) {
            super(packOutput, materials);
        }

        @Override
        protected void addMaterialTraits() {
            addDefaultTraits(slimecore, ModifierIds.slimeball);
            addDefaultTraits(fluidcore, TGTinkersModifiers.spewing);
            addDefaultTraits(beaconcore, TGTinkersModifiers.spotlight);
            addDefaultTraits(arrowcore, TGTinkersModifiers.shooting);
        }

        @Override
        public String getName() {
            return "Tinker's Golem Material Traits";
        }
    }

    public static class Sprites extends AbstractMaterialSpriteProvider
    {

        @Override
        public String getName() {
            return "Tinker's Golem Material Sprites";
        }

        @Override
        protected void addAllMaterials() {
            var NOP = new OffsettingSpriteTransformer(null,0,0);
            buildMaterial(slimecore).statType(CannonCoreMaterialStats.ID).transformer(NOP);
            buildMaterial(fluidcore).statType(CannonCoreMaterialStats.ID).transformer(NOP);
            buildMaterial(beaconcore).statType(CannonCoreMaterialStats.ID).transformer(NOP);
            buildMaterial(arrowcore).statType(CannonCoreMaterialStats.ID).transformer(NOP);
        }
    }

    public static class RenderInfo extends AbstractMaterialRenderInfoProvider
    {

        public RenderInfo(PackOutput packOutput, ExistingFileHelper existingFileHelper) {
            super(packOutput, new Sprites(), existingFileHelper);
        }

        @Override
        public String getName() {
            return "Tinker's Golem Material Render Info";
        }

        @Override
        protected void addMaterialRenderInfo() {
            buildRenderInfo(slimecore);
            buildRenderInfo(fluidcore);
            buildRenderInfo(beaconcore).luminosity(15);
            buildRenderInfo(arrowcore);
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
            materialRecipe(consumer, fluidcore, Ingredient.of(TinkerSmeltery.searedFluidCannon), 1, 1, folder + "fluidcore");
            materialRecipe(consumer, beaconcore, Ingredient.of(Items.BEACON), 1, 1, folder + "beaconcore");
            materialRecipe(consumer, arrowcore, Ingredient.of(Items.DISPENSER), 1, 1, folder + "arrowcore");
        }

        @Override
        public String getName() {
            return "Tinker's Golem Material Recipes";
        }
    }
}
