package golemknights.tinkersgolem.data;

import com.tterrag.registrate.providers.RegistrateRecipeProvider;
import com.tterrag.registrate.util.DataIngredient;
import com.tterrag.registrate.util.entry.ItemEntry;
import dev.xkmc.l2library.serial.recipe.ConditionalRecipeWrapper;
import dev.xkmc.modulargolems.content.core.IGolemPart;
import dev.xkmc.modulargolems.content.entity.common.AbstractGolemEntity;
import dev.xkmc.modulargolems.content.item.golem.GolemHolder;
import dev.xkmc.modulargolems.content.item.golem.GolemPart;
import dev.xkmc.modulargolems.content.recipe.GolemAssembleBuilder;
import dev.xkmc.modulargolems.content.recipe.GolemReplaceBuilder;
import dev.xkmc.modulargolems.content.recipe.GolemSmithBuilder;
import dev.xkmc.modulargolems.init.ModularGolems;
import dev.xkmc.modulargolems.init.data.RecipeGen;
import dev.xkmc.modulargolems.init.registrate.GolemItems;
import golemknights.tinkersgolem.TinkersGolem;
import golemknights.tinkersgolem.recipes.OverslimeRecoverBuilder;
import golemknights.tinkersgolem.register.TGEntities;
import golemknights.tinkersgolem.register.TGGolemModifiers;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.Tags;
import net.minecraftforge.registries.ForgeRegistries;
import slimeknights.mantle.recipe.helper.ItemOutput;
import slimeknights.mantle.registration.object.FlowingFluidObject;
import slimeknights.tconstruct.TConstruct;
import slimeknights.tconstruct.common.TinkerTags;
import slimeknights.tconstruct.fluids.TinkerFluids;
import slimeknights.tconstruct.library.recipe.casting.ItemCastingRecipeBuilder;
import slimeknights.tconstruct.shared.TinkerCommons;
import slimeknights.tconstruct.shared.TinkerMaterials;
import slimeknights.tconstruct.shared.block.SlimeType;
import slimeknights.tconstruct.tools.TinkerModifiers;
import slimeknights.tconstruct.world.TinkerWorld;

import java.util.function.BiFunction;
import java.util.function.Consumer;

public class TGRecipeGen {
	public static void genRecipe(RegistrateRecipeProvider pvd) {

		// overslime
		{
			new OverslimeRecoverBuilder(Ingredient.of(Items.SLIME_BALL), 10).save(pvd, TinkersGolem.getResource("slime"));
			new OverslimeRecoverBuilder(Ingredient.of(TinkerCommons.slimeball.get(SlimeType.SKY)), 25).save(pvd, TinkersGolem.getResource("sky_slime"));
			new OverslimeRecoverBuilder(Ingredient.of(TinkerCommons.slimeball.get(SlimeType.ICHOR)), 50).save(pvd, TinkersGolem.getResource("ichor"));
			new OverslimeRecoverBuilder(Ingredient.of(TinkerCommons.slimeball.get(SlimeType.ENDER)), 40).save(pvd, TinkersGolem.getResource("ender_slime"));
		}

		// slime golem
		{

			unlock(pvd, new GolemAssembleBuilder(TGEntities.HOLDER_SLIME.get(), 1)::unlockedBy,
					TGEntities.SLIME_CORE.get())
					.pattern("A").pattern("B")
					.define('A', TGEntities.SLIME_CORE.get())
					.define('B', TGEntities.SLIME_SHELL.get())
					.save(pvd, TinkersGolem.getResource("slime_golem/assemble_holder"));

			unlock(pvd, new GolemAssembleBuilder(TGEntities.HOLDER_SLIME.get(), 1)::unlockedBy,
					TGEntities.SLIME_CORE.get())
					.pattern("A").pattern("B")
					.define('A', TGEntities.SLIME_SHELL.get())
					.define('B', TGEntities.SLIME_CORE.get())
					.save(pvd, TinkersGolem.getResource("slime_golem/assemble_holder_inverted"));

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

			unlock(pvd, ShapedRecipeBuilder.shaped(RecipeCategory.MISC, TGEntities.SLIME_CORE.get(), 1)::unlockedBy,
					Items.SLIME_BALL)
					.pattern(" S ").pattern("SCS").pattern(" S ")
					.define('C', Items.CLAY_BALL)
					.define('S', Tags.Items.SLIMEBALLS)
					.save(pvd);

			unlock(pvd, ShapedRecipeBuilder.shaped(RecipeCategory.MISC, TGEntities.SLIME_SHELL.get(), 1)::unlockedBy,
					Items.SLIME_BALL)
					.pattern("S S").pattern(" C ").pattern("S S")
					.define('C', Items.CLAY_BALL)
					.define('S', Tags.Items.SLIMEBALLS)
					.save(pvd);

			RecipeGen.expand(pvd, TGEntities.HOLDER_SLIME, GolemItems.ADD_DIAMOND);
			RecipeGen.expand(pvd, TGEntities.HOLDER_SLIME, GolemItems.ADD_NETHERITE);
			RecipeGen.expand(pvd, TGEntities.HOLDER_SLIME, TGGolemModifiers.ITEM_SUPERCRITICAL);

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

		// upgrades
		{
			unlock(pvd, ShapedRecipeBuilder.shaped(RecipeCategory.MISC, TGGolemModifiers.ITEM_OVERGROWTH.get(), 1)::unlockedBy, GolemItems.EMPTY_UPGRADE.get())
					.pattern(" A ").pattern("AEA").pattern(" A ")
					.define('E', GolemItems.EMPTY_UPGRADE.get())
					.define('A', TinkerTags.Items.SLIMY_LOGS)
					.save(pvd);

			unlock(pvd, ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, TGGolemModifiers.ITEM_SUPER_OVERGROWTH.get(), 1)::unlockedBy, TGGolemModifiers.ITEM_OVERGROWTH.get())
					.requires(TGGolemModifiers.ITEM_OVERGROWTH.get())
					.requires(TinkerWorld.greenheart.get())
					.requires(TinkerWorld.skyroot.get())
					.requires(TinkerWorld.bloodshroom.get())
					.requires(TinkerWorld.enderbark.get())
					.save(pvd);

			unlock(pvd, ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, TGGolemModifiers.ITEM_OVERWORKED.get(), 1)::unlockedBy, GolemItems.EMPTY_UPGRADE.get())
					.requires(GolemItems.EMPTY_UPGRADE.get())
					.requires(Items.GOLD_INGOT, 4)
					.requires(TinkerWorld.greenheart.get())
					.requires(TinkerWorld.skyroot.get())
					.requires(TinkerWorld.bloodshroom.get())
					.requires(TinkerWorld.enderbark.get())
					.save(pvd);

			unlock(pvd, ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, TGGolemModifiers.ITEM_SUPERCRITICAL.get(), 1)::unlockedBy, GolemItems.EMPTY_UPGRADE.get())
					.requires(GolemItems.EMPTY_UPGRADE.get())
					.requires(TinkerWorld.greenheart.get())
					.requires(TinkerWorld.skyroot.get())
					.requires(TinkerWorld.bloodshroom.get())
					.requires(TinkerWorld.enderbark.get())
					.requires(TinkerWorld.earthGeode.get())
					.requires(TinkerWorld.skyGeode.get())
					.requires(TinkerWorld.ichorGeode.get())
					.requires(TinkerWorld.enderGeode.get())
					.save(pvd);

			unlock(pvd, ShapedRecipeBuilder.shaped(RecipeCategory.MISC, TGGolemModifiers.ITEM_OVERLORD.get(), 1)::unlockedBy, GolemItems.EMPTY_UPGRADE.get())
					.pattern("BAB").pattern("AEA").pattern("BAB")
					.define('E', GolemItems.EMPTY_UPGRADE.get())
					.define('A', TinkerMaterials.queensSlime.getIngotTag())
					.define('B', TinkerTags.Items.KNIGHTMETAL_SHARD)
					.save(pvd);

			unlock(pvd, ShapedRecipeBuilder.shaped(RecipeCategory.MISC, TGGolemModifiers.ITEM_OVERBURN.get(), 1)::unlockedBy, GolemItems.EMPTY_UPGRADE.get())
					.pattern("BAB").pattern("AEA").pattern("BAB")
					.define('E', GolemItems.EMPTY_UPGRADE.get())
					.define('A', TinkerMaterials.cinderslime.getIngotTag())
					.define('B', TinkerWorld.cobaltShard.get())
					.save(pvd);

			unlock(pvd, ShapedRecipeBuilder.shaped(RecipeCategory.MISC, TGGolemModifiers.ITEM_OVERFORCED.get(), 1)::unlockedBy, GolemItems.EMPTY_UPGRADE.get())
					.pattern("AAA").pattern("AEA").pattern("AAA")
					.define('E', GolemItems.EMPTY_UPGRADE.get())
					.define('A', TinkerModifiers.slimesteelReinforcement.get())
					.save(pvd);

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
		var rl = TinkersGolem.getResource("casting/" + id.getPath() + "_casting_" + item_name);

		ItemStack result = GolemPart.setMaterial(part.getDefaultInstance(), id);
		var modid = id.getNamespace();
		Consumer<FinishedRecipe> out = modid.equals(TConstruct.MOD_ID) || modid.equals(ModularGolems.MODID) ||
				modid.equals(TinkersGolem.MODID) ? pvd : ConditionalRecipeWrapper.mod(pvd, modid);
		TGRecipeGen.unlock(pvd, ItemCastingRecipeBuilder.basinRecipe(ItemOutput.fromStack(result))::unlockedBy, part)
				.setCast(part, true).setFluidAndTime(fluid, ingot * part.count)
				.save(out, rl);
	}

	public static <T extends AbstractGolemEntity<T, P>, P extends IGolemPart<P>> void expand(RegistrateRecipeProvider pvd, ItemEntry<GolemHolder<T, P>> holder, ItemEntry<?> template) {
		var id = TinkersGolem.getResource(template.getId().getPath() + "_" + holder.getId().getPath());
		unlock(pvd, new GolemSmithBuilder(holder.get(), template)::unlocks, template.get()).save(pvd, id);
	}

	public static <T> T unlock(RegistrateRecipeProvider pvd, BiFunction<String, InventoryChangeTrigger.TriggerInstance, T> func, Item item) {
		return func.apply("has_" + pvd.safeName(item), DataIngredient.items(item).getCritereon(pvd));
	}

}
