package golemknights.tinkersgolem.register;

import golemknights.tinkersgolem.TinkersGolem;
import slimeknights.tconstruct.library.materials.definition.MaterialId;
import slimeknights.tconstruct.library.materials.definition.MaterialVariantId;

public class TGMaterials {
    public static final MaterialId slimecore = id("slimecore");
    public static final MaterialId fluidcore = id("fluidcore");
    private static MaterialId id(String name) {
        return new MaterialId(TinkersGolem.MODID, name);
    }
}
