package golemknights.tinkersgolem.data;

import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraftforge.common.crafting.conditions.IConditionBuilder;
import slimeknights.mantle.recipe.data.IRecipeHelper;
import golemknights.tinkersgolem.TinkersGolem;

import java.util.function.Consumer;

/**
 * Shared logic for each module's recipe provider
 */
public abstract class BaseRecipeProvider extends RecipeProvider implements IConditionBuilder, IRecipeHelper {
  public BaseRecipeProvider(PackOutput generator) {
    super(generator);
  }

  @Override
  protected abstract void buildRecipes(Consumer<FinishedRecipe> consumer);

  @Override
  public abstract String getName();

  @Override
  public String getModId() {
    return TinkersGolem.MODID;
  }
}
