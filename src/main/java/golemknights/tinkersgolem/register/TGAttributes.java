package golemknights.tinkersgolem.register;

import com.tterrag.registrate.util.entry.RegistryEntry;
import dev.xkmc.l2library.base.L2Registrate;
import dev.xkmc.modulargolems.content.core.GolemStatType;
import dev.xkmc.modulargolems.content.core.StatFilterType;
import dev.xkmc.modulargolems.init.ModularGolems;
import dev.xkmc.modulargolems.init.registrate.GolemTypes;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.minecraftforge.event.entity.EntityAttributeModificationEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

import static dev.xkmc.modulargolems.init.ModularGolems.REGISTRATE;
import static golemknights.tinkersgolem.TinkersGolem.MODID;

public class TGAttributes {
	public static final DeferredRegister<Attribute> ATTRIBUTES = DeferredRegister.create(ForgeRegistries.ATTRIBUTES, MODID);
	public static final RegistryObject<Attribute> MAX_OVERSLIME = ATTRIBUTES.register("max_overslime", () -> new RangedAttribute(MODID + ".max_overslime", 0, 0, Integer.MAX_VALUE).setSyncable(true));

	public static final RegistryEntry<GolemStatType> STAT_OVERSLIME = regStat("max_overslime", MAX_OVERSLIME, GolemStatType.Kind.ADD, StatFilterType.MASS);

	private static RegistryEntry<GolemStatType> regStat(String id, Supplier<Attribute> sup, GolemStatType.Kind kind, StatFilterType type) {
		var cls = new L2Registrate.RegistryInstance<GolemStatType>(() -> null, ResourceKey.createRegistryKey(new ResourceLocation(ModularGolems.MODID, "stat_type")));
		return REGISTRATE.generic(cls, id, () -> new GolemStatType(sup, kind, type)).register();
	}

	/**
	 * Adds attributes.
	 */
	public static void setupAttributes(EntityAttributeModificationEvent event) {
		if (event.getTypes().contains(GolemTypes.ENTITY_GOLEM.get())) {
			event.add(GolemTypes.ENTITY_GOLEM.get(), MAX_OVERSLIME.get());
		}
		if (event.getTypes().contains(GolemTypes.ENTITY_HUMANOID.get())) {
			event.add(GolemTypes.ENTITY_HUMANOID.get(), MAX_OVERSLIME.get());
		}
		if (event.getTypes().contains(GolemTypes.ENTITY_DOG.get())) {
			event.add(GolemTypes.ENTITY_DOG.get(), MAX_OVERSLIME.get());
		}
	}
}
