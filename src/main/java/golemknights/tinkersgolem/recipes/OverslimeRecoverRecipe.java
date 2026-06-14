package golemknights.tinkersgolem.recipes;

import golemknights.tinkersgolem.register.TGRecipes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import slimeknights.mantle.data.loadable.common.IngredientLoadable;
import slimeknights.mantle.data.loadable.field.ContextKey;
import slimeknights.mantle.data.loadable.field.LoadableField;
import slimeknights.mantle.data.loadable.primitive.FloatLoadable;
import slimeknights.mantle.data.loadable.record.RecordLoadable;
import slimeknights.mantle.recipe.ICustomOutputRecipe;
import slimeknights.mantle.recipe.container.IEmptyContainer;

public class OverslimeRecoverRecipe implements ICustomOutputRecipe<IEmptyContainer> {
    public static final RecordLoadable<OverslimeRecoverRecipe> LOADER = RecordLoadable.create(
            ContextKey.ID.requiredField(),
            IngredientLoadable.DISALLOW_EMPTY.requiredField("ingredient", (i) -> i.ingredient),
            FloatLoadable.ANY.requiredField("result", (r) -> r.output),
            OverslimeRecoverRecipe::new
    );
    private final ResourceLocation id;
    protected final Ingredient ingredient;
    protected final float output;
    public OverslimeRecoverRecipe(ResourceLocation id, Ingredient ingredient, float output) {
        this.id = id;
        this.ingredient = ingredient;
        this.output = output;
    }
    @Override
    @Deprecated
    public boolean matches(IEmptyContainer inv, Level worldIn) {
        return false;
    }
    public float getOutput() {
        return this.output;
    }
    @Override
    public ResourceLocation getId() {
        return id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return TGRecipes.OVERSLIME_RECOVER_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return TGRecipes.OVERSLIME_RECOVER.get();
    }

    public boolean matches(ItemStack type) {
        return ingredient.test(type);
    }
}
