package golemknights.tinkersgolem.item;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import slimeknights.mantle.registration.object.EnumObject;
import slimeknights.tconstruct.common.Sounds;
import slimeknights.tconstruct.library.tools.definition.ModifiableArmorMaterial;
import golemknights.tinkersgolem.item.armor.ModifiableMetalGolemArmorItem;
import static golemknights.tinkersgolem.TinkersGolem.ITEMS;
import static golemknights.tinkersgolem.TinkersGolem.loc;

import static dev.xkmc.modulargolems.content.client.armor.GolemModelPaths.HELMETS;
import static dev.xkmc.modulargolems.content.client.armor.GolemModelPaths.CHESTPLATES;
import static dev.xkmc.modulargolems.content.client.armor.GolemModelPaths.LEGGINGS;
import static dev.xkmc.modulargolems.content.client.armor.GolemModelPaths.BOOTS_DIAMOND;
public class TGItems {
        public static void load() {
        }
        public static final String METAL_GOLEM = "metal_golem";
        public static final ModifiableArmorMaterial GOLEM = ModifiableArmorMaterial.create(loc(METAL_GOLEM), Sounds.EQUIP_PLATE.getSound());
        protected static final Item.Properties UNSTACKABLE_PROPS = new Item.Properties().stacksTo(1);
        public static final ResourceLocation[] METAL_GOLEM_ARMOR_MODELS = { HELMETS, CHESTPLATES, LEGGINGS, BOOTS_DIAMOND };
        public static final EnumObject<ArmorItem.Type, ModifiableMetalGolemArmorItem> metalGolemArmor = ITEMS
                        .registerEnum(
                                        METAL_GOLEM, ArmorItem.Type.values(),
                                        type -> new ModifiableMetalGolemArmorItem(UNSTACKABLE_PROPS, type,
                                                        GOLEM,
                                                        METAL_GOLEM_ARMOR_MODELS[type.ordinal()], loc(METAL_GOLEM+"_"+type.getName())));
        
}
