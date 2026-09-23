package golemknights.tinkersgolem.library.materialstats;

import golemknights.tinkersgolem.TinkersGolem;
import golemknights.tinkersgolem.register.TGStats;
import net.minecraft.network.chat.Component;
import slimeknights.mantle.data.loadable.primitive.FloatLoadable;
import slimeknights.mantle.data.loadable.record.RecordLoadable;
import slimeknights.tconstruct.TConstruct;
import slimeknights.tconstruct.library.materials.stats.IMaterialStats;
import slimeknights.tconstruct.library.materials.stats.MaterialStatType;
import slimeknights.tconstruct.library.materials.stats.MaterialStatsId;
import slimeknights.tconstruct.library.tools.stat.IToolStat;
import slimeknights.tconstruct.library.tools.stat.ModifierStatsBuilder;
import slimeknights.tconstruct.library.tools.stat.ToolStats;

import java.util.ArrayList;
import java.util.List;

import static slimeknights.tconstruct.tools.stats.HandleMaterialStats.formatDurability;

public record CannonCoreMaterialStats(float durability, float drawSpeed, float cannonRange) implements IMaterialStats.ScaledTooltip {
    public static final MaterialStatsId ID = new MaterialStatsId(TConstruct.getResource("handle"));
    public static final MaterialStatType<CannonCoreMaterialStats> TYPE = new MaterialStatType<>(
            ID, new CannonCoreMaterialStats(0.0F, 0.0F, 0.0F),
            RecordLoadable.create(
                    FloatLoadable.ANY.defaultField("durability", 0.0F, true, CannonCoreMaterialStats::durability),
                    FloatLoadable.ANY.defaultField("mining_speed", 0.0F, true, CannonCoreMaterialStats::drawSpeed),
                    FloatLoadable.ANY.defaultField("melee_speed", 0.0F, true, CannonCoreMaterialStats::cannonRange),
                    CannonCoreMaterialStats::new
            )
    );
    private static final List<Component> DESCRIPTION = List.of(
            IMaterialStats.makeTooltip(TinkersGolem.getResource("cannon_core.durability.description")),
            IMaterialStats.makeTooltip(TinkersGolem.getResource("cannon_core.draw_speed.description")),
            IMaterialStats.makeTooltip(TinkersGolem.getResource("cannon_core.cannon_range.description"))
    );

    static final String DRAW_SPEED_PREFIX = IMaterialStats.makeTooltipKey(TConstruct.getResource("draw_speed"));
    static final String CANNNON_RANGE_PREFIX = IMaterialStats.makeTooltipKey(TinkersGolem.getResource("cannon_range"));
    @Override
    public MaterialStatType<?> getType() {
        return TYPE;
    }

    @Override
    public List<Component> getLocalizedInfo(float scale) {
        List<Component> list = new ArrayList<>();
        list.add(formatDurability(this.durability * scale));
        list.add(IToolStat.formatColoredBonus(DRAW_SPEED_PREFIX, this.drawSpeed * scale));
        list.add(IToolStat.formatColoredBonus(CANNNON_RANGE_PREFIX, this.cannonRange * scale));
        return list;
    }

    @Override
    public List<Component> getLocalizedDescriptions() {
        return DESCRIPTION;
    }

    @Override
    public void apply(ModifierStatsBuilder builder, float scale) {
        ToolStats.DURABILITY.percent(builder, this.durability * scale);
        ToolStats.DRAW_SPEED.percent(builder, this.drawSpeed * scale);
        TGStats.CANNON_RANGE.percent(builder, this.cannonRange * scale);
    }
}
