package golemknights.tinkersgolem;

import com.tterrag.registrate.providers.ProviderType;
import dev.xkmc.l2damagetracker.contents.attack.AttackEventHandler;
import dev.xkmc.l2library.base.L2Registrate;
import dev.xkmc.l2library.serial.config.PacketHandlerWithConfig;
import golemknights.tinkersgolem.cap.OverslimeCap;
import golemknights.tinkersgolem.cap.OverslimeSyncPacket;
import golemknights.tinkersgolem.data.*;
import golemknights.tinkersgolem.entity.SlimeTankSyncPacket;
import golemknights.tinkersgolem.events.TGAttackListener;
import golemknights.tinkersgolem.register.*;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import slimeknights.mantle.registration.deferred.EntityTypeDeferredRegister;
import slimeknights.mantle.registration.deferred.SynchronizedDeferredRegister;
import slimeknights.tconstruct.common.registration.ItemDeferredRegisterExtension;
import slimeknights.tconstruct.library.utils.Util;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(TinkersGolem.MODID)
@Mod.EventBusSubscriber(modid = TinkersGolem.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
@SuppressWarnings("removal")
public class TinkersGolem {

	public static final String MODID = "tinkers_golem";
	public static final Logger LOGGER = LogManager.getLogger();
	public static final L2Registrate REGISTRATE = new L2Registrate(MODID);
	public static final IEventBus MOD_BUS = FMLJavaModLoadingContext.get().getModEventBus();

	public static final PacketHandlerWithConfig HANDLER = new PacketHandlerWithConfig(
			getResource("main"), 1,
			e -> e.create(OverslimeSyncPacket.class, NetworkDirection.PLAY_TO_CLIENT),
			e -> e.create(SlimeTankSyncPacket.class, NetworkDirection.PLAY_TO_CLIENT)
	);

	public static final ItemDeferredRegisterExtension ITEMS = new ItemDeferredRegisterExtension(MODID);
	public static final EntityTypeDeferredRegister ENTITIES = new EntityTypeDeferredRegister(MODID);
	public static final DeferredRegister<ParticleType<?>> PARTICLES = DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, MODID);
	public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES = DeferredRegister.create(Registries.RECIPE_TYPE, MODID);
	public static final SynchronizedDeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = SynchronizedDeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, MODID);
	public static final DeferredRegister<Attribute> ATTRIBUTES = DeferredRegister.create(ForgeRegistries.ATTRIBUTES, MODID);
	public static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);
	// public static final ConfigTypeEntry<SpawnConfig> SPAWN = new
	// ConfigTypeEntry<>(HANDLER, "spawn", SpawnConfig.class);
	// public static final ConfigTypeEntry<EquipmentConfig> ITEMS = new
	// ConfigTypeEntry<>(HANDLER, "equipment", EquipmentConfig.class);
	// public static final ConfigTypeEntry<TrialConfig> TRIAL = new
	// ConfigTypeEntry<>(HANDLER, "trial", TrialConfig.class);

	public TinkersGolem() {
		TGItems.load();
		TGAttributes.load();
		TGEntities.load();
		TGParticles.load();
		TGGolemModifiers.load();
		TGRecipes.load();
		TGTabs.load();
		ITEMS.register(MOD_BUS);
		ENTITIES.register(MOD_BUS);
		PARTICLES.register(MOD_BUS);
		RECIPE_TYPES.register(MOD_BUS);
		RECIPE_SERIALIZERS.register(MOD_BUS);
		ATTRIBUTES.register(MOD_BUS);
		TABS.register(MOD_BUS);
		TGTinkersModifiers.registers(MOD_BUS);
		MOD_BUS.addListener(TGAttributes::setupAttributes);
		// GDItems.register();
		// GDModifiers.register();
		// GDWorldGen.register();
		// GDTriggers.register();
		// GDConfig.init();
		OverslimeCap.register();
		AttackEventHandler.register(3516, new TGAttackListener());
	}

	@SubscribeEvent(priority = EventPriority.HIGH)
	public static void gatherData(GatherDataEvent event) {
		REGISTRATE.addDataGenerator(ProviderType.LANG, TGLang::genLang);
		// REGISTRATE.addDataGenerator(ProviderType.LOOT, GDLootGen::genLoot);
		REGISTRATE.addDataGenerator(ProviderType.RECIPE, TGRecipeGen::genRecipe);
		// REGISTRATE.addDataGenerator(ProviderType.ADVANCEMENT, GDAdvGen::genAdv);
		REGISTRATE.addDataGenerator(ProviderType.ITEM_TAGS, TGTagGen::genItemTag);

		var gen = event.getGenerator();
		var output = gen.getPackOutput();
		var pvd = event.getLookupProvider();
		var helper = event.getExistingFileHelper();
		var server = event.includeServer();
		gen.addProvider(server, new TGConfigGen(gen));
		gen.addProvider(server, new TGToolDefinitionDataProvider(output));
		gen.addProvider(server, new TGStationSlotLayoutProvider(output));
		gen.addProvider(server, new TGToolsRecipeProvider(output));
		gen.addProvider(server, new TGArmorModelProvider(output));
		gen.addProvider(server, new TGToolItemModelProvider(output, helper));
		gen.addProvider(server, new TGItemModelProvider(output, helper));
	}

	/**
	 * Get the ResourceLocation in Tinkers' Golem's namespace.
	 *
	 * @return the ResourceLocation.
	 */
	public static ResourceLocation getResource(String id) {
		return new ResourceLocation(MODID, id);
	}
	public static String makeTranslationKey(String base, String name) {
		return Util.makeTranslationKey(base, getResource(name));
	}

}