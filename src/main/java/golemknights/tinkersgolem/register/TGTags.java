package golemknights.tinkersgolem.register;

import golemknights.tinkersgolem.TinkersGolem;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public class TGTags {
    public static void init(){
        Items.init();
    }
    public static class Items {
        public static void init(){}
        public static final TagKey<Item> METAL_GOLEM_WEAPON = local("modifiable/melee/metal_golem_weapon");
        private static TagKey<Item> local(String name) {
            return TagKey.create(Registries.ITEM, TinkersGolem.getResource(name));
        }
    }
}
