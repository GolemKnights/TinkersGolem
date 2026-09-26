package golemknights.tinkersgolem.data;

import dev.xkmc.modulargolems.init.registrate.GolemItems;
import golemknights.tinkersgolem.library.materialstats.CannonCoreMaterialStats;
import golemknights.tinkersgolem.register.TGItems;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.crafting.CompoundIngredient;
import slimeknights.mantle.data.predicate.IJsonPredicate;
import slimeknights.tconstruct.library.data.recipe.IMaterialRecipeHelper;
import slimeknights.tconstruct.library.data.recipe.IToolRecipeHelper;
import slimeknights.tconstruct.library.json.predicate.material.MaterialPredicate;
import slimeknights.tconstruct.library.json.predicate.material.MaterialStatTypePredicate;
import slimeknights.tconstruct.library.materials.stats.MaterialStatsId;
import slimeknights.tconstruct.library.recipe.ingredient.MaterialIngredient;
import slimeknights.tconstruct.library.recipe.ingredient.MaterialValueIngredient;
import slimeknights.tconstruct.library.recipe.tinkerstation.building.ToolBuildingRecipeBuilder;
import slimeknights.tconstruct.tools.TinkerToolParts;
import slimeknights.tconstruct.tools.stats.LimbMaterialStats;

import java.util.function.Consumer;
import java.util.function.Function;

public class TGToolsRecipeProvider extends BaseRecipeProvider implements IMaterialRecipeHelper, IToolRecipeHelper {
    public TGToolsRecipeProvider(PackOutput generator) {
        super(generator);
    }

    @Override
    protected void buildRecipes(Consumer<FinishedRecipe> consumer) {
        this.addToolBuildingRecipes(consumer);
        this.addPartRecipes(consumer);
        this.addRecycleRecipes(consumer);
    }
    private void addToolBuildingRecipes(Consumer<FinishedRecipe> consumer) {
        String folder = "tools/building/";
        String armorFolder = "tools/armor/";
        String metalGolemFolder = armorFolder + "metal_golem/";
        Function<MaterialStatsId, Ingredient> material = (type) -> CompoundIngredient.of(MaterialValueIngredient.of(new MaterialStatTypePredicate(type), 1.0F), MaterialIngredient.of(TinkerToolParts.fakeIngot, new MaterialStatTypePredicate(type)));
        Function<MaterialStatsId, Ingredient> cannonCoreMaterial = (type) -> MaterialValueIngredient.of(new MaterialStatTypePredicate(type), 1.0F);
        TGItems.metalGolemArmor.forEach(
                (item) -> ToolBuildingRecipeBuilder.toolBuildingRecipe(item)
                        .addExtraRequirement(Ingredient.of(GolemItems.GOLEM_TEMPLATE.get()))
                        .addExtraRequirement(Ingredient.of(GolemItems.GOLEM_TEMPLATE.get()))
                        .layoutSlot(TGItems.METAL_GOLEM_ARMOR_PATTERN)
                        .save(consumer, this.prefix(id(item), metalGolemFolder))
        );
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, TGItems.cannonItem)
                .pattern(" AA")
                .pattern("C  ")
                .pattern("TBB")
                .define('T', GolemItems.GOLEM_TEMPLATE.get())
                .define('C', cannonCoreMaterial.apply(CannonCoreMaterialStats.ID))
                .define('A', material.apply(LimbMaterialStats.ID))
                .define('B', material.apply(LimbMaterialStats.ID))
                .unlockedBy("has_template", has(GolemItems.GOLEM_TEMPLATE.get()))
                .save(consumer, this.prefix(id(TGItems.cannonItem.get()), folder))
        ;
    }
    private void addPartRecipes(Consumer<FinishedRecipe> consumer) {
        String partFolder = "tools/parts/";
        String castFolder = "smeltery/casts/";
        this.partRecipes(consumer, TGItems.metalGolemPlating.get(ArmorItem.Type.HELMET), TGItems.helmetMetalGolemPlatingCast, 19, partFolder, castFolder);
        this.partRecipes(consumer, TGItems.metalGolemPlating.get(ArmorItem.Type.CHESTPLATE), TGItems.chestplateMetalGolemPlatingCast, 44, partFolder, castFolder);
        this.partRecipes(consumer, TGItems.metalGolemPlating.get(ArmorItem.Type.LEGGINGS), TGItems.leggingsMetalGolemPlatingCast, 11, partFolder, castFolder);
        this.partRecipes(consumer, TGItems.metalGolemPlating.get(ArmorItem.Type.BOOTS), TGItems.bootsMetalGolemPlatingCast, 4, partFolder, castFolder);
    }
    private void addRecycleRecipes(Consumer<FinishedRecipe> consumer) {
        String folder = "tools/recycling/";
    }
    @Override
    public String getName() {
        return "Tinkers' Golem Tool Recipes";
    }
}
