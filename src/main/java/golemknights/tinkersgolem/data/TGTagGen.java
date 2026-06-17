package golemknights.tinkersgolem.data;

import com.tterrag.registrate.providers.RegistrateItemTagsProvider;
import dev.xkmc.modulargolems.init.data.MGTagGen;
import golemknights.tinkersgolem.TinkersGolem;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import slimeknights.tconstruct.shared.TinkerMaterials;

public class TGTagGen {

	public static final TagKey<Item> SLIME_PART = item("slime_part");

	public static TagKey<Item> item(String id) {
		return ItemTags.create(TinkersGolem.getResource(id));
	}

	public static void genItemTag(RegistrateItemTagsProvider pvd) {
		pvd.addTag(MGTagGen.SPECIAL_CRAFT).add(
				TinkerMaterials.slimesteel.asItem(),
				TinkerMaterials.queensSlime.asItem(),
				TinkerMaterials.cinderslime.asItem()
		);
	}

}
