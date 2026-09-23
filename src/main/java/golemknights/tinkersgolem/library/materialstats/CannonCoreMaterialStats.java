package golemknights.tinkersgolem.library.materialstats;

import net.minecraft.network.chat.Component;
import slimeknights.tconstruct.library.materials.stats.IMaterialStats;
import slimeknights.tconstruct.library.materials.stats.IRepairableMaterialStats;
import slimeknights.tconstruct.library.materials.stats.MaterialStatType;
import slimeknights.tconstruct.library.tools.stat.ModifierStatsBuilder;

import java.util.List;

public record CannonCoreMaterialStats() implements IMaterialStats.ScaledTooltip {
    @Override
    public MaterialStatType<?> getType() {
        return null;
    }

    @Override
    public List<Component> getLocalizedInfo(float scale) {
        return null;
    }

    @Override
    public List<Component> getLocalizedDescriptions() {
        return null;
    }

    @Override
    public void apply(ModifierStatsBuilder builder, float v) {
    }

}
