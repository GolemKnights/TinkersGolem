package golemknights.tinkersgolem;

import dev.xkmc.l2library.base.L2Registrate;
import dev.xkmc.l2library.serial.config.PacketHandlerWithConfig;
import golemknights.tinkersgolem.register.TGAttributes;
import golemknights.tinkersgolem.register.TGItems;
import golemknights.tinkersgolem.register.TGModifiers;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.event.entity.EntityAttributeModificationEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import slimeknights.mantle.registration.deferred.ItemDeferredRegister;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import slimeknights.mantle.registration.deferred.SynchronizedDeferredRegister;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(TinkersGolem.MODID)
@Mod.EventBusSubscriber(modid = TinkersGolem.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class TinkersGolem {

	public static final String MODID = "tinkers_golem";
	public static final Logger LOGGER = LogManager.getLogger();
	public static final L2Registrate REGISTRATE = new L2Registrate(MODID);
	public static final IEventBus MOD_BUS = FMLJavaModLoadingContext.get().getModEventBus();

	public static final PacketHandlerWithConfig HANDLER = new PacketHandlerWithConfig(
			new ResourceLocation(TinkersGolem.MODID, "main"), 1);

	public static final ItemDeferredRegister ITEMS = new ItemDeferredRegister(MODID);
    public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES = DeferredRegister.create(Registries.RECIPE_TYPE, MODID);
    public static final SynchronizedDeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = SynchronizedDeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, MODID);

    // public static final ConfigTypeEntry<SpawnConfig> SPAWN = new
	// ConfigTypeEntry<>(HANDLER, "spawn", SpawnConfig.class);
	// public static final ConfigTypeEntry<EquipmentConfig> ITEMS = new
	// ConfigTypeEntry<>(HANDLER, "equipment", EquipmentConfig.class);
	// public static final ConfigTypeEntry<TrialConfig> TRIAL = new
	// ConfigTypeEntry<>(HANDLER, "trial", TrialConfig.class);

	public TinkersGolem() {
		MinecraftForge.EVENT_BUS.register(this);
		ITEMS.register(MOD_BUS);
		TGItems.load();
		TGAttributes.ATTRIBUTES.register(MOD_BUS);
		RECIPE_TYPES.register(MOD_BUS);
		RECIPE_SERIALIZERS.register(MOD_BUS);
        TGModifiers.registers(MOD_BUS);
		MOD_BUS.addListener(TGAttributes::setupAttributes);
		// GDItems.register();
		// GDModifiers.register();
		// GDWorldGen.register();
		// GDTriggers.register();
		// GDConfig.init();
		// AttackEventHandler.register(3513, new GDAttackListener());
		// if (ModList.get().isLoaded(CataDispatch.MODID)) {
		// MinecraftForge.EVENT_BUS.register(CataclysmEventHandler.class);
		// FMLJavaModLoadingContext.get().getModEventBus().register(CataclysmModEventHandler.class);
		// }
	}

	@SubscribeEvent
	public static void modifyAttributes(EntityAttributeModificationEvent event) {
	}

	// @SubscribeEvent
	// public static void setup(final FMLCommonSetupEvent event) {
	// event.enqueueWork(() -> {
	// DungeonFactionRegistry.register();
	// IItemSelector.register(new SummonWandSelector(MODID));
	// if (ModList.get().isLoaded(CataDispatch.MODID)) {
	// CataclysmFactions.register();
	// IItemSelector.register(new SummonWandSelector(MODID));
	// }
	// if (ModList.get().isLoaded(TFDispatch.MODID)) {
	// TwilightFactions.register();
	// IItemSelector.register(new SummonWandSelector(MODID));
	// }
	// });
	// }

	@SubscribeEvent(priority = EventPriority.HIGH)
	public static void gatherData(GatherDataEvent event) {
		// REGISTRATE.addDataGenerator(ProviderType.LANG, GDLang::genLang);
		// REGISTRATE.addDataGenerator(ProviderType.LOOT, GDLootGen::genLoot);
		// REGISTRATE.addDataGenerator(ProviderType.RECIPE, GDRecipeGen::genRecipe);
		// REGISTRATE.addDataGenerator(ProviderType.ADVANCEMENT, GDAdvGen::genAdv);
		// REGISTRATE.addDataGenerator(ProviderType.ITEM_TAGS, GDTagGen::genItemTag);

		// var gen = event.getGenerator();
		// var output = gen.getPackOutput();
		// var pvd = event.getLookupProvider();
		// var helper = event.getExistingFileHelper();
		// var server = event.includeServer();
		// GDStructureGen reg = new GDStructureGen(output, pvd);
		// gen.addProvider(server, new GDConfigGen(gen));
		// gen.addProvider(server, reg);
		// gen.addProvider(server, new GDBiomeTagsProvider(output, pvd, helper));
		// gen.addProvider(server, new GDGLMGen(output));
		// gen.addProvider(server, new GDStructureTagsProvider(output,
		// reg.getRegistryProvider(), helper));
		// new GDDamageTypes(output, pvd, helper).generate(server, gen);
	}

	/**
	 * Get the ResourceLocation in Tinkers' Golem's namespace.
	 * @return the ResourceLocation.
	 */
	public static ResourceLocation getResource(String id) {
		return new ResourceLocation(MODID, id);
	}

}