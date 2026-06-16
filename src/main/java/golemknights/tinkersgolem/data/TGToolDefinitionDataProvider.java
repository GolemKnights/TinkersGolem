package golemknights.tinkersgolem.data;

import golemknights.tinkersgolem.register.TGItems;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.ArmorItem;
import slimeknights.tconstruct.library.data.tinkering.AbstractToolDefinitionDataProvider;
import slimeknights.tconstruct.library.materials.RandomMaterial;
import slimeknights.tconstruct.library.tools.SlotType;
import slimeknights.tconstruct.library.tools.definition.module.build.MultiplyStatsModule;
import slimeknights.tconstruct.library.tools.definition.module.build.ToolSlotsModule;
import slimeknights.tconstruct.library.tools.definition.module.material.DefaultMaterialsModule;
import slimeknights.tconstruct.library.tools.definition.module.material.PartStatsModule;
import slimeknights.tconstruct.library.tools.nbt.MultiplierNBT;
import slimeknights.tconstruct.library.tools.stat.ToolStats;
import slimeknights.tconstruct.tools.TinkerToolParts;

import static golemknights.tinkersgolem.TinkersGolem.MODID;

public class TGToolDefinitionDataProvider extends AbstractToolDefinitionDataProvider {
    public TGToolDefinitionDataProvider(PackOutput packOutput) {
        super(packOutput, MODID);
    }

    @Override
    protected void addToolDefinitions() {
        RandomMaterial tier2Material = RandomMaterial.random().tier(1, 2).build();
        DefaultMaterialsModule metalGolemMaterials = DefaultMaterialsModule.builder().material(new RandomMaterial[]{tier2Material, tier2Material}).build();;
        defineArmor(TGItems.GOLEM).modules(
                (slots) -> PartStatsModule
                        .armor(slots)
                        .part(TGItems.metal_golem_plating, 0.6F)
                        .part(TinkerToolParts.plating, 0.2F)
                        .part(TinkerToolParts.plating, 0.2F)
                )
                .module(metalGolemMaterials)
                .module(
                        ArmorItem.Type.CHESTPLATE,
                        new MultiplyStatsModule(MultiplierNBT.builder()
                                .set(ToolStats.ATTACK_DAMAGE, 0.4F)
                                .build()
                        )
                )
                .module(
                        ToolSlotsModule.builder()
                                .slots(SlotType.UPGRADE, 2)
                                .slots(SlotType.DEFENSE, 2)
                                .build()
                );
    }

    @Override
    public String getName() {
        return "Tinkers' Golem Tool Definition Data Generator";
    }
}
