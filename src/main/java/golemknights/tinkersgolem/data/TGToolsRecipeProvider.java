package golemknights.tinkersgolem.data;

import dev.xkmc.modulargolems.init.registrate.GolemItems;
import golemknights.tinkersgolem.library.materialstats.CannonCoreMaterialStats;
import golemknights.tinkersgolem.library.recipes.ExtendedShapedMaterialsRecipe;
import golemknights.tinkersgolem.library.recipes.ExtendedShapedMaterialsRecipe.ExtendedMaterialIngredient;
import golemknights.tinkersgolem.library.recipes.ExtendedShapedMaterialsRecipe.ExtendedMaterialValueIngredient;
import golemknights.tinkersgolem.library.recipes.SlotSkippingMaterialSwappingRecipe;
import golemknights.tinkersgolem.register.TGItems;
import golemknights.tinkersgolem.register.TGMaterials;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.crafting.CompoundIngredient;
import slimeknights.mantle.recipe.ingredient.SizedIngredient;
import slimeknights.tconstruct.common.recipe.LoadableFinishedRecipe;
import slimeknights.tconstruct.library.data.recipe.IMaterialRecipeHelper;
import slimeknights.tconstruct.library.data.recipe.IToolRecipeHelper;
import slimeknights.tconstruct.library.json.predicate.material.MaterialPredicate;
import slimeknights.tconstruct.library.json.predicate.material.MaterialStatTypePredicate;
import slimeknights.tconstruct.library.materials.stats.MaterialStatsId;
import slimeknights.tconstruct.library.recipe.ingredient.MaterialIngredient;
import slimeknights.tconstruct.library.recipe.ingredient.MaterialValueIngredient;
import slimeknights.tconstruct.library.recipe.partbuilder.recycle.PartBuilderToolRecycleBuilder;
import slimeknights.tconstruct.library.recipe.tinkerstation.building.MaterialSwappingRecipeBuilder;
import slimeknights.tconstruct.library.recipe.tinkerstation.building.ToolBuildingRecipeBuilder;
import slimeknights.tconstruct.shared.TinkerMaterials;
import slimeknights.tconstruct.tools.TinkerToolParts;
import slimeknights.tconstruct.tools.stats.LimbMaterialStats;

import static golemknights.tinkersgolem.TinkersGolem.getResource;

import java.util.List;
import java.util.function.BiFunction;
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
        this.addSwappingRecipes(consumer);
    }
    private void addToolBuildingRecipes(Consumer<FinishedRecipe> consumer) {
        String folder = "tools/building/";
        String armorFolder = "tools/armor/";
        String metalGolemFolder = armorFolder + "metal_golem/";
        BiFunction<MaterialStatsId, Integer, Ingredient> material = (type, slot) -> new ExtendedMaterialIngredient(CompoundIngredient.of(MaterialValueIngredient.of(new MaterialStatTypePredicate(type), 1.0F), MaterialIngredient.of(TinkerToolParts.fakeIngot, new MaterialStatTypePredicate(type))), slot);
        BiFunction<MaterialStatsId, Integer, Ingredient> cannonCoreMaterial = (type, slot) -> new ExtendedMaterialIngredient(new ExtendedMaterialValueIngredient(new MaterialStatTypePredicate(type), 1.0F, 1.0F), slot, new MaterialStatTypePredicate(type));
        TGItems.metalGolemArmor.forEach(
                (item) -> ToolBuildingRecipeBuilder.toolBuildingRecipe(item)
                        .addExtraRequirement(Ingredient.of(GolemItems.GOLEM_TEMPLATE.get()))
                        .addExtraRequirement(Ingredient.of(GolemItems.GOLEM_TEMPLATE.get()))
                        .layoutSlot(TGItems.METAL_GOLEM_ARMOR_PATTERN)
                        .save(consumer, this.prefix(id(item), metalGolemFolder))
        );
        ToolBuildingRecipeBuilder.toolBuildingRecipe(TGItems.metalGolemGloves.get())
                .addExtraRequirement(Ingredient.of(GolemItems.GOLEM_TEMPLATE.get()))
                .addExtraRequirement(Ingredient.of(GolemItems.GOLEM_TEMPLATE.get()))
                .layoutSlot(TGItems.METAL_GOLEM_ARMOR_PATTERN)
                .save(consumer, this.prefix(id(TGItems.metalGolemGloves.get()), metalGolemFolder));
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, TGItems.cannonItem)
                .pattern(" AB")
                .pattern("C  ")
                .pattern("TAB")
                .define('T', GolemItems.GOLEM_TEMPLATE.get())
                .define('C', cannonCoreMaterial.apply(CannonCoreMaterialStats.ID, 0))
                .define('A', material.apply(LimbMaterialStats.ID, 1))
                .define('B', material.apply(LimbMaterialStats.ID, 2))
                .unlockedBy("has_template", has(GolemItems.GOLEM_TEMPLATE.get()))
                .save(ExtendedShapedMaterialsRecipe.ConsumerBuilder.createConsumer(consumer), this.prefix(id(TGItems.cannonItem.get()), folder));

        ToolBuildingRecipeBuilder.toolBuildingRecipe(TGItems.swordItem.get())
                .addExtraRequirement(Ingredient.of(GolemItems.GOLEM_TEMPLATE.get()))
                .save(consumer, this.prefix(id(TGItems.swordItem.get()), metalGolemFolder));
        ToolBuildingRecipeBuilder.toolBuildingRecipe(TGItems.axeItem.get())
                .addExtraRequirement(Ingredient.of(GolemItems.GOLEM_TEMPLATE.get()))
                .save(consumer, this.prefix(id(TGItems.axeItem.get()), metalGolemFolder));
        ToolBuildingRecipeBuilder.toolBuildingRecipe(TGItems.spearItem.get())
                .addExtraRequirement(Ingredient.of(GolemItems.GOLEM_TEMPLATE.get()))
                .save(consumer, this.prefix(id(TGItems.spearItem.get()), metalGolemFolder));
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
        PartBuilderToolRecycleBuilder.tool(TGItems.cannonItem)
        .part(TGItems.cannonCoreItem)
        .part(TinkerToolParts.bowLimb)
        .part(TinkerToolParts.repairKit)
        .save(consumer,this.prefix(id(TGItems.cannonItem.get()), folder));
    }

    private void addSwappingRecipes(Consumer<FinishedRecipe> consumer) {
        String folder = "tools/part_swapping/";
        MaterialSwappingRecipeBuilder.tool(TGItems.cannonItem.get())
                .index(0)
                .materials(new MaterialStatTypePredicate(CannonCoreMaterialStats.ID), 1)
                .save(consumer, this.prefix(getResource("cannon_core"), folder));
        MaterialSwappingRecipeBuilder.tool(TGItems.cannonItem.get())
                .index(0)
                .material(TGMaterials.slimecore,
                        SizedIngredient.fromTag(TinkerMaterials.slimesteel.getBlockItemTag(), 1))
                .save(consumer, this.prefix(getResource("cannon_core_slime"), folder));
        // 肢体材料：0 号槽里出现有效材料时整条配方忽略（避免被兜底到核心上而报 INVALID_MATERIAL）
        // indices = {1, 2}：默认换 1 号肢体，材料放在第 2 个输入槽时换成 2 号
        LoadableFinishedRecipe.save(consumer, SlotSkippingMaterialSwappingRecipe.LOADER,
                new SlotSkippingMaterialSwappingRecipe(
                        this.prefix(getResource("cannon_limb"), folder),
                        Ingredient.of(TGItems.cannonItem.get()), 16,
                        new MaterialStatTypePredicate(LimbMaterialStats.ID), 2,
                        new int[] { 1, 2 }, List.of(), new int[] { 0 }));
    }

    @Override
    public String getName() {
        return "Tinkers' Golem Tool Recipes";
    }
}
