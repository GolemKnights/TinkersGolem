package golemknights.tinkersgolem.library.recipes;

import java.util.List;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import slimeknights.mantle.data.loadable.array.ArrayLoadable;
import slimeknights.mantle.data.loadable.array.IntArrayLoadable;
import slimeknights.mantle.data.loadable.field.ContextKey;
import slimeknights.mantle.data.loadable.field.RecordField;
import slimeknights.mantle.data.loadable.primitive.IntLoadable;
import slimeknights.mantle.data.loadable.record.RecordLoadable;
import slimeknights.mantle.data.predicate.IJsonPredicate;
import slimeknights.mantle.recipe.ingredient.SizedIngredient;
import slimeknights.tconstruct.library.json.predicate.material.MaterialPredicate;
import slimeknights.tconstruct.library.materials.definition.MaterialVariantId;
import slimeknights.tconstruct.library.recipe.tinkerstation.ITinkerStationContainer;
import slimeknights.tconstruct.library.recipe.tinkerstation.building.MaterialValueSwappingRecipe;
import golemknights.tinkersgolem.register.TGRecipes;

/**
 * {@link MaterialValueSwappingRecipe} 的变体：若指定的输入槽里放着有效材料，整条配方**不匹配**（忽略而非报错）。
 * <p>
 * 用于避免材料被放在 0 号槽时，被兜底到 indices[0] 指向的部件上、进而报 INVALID_MATERIAL。
 */
public class SlotSkippingMaterialSwappingRecipe extends MaterialValueSwappingRecipe {
    /** material / cost 在父类是 private，这里各留一份供 LOADER 读写 */
    private final IJsonPredicate<MaterialVariantId> material;
    private final int cost;
    /** 这些输入槽里若出现有效材料，则整条配方不匹配 */
    private final int[] skips;

    private static final RecordField<int[], SlotSkippingMaterialSwappingRecipe> SKIPS_FIELD = new IntArrayLoadable(
            IntLoadable.FROM_ZERO, ArrayLoadable.COMPACT, 10).requiredField("skips", r -> r.skips);

    /** 字段顺序必须与构造函数参数顺序一致 */
    public static final RecordLoadable<SlotSkippingMaterialSwappingRecipe> LOADER = RecordLoadable.create(
            ContextKey.ID.requiredField(), TOOLS_FIELD, STACK_SIZE_FIELD,
            MaterialPredicate.LOADER.requiredField("material", r -> r.material),
            IntLoadable.FROM_ONE.requiredField("cost", r -> r.cost),
            INDICES_FIELD, EXTRA_REQUIREMENTS_FIELD, SKIPS_FIELD,
            SlotSkippingMaterialSwappingRecipe::new);

    public SlotSkippingMaterialSwappingRecipe(ResourceLocation id, Ingredient tools, int maxStackSize,
            IJsonPredicate<MaterialVariantId> material, int cost, int[] indices,
            List<SizedIngredient> extraRequirements, int[] skips) {
        super(id, tools, maxStackSize, material, cost, indices, extraRequirements);
        this.material = material;
        this.cost = cost;
        this.skips = skips;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return TGRecipes.SLOT_SKIPPING_MATERIAL_SWAPPING_SERIALIZER.get();
    }

    @Override
    public boolean matches(ITinkerStationContainer inv, Level world) {
        int count = inv.getInputCount();
        for (int slot : skips) {
            if (slot < count && isMaterial(inv.getInput(slot)))
                return false;
        }
        return super.matches(inv, world);
    }
}
