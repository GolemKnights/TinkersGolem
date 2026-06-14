package golemknights.tinkersgolem.recipes;

import golemknights.tinkersgolem.register.TGRecipes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeManager;
import slimeknights.mantle.recipe.helper.RecipeHelper;
import slimeknights.tconstruct.common.recipe.RecipeCacheInvalidator;

import java.util.*;

public class OverslimeRecoverRecipeCache {
    private static final Map<ItemStack, List<OverslimeRecoverRecipe>> CACHE = new HashMap();

    public static List<OverslimeRecoverRecipe> findRecipe(RecipeManager manager, ItemStack type) {
        if (CACHE.containsKey(type)) {
            return CACHE.get(type);
        } else {
            List<OverslimeRecoverRecipe> list = new ArrayList<>();
            for (OverslimeRecoverRecipe recipe : RecipeHelper.getRecipes(manager, TGRecipes.overslime_recover_recipe_type.get(), OverslimeRecoverRecipe.class)) {
                if (recipe.matches(type)) {
                    list.add(recipe);
                }
            }
            if (list.isEmpty()) {
                list = Collections.emptyList();
            }
            CACHE.put(type, list);
            return list;
        }
    }
    static {
        RecipeCacheInvalidator.addReloadListener((client) -> {
            CACHE.clear();
        });
    }
}
