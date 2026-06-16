package golemknights.tinkersgolem.data;

import golemknights.tinkersgolem.TinkersGolem;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public class TGTagGen {

	public static final TagKey<Item> SLIME_PART = item("slime_part");

	public static TagKey<Item> item(String id) {
		return ItemTags.create(TinkersGolem.getResource(id));
	}

}
