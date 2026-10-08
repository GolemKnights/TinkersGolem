package golemknights.tinkersgolem.data;

import golemknights.tinkersgolem.library.materialstats.CannonCoreMaterialStats;
import golemknights.tinkersgolem.register.TGItems;
import golemknights.tinkersgolem.register.TGTinkersModifiers;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.ArmorItem;
import slimeknights.tconstruct.library.data.tinkering.AbstractToolDefinitionDataProvider;
import slimeknights.tconstruct.library.materials.RandomMaterial;
import slimeknights.tconstruct.library.tools.SlotType;
import slimeknights.tconstruct.library.tools.definition.module.build.MultiplyStatsModule;
import slimeknights.tconstruct.library.tools.definition.module.build.SetStatsModule;
import slimeknights.tconstruct.library.tools.definition.module.build.ToolSlotsModule;
import slimeknights.tconstruct.library.tools.definition.module.build.ToolTraitsModule;
import slimeknights.tconstruct.library.tools.definition.module.display.MaterialToolNameModule;
import slimeknights.tconstruct.library.tools.definition.module.display.UniqueMaterialToolName;
import slimeknights.tconstruct.library.tools.definition.module.material.DefaultMaterialsModule;
import slimeknights.tconstruct.library.tools.definition.module.material.MaterialStatsModule;
import slimeknights.tconstruct.library.tools.definition.module.material.PartStatsModule;
import slimeknights.tconstruct.library.tools.nbt.MultiplierNBT;
import slimeknights.tconstruct.library.tools.nbt.StatsNBT;
import slimeknights.tconstruct.library.tools.stat.ToolStats;
import slimeknights.tconstruct.tools.TinkerToolParts;
import slimeknights.tconstruct.tools.stats.LimbMaterialStats;

import static golemknights.tinkersgolem.TinkersGolem.MODID;

public class TGToolDefinitionDataProvider extends AbstractToolDefinitionDataProvider {
    public TGToolDefinitionDataProvider(PackOutput packOutput) {
        super(packOutput, MODID);
    }

    @Override
    protected void addToolDefinitions() {
        RandomMaterial tier1Material = RandomMaterial.random().tier(1).build();
        RandomMaterial tier2Material = RandomMaterial.random().tier(1, 2).build();
        RandomMaterial nonHiddenMaterial = RandomMaterial.random().build();
        defineArmor(TGItems.GOLEM).modules(
                (slots) -> PartStatsModule
                        .armor(slots)
                        .part(TGItems.metalGolemPlating, 0.6F)
                        .part(TinkerToolParts.plating, 0.2F)
                        .part(TinkerToolParts.plating, 0.2F)
                )
                .module(
                        DefaultMaterialsModule.builder()
                                .material(tier2Material, tier2Material, tier2Material)
                                .build()
                )
                .modules((slots) -> MultiplyStatsModule
                        .armor(slots)
                        .setAll(ToolStats.DURABILITY, 4)
                        .setAll(ToolStats.ARMOR, 2)
                        .setAll(ToolStats.ARMOR_TOUGHNESS, 3)
                        .setAll(ToolStats.KNOCKBACK_RESISTANCE, 2)
                        .set(ArmorItem.Type.CHESTPLATE, ToolStats.ATTACK_DAMAGE, 0.8f)
                )
                .module(
                        ToolSlotsModule.builder()
                                .slots(SlotType.UPGRADE, 2)
                                .slots(SlotType.DEFENSE, 2)
                                .build()
                )
        ;
        define(TGItems.GLOVES_TOOL)
                .module(
                        PartStatsModule
                                .parts()
                                .part(TGItems.metalGolemPlating.get(ArmorItem.Type.CHESTPLATE), 0.4F)
                                .part(TinkerToolParts.largePlate, 0.3F)
                                .part(TinkerToolParts.largePlate, 0.3F)
                                .build()
                )
                .module(
                        DefaultMaterialsModule.builder()
                                .material(tier2Material, tier1Material, tier1Material)
                                .build()
                )
                .module(
                        new SetStatsModule(StatsNBT.builder()
                                .set(ToolStats.ATTACK_DAMAGE, 3)
                                .build()
                        )
                )
                .module(
                        new MultiplyStatsModule(MultiplierNBT.builder()
                                .set(ToolStats.DURABILITY, 4)
                                .set(ToolStats.ARMOR, 2)
                                .set(ToolStats.ARMOR_TOUGHNESS, 3)
                                .set(ToolStats.KNOCKBACK_RESISTANCE, 2)
                                .set(ToolStats.ATTACK_DAMAGE, 1.6f)
                                .build()
                        )
                )
                .module(
                        ToolSlotsModule.builder()
                                .slots(SlotType.UPGRADE, 1)
                                .slots(SlotType.DEFENSE, 1)
                                .slots(SlotType.ABILITY, 1)
                                .build()
                )
        ;
        define(TGItems.CANNON_TOOL)
                .module(
                        MaterialStatsModule.stats()
                                .stat(CannonCoreMaterialStats.ID)
                                .stat(LimbMaterialStats.ID)
                                .stat(LimbMaterialStats.ID)
                                .primaryPart(1)
                                .build()
                )
                .module(
                        DefaultMaterialsModule.builder()
                                .material(nonHiddenMaterial, tier1Material, tier1Material)
                                .build()
                )
                .module(
                        new MultiplyStatsModule(MultiplierNBT.builder()
                                .set(ToolStats.DURABILITY, 2)
                                .build()
                        )
                )
                .module(
                        ToolSlotsModule.builder()
                            .slots(SlotType.UPGRADE, 3)
                            .slots(SlotType.ABILITY, 1)
                            .build()
                )
                .module(
                        UniqueMaterialToolName.FIRST
                )
                .module(
                        MaterialToolNameModule.REPAIRABLE
                )
                .module(
                        ToolTraitsModule.builder().trait(TGTinkersModifiers.ammoFinder).build()
                )
        ;
    }

    @Override
    public String getName() {
        return "Tinkers' Golem Tool Definition Data Generator";
    }
}
