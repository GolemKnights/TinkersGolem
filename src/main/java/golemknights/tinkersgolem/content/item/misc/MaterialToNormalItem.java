package golemknights.tinkersgolem.content.item.misc;

import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import slimeknights.tconstruct.common.recipe.RecipeCacheInvalidator;
import slimeknights.tconstruct.library.materials.definition.MaterialId;
import slimeknights.tconstruct.library.materials.definition.MaterialVariantId;
import slimeknights.tconstruct.library.materials.stats.MaterialStatsId;
import slimeknights.tconstruct.library.recipe.material.MaterialRecipeCache;
import slimeknights.tconstruct.library.tools.part.IMaterialItem;

public class MaterialToNormalItem extends Item implements IMaterialItem {

    private static final Properties none = new Item.Properties().stacksTo(0);
    public final MaterialStatsId statType;
    public final Predicate<ItemStack> isAllowedOutput;
    public final int count;
    private final Map<MaterialVariantId, ItemStack> cacheMap = new ConcurrentHashMap<>();

    public MaterialToNormalItem(MaterialStatsId statType) {
        this(statType, i -> true, -1);
    }

    public MaterialToNormalItem(MaterialStatsId statType, int count) {
        this(statType, i -> true, count);
    }

    public MaterialToNormalItem(MaterialStatsId statType, float amount) {
        this(statType, amount, amount, -1);
    }

    public MaterialToNormalItem(MaterialStatsId statType, float amount, int count) {
        this(statType, amount, amount, count);
    }

    public MaterialToNormalItem(MaterialStatsId statType, float minAmount, float maxAmount,
            int count) {
        this(statType, i -> {
            return MaterialRecipeCache.getAllRecipes().stream().filter(r -> r.getIngredient().test(i)).anyMatch(r -> {
                var value = r.getValue();
                return value >= minAmount && value <= maxAmount;
            });
        }, count);
    }

    public MaterialToNormalItem(MaterialStatsId statType, Predicate<ItemStack> isAllowedOutput,
            int count) {
        super(none);
        this.statType = statType;
        this.isAllowedOutput = isAllowedOutput;
        this.count = count;
        RecipeCacheInvalidator.addReloadListener(client -> cacheMap.clear());
    }

    @Override
    public MaterialVariantId getMaterial(ItemStack stack) {
        return MaterialId.UNKNOWN;
    }

    @Override
    public ItemStack setMaterialForced(ItemStack stack, MaterialVariantId material) {
        return cacheMap.computeIfAbsent(material,
                m -> MaterialRecipeCache.getRecipes(m).stream().map(r -> r.getIngredient().getItems())
                        .<ItemStack>flatMap(Arrays::stream).filter(isAllowedOutput).map(i -> {
                            i = i.copy();
                            if (count != -1)
                                i.setCount(count);
                            return i;
                        }).findAny().orElse(ItemStack.EMPTY))
                .copy();
    }

}
