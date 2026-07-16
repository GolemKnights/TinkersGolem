package golemknights.tinkersgolem.data;

import golemknights.tinkersgolem.register.TGItems;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import slimeknights.tconstruct.library.client.armor.texture.MaterialArmorTextureSupplier;
import slimeknights.tconstruct.library.client.data.AbstractArmorModelProvider;
import static golemknights.tinkersgolem.TinkersGolem.getResource;

public class TGArmorModelProvider extends AbstractArmorModelProvider {
    public TGArmorModelProvider(PackOutput packOutput) {
        super(packOutput);
    }

    @Override
    protected void addModels() {
        for (String part : new String[] { "_helmet", "_chestplate", "_leggings", "_boots" }) {
            ResourceLocation res = getResource(TGItems.METAL_GOLEM + part);
            this.addModel(
                    res,
                    new MaterialArmorTextureSupplier.Material(res, "/huge_plating_", 0),
                    new MaterialArmorTextureSupplier.Material(res, "/small_plating1_", 1),
                    new MaterialArmorTextureSupplier.Material(res, "/small_plating2_", 2));
        }
    }

    @Override
    public String getName() {
        return "Tinkers' Golem Armor Models";
    }
}
