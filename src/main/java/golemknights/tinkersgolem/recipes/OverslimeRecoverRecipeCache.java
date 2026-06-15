package golemknights.tinkersgolem.recipes;

import golemknights.tinkersgolem.register.TGRecipes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeManager;
import slimeknights.mantle.recipe.helper.RecipeHelper;
import slimeknights.tconstruct.common.recipe.RecipeCacheInvalidator;

import java.util.*;

public class OverslimeRecoverRecipeCache {
    private static final Map<Item, List<OverslimeRecoverRecipe>> CACHE = new HashMap<>();

    public static List<OverslimeRecoverRecipe> findRecipe(RecipeManager manager, ItemStack type) {
        if (!type.hasTag() && CACHE.containsKey(type.getItem())) {
            return CACHE.get(type.getItem());
        } else {
            List<OverslimeRecoverRecipe> list = new ArrayList<>();
            for (OverslimeRecoverRecipe recipe : RecipeHelper.getRecipes(manager,
                    TGRecipes.OVERSLIME_RECOVER.get(), OverslimeRecoverRecipe.class)) {
                if (recipe.matches(type)) {
                    list.add(recipe);
                }
            }
            if (list.isEmpty()) {
                list = Collections.emptyList();
            }
            list = Collections.unmodifiableList(list);
            if (!type.hasTag()) {
                CACHE.put(type.getItem(), list);
            }
            return list;
        }
    }

    static {
        RecipeCacheInvalidator.addReloadListener((client) -> {
            CACHE.clear();
        });
    }
}
