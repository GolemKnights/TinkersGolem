package golemknights.tinkersgolem.data;

import golemknights.tinkersgolem.library.materialstats.CannonCoreMaterialStats;
import net.minecraft.data.PackOutput;
import slimeknights.tconstruct.library.data.material.AbstractMaterialDataProvider;
import slimeknights.tconstruct.library.data.material.AbstractMaterialStatsDataProvider;
import slimeknights.tconstruct.library.data.material.AbstractMaterialTraitDataProvider;
import slimeknights.tconstruct.tools.data.ModifierIds;

import static golemknights.tinkersgolem.register.TGMaterials.slimecore;

public class TGMaterialProvider extends AbstractMaterialDataProvider {
    public TGMaterialProvider(PackOutput packOutput) {
        super(packOutput);
    }

    @Override
    protected void addMaterials() {
        material(slimecore).tier(3).sort(1).craftable();
    }

    @Override
    public String getName() {
        return "Tinker's Golem Materials";
    }

    public static class Stats extends AbstractMaterialStatsDataProvider {
        public Stats(PackOutput packOutput, AbstractMaterialDataProvider materials) {
            super(packOutput, materials);
        }

        @Override
        protected void addMaterialStats() {
            addMaterialStats(slimecore, new CannonCoreMaterialStats(0, 0, 15));
        }

        @Override
        public String getName() {
            return "Tinker's Golem Material Stats";
        }
    }

    public static class Traits extends AbstractMaterialTraitDataProvider {
        public Traits(PackOutput packOutput, AbstractMaterialDataProvider materials) {
            super(packOutput, materials);
        }

        @Override
        protected void addMaterialTraits() {
            addDefaultTraits(slimecore, ModifierIds.slimeball);
        }

        @Override
        public String getName() {
            return "Tinker's Golem Material Traits";
        }
    }
}
