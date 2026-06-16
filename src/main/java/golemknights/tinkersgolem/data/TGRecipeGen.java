package golemknights.tinkersgolem.data;

import com.tterrag.registrate.providers.RegistrateRecipeProvider;
import com.tterrag.registrate.util.DataIngredient;
import dev.xkmc.modulargolems.content.recipe.GolemAssembleBuilder;
import dev.xkmc.modulargolems.content.recipe.GolemReplaceBuilder;
import golemknights.tinkersgolem.TinkersGolem;
import golemknights.tinkersgolem.register.TGEntities;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.world.item.Item;

import java.util.function.BiFunction;

public class TGRecipeGen {
	public static void genRecipe(RegistrateRecipeProvider pvd) {

		unlock(pvd, new GolemAssembleBuilder(TGEntities.HOLDER_SLIME.get(), 1)::unlockedBy,
				TGEntities.SLIME_CORE.get())
				.pattern("A").pattern("B")
				.define('A', TGEntities.SLIME_CORE.get())
				.define('B', TGEntities.SLIME_SHELL.get())
				.save(pvd, TinkersGolem.getResource("slime_golem/assemble_holder"));

		unlock(pvd, new GolemReplaceBuilder(TGEntities.HOLDER_SLIME.get(), 1)::unlockedBy,
				TGEntities.SLIME_CORE.get())
				.pattern("P").pattern("H")
				.define('P', TGEntities.SLIME_CORE.get())
				.define('H', TGEntities.HOLDER_SLIME.get())
				.save(pvd, TinkersGolem.getResource("slime_golem/replace_core"));

		unlock(pvd, new GolemReplaceBuilder(TGEntities.HOLDER_SLIME.get(), 1)::unlockedBy,
				TGEntities.SLIME_SHELL.get())
				.pattern("H").pattern("P")
				.define('P', TGEntities.SLIME_SHELL.get())
				.define('H', TGEntities.HOLDER_SLIME.get())
				.save(pvd, TinkersGolem.getResource("slime_golem/replace_shell"));
	}

	public static <T> T unlock(RegistrateRecipeProvider pvd, BiFunction<String, InventoryChangeTrigger.TriggerInstance, T> func, Item item) {
		return func.apply("has_" + pvd.safeName(item), DataIngredient.items(item).getCritereon(pvd));
	}

}
