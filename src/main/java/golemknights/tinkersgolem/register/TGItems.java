package golemknights.tinkersgolem.register;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import slimeknights.mantle.registration.object.EnumObject;
import slimeknights.mantle.registration.object.ItemObject;
import slimeknights.tconstruct.common.Sounds;
import slimeknights.tconstruct.common.registration.CastItemObject;
import slimeknights.tconstruct.library.recipe.partbuilder.Pattern;
import slimeknights.tconstruct.library.tools.definition.ModifiableArmorMaterial;
import golemknights.tinkersgolem.item.armor.ModifiableDogGolemArmorItem;
import golemknights.tinkersgolem.item.armor.ModifiableMetalGolemArmorItem;
import slimeknights.tconstruct.library.tools.part.PartCastItem;
import slimeknights.tconstruct.library.tools.part.ToolPartItem;
import slimeknights.tconstruct.tools.stats.PlatingMaterialStats;

import static dev.xkmc.modulargolems.content.client.armor.GolemModelPaths.HELMETS;
import static dev.xkmc.modulargolems.content.client.armor.GolemModelPaths.CHESTPLATES;
import static dev.xkmc.modulargolems.content.client.armor.GolemModelPaths.LEGGINGS;
import static dev.xkmc.modulargolems.content.client.armor.GolemModelPaths.BOOTS_DIAMOND;
import static golemknights.tinkersgolem.TinkersGolem.*;

public class TGItems {
    public static void load() {
    }
    private static Pattern pattern(String name) {
        return new Pattern(MODID, name);
    }
    public static final String METAL_GOLEM = "metal_golem";
    public static final String METAL_GOLEM_PLATING = "metal_golem_plating";
    public static final ModifiableArmorMaterial GOLEM = ModifiableArmorMaterial.create(getResource(METAL_GOLEM),
            Sounds.EQUIP_PLATE.getSound());
    protected static final Item.Properties UNSTACKABLE_PROPS = new Item.Properties().stacksTo(1);
    protected static final Item.Properties ITEM_PROPS = new Item.Properties();
    public static final ResourceLocation[] METAL_GOLEM_ARMOR_MODELS = { HELMETS, CHESTPLATES, LEGGINGS,
            BOOTS_DIAMOND };
    //大傀儡盔甲
    public static final EnumObject<ArmorItem.Type, ModifiableMetalGolemArmorItem> metalGolemArmor = ITEMS
            .registerEnum(
                    METAL_GOLEM, ArmorItem.Type.values(),
                    type -> new ModifiableMetalGolemArmorItem(UNSTACKABLE_PROPS, type,
                            GOLEM,
                            METAL_GOLEM_ARMOR_MODELS[type.ordinal()],
                            getResource(METAL_GOLEM + "_" + type.getName())
                    )
            );

    public static final String DOG_GOLEM_ARMOR = "dog_golem_armor";
    //狗傀儡盔甲
    public static final ItemObject<ModifiableDogGolemArmorItem> dogGolemArmor = ITEMS.register(
            DOG_GOLEM_ARMOR,
            () -> new ModifiableDogGolemArmorItem(UNSTACKABLE_PROPS, GOLEM, getResource(DOG_GOLEM_ARMOR)));
    //大傀儡盔甲镶板
    public static final EnumObject<ArmorItem.Type, ToolPartItem> metal_golem_plating = ITEMS.registerEnum(
            ArmorItem.Type.values(),
            METAL_GOLEM_PLATING,
            (type) -> new ToolPartItem(ITEM_PROPS, PlatingMaterialStats.TYPES.get(type.ordinal()).getId())
    );
    public static final Pattern METAL_GOLEM_ARMOR_PATTERN = pattern("metal_golem_armor");
    public static final Pattern METAL_GOLEM_PLATING_PATTERN = pattern(METAL_GOLEM_PLATING);
    public static final Pattern GOLEM_TEMPLATE_PATTERN = pattern("golem_template");
    public static final CastItemObject helmetMetalGolemPlatingCast = ITEMS.registerCast(
            "helmet_metal_golem_plating",
            () -> new PartCastItem(ITEM_PROPS, () -> metal_golem_plating.get(ArmorItem.Type.HELMET))
    );
    public static final CastItemObject chestplateMetalGolemPlatingCast = ITEMS.registerCast(
            "chestplate_metal_golem_plating",
            () -> new PartCastItem(ITEM_PROPS, () -> metal_golem_plating.get(ArmorItem.Type.CHESTPLATE))
    );
    public static final CastItemObject leggingsMetalGolemPlatingCast = ITEMS.registerCast(
            "leggings_metal_golem_plating",
            () -> new PartCastItem(ITEM_PROPS, () -> metal_golem_plating.get(ArmorItem.Type.LEGGINGS))
    );
    public static final CastItemObject bootsMetalGolemPlatingCast = ITEMS.registerCast(
            "boots_metal_golem_plating",
            () -> new PartCastItem(ITEM_PROPS, () -> metal_golem_plating.get(ArmorItem.Type.BOOTS))
    );
}
