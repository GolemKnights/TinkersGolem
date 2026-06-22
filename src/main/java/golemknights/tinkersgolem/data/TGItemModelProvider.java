package golemknights.tinkersgolem.data;

import golemknights.tinkersgolem.TinkersGolem;
import golemknights.tinkersgolem.register.TGItems;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import slimeknights.mantle.registration.object.ItemObject;
import slimeknights.tconstruct.common.data.model.MaterialModelBuilder;
import slimeknights.tconstruct.common.registration.CastItemObject;
import slimeknights.tconstruct.library.tools.part.MaterialItem;

import static golemknights.tinkersgolem.TinkersGolem.MODID;

@SuppressWarnings("removal")
public class TGItemModelProvider extends ItemModelProvider {
    private final ModelFile.UncheckedModelFile GENERATED = new ModelFile.UncheckedModelFile("item/generated");
    public TGItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        /*TGItems.metal_golem_plating.forEach((slot, item) -> {
            MaterialModelBuilder<ItemModelBuilder> b = this.part(item, "armor/metal_golem/" + slot.getName() + "/plating");
            if (slot == ArmorItem.Type.HELMET) {
                b.offset(0, 2);
            } else if (slot == ArmorItem.Type.LEGGINGS) {
                b.offset(0, 1);
            }
        });*/
        this.cast(TGItems.bootsMetalGolemPlatingCast);
        this.cast(TGItems.leggingsMetalGolemPlatingCast);
        this.cast(TGItems.chestplateMetalGolemPlatingCast);
        this.cast(TGItems.helmetMetalGolemPlatingCast);
    }

    private ResourceLocation id(ItemLike item) {
        return BuiltInRegistries.ITEM.getKey(item.asItem());
    }

    private ItemModelBuilder generated(ResourceLocation item, ResourceLocation texture) {
        return this.getBuilder(item.toString()).parent(this.GENERATED).texture("layer0", texture);
    }

    private ItemModelBuilder generated(ResourceLocation item, String texture) {
        return this.generated(item, new ResourceLocation(item.getNamespace(), texture));
    }

    private ItemModelBuilder generated(ItemLike item, String texture) {
        return this.generated(this.id(item), texture);
    }

    private ItemModelBuilder basicItem(ResourceLocation item, String texture) {
        return this.generated(item, "item/" + texture);
    }

    private ItemModelBuilder basicItem(ItemLike item, String texture) {
        return this.basicItem(this.id(item), texture);
    }

    private MaterialModelBuilder<ItemModelBuilder> part(ResourceLocation part, String texture) {
        return this.withExistingParent(part.getPath(), "forge:item/default").texture("texture", TinkersGolem.getResource("item/tool/" + texture)).customLoader(MaterialModelBuilder::new);
    }

    private MaterialModelBuilder<ItemModelBuilder> part(Item item, String texture) {
        return this.part(this.id(item), texture);
    }

    private MaterialModelBuilder<ItemModelBuilder> part(ItemObject<? extends MaterialItem> part, String texture) {
        return this.part(part.getId(), texture);
    }

    private void part(ItemObject<? extends MaterialItem> part) {
        this.part(part, "parts/" + part.getId().getPath());
    }

    private void cast(CastItemObject cast) {
        String name = cast.getName().getPath();
        this.basicItem(cast.getId(), "cast/" + name);
        this.basicItem(cast.getSand(), "sand_cast/" + name);
        this.basicItem(cast.getRedSand(), "red_sand_cast/" + name);
    }
}
