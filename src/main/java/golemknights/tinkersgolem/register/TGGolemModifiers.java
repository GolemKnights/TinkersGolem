package golemknights.tinkersgolem.register;

import com.tterrag.registrate.builders.ItemBuilder;
import com.tterrag.registrate.providers.ProviderType;
import com.tterrag.registrate.util.entry.ItemEntry;
import com.tterrag.registrate.util.entry.RegistryEntry;
import com.tterrag.registrate.util.nullness.NonNullSupplier;
import dev.xkmc.l2library.base.L2Registrate;
import dev.xkmc.l2library.base.NamedEntry;
import dev.xkmc.modulargolems.content.core.StatFilterType;
import dev.xkmc.modulargolems.content.item.upgrade.AddSlotTemplate;
import dev.xkmc.modulargolems.content.item.upgrade.SimpleUpgradeItem;
import dev.xkmc.modulargolems.content.modifier.base.AttributeGolemModifier;
import dev.xkmc.modulargolems.content.modifier.base.GolemModifier;
import dev.xkmc.modulargolems.content.modifier.base.PotionAttackModifier;
import dev.xkmc.modulargolems.init.ModularGolems;
import dev.xkmc.modulargolems.init.data.MGTagGen;
import dev.xkmc.modulargolems.init.registrate.GolemItems;
import dev.xkmc.modulargolems.init.registrate.GolemTypes;
import golemknights.tinkersgolem.TinkersGolem;
import golemknights.tinkersgolem.data.TGConfig;
import golemknights.tinkersgolem.data.TGTagGen;
import golemknights.tinkersgolem.item.misc.SpecialUpgradeItem;
import golemknights.tinkersgolem.modifiers.golem.*;
import golemknights.tinkersgolem.modifiers.slime.*;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraftforge.client.model.generators.ModelFile;
import org.apache.commons.lang3.mutable.Mutable;
import org.apache.commons.lang3.mutable.MutableObject;
import slimeknights.tconstruct.shared.TinkerEffects;

import javax.annotation.Nullable;
import java.util.function.Supplier;

public class TGGolemModifiers {

	public static final RegistryEntry<OvergrowthModifier> OVERGROWTH;
	public static final RegistryEntry<AttributeGolemModifier> OVERFORCED, OVERWORKED, OVERFILL;
	public static final RegistryEntry<PotionAttackModifier> ENDERFERENCE;
	//public static final RegistryEntry<PotionDefenseModifier> space;
	public static final RegistryEntry<OverlordModifier> OVERLORD;
	public static final RegistryEntry<OverburnModifier> OVERBURN;
	public static final RegistryEntry<OversmeltModifier> OVERSMELT;
	public static final RegistryEntry<OvershockModifier> OVERSHOCK;
	public static final RegistryEntry<OverbonkingModifier> OVERBOKING;
	public static final RegistryEntry<OverteleportModifier> OVERTELEPORT;
	public static final RegistryEntry<OverdriveModifier> OVERDRIVE;
	public static final RegistryEntry<SpillingModifier> SPILLING;
	public static final RegistryEntry<WettingModifier> WETTING;
	public static final RegistryEntry<CrazyMetalModifier> CRAZY_METAL;
	public static final RegistryEntry<CountermeasuresModifier> COUNTERMEASURES;
	public static final RegistryEntry<SymbionicModifier> SYMBIONIC;
	public static final RegistryEntry<LightlyAttackModifier> LIGHTLY_ATTACK;
	public static final RegistryEntry<DuritaeModifier> DURITAE;
	public static final RegistryEntry<RepulsiveModifier> REPULSIVE;
	public static final RegistryEntry<FieryExplosionModifier> FIERY_EXPLOSION;
	public static final RegistryEntry<SwallowModifier> SWALLOW;

	public static final RegistryEntry<SlimeSlotModifier> OVERTALENTED;

	public static final ItemEntry<SimpleUpgradeItem> ITEM_OVERGROWTH, ITEM_SUPER_OVERGROWTH,
			ITEM_OVERWORKED, ITEM_OVERFORCED, ITEM_OVERFILL, ITEM_OVERLORD, ITEM_OVERBURN,
			ITEM_SPILLING, ITEM_WETTING, ITEM_ENDERFERENCE;
	public static final ItemEntry<SpecialUpgradeItem> ITEM_OVERDRIVE;
	public static final ItemEntry<AddSlotTemplate> QUEENS_SLIME_EXPAND, CINDERSLIME_EXPAND, SLIMESTEEL_EXPAND;

	static {
		OVERWORKED = reg("overworked", () -> new AttributeGolemModifier(4,
				new AttributeGolemModifier.AttrEntry(TGAttributes.STAT_OVERSLIME_RECOVERY, TGConfig.COMMON.overworkedFactor::get)), null);
		OVERFORCED = reg("overforced", () -> new AttributeGolemModifier(5,
				new AttributeGolemModifier.AttrEntry(TGAttributes.STAT_OVERSLIME, TGConfig.COMMON.overforcedAmount::get)), null);
		OVERFILL = reg("overfill", () -> new AttributeGolemModifier(5,
				new AttributeGolemModifier.AttrEntry(TGAttributes.STAT_TANK_CAPACITY, TGConfig.COMMON.overfillFactor::get)), null);

		ENDERFERENCE = reg("enderference", () -> new PotionAttackModifier(StatFilterType.MASS, 3,
				(i) -> new MobEffectInstance(TinkerEffects.enderference.get(), 100 * i, 0)
		), "Potion Upgrade: Enderference", null);

		OVERGROWTH = reg("overgrowth", OvergrowthModifier::new, "Recover %s overslime per second");
		OVERLORD = reg("overlord", OverlordModifier::new, "When taking damage, recover overslime by %s%% of damage taken");
		OVERBURN = reg("overburn", OverburnModifier::new, "Consumes fluid fuel to recover overslime over time; only effective for slime golems");
		OVERSMELT = reg("oversmelt", OversmeltModifier::new, "Recover %s overslime per second on warm blocks");
		OVERSHOCK = reg("overshock", OvershockModifier::new, "Upon landing, deal %s%% of attack damage to surrounding targets");
		OVERBOKING = reg("overboking", OverbonkingModifier::new, "Has %s%% chance to push away the target when dealing damage");
		OVERTELEPORT = reg("overteleport", OverteleportModifier::new, "Teleport target when dealing damage, or teleport itself when taking damage");
		OVERDRIVE = reg("overdrive", OverdriveModifier::new, "When overslime is above max health: consume overslime to heal or to grow larger; only effective for slime golems");
		OVERTALENTED = reg("overtalented", SlimeSlotModifier::new, "Allows %s more slime upgrades");
		SPILLING = reg("spilling", SpillingModifier::new, "Slime golem spills fluid on target while attacking");
		WETTING = reg("wetting", WettingModifier::new, "Slime golem is wetted by fluids when attacked");
		CRAZY_METAL = reg("crazy_metal", CrazyMetalModifier::new, "Upon attacking, has a 10% chance per level to recover 5*damage health");
		COUNTERMEASURES = reg("countermeasures", CountermeasuresModifier::new, "When taking damage bypass armor, reduce it 10% per level");
		SYMBIONIC = reg("symbionic", SymbionicModifier::new, "When it is healed, heal its owner 20% amount per level");
		LIGHTLY_ATTACK = reg("lightly_attack", LightlyAttackModifier::new, "Upon attacking, improve movement speed");
		DURITAE = reg("duritae", DuritaeModifier::new, "");
		REPULSIVE = reg("repulsive", RepulsiveModifier::new, "");
		FIERY_EXPLOSION = reg("fiery_explosion", FieryExplosionModifier::new, "");
		SWALLOW = reg("swallow", SwallowModifier::new, "");

		ITEM_OVERGROWTH = regUpgradeImpl("overgrowth", () -> OVERGROWTH, 1, false, TinkersGolem.MODID).tag(TGTagGen.SLIME_UPGRADES).register();
		ITEM_SUPER_OVERGROWTH = regUpgradeImpl("overgrown", () -> OVERGROWTH, 4, false, TinkersGolem.MODID).tag(TGTagGen.SLIME_UPGRADES).register();
		ITEM_OVERWORKED = regUpgradeImpl("overworked", () -> OVERWORKED, 1, false, TinkersGolem.MODID).tag(TGTagGen.SLIME_UPGRADES).register();
		ITEM_OVERFORCED = regUpgradeImpl("overforced", () -> OVERFORCED, 1, false, TinkersGolem.MODID).tag(TGTagGen.SLIME_UPGRADES).register();
		ITEM_OVERFILL = regUpgradeImpl("overfill", () -> OVERFILL, 1, false, TinkersGolem.MODID).tag(TGTagGen.SLIME_UPGRADES).register();
		ITEM_OVERLORD = regUpgradeImpl("overlord", () -> OVERLORD, 1, false, TinkersGolem.MODID).tag(TGTagGen.SLIME_UPGRADES).register();
		ITEM_OVERBURN = regUpgradeImpl("overburn", () -> OVERBURN, 1, false, TinkersGolem.MODID).tag(TGTagGen.SLIME_UPGRADES).register();
		ITEM_SPILLING = regUpgradeImpl("spilling", () -> SPILLING, 1, false, TinkersGolem.MODID).tag(TGTagGen.SLIME_UPGRADES).register();
		ITEM_WETTING = regUpgradeImpl("wetting", () -> WETTING, 1, false, TinkersGolem.MODID).tag(TGTagGen.SLIME_UPGRADES).register();
		ITEM_ENDERFERENCE = regUpgradeImpl("enderference", () -> ENDERFERENCE, 1, false, TinkersGolem.MODID).tag(MGTagGen.POTION_UPGRADES).register();
		ITEM_OVERDRIVE = specialUpgrade("overdrive", () -> OVERDRIVE, TinkersGolem.MODID).tag(TGTagGen.SLIME_UPGRADES).register();

		SLIMESTEEL_EXPAND = addSlot("slimesteel_expand", () -> OVERTALENTED, TinkersGolem.MODID).lang("Slime Golem Expansion Template: Slimesteel").register();
		QUEENS_SLIME_EXPAND = addSlot("queens_slime_expand", () -> OVERTALENTED, TinkersGolem.MODID).lang("Slime Golem Expansion Template: Queen's Slime").register();
		CINDERSLIME_EXPAND = addSlot("cinderslime_expand", () -> OVERTALENTED, TinkersGolem.MODID).lang("Slime Golem Expansion Template: Cinderslime").register();

	}

	@SuppressWarnings("removal")
	public static <T extends GolemModifier> RegistryEntry<T> reg(String id, NonNullSupplier<T> sup, @Nullable String def) {
		var cls = new L2Registrate.RegistryInstance<GolemModifier>(() -> null, ResourceKey.createRegistryKey(new ResourceLocation(ModularGolems.MODID, "modifier")));
		Mutable<RegistryEntry<T>> holder = new MutableObject<>();
		L2Registrate.GenericBuilder<GolemModifier, T> ans = TinkersGolem.REGISTRATE.generic(cls, id, sup).defaultLang();
		if (def != null) {
			ans.addMiscData(ProviderType.LANG, (pvd) -> pvd.add(holder.getValue().get().getDescriptionId() + ".desc", def));
		}
		RegistryEntry<T> result = ans.register();
		holder.setValue(result);
		return result;
	}
	public static <T extends GolemModifier> RegistryEntry<T> reg(String id, NonNullSupplier<T> sup, String name, @Nullable String def) {
		Mutable<RegistryEntry<T>> holder = new MutableObject<>();
		L2Registrate.GenericBuilder<GolemModifier, T> ans = TinkersGolem.REGISTRATE.generic(GolemTypes.MODIFIERS, id, sup).defaultLang();
		ans.lang(NamedEntry::getDescriptionId, name);
		if (def != null) {
			ans.addMiscData(ProviderType.LANG, (pvd) -> pvd.add(((GolemModifier)((RegistryEntry<?>)holder.getValue()).get()).getDescriptionId() + ".desc", def));
		}
		RegistryEntry<T> result = ans.register();
		holder.setValue(result);
		return result;
	}

	@SuppressWarnings("removal")
	public static ItemBuilder<SimpleUpgradeItem, L2Registrate> regUpgradeImpl(String id, Supplier<RegistryEntry<? extends GolemModifier>> mod, int level, boolean foil, String modid) {
		return TinkersGolem.REGISTRATE.item(id, (p) -> new SimpleUpgradeItem(p, mod.get()::get, level, foil))
				.model((ctx, pvd) -> pvd.generated(ctx, new ResourceLocation(modid, "item/upgrades/" + id))
						.override().predicate(new ResourceLocation(ModularGolems.MODID, "blue_arrow"), 0.5f)
						.model(pvd.getBuilder(pvd.name(ctx) + "_purple")
								.parent(new ModelFile.UncheckedModelFile("item/generated"))
								.texture("layer0", new ResourceLocation(modid, "item/upgrades/" + id))
								.texture("layer1", new ResourceLocation(ModularGolems.MODID, "item/purple_arrow")))
						.end().override().predicate(new ResourceLocation(ModularGolems.MODID, "blue_arrow"), 1)
						.model(pvd.getBuilder(pvd.name(ctx) + "_blue")
								.parent(new ModelFile.UncheckedModelFile("item/generated"))
								.texture("layer0", new ResourceLocation(modid, "item/upgrades/" + id))
								.texture("layer1", new ResourceLocation(ModularGolems.MODID, "item/blue_arrow")))
						.end())
				.tab(GolemItems.UPGRADES.getKey());
	}

	@SuppressWarnings("removal")
	public static ItemBuilder<SpecialUpgradeItem, L2Registrate> specialUpgrade(String id, Supplier<RegistryEntry<? extends GolemModifier>> mod, String modid) {
		return TinkersGolem.REGISTRATE.item(id, (p) -> new SpecialUpgradeItem(p, mod.get()::get))
				.model((ctx, pvd) -> pvd.generated(ctx, new ResourceLocation(modid, "item/upgrades/" + id)))
				.tab(GolemItems.UPGRADES.getKey());
	}

	@SuppressWarnings("removal")
	public static ItemBuilder<AddSlotTemplate, L2Registrate> addSlot(String id, Supplier<RegistryEntry<? extends GolemModifier>> mod, String modid) {
		return TinkersGolem.REGISTRATE.item(id, (p) -> new AddSlotTemplate(p, mod.get()))
				.model((ctx, pvd) -> pvd.generated(ctx, new ResourceLocation(modid, "item/upgrades/" + id)))
				.tab(GolemItems.UPGRADES.getKey());
	}

	public static void load() {

	}

}
