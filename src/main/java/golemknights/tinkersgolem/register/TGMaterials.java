package golemknights.tinkersgolem.register;

import golemknights.tinkersgolem.TinkersGolem;
import slimeknights.tconstruct.library.materials.definition.MaterialId;

public class TGMaterials {
    public static final MaterialId slimecore = id("slimecore");
    public static final MaterialId fluidcore = id("fluidcore");
    public static final MaterialId arrowcore = id("arrowcore");
    public static final MaterialId beaconcore = id("beaconcore");
    private static MaterialId id(String name) {
        return new MaterialId(TinkersGolem.MODID, name);
    }
}
