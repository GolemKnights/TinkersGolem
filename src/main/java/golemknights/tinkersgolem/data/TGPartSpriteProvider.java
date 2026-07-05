package golemknights.tinkersgolem.data;

import golemknights.tinkersgolem.TinkersGolem;
import net.minecraft.world.item.ArmorItem;
import slimeknights.tconstruct.library.client.data.material.AbstractPartSpriteProvider;
import slimeknights.tconstruct.library.materials.stats.MaterialStatType;
import slimeknights.tconstruct.library.materials.stats.MaterialStatsId;
import slimeknights.tconstruct.tools.stats.PlatingMaterialStats;

public class TGPartSpriteProvider extends AbstractPartSpriteProvider {
    public TGPartSpriteProvider() {
        super(TinkersGolem.MODID);
    }

    @Override
    protected void addAllSpites() {
        for (ArmorItem.Type slot: ArmorItem.Type.values()){
            this.buildTool("armor/metal_golem/" + slot.getName()).disallowAnimated()
                    .addBreakablePart("huge_plating", PlatingMaterialStats.TYPES.get(slot.ordinal()).getId())
                    .addBreakablePart("small_plating1", PlatingMaterialStats.TYPES.get(slot.ordinal()).getId())
                    .addBreakablePart("small_plating2", PlatingMaterialStats.TYPES.get(slot.ordinal()).getId())
                    ;
            this.addPart(slot.getName() + "_metal_golem_plating", PlatingMaterialStats.TYPES.get(slot.ordinal()).getId());
        }
    }
    @Override
    public String getName() {
        return "Tinkers' Golem Parts";
    }
}
