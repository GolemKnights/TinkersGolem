package golemknights.tinkersgolem.recipes;

import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;
import slimeknights.mantle.recipe.data.AbstractRecipeBuilder;

import java.util.function.Consumer;

public class OverslimeRecoverBuilder extends AbstractRecipeBuilder<OverslimeRecoverBuilder> {

	private final Ingredient item;
	private final int amount;

	public OverslimeRecoverBuilder(Ingredient item, int amount) {
		super();
		this.item = item;
		this.amount = amount;
	}

	@Override
	public void save(Consumer<FinishedRecipe> pvd) {
		throw new IllegalArgumentException("Must provide recipe id");
	}

	@Override
	public void save(Consumer<FinishedRecipe> pvd, ResourceLocation id) {
		ResourceLocation advancementId = this.buildOptionalAdvancement(id, "overslime_recover");
		pvd.accept(new LoadableFinishedRecipe<>(
				new OverslimeRecoverRecipe(id.withPrefix("overslime_recover/"), item, amount),
				OverslimeRecoverRecipe.LOADER, advancementId));
	}

}
