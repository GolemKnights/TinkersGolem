package golemknights.tinkersgolem.register;

import golemknights.tinkersgolem.client.TGModelPaths;
import golemknights.tinkersgolem.content.item.armor.ModifiableDogGolemArmorItem;
import golemknights.tinkersgolem.content.item.armor.ModifiableMetalGolemArmorItem;
import golemknights.tinkersgolem.content.item.misc.MaterialToNormalItem;
import golemknights.tinkersgolem.content.item.weapon.ModifiableMetalGolemWeaponItem;
import golemknights.tinkersgolem.content.item.weapon.ModifiableShoulderCannonItem;
import golemknights.tinkersgolem.library.materialstats.CannonCoreMaterialStats;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import slimeknights.mantle.registration.object.EnumObject;
import slimeknights.mantle.registration.object.ItemObject;
import slimeknights.tconstruct.common.Sounds;
import slimeknights.tconstruct.common.registration.CastItemObject;
import slimeknights.tconstruct.library.recipe.partbuilder.Pattern;
import slimeknights.tconstruct.library.tools.definition.ModifiableArmorMaterial;
import slimeknights.tconstruct.library.tools.definition.ToolDefinition;
import slimeknights.tconstruct.library.tools.part.PartCastItem;
import slimeknights.tconstruct.library.tools.part.ToolPartItem;
import slimeknights.tconstruct.tools.stats.PlatingMaterialStats;

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
    // 大傀儡盔甲
    public static final EnumObject<ArmorItem.Type, ModifiableMetalGolemArmorItem> metalGolemArmor = ITEMS
            .registerEnum(
                    METAL_GOLEM, ArmorItem.Type.values(),
                    type -> new ModifiableMetalGolemArmorItem(UNSTACKABLE_PROPS, type,
                            GOLEM,
                            TGModelPaths.METAL_GOLEM_ARMOR_V2,
                            getResource(METAL_GOLEM + "_" + type.getName())));
    public static final ToolDefinition GLOVES_TOOL = ToolDefinition.create(getResource(METAL_GOLEM + "_gloves"));
    public static final ItemObject<ModifiableMetalGolemArmorItem> metalGolemGloves = ITEMS.register(
            METAL_GOLEM + "_gloves",
            () -> new ModifiableMetalGolemArmorItem(UNSTACKABLE_PROPS, ArmorItem.Type.CHESTPLATE,
                    GLOVES_TOOL,
                    TGModelPaths.METAL_GOLEM_ARMOR,
                    getResource(METAL_GOLEM + "_gloves")));

    public static final String DOG_GOLEM_ARMOR = "dog_golem_armor";
    // 狗傀儡盔甲
    public static final ItemObject<ModifiableDogGolemArmorItem> dogGolemArmor = ITEMS.register(
            DOG_GOLEM_ARMOR,
            () -> new ModifiableDogGolemArmorItem(UNSTACKABLE_PROPS, GOLEM, getResource(DOG_GOLEM_ARMOR)));

    // 炮
    public static final String CANNON = "cannon";
    public static final ToolDefinition CANNON_TOOL = ToolDefinition.create(getResource(CANNON));
    public static final ItemObject<ModifiableShoulderCannonItem> cannonItem = ITEMS.register(
            CANNON,
            () -> new ModifiableShoulderCannonItem(UNSTACKABLE_PROPS, CANNON_TOOL));
    // 技术性物品
    public static final ItemObject<MaterialToNormalItem> cannonCoreItem = ITEMS.register("cannon_core",
            ()->new MaterialToNormalItem(CannonCoreMaterialStats.ID));
    // 大傀儡武器
    public static final String SWORD = "golem_sword";
    public static final ToolDefinition SWORD_TOOL = ToolDefinition.create(getResource(SWORD));
    public static final ItemObject<ModifiableMetalGolemWeaponItem> swordItem = ITEMS.register(
            SWORD,
            () -> new ModifiableMetalGolemWeaponItem(UNSTACKABLE_PROPS, SWORD_TOOL));
    public static final String AXE = "golem_axe";
    public static final ToolDefinition AXE_TOOL = ToolDefinition.create(getResource(AXE));
    public static final ItemObject<ModifiableMetalGolemWeaponItem> axeItem = ITEMS.register(
            AXE,
            () -> new ModifiableMetalGolemWeaponItem(UNSTACKABLE_PROPS, AXE_TOOL));
    public static final String SPEAR = "golem_spear";
    public static final ToolDefinition SPEAR_TOOL = ToolDefinition.create(getResource(SPEAR));
    public static final ItemObject<ModifiableMetalGolemWeaponItem> spearItem = ITEMS.register(
            SPEAR,
            () -> new ModifiableMetalGolemWeaponItem(UNSTACKABLE_PROPS, SPEAR_TOOL));
    // 大傀儡盔甲镶板
    public static final EnumObject<ArmorItem.Type, ToolPartItem> metalGolemPlating = ITEMS.registerEnum(
            ArmorItem.Type.values(),
            METAL_GOLEM_PLATING,
            (type) -> new ToolPartItem(ITEM_PROPS, PlatingMaterialStats.TYPES.get(type.ordinal()).getId()));
    public static final Pattern METAL_GOLEM_ARMOR_PATTERN = pattern("metal_golem_armor");
    public static final Pattern METAL_GOLEM_PLATING_PATTERN = pattern(METAL_GOLEM_PLATING);
    public static final CastItemObject helmetMetalGolemPlatingCast = ITEMS.registerCast(
            "helmet_metal_golem_plating",
            () -> new PartCastItem(ITEM_PROPS, () -> metalGolemPlating.get(ArmorItem.Type.HELMET)));
    public static final CastItemObject chestplateMetalGolemPlatingCast = ITEMS.registerCast(
            "chestplate_metal_golem_plating",
            () -> new PartCastItem(ITEM_PROPS, () -> metalGolemPlating.get(ArmorItem.Type.CHESTPLATE)));
    public static final CastItemObject leggingsMetalGolemPlatingCast = ITEMS.registerCast(
            "leggings_metal_golem_plating",
            () -> new PartCastItem(ITEM_PROPS, () -> metalGolemPlating.get(ArmorItem.Type.LEGGINGS)));
    public static final CastItemObject bootsMetalGolemPlatingCast = ITEMS.registerCast(
            "boots_metal_golem_plating",
            () -> new PartCastItem(ITEM_PROPS, () -> metalGolemPlating.get(ArmorItem.Type.BOOTS)));
}
