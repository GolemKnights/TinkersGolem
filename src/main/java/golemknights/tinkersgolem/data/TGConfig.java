package golemknights.tinkersgolem.data;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.IConfigSpec;
import net.minecraftforge.fml.config.ModConfig;
import org.apache.commons.lang3.tuple.Pair;

public class TGConfig {

	public static class Client {

		Client(ForgeConfigSpec.Builder builder) {
		}

	}

	public static class Common {

		public final ForgeConfigSpec.DoubleValue overgrowthRate;
		public final ForgeConfigSpec.DoubleValue overforcedAmount;
		public final ForgeConfigSpec.DoubleValue overfillFactor;
		public final ForgeConfigSpec.DoubleValue overworkedFactor;
		public final ForgeConfigSpec.DoubleValue overlordFactor;
		public final ForgeConfigSpec.IntValue overburnConsumption;
		public final ForgeConfigSpec.DoubleValue oversmeltRate;
		public final ForgeConfigSpec.DoubleValue overshockFactor;
		public final ForgeConfigSpec.DoubleValue overbonkingChance;
		public final ForgeConfigSpec.IntValue overteleportRange;
		public final ForgeConfigSpec.IntValue overteleportRangePerLevel;

		Common(ForgeConfigSpec.Builder builder) {
			overgrowthRate = builder.comment("Overgrowth recovery per second per level")
					.defineInRange("overgrowthRate", 0.5d, 0, 10);
			overforcedAmount = builder.comment("Overforced additional overslime per level")
					.defineInRange("overforcedAmount", 20d, 0, 1000);
			overfillFactor = builder.comment("overfill tank capacity factor per level")
					.defineInRange("overfillFactor", 0.5d, 0, 10);
			overworkedFactor = builder.comment("Overworked additional overslime recovery per level")
					.defineInRange("overworkedFactor", 0.75d, 0, 1000);
			overlordFactor = builder.comment("Overlord overslime recovery per damage taken per level")
					.defineInRange("overlordFactor", 0.1d, 0, 1000);
			overburnConsumption = builder.comment("Overburn fuel rate consumption per overslime restored")
					.defineInRange("overburnConsumption", 2, 1, 100);
			oversmeltRate = builder.comment("Oversmelt recovery per second per level")
					.defineInRange("oversmeltRate", 1d, 0, 10);
			overshockFactor = builder.comment("Overshock damage factor per level")
					.defineInRange("overshockFactor", 0.2d, 0, 10);
			overbonkingChance = builder.comment("Overbonking knockback chance per level")
					.defineInRange("overbonkingChance", 0.2d, 0, 10);
			overteleportRange = builder.comment("Overteleport destination base range")
					.defineInRange("overteleportRange", 16, 1, 100);
			overteleportRangePerLevel = builder.comment("Overteleport destination range per level")
					.defineInRange("overteleportRangePerLevel", 8, 1, 100);
		}

	}

	public static final ForgeConfigSpec CLIENT_SPEC;
	public static final Client CLIENT;

	public static final ForgeConfigSpec COMMON_SPEC;
	public static final Common COMMON;

	static {
		final Pair<Client, ForgeConfigSpec> client = new ForgeConfigSpec.Builder().configure(Client::new);
		CLIENT_SPEC = client.getRight();
		CLIENT = client.getLeft();

		final Pair<Common, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(Common::new);
		COMMON_SPEC = specPair.getRight();
		COMMON = specPair.getLeft();
	}

	public static void init() {
		register(ModConfig.Type.CLIENT, CLIENT_SPEC);
		register(ModConfig.Type.COMMON, COMMON_SPEC);
	}

	private static void register(ModConfig.Type type, IConfigSpec<?> spec) {
		var mod = ModLoadingContext.get().getActiveContainer();
		String path = "l2_configs/" + mod.getModId() + "-" + type.extension() + ".toml";
		ModLoadingContext.get().registerConfig(type, spec, path);
	}


}
