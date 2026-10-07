package golemknights.tinkersgolem.content.recipe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import javax.annotation.Nullable;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

import golemknights.tinkersgolem.TinkersGolem;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.IIngredientSerializer;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent.Finish;
import slimeknights.mantle.data.predicate.IJsonPredicate;
import slimeknights.tconstruct.library.json.predicate.material.MaterialPredicate;
import slimeknights.tconstruct.library.materials.definition.MaterialId;
import slimeknights.tconstruct.library.materials.definition.MaterialVariantId;
import slimeknights.tconstruct.library.recipe.ingredient.MaterialValueIngredient;
import slimeknights.tconstruct.library.recipe.ingredient.NestedIngredient;
import slimeknights.tconstruct.library.recipe.material.MaterialRecipe;
import slimeknights.tconstruct.library.recipe.material.MaterialRecipeCache;
import slimeknights.tconstruct.library.recipe.material.MaterialValue;
import slimeknights.tconstruct.library.tools.nbt.MaterialNBT;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;
import slimeknights.tconstruct.library.tools.part.IMaterialItem;

public class ExtendedShapedMaterialsRecipe extends ShapedRecipe {

    // 缓存上一次的匹配结果
    private transient Map<Integer, Ingredient> lastMatch;

    public ExtendedShapedMaterialsRecipe(ResourceLocation id, String group, CraftingBookCategory category,
            int width, int height, NonNullList<Ingredient> ingredients,
            ItemStack result, boolean showNotification) {
        super(id, group, category, width, height, ingredients, result, showNotification);
    }

    public ExtendedShapedMaterialsRecipe(ResourceLocation id, String group, CraftingBookCategory category,
            int width, int height, NonNullList<Ingredient> ingredients,
            ItemStack result) {
        this(id, group, category, width, height, ingredients, result, true);
    }

    // ==================== 共用匹配逻辑（带缓存） ====================
    /**
     * 匹配容器中的物品与配方的原料，返回每个格子对应的原料。
     * 若无法匹配则抛出异常。
     */
    private Map<Integer, Ingredient> matchIngredients(CraftingContainer container) {
        // 优先验证缓存
        if (lastMatch != null && isCacheValid(container)) {
            return lastMatch;
        }

        int containerWidth = container.getWidth();
        int containerHeight = container.getHeight();
        List<Ingredient> ingredients = getIngredients().stream()
                .filter(ing -> ing != Ingredient.EMPTY)
                .collect(Collectors.toList());

        // 情况1：容器宽度为0或高度为0（例如模组假工作台），使用无序回溯匹配
        if (containerWidth == 0 || containerHeight == 0) {
            List<Integer> nonEmptySlots = new ArrayList<>();
            for (int i = 0; i < container.getContainerSize(); i++) {
                if (!container.getItem(i).isEmpty()) {
                    nonEmptySlots.add(i);
                }
            }
            if (nonEmptySlots.size() != ingredients.size()) {
                throw new IllegalArgumentException("Item count mismatch: expected " + ingredients.size() +
                        ", got " + nonEmptySlots.size());
            }
            Map<Integer, Ingredient> slotToIngredient = new HashMap<>();
            boolean[] slotUsed = new boolean[container.getContainerSize()];
            boolean found = backtrackMatch(0, ingredients, nonEmptySlots, slotUsed, slotToIngredient, container);
            if (!found) {
                throw new IllegalArgumentException("Cannot match recipe ingredients (unordered)");
            }
            lastMatch = slotToIngredient;
            return slotToIngredient;
        }

        // 情况2：正常有序网格，尝试所有起始位置和镜像
        int recipeWidth = getWidth();
        int recipeHeight = getHeight();
        for (int startX = 0; startX <= containerWidth - recipeWidth; startX++) {
            for (int startY = 0; startY <= containerHeight - recipeHeight; startY++) {
                for (boolean mirrored : new boolean[] { false, true }) {
                    if (!matchesShape(container, startX, startY, mirrored))
                        continue;

                    Map<Integer, Ingredient> matchedSlots = new HashMap<>();
                    // 临时原料列表，用于消耗标记（不修改原列表）
                    List<Ingredient> remainingIngredients = new ArrayList<>(ingredients);

                    for (int row = 0; row < recipeHeight; row++) {
                        for (int col = 0; col < recipeWidth; col++) {
                            int ingredientIndex = mirrored
                                    ? (recipeWidth - col - 1) + row * recipeWidth
                                    : col + row * recipeWidth;
                            // 原配方 getIngredients() 包含空格，需要映射到过滤后的 ingredients 列表
                            Ingredient originalIng = getIngredients().get(ingredientIndex);
                            if (originalIng == Ingredient.EMPTY)
                                continue;

                            // 在剩余的原料列表中找到匹配的原料（支持同种原料重复）
                            int matchedIdx = -1;
                            for (int idx = 0; idx < remainingIngredients.size(); idx++) {
                                if (remainingIngredients.get(idx) == originalIng) {
                                    matchedIdx = idx;
                                    break;
                                }
                            }
                            if (matchedIdx == -1) {
                                matchedSlots = null;
                                break;
                            }

                            int containerSlot = (startY + row) * containerWidth + (startX + col);
                            if (containerSlot >= container.getContainerSize()) {
                                matchedSlots = null;
                                break;
                            }
                            ItemStack stack = container.getItem(containerSlot);
                            if (stack.isEmpty() || !originalIng.test(stack)) {
                                matchedSlots = null;
                                break;
                            }

                            matchedSlots.put(containerSlot, originalIng);
                            remainingIngredients.remove(matchedIdx);
                        }
                        if (matchedSlots == null)
                            break;
                    }

                    if (matchedSlots != null && remainingIngredients.isEmpty()) {
                        lastMatch = matchedSlots;
                        return matchedSlots;
                    }
                }
            }
        }
        throw new IllegalArgumentException("Cannot match recipe ingredients (shaped)");
    }

    /**
     * 判断在给定偏移和镜像下形状是否匹配（不检查原料对应关系，仅检查物品是否被原料接受）
     */
    private boolean matchesShape(CraftingContainer container, int startX, int startY, boolean mirrored) {
        NonNullList<Ingredient> ingredients = getIngredients();
        int recipeWidth = getWidth();
        int recipeHeight = getHeight();
        int containerWidth = container.getWidth();

        for (int i = 0; i < containerWidth; i++) {
            for (int j = 0; j < container.getHeight(); j++) {
                int k = i - startX;
                int l = j - startY;
                Ingredient ingredient = Ingredient.EMPTY;
                if (k >= 0 && l >= 0 && k < recipeWidth && l < recipeHeight) {
                    if (mirrored) {
                        ingredient = ingredients.get(recipeWidth - k - 1 + l * recipeWidth);
                    } else {
                        ingredient = ingredients.get(k + l * recipeWidth);
                    }
                }
                if (!ingredient.test(container.getItem(i + j * containerWidth))) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * 验证缓存的匹配结果是否仍适用于当前容器
     */
    private boolean isCacheValid(CraftingContainer container) {
        if (lastMatch == null)
            return false;
        // 检查每个缓存槽位：物品非空且通过原料测试
        for (Map.Entry<Integer, Ingredient> entry : lastMatch.entrySet()) {
            int slot = entry.getKey();
            if (slot >= container.getContainerSize())
                return false;
            ItemStack stack = container.getItem(slot);
            if (stack.isEmpty())
                return false;
            if (!entry.getValue().test(stack))
                return false;
        }
        // 检查容器中是否有未被缓存覆盖的非空槽位
        for (int i = 0; i < container.getContainerSize(); i++) {
            if (!container.getItem(i).isEmpty() && !lastMatch.containsKey(i)) {
                return false;
            }
        }
        // 确保缓存大小等于非空原料数量
        long nonEmptyIngredientCount = getIngredients().stream().filter(ing -> ing != Ingredient.EMPTY).count();
        return lastMatch.size() == nonEmptyIngredientCount;
    }

    public boolean matches(CraftingContainer container, Level level) {
        if (!super.matches(container, level))
            return false;
        Map<Integer, Ingredient> slotToIngredient;
        try {
            slotToIngredient = matchIngredients(container);
        } catch (IllegalArgumentException e) {
            return false;
        }
        MaterialVariantId[] materials = new MaterialVariantId[ExtendedMaterialIngredient.MAX_SLOT];
        for (var entry : slotToIngredient.entrySet()) {
            int slot = entry.getKey();
            ItemStack inputStack = container.getItem(slot);
            Ingredient ingredient = entry.getValue();
            if (!testAndApplyMaterial(inputStack, ingredient, materials))
                return false;
        }
        return true;
    }

    private boolean testAndApplyMaterial(ItemStack stack, Ingredient ing, MaterialVariantId[] materials) {

        if (ing instanceof ExtendedMaterialIngredient ext) {
            MaterialVariantId current = materials[ext.slot];
            MaterialVariantId matched;
            if (stack.getItem() instanceof IMaterialItem materialItem) {
                matched = materialItem.getMaterial(stack);
            } else {
                matched = MaterialRecipeCache.getAllRecipes().stream()
                        .filter(r -> r.getIngredient().test(stack))
                        .map(r -> r.getMaterial().getVariant())
                        .filter(v -> ext.material.matches(v)).findAny().get();
                if (matched == null)
                    return false;
            }
            // first occurrence? thats our material
            if (current == null) {
                materials[ext.slot] = matched;
                return true;
            } else if (!current.matchesVariant(matched)) {
                // if same material but different variants, just discard the variant
                if (current.getId().equals(matched.getId())) {
                    materials[ext.slot] = current.getId();
                    return true;
                } else {
                    // if different materials, no match
                    return false;
                }
            }
        }
        return true;
    }

    // ==================== 合成输出处理 ====================
    @Override
    public ItemStack assemble(CraftingContainer container, RegistryAccess registryAccess) {
        ItemStack result = super.assemble(container, registryAccess);
        if (result.isEmpty()) {
            return result;
        }
        Map<Integer, Ingredient> slotToIngredient;
        try {
            slotToIngredient = matchIngredients(container);
        } catch (IllegalArgumentException e) {
            return ItemStack.EMPTY;
        }
        MaterialVariantId[] materials = new MaterialVariantId[ExtendedMaterialIngredient.MAX_SLOT];
        for (var entry : slotToIngredient.entrySet()) {
            int slot = entry.getKey();
            ItemStack inputStack = container.getItem(slot);
            Ingredient ingredient = entry.getValue();
            if (!testAndApplyMaterial(inputStack, ingredient, materials))
                return ItemStack.EMPTY;
        }
        ToolStack tool = ToolStack.from(result);
        for (int i = 0; i < ExtendedMaterialIngredient.MAX_SLOT; i++) {
            MaterialVariantId matVar = materials[i];
            TinkersGolem.LOGGER.debug("{} is {}", i, matVar);
            if (matVar != null)
                tool.replaceMaterial(i, matVar);
        }
        return result;
    }

    public static class ExtendedMaterialIngredient extends NestedIngredient {

        /** 材料槽位（材料下标）上限，需与材料数组容量保持一致 */
        private static final int MAX_SLOT = 9;

        private final int slot;
        private final IJsonPredicate<MaterialVariantId> material;

        public ExtendedMaterialIngredient(Ingredient nested, int slot) {
            this(nested, slot, MaterialPredicate.ANY);
        }

        public ExtendedMaterialIngredient(Ingredient nested, int slot, IJsonPredicate<MaterialVariantId> material) {
            super(nested);
            this.slot = slot;
            this.material = material;
        }

        @Override
        public JsonElement toJson() {
            JsonObject result = new JsonObject();
            result.add("match", nested.toJson());
            result.addProperty("type", Serializer.ID.toString());
            result.addProperty("slot", slot);
            if (material != MaterialPredicate.ANY)
                result.add("material", MaterialPredicate.LOADER.serialize(material));
            return result;
        }

        @Override
        public boolean isSimple() {
            return false;
        }

        @Override
        public IIngredientSerializer<? extends Ingredient> getSerializer() {
            return Serializer.INSTANCE;
        }

        /** 注册自定义原料序列化器，需在 FMLCommonSetupEvent 中调用 */
        public static void register() {
            CraftingHelper.register(Serializer.ID, Serializer.INSTANCE);
        }

        /** Serializer instance */
        public enum Serializer implements IIngredientSerializer<ExtendedMaterialIngredient> {
            INSTANCE;

            public static final ResourceLocation ID = TinkersGolem.getResource("material");

            @Override
            public ExtendedMaterialIngredient parse(JsonObject json) {
                Ingredient match = Ingredient.fromJson(json.get("match"));
                int slot = GsonHelper.getAsInt(json, "slot");
                IJsonPredicate<MaterialVariantId> mat = json.has("material")
                        ? MaterialPredicate.LOADER.convert(json.get("material"), "material")
                        : MaterialPredicate.ANY;
                if (slot < 0 || slot >= MAX_SLOT)
                    throw new JsonSyntaxException("Material slot out of range [0, " + MAX_SLOT + "): " + slot);
                return new ExtendedMaterialIngredient(match, slot, mat);
            }

            @Override
            public ExtendedMaterialIngredient parse(FriendlyByteBuf buffer) {
                Ingredient match = Ingredient.fromNetwork(buffer);
                int slot = buffer.readByte();
                IJsonPredicate<MaterialVariantId> mat = MaterialPredicate.LOADER.decode(buffer);
                if (slot < 0 || slot >= MAX_SLOT)
                    throw new IllegalArgumentException("Material slot out of range [0, " + MAX_SLOT + "): " + slot);
                return new ExtendedMaterialIngredient(match, slot, mat);
            }

            @Override
            public void write(FriendlyByteBuf buffer, ExtendedMaterialIngredient ingredient) {
                ingredient.nested.toNetwork(buffer);
                buffer.writeByte(ingredient.slot);
                MaterialPredicate.LOADER.encode(buffer, ingredient.material);
            }
        }
    }

    public static class ExtendedMaterialValueIngredient extends MaterialValueIngredient {

        public ExtendedMaterialValueIngredient(IJsonPredicate<MaterialVariantId> material, float minValue,
                float maxValue) {
            super(material, minValue, maxValue);
        }

        @Override
        public boolean test(@Nullable ItemStack stack) {
            if (stack == null) {
                return false;
            }
            return MaterialRecipeCache.getAllRecipes().stream().filter(r -> r.getIngredient().test(stack))
                    .anyMatch(r -> test(r));
        }

        @Override
        public JsonElement toJson() {
            JsonObject json = (JsonObject) super.toJson();
            json.addProperty("type", Serializer.ID.toString());
            return json;
        }

        @Override
        public IIngredientSerializer<? extends Ingredient> getSerializer() {
            return Serializer.INSTANCE;
        }

        /** 注册自定义原料序列化器，需在 FMLCommonSetupEvent 中调用 */
        public static void register() {
            CraftingHelper.register(Serializer.ID, Serializer.INSTANCE);
        }

        /** Serializer instance */
        public enum Serializer implements IIngredientSerializer<ExtendedMaterialValueIngredient> {
            INSTANCE;

            public static final ResourceLocation ID = TinkersGolem.getResource("material_value");

            @Override
            public ExtendedMaterialValueIngredient parse(JsonObject json) {
                MaterialValueIngredient base = MaterialValueIngredient.Serializer.INSTANCE.parse(json);
                return new ExtendedMaterialValueIngredient(base.getMaterial(), base.getMinValue(), base.getMaxValue());
            }

            @Override
            public ExtendedMaterialValueIngredient parse(FriendlyByteBuf buffer) {
                MaterialValueIngredient base = MaterialValueIngredient.Serializer.INSTANCE.parse(buffer);
                return new ExtendedMaterialValueIngredient(base.getMaterial(), base.getMinValue(), base.getMaxValue());
            }

            @Override
            public void write(FriendlyByteBuf buffer, ExtendedMaterialValueIngredient ingredient) {
                MaterialValueIngredient.Serializer.INSTANCE.write(buffer, ingredient);
            }
        }
    }

    // ==================== 序列化 ====================
    @Override
    public RecipeSerializer<?> getSerializer() {
        return Serializer.INSTANCE;
    }

    public static class Serializer implements RecipeSerializer<ExtendedShapedMaterialsRecipe> {
        public static final Serializer INSTANCE = new Serializer();

        @Override
        public ExtendedShapedMaterialsRecipe fromJson(ResourceLocation id, JsonObject json) {
            ShapedRecipe recipe = net.minecraft.world.item.crafting.RecipeSerializer.SHAPED_RECIPE.fromJson(id, json);
            return new ExtendedShapedMaterialsRecipe(recipe.getId(), recipe.getGroup(), recipe.category(),
                    recipe.getWidth(), recipe.getHeight(), recipe.getIngredients(),
                    recipe.getResultItem(RegistryAccess.EMPTY), recipe.showNotification());
        }

        @Override
        public ExtendedShapedMaterialsRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buffer) {
            ShapedRecipe recipe = net.minecraft.world.item.crafting.RecipeSerializer.SHAPED_RECIPE.fromNetwork(id,
                    buffer);
            return new ExtendedShapedMaterialsRecipe(recipe.getId(), recipe.getGroup(), recipe.category(),
                    recipe.getWidth(), recipe.getHeight(), recipe.getIngredients(),
                    recipe.getResultItem(RegistryAccess.EMPTY), recipe.showNotification());
        }

        @Override
        public void toNetwork(FriendlyByteBuf buffer, ExtendedShapedMaterialsRecipe recipe) {
            net.minecraft.world.item.crafting.RecipeSerializer.SHAPED_RECIPE.toNetwork(buffer, recipe);
        }
    }

    // 原回溯方法保持不变，但建议改为 private static（如需供 Shaped 使用，保持 public static）
    public static boolean backtrackMatch(int idx, List<Ingredient> ingredients, List<Integer> slots,
            boolean[] slotUsed, Map<Integer, Ingredient> slotToIngredient,
            CraftingContainer container) {
        if (idx == ingredients.size())
            return true;
        Ingredient ingredient = ingredients.get(idx);
        for (int slot : slots) {
            if (!slotUsed[slot] && ingredient.test(container.getItem(slot))) {
                slotUsed[slot] = true;
                slotToIngredient.put(slot, ingredient);
                if (backtrackMatch(idx + 1, ingredients, slots, slotUsed, slotToIngredient, container)) {
                    return true;
                }
                slotUsed[slot] = false;
                slotToIngredient.remove(slot);
            }
        }
        return false;
    }

    public static class ConsumerBuilder {
        public static Consumer<FinishedRecipe> createConsumer(Consumer<FinishedRecipe> nested) {
            return recipe -> nested.accept(new FinishedRecipe() {
                @Override
                public void serializeRecipeData(JsonObject p_125967_) {
                    recipe.serializeRecipeData(p_125967_);
                }

                @Override
                public ResourceLocation getId() {
                    return recipe.getId();
                }

                @Override
                public RecipeSerializer<?> getType() {
                    return Serializer.INSTANCE;
                }

                @Override
                @Nullable
                public JsonObject serializeAdvancement() {
                    return recipe.serializeAdvancement();
                }

                @Override
                @Nullable
                public ResourceLocation getAdvancementId() {
                    return recipe.getAdvancementId();
                }
            });
        }
    }
}