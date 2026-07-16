package golemknights.tinkersgolem.register;

import golemknights.tinkersgolem.recipes.GolemSeveringRecipe;
import golemknights.tinkersgolem.recipes.OverslimeRecoverRecipe;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.registries.RegistryObject;
import slimeknights.mantle.recipe.helper.LoadableRecipeSerializer;
import slimeknights.mantle.recipe.helper.SimpleRecipeSerializer;

import static golemknights.tinkersgolem.TinkersGolem.*;

public class TGRecipes {
	public static final RegistryObject<RecipeType<OverslimeRecoverRecipe>> OVERSLIME_RECOVER = register("overslime_recover");
	public static final RegistryObject<RecipeSerializer<OverslimeRecoverRecipe>> OVERSLIME_RECOVER_SERIALIZER = RECIPE_SERIALIZERS.register("overslime_recover", () -> LoadableRecipeSerializer.of(OverslimeRecoverRecipe.LOADER));
	public static final RegistryObject<RecipeSerializer<GolemSeveringRecipe>> GOLEM_SEVERING_SERIALIZER = RECIPE_SERIALIZERS.register("golem_severing", () -> new SimpleRecipeSerializer<>(GolemSeveringRecipe::new));

	static <T extends Recipe<?>> RegistryObject<RecipeType<T>> register(String name) {
		return RECIPE_TYPES.register(name, () -> new RecipeType<T>() {
			public String toString() {
				return getResource(name).toString();
			}
		});
	}

	public static void load() {

	}

}
