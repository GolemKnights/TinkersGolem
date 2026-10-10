package golemknights.tinkersgolem.register;

import static golemknights.tinkersgolem.TinkersGolem.getResource;

import golemknights.tinkersgolem.TinkersGolem;
import net.minecraft.tags.TagKey;
import slimeknights.tconstruct.library.materials.definition.IMaterial;
import slimeknights.tconstruct.library.materials.definition.MaterialId;
import slimeknights.tconstruct.library.materials.definition.MaterialManager;

public class TGMaterials {
    public static final TagKey<IMaterial> cannoncore = MaterialManager.getTag(getResource("cannoncore"));
    public static final MaterialId slimecore = id("slimecore");
    public static final MaterialId fluidcore = id("fluidcore");
    public static final MaterialId arrowcore = id("arrowcore");
    public static final MaterialId beaconcore = id("beaconcore");

    private static MaterialId id(String name) {
        return new MaterialId(TinkersGolem.MODID, name);
    }
}
