package golemknights.tinkersgolem.register;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import slimeknights.mantle.registration.object.EnumObject;
import slimeknights.mantle.registration.object.ItemObject;
import slimeknights.tconstruct.common.Sounds;
import slimeknights.tconstruct.library.materials.stats.MaterialStatType;
import slimeknights.tconstruct.library.tools.definition.ModifiableArmorMaterial;
import golemknights.tinkersgolem.item.armor.ModifiableDogGolemArmorItem;
import golemknights.tinkersgolem.item.armor.ModifiableMetalGolemArmorItem;
import slimeknights.tconstruct.library.tools.part.ToolPartItem;
import slimeknights.tconstruct.tools.stats.PlatingMaterialStats;

import static golemknights.tinkersgolem.TinkersGolem.ITEMS;
import static golemknights.tinkersgolem.TinkersGolem.getResource;

import static dev.xkmc.modulargolems.content.client.armor.GolemModelPaths.HELMETS;
import static dev.xkmc.modulargolems.content.client.armor.GolemModelPaths.CHESTPLATES;
import static dev.xkmc.modulargolems.content.client.armor.GolemModelPaths.LEGGINGS;
import static dev.xkmc.modulargolems.content.client.armor.GolemModelPaths.BOOTS_DIAMOND;

public class TGItems {
    public static void load() {
    }
    public static final String METAL_GOLEM = "metal_golem";
    public static final ModifiableArmorMaterial GOLEM = ModifiableArmorMaterial.create(getResource(METAL_GOLEM),
            Sounds.EQUIP_PLATE.getSound());
    protected static final Item.Properties UNSTACKABLE_PROPS = new Item.Properties().stacksTo(1);
    protected static final Item.Properties ITEM_PROPS = new Item.Properties();
    public static final ResourceLocation[] METAL_GOLEM_ARMOR_MODELS = { HELMETS, CHESTPLATES, LEGGINGS,
            BOOTS_DIAMOND };
    public static final EnumObject<ArmorItem.Type, ModifiableMetalGolemArmorItem> metalGolemArmor = ITEMS
            .registerEnum(
                    METAL_GOLEM, ArmorItem.Type.values(),
                    type -> new ModifiableMetalGolemArmorItem(UNSTACKABLE_PROPS, type,
                            GOLEM,
                            METAL_GOLEM_ARMOR_MODELS[type.ordinal()],
                            getResource(METAL_GOLEM + "_" + type.getName())));

    public static final String DOG_GOLEM_ARMOR = "dog_golem_armor";
    public static final ItemObject<ModifiableDogGolemArmorItem> dogGolemArmor = ITEMS.register(
            DOG_GOLEM_ARMOR,
            () -> new ModifiableDogGolemArmorItem(UNSTACKABLE_PROPS, GOLEM, getResource(DOG_GOLEM_ARMOR)));
    public static final EnumObject<ArmorItem.Type, ToolPartItem> metal_golem_plating = ITEMS.registerEnum(
            ArmorItem.Type.values(),
            "metal_golem_plating",
            (type) -> new ToolPartItem(ITEM_PROPS, PlatingMaterialStats.TYPES.get(type.ordinal()).getId())
    );
}
