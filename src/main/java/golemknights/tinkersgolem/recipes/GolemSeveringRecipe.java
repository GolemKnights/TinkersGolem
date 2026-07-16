package golemknights.tinkersgolem.recipes;

import dev.xkmc.modulargolems.content.config.GolemMaterial;
import dev.xkmc.modulargolems.content.entity.common.AbstractGolemEntity;
import dev.xkmc.modulargolems.content.item.golem.GolemPart;
import dev.xkmc.modulargolems.init.registrate.GolemTypes;
import golemknights.tinkersgolem.register.TGRecipes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import slimeknights.mantle.recipe.helper.ItemOutput;
import slimeknights.mantle.recipe.ingredient.EntityIngredient;
import slimeknights.tconstruct.library.recipe.modifiers.severing.SeveringRecipe;

import java.util.ArrayList;
import java.util.Set;

public class GolemSeveringRecipe extends SeveringRecipe {
    public GolemSeveringRecipe(ResourceLocation id) {
        super(id, EntityIngredient.of(
                Set.of(
                        GolemTypes.ENTITY_GOLEM.get(),
                        GolemTypes.ENTITY_HUMANOID.get(),
                        GolemTypes.ENTITY_DOG.get()
                )
        ), ItemOutput.fromStack(ItemStack.EMPTY));
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return TGRecipes.GOLEM_SEVERING_SERIALIZER.get();
    }

    @Override
    public ItemStack getOutput(Entity entity) {
        if (entity instanceof AbstractGolemEntity<?, ?> golem){
            RandomSource random = golem.getRandom();
            GolemMaterial mat;
            ArrayList<GolemMaterial> mats = golem.getMaterials();
            mat = mats.get(random.nextInt(mats.size()));
            golem.spawnAtLocation(GolemPart.setMaterial(mat.part().getDefaultInstance(), mat.id()));
        }
        return ItemStack.EMPTY;
    }
}
