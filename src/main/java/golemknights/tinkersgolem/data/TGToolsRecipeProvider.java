package golemknights.tinkersgolem.data;

import dev.xkmc.modulargolems.init.registrate.GolemItems;
import golemknights.tinkersgolem.TinkersGolem;
import golemknights.tinkersgolem.register.TGItems;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.crafting.Ingredient;
import slimeknights.tconstruct.library.data.recipe.IMaterialRecipeHelper;
import slimeknights.tconstruct.library.data.recipe.IToolRecipeHelper;
import slimeknights.tconstruct.library.recipe.tinkerstation.building.ToolBuildingRecipeBuilder;

import java.util.function.Consumer;

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
        TGItems.metalGolemArmor.forEach(
                (item) -> ToolBuildingRecipeBuilder.toolBuildingRecipe(item)
                        .addExtraRequirement(
                                Ingredient.of(
                                        GolemItems.GOLEM_TEMPLATE.get(),
                                        GolemItems.GOLEM_TEMPLATE.get()
                                )
                        ).layoutSlot(TGItems.METAL_GOLEM_ARMOR)
                        .save(consumer, this.prefix(id(item), metalGolemFolder))
        );
    }
    private void addPartRecipes(Consumer<FinishedRecipe> consumer) {
        String partFolder = "tools/parts/";
        String castFolder = "smeltery/casts/";
        this.partRecipes(consumer, TGItems.metal_golem_plating.get(ArmorItem.Type.HELMET), TGItems.helmetMetalGolemPlatingCast, 19, partFolder, castFolder);
        this.partRecipes(consumer, TGItems.metal_golem_plating.get(ArmorItem.Type.CHESTPLATE), TGItems.chestplateMetalGolemPlatingCast, 44, partFolder, castFolder);
        this.partRecipes(consumer, TGItems.metal_golem_plating.get(ArmorItem.Type.LEGGINGS), TGItems.leggingsMetalGolemPlatingCast, 11, partFolder, castFolder);
        this.partRecipes(consumer, TGItems.metal_golem_plating.get(ArmorItem.Type.BOOTS), TGItems.bootsMetalGolemPlatingCast, 4, partFolder, castFolder);
    }
    private void addRecycleRecipes(Consumer<FinishedRecipe> consumer) {
        String folder = "tools/recycling/";
    }
    @Override
    public String getName() {
        return "Tinkers' Golem Tool Recipes";
    }
}
