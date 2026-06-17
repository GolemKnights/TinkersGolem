package golemknights.tinkersgolem.data;

import com.tterrag.registrate.providers.RegistrateRecipeProvider;
import com.tterrag.registrate.util.DataIngredient;
import dev.xkmc.l2library.serial.recipe.ConditionalRecipeWrapper;
import dev.xkmc.modulargolems.content.item.golem.GolemPart;
import dev.xkmc.modulargolems.content.recipe.GolemAssembleBuilder;
import dev.xkmc.modulargolems.content.recipe.GolemReplaceBuilder;
import dev.xkmc.modulargolems.init.ModularGolems;
import golemknights.tinkersgolem.TinkersGolem;
import golemknights.tinkersgolem.register.TGEntities;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;
import slimeknights.mantle.recipe.helper.ItemOutput;
import slimeknights.mantle.registration.object.FlowingFluidObject;
import slimeknights.tconstruct.TConstruct;
import slimeknights.tconstruct.fluids.TinkerFluids;
import slimeknights.tconstruct.library.recipe.casting.ItemCastingRecipeBuilder;

import java.util.function.BiFunction;
import java.util.function.Consumer;

public class TGRecipeGen {
	public static void genRecipe(RegistrateRecipeProvider pvd) {

		// slime golem
		{

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

		// materials
		{
			genSlime(pvd, TinkersGolem.getResource("earth_slime"), TinkerFluids.earthSlime, 250);
			genSlime(pvd, TinkersGolem.getResource("sky_slime"), TinkerFluids.skySlime, 250);
			genSlime(pvd, TinkersGolem.getResource("ichor"), TinkerFluids.ichor, 250);
			genSlime(pvd, TinkersGolem.getResource("ender_slime"), TinkerFluids.enderSlime, 250);

			genAll(pvd, TConstruct.getResource("slimesteel"), TinkerFluids.moltenSlimesteel, 90);
			genAll(pvd, TConstruct.getResource("queens_slime"), TinkerFluids.moltenQueensSlime, 90);
			genAll(pvd, TConstruct.getResource("cinderslime"), TinkerFluids.moltenCinderslime, 90);

			var core = TGEntities.SLIME_CORE.get();
			genCasting(core, pvd, TConstruct.getResource("slimesteel"), TinkerFluids.moltenSlimesteel, 90);
			genCasting(core, pvd, TConstruct.getResource("queens_slime"), TinkerFluids.moltenQueensSlime, 90);
			genCasting(core, pvd, TConstruct.getResource("cinderslime"), TinkerFluids.moltenCinderslime, 90);

			genCasting(core, pvd, TConstruct.getResource("amethyst_bronze"), TinkerFluids.moltenAmethystBronze, 90);
			genCasting(core, pvd, TConstruct.getResource("manyullyn"), TinkerFluids.moltenManyullyn, 90);
			genCasting(core, pvd, TConstruct.getResource("hepatizon"), TinkerFluids.moltenHepatizon, 90);
			genCasting(core, pvd, TConstruct.getResource("cobalt"), TinkerFluids.moltenCobalt, 90);
			genCasting(core, pvd, TConstruct.getResource("rose_gold"), TinkerFluids.moltenRoseGold, 90);

			genCasting(core, pvd, ModularGolems.loc("copper"), TinkerFluids.moltenCopper, 90);
			genCasting(core, pvd, ModularGolems.loc("iron"), TinkerFluids.moltenIron, 90);
			genCasting(core, pvd, ModularGolems.loc("gold"), TinkerFluids.moltenGold, 90);
			genCasting(core, pvd, ModularGolems.loc("netherite"), TinkerFluids.moltenNetherite, 90);

		}

	}

	private static void genAll(RegistrateRecipeProvider pvd, ResourceLocation id, FlowingFluidObject<?> fluid, int ingot) {
		for (var e : GolemPart.LIST) {
			if (e.getEntityType() == TGEntities.TYPE_SLIME.get()) continue;
			genCasting(e, pvd, id, fluid, ingot);
		}
	}

	private static void genSlime(RegistrateRecipeProvider pvd, ResourceLocation id, FlowingFluidObject<?> fluid, int ingot) {
		for (var e : GolemPart.LIST) {
			if (e.getEntityType() != TGEntities.TYPE_SLIME.get()) continue;
			genCasting(e, pvd, id, fluid, ingot);
		}
	}

	private static void genCasting(GolemPart<?, ?> part, RegistrateRecipeProvider pvd, ResourceLocation id, FlowingFluidObject<?> fluid, int ingot) {
		var part_rl = ForgeRegistries.ITEMS.getKey(part);
		assert part_rl != null;
		String item_name = part_rl.getPath();
		var rl = new ResourceLocation(TinkersGolem.MODID, "casting/" + id.getPath() + "_casting_" + item_name);

		ItemStack result = GolemPart.setMaterial(part.getDefaultInstance(), id);
		var modid = id.getNamespace();
		Consumer<FinishedRecipe> out = modid.equals(TConstruct.MOD_ID) || modid.equals(ModularGolems.MODID) ||
				modid.equals(TinkersGolem.MODID) ? pvd : ConditionalRecipeWrapper.mod(pvd, modid);
		TGRecipeGen.unlock(pvd, ItemCastingRecipeBuilder.basinRecipe(ItemOutput.fromStack(result))::unlockedBy, part)
				.setCast(part, true).setFluidAndTime(fluid, ingot * part.count)
				.save(out, rl);
	}

	public static <T> T
	unlock(RegistrateRecipeProvider pvd, BiFunction<String, InventoryChangeTrigger.TriggerInstance, T> func, Item item) {
		return func.apply("has_" + pvd.safeName(item), DataIngredient.items(item).getCritereon(pvd));
	}

}
