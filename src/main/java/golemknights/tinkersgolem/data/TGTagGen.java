package golemknights.tinkersgolem.data;

import com.tterrag.registrate.providers.RegistrateItemTagsProvider;
import dev.xkmc.modulargolems.init.data.MGTagGen;
import golemknights.tinkersgolem.TinkersGolem;
import golemknights.tinkersgolem.register.TGItems;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import slimeknights.tconstruct.common.TinkerTags;
import slimeknights.tconstruct.shared.TinkerMaterials;

public class TGTagGen {

	public static final TagKey<Item> SLIME_PART = item("slime_part");
	public static final TagKey<Item> SLIME_UPGRADE = item("slime_upgrade");

	public static TagKey<Item> item(String id) {
		return ItemTags.create(TinkersGolem.getResource(id));
	}

	public static void genItemTag(RegistrateItemTagsProvider pvd) {
		pvd.addTag(MGTagGen.SPECIAL_CRAFT).add(
				TinkerMaterials.slimesteel.getIngot(),
				TinkerMaterials.queensSlime.getIngot(),
				TinkerMaterials.cinderslime.getIngot()
		);
		//Tools
		pvd.addTag(TinkerTags.Items.BONUS_SLOTS).add(
				TGItems.metalGolemArmor.get(ArmorItem.Type.HELMET).asItem(),
				TGItems.metalGolemArmor.get(ArmorItem.Type.CHESTPLATE).asItem(),
				TGItems.metalGolemArmor.get(ArmorItem.Type.LEGGINGS).asItem(),
				TGItems.metalGolemArmor.get(ArmorItem.Type.BOOTS).asItem()
		);
		pvd.addTag(TinkerTags.Items.MULTIPART_TOOL).add(
				TGItems.metalGolemArmor.get(ArmorItem.Type.HELMET).asItem(),
				TGItems.metalGolemArmor.get(ArmorItem.Type.CHESTPLATE).asItem(),
				TGItems.metalGolemArmor.get(ArmorItem.Type.LEGGINGS).asItem(),
				TGItems.metalGolemArmor.get(ArmorItem.Type.BOOTS).asItem()
		);
		pvd.addTag(TinkerTags.Items.AOE).add(
				TGItems.metalGolemArmor.get(ArmorItem.Type.HELMET).asItem(),
				TGItems.metalGolemArmor.get(ArmorItem.Type.CHESTPLATE).asItem(),
				TGItems.metalGolemArmor.get(ArmorItem.Type.LEGGINGS).asItem(),
				TGItems.metalGolemArmor.get(ArmorItem.Type.BOOTS).asItem()
		);
		pvd.addTag(TinkerTags.Items.DURABILITY).add(
				TGItems.metalGolemArmor.get(ArmorItem.Type.HELMET).asItem(),
				TGItems.metalGolemArmor.get(ArmorItem.Type.CHESTPLATE).asItem(),
				TGItems.metalGolemArmor.get(ArmorItem.Type.LEGGINGS).asItem(),
				TGItems.metalGolemArmor.get(ArmorItem.Type.BOOTS).asItem()
		);
		pvd.addTag(TinkerTags.Items.BROAD_TOOLS).add(
				TGItems.metalGolemArmor.get(ArmorItem.Type.HELMET).asItem(),
				TGItems.metalGolemArmor.get(ArmorItem.Type.CHESTPLATE).asItem(),
				TGItems.metalGolemArmor.get(ArmorItem.Type.LEGGINGS).asItem(),
				TGItems.metalGolemArmor.get(ArmorItem.Type.BOOTS).asItem()
		);
		pvd.addTag(TinkerTags.Items.BOOTS).add(
				TGItems.metalGolemArmor.get(ArmorItem.Type.BOOTS).asItem()
		);
		pvd.addTag(TinkerTags.Items.LEGGINGS).add(
				TGItems.metalGolemArmor.get(ArmorItem.Type.LEGGINGS).asItem()
		);
		pvd.addTag(TinkerTags.Items.CHESTPLATES).add(
				TGItems.metalGolemArmor.get(ArmorItem.Type.CHESTPLATE).asItem()
		);
		pvd.addTag(TinkerTags.Items.HELMETS).add(
				TGItems.metalGolemArmor.get(ArmorItem.Type.HELMET).asItem()
		);
		pvd.addTag(TinkerTags.Items.MIGHTY_ARMOR).add(
				TGItems.metalGolemArmor.get(ArmorItem.Type.HELMET).asItem(),
				TGItems.metalGolemArmor.get(ArmorItem.Type.CHESTPLATE).asItem(),
				TGItems.metalGolemArmor.get(ArmorItem.Type.LEGGINGS).asItem(),
				TGItems.metalGolemArmor.get(ArmorItem.Type.BOOTS).asItem()
		);
		//pvd.addTag(TinkerTags.Items.TRIM);
		//Parts
		pvd.addTag(TinkerTags.Items.BARTERED_PARTS).add(
				TGItems.metal_golem_plating.get(ArmorItem.Type.HELMET).asItem(),
				TGItems.metal_golem_plating.get(ArmorItem.Type.CHESTPLATE).asItem(),
				TGItems.metal_golem_plating.get(ArmorItem.Type.LEGGINGS).asItem(),
				TGItems.metal_golem_plating.get(ArmorItem.Type.BOOTS).asItem()
		);
		//Casts
		pvd.addTag(TinkerTags.Items.GOLD_CASTS).add(
				TGItems.helmetMetalGolemPlatingCast.get().asItem(),
				TGItems.chestplateMetalGolemPlatingCast.get().asItem(),
				TGItems.leggingsMetalGolemPlatingCast.get().asItem(),
				TGItems.bootsMetalGolemPlatingCast.get().asItem()
		);
		pvd.addTag(TinkerTags.Items.SAND_CASTS).add(
				TGItems.helmetMetalGolemPlatingCast.getSand().asItem(),
				TGItems.chestplateMetalGolemPlatingCast.getSand().asItem(),
				TGItems.leggingsMetalGolemPlatingCast.getSand().asItem(),
				TGItems.bootsMetalGolemPlatingCast.getSand().asItem()
		);
		pvd.addTag(TinkerTags.Items.RED_SAND_CASTS).add(
				TGItems.helmetMetalGolemPlatingCast.getRedSand().asItem(),
				TGItems.chestplateMetalGolemPlatingCast.getRedSand().asItem(),
				TGItems.leggingsMetalGolemPlatingCast.getRedSand().asItem(),
				TGItems.bootsMetalGolemPlatingCast.getRedSand().asItem()
		);
		pvd.addTag(TinkerTags.Items.MULTI_USE_CASTS).addTags(
				TGItems.helmetMetalGolemPlatingCast.getMultiUseTag(),
				TGItems.chestplateMetalGolemPlatingCast.getMultiUseTag(),
				TGItems.leggingsMetalGolemPlatingCast.getMultiUseTag(),
				TGItems.bootsMetalGolemPlatingCast.getMultiUseTag()
		);
		pvd.addTag(TinkerTags.Items.SINGLE_USE).addTags(
				TGItems.helmetMetalGolemPlatingCast.getSingleUseTag(),
				TGItems.chestplateMetalGolemPlatingCast.getSingleUseTag(),
				TGItems.leggingsMetalGolemPlatingCast.getSingleUseTag(),
				TGItems.bootsMetalGolemPlatingCast.getSingleUseTag()
		);
		pvd.addTag(TGItems.helmetMetalGolemPlatingCast.getMultiUseTag()).add(
				TGItems.helmetMetalGolemPlatingCast.get().asItem()
		);
		pvd.addTag(TGItems.chestplateMetalGolemPlatingCast.getMultiUseTag()).add(
				TGItems.chestplateMetalGolemPlatingCast.get().asItem()
		);
		pvd.addTag(TGItems.leggingsMetalGolemPlatingCast.getMultiUseTag()).add(
				TGItems.leggingsMetalGolemPlatingCast.get().asItem()
		);
		pvd.addTag(TGItems.bootsMetalGolemPlatingCast.getMultiUseTag()).add(
				TGItems.bootsMetalGolemPlatingCast.get().asItem()
		);
		pvd.addTag(TGItems.helmetMetalGolemPlatingCast.getSingleUseTag()).add(
				TGItems.helmetMetalGolemPlatingCast.getSand().asItem(),
				TGItems.helmetMetalGolemPlatingCast.getRedSand().asItem()
		);
		pvd.addTag(TGItems.chestplateMetalGolemPlatingCast.getSingleUseTag()).add(
				TGItems.chestplateMetalGolemPlatingCast.getSand().asItem(),
				TGItems.chestplateMetalGolemPlatingCast.getRedSand().asItem()
		);
		pvd.addTag(TGItems.leggingsMetalGolemPlatingCast.getSingleUseTag()).add(
				TGItems.leggingsMetalGolemPlatingCast.getSand().asItem(),
				TGItems.leggingsMetalGolemPlatingCast.getRedSand().asItem()
		);
		pvd.addTag(TGItems.bootsMetalGolemPlatingCast.getSingleUseTag()).add(
				TGItems.bootsMetalGolemPlatingCast.getSand().asItem(),
				TGItems.bootsMetalGolemPlatingCast.getRedSand().asItem()
		);
	}

}
