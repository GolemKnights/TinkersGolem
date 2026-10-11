package golemknights.tinkersgolem.register;

import golemknights.tinkersgolem.TinkersGolem;
import golemknights.tinkersgolem.library.materialstats.CannonCoreMaterialStats;
import slimeknights.tconstruct.common.TinkerTags;
import slimeknights.tconstruct.library.materials.IMaterialRegistry;
import slimeknights.tconstruct.library.materials.MaterialRegistry;
import slimeknights.tconstruct.library.materials.stats.MaterialStatsId;
import slimeknights.tconstruct.library.tools.stat.FloatToolStat;
import slimeknights.tconstruct.library.tools.stat.ToolStatId;
import slimeknights.tconstruct.library.tools.stat.ToolStats;

public class TGStats {
    // Tool Stats
    public static final FloatToolStat CANNON_RANGE = ToolStats.register(
            new FloatToolStat(
                    new ToolStatId(TinkersGolem.getResource("cannon_range")),
                    0xFF47CC47, 0.0F, 0.0F, 2048.0F,
                    TinkerTags.Items.RANGED
            )
    );
    public static final FloatToolStat ATTACK_DAMAGE_PERCENT = ToolStats.register(
            new FloatToolStat(
                    new ToolStatId(TinkersGolem.getResource("attack_damage_percent")),
                    0x289B9C, 0.0F, -1.0F, 2048.0F,
                    TGTags.Items.METAL_GOLEM_WEAPON
            )
    );
    public static final FloatToolStat ENTITY_REACH = ToolStats.register(
            new FloatToolStat(
                    new ToolStatId(TinkersGolem.getResource("entity_reach")),
                    0xFFFFFF, 0.0F, 0.0F, 2048.0F,
                    TGTags.Items.METAL_GOLEM_WEAPON
            )
    );
    public static final FloatToolStat GOLEM_SWEEP = ToolStats.register(
            new FloatToolStat(
                    new ToolStatId(TinkersGolem.getResource("golem_sweep")),
                    0xFFFFFF, 0.0F, 0.0F, 2048.0F,
                    TGTags.Items.METAL_GOLEM_WEAPON
            )
    );
    // Material Stats
    public static final MaterialStatsId CANNON = new MaterialStatsId(TinkersGolem.getResource("cannon"));

    public static void setup() {
        IMaterialRegistry registry = MaterialRegistry.getInstance();
        registry.registerStatType(CannonCoreMaterialStats.TYPE, CANNON);
    }
}
