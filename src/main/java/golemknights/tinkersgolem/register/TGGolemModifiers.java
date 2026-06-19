package golemknights.tinkersgolem.register;

import com.tterrag.registrate.builders.ItemBuilder;
import com.tterrag.registrate.providers.ProviderType;
import com.tterrag.registrate.util.entry.RegistryEntry;
import com.tterrag.registrate.util.nullness.NonNullSupplier;
import dev.xkmc.l2library.base.L2Registrate;
import dev.xkmc.modulargolems.content.item.upgrade.SimpleUpgradeItem;
import dev.xkmc.modulargolems.content.modifier.base.AttributeGolemModifier;
import dev.xkmc.modulargolems.content.modifier.base.GolemModifier;
import dev.xkmc.modulargolems.init.ModularGolems;
import dev.xkmc.modulargolems.init.registrate.GolemItems;
import golemknights.tinkersgolem.TinkersGolem;
import golemknights.tinkersgolem.modifiers.golem.*;
import golemknights.tinkersgolem.modifiers.slime.OverburnModifier;
import golemknights.tinkersgolem.modifiers.slime.OvershockModifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.model.generators.ModelFile;
import org.apache.commons.lang3.mutable.Mutable;
import org.apache.commons.lang3.mutable.MutableObject;

import javax.annotation.Nullable;
import java.util.function.Supplier;

public class TGGolemModifiers {

	public static final RegistryEntry<OvergrowthModifier> OVERGROWTH;
	public static final RegistryEntry<AttributeGolemModifier> OVERFORCED, OVERWORKED;
	public static final RegistryEntry<OverlordModifier> OVERLORD;
    public static final RegistryEntry<OverburnModifier> OVERBURN;
    public static final RegistryEntry<OversmeltModifier> OVERSMELT;
    public static final RegistryEntry<OvershockModifier> OVERSHOCK;
    public static final RegistryEntry<OverbonkingModifier> OVERBOKING;
    public static final RegistryEntry<OverteleportModifier> OVERTELEPORT;

	static {
		OVERWORKED = reg("overworked", () -> new AttributeGolemModifier(3,
				new AttributeGolemModifier.AttrEntry(TGAttributes.STAT_OVERSLIME_RECOVERY, () -> 1)), null);
		OVERFORCED = reg("overforced", () -> new AttributeGolemModifier(5,
				new AttributeGolemModifier.AttrEntry(TGAttributes.STAT_OVERSLIME, () -> 20)), null);

		OVERGROWTH = reg("overgrowth", OvergrowthModifier::new, "Slowly recover overslime over time");
        OVERLORD = reg("overlord", OverlordModifier::new, "When taking damage, recover overslime by %s%% of damage taken");
        OVERBURN = reg("overburn", OverburnModifier::new, "Consumes fluid fuel to recover overslime over time");
        OVERSMELT = reg("oversmelt", OversmeltModifier::new, "Recover overslime over time on blocks that warm striders");
        OVERSHOCK = reg("overshock", OvershockModifier::new, "Has %s%% chance to deal area damage upon landing");
        OVERBOKING = reg("overboking", OverbonkingModifier::new, "Has %s%% chance to push away the target when dealing damage");
        OVERTELEPORT = reg("overteleport", OverteleportModifier::new, "Teleport target when dealing damage, or teleport itself when taking damage");
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

	public static void load() {

	}

}
