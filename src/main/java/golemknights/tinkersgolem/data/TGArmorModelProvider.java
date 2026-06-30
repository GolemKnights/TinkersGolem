package golemknights.tinkersgolem.data;

import golemknights.tinkersgolem.register.TGItems;
import net.minecraft.data.PackOutput;
import slimeknights.tconstruct.library.client.armor.texture.ArmorTextureSupplier;
import slimeknights.tconstruct.library.client.armor.texture.MaterialArmorTextureSupplier;
import slimeknights.tconstruct.library.client.armor.texture.TrimArmorTextureSupplier;
import slimeknights.tconstruct.library.client.data.AbstractArmorModelProvider;
import slimeknights.tconstruct.tools.ArmorDefinitions;

public class TGArmorModelProvider extends AbstractArmorModelProvider {
    public TGArmorModelProvider(PackOutput packOutput) {
        super(packOutput);
    }

    @Override
    protected void addModels() {
        this.addModel(
                TGItems.GOLEM,
                (name) -> new ArmorTextureSupplier[]{
                        new MaterialArmorTextureSupplier.Material(name, "/huge_plating_", 0),
                        new MaterialArmorTextureSupplier.Material(name, "/small_plating1_", 1),
                        new MaterialArmorTextureSupplier.Material(name, "/small_plating2_", 2),
                        TrimArmorTextureSupplier.INSTANCE
                }
                );
    }

    @Override
    public String getName() {
        return "Tinkers' Golem Armor Models";
    }
}
