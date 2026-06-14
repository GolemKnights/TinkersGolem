package golemknights.tinkersgolem.register;

import golemknights.tinkersgolem.recipes.OverslimeRecoverRecipe;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import slimeknights.mantle.recipe.helper.LoadableRecipeSerializer;
import slimeknights.mantle.registration.deferred.SynchronizedDeferredRegister;

import static golemknights.tinkersgolem.TinkersGolem.MODID;

public class TGRecipes {
    public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES = DeferredRegister.create(Registries.RECIPE_TYPE, MODID);
    public static final SynchronizedDeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = SynchronizedDeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, MODID);
    public static final RegistryObject<RecipeType<OverslimeRecoverRecipe>> overslime_recover_recipe_type = register("overslime_recover");
    public static final RegistryObject<RecipeSerializer<OverslimeRecoverRecipe>> overslime_recover_recipe_serializer = RECIPE_SERIALIZERS.register("overslime_recover", () -> LoadableRecipeSerializer.of(OverslimeRecoverRecipe.LOADER));
    static <T extends Recipe<?>> RegistryObject<RecipeType<T>> register(String name) {
        return RECIPE_TYPES.register(name, () -> new RecipeType<T>() {
            public String toString() {
                return MODID + ":" + name;
            }
        });
    }
}
