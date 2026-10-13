package golemknights.tinkersgolem.data;

import golemknights.tinkersgolem.TinkersGolem;
import golemknights.tinkersgolem.library.materialstats.CannonCoreMaterialStats;
import net.minecraft.world.item.ArmorItem;
import slimeknights.tconstruct.library.client.data.material.AbstractPartSpriteProvider;
import slimeknights.tconstruct.tools.stats.LimbMaterialStats;
import slimeknights.tconstruct.tools.stats.PlatingMaterialStats;

public class TGPartSpriteProvider extends AbstractPartSpriteProvider {
    public TGPartSpriteProvider() {
        super(TinkersGolem.MODID);
    }

    @Override
    protected void addAllSpites() {
        String gloves = "gloves";
        for (ArmorItem.Type slot : ArmorItem.Type.values()) {
            buildTool("armor/metal_golem/" + slot.getName()).disallowAnimated()
                    .addBreakablePart("huge_plating", PlatingMaterialStats.TYPES.get(slot.ordinal()).getId())
                    .addBreakablePart("small_plating1", PlatingMaterialStats.TYPES.get(slot.ordinal()).getId())
                    .addBreakablePart("small_plating2", PlatingMaterialStats.TYPES.get(slot.ordinal()).getId());
            addTexture("tinker_armor/metal_golem_"+slot.getName()+"/huge_plating_armor", PlatingMaterialStats.TYPES.get(slot.ordinal()).getId()).disallowAnimated();
            addTexture("tinker_armor/metal_golem_"+slot.getName()+"/small_plating1_armor", PlatingMaterialStats.TYPES.get(slot.ordinal()).getId()).disallowAnimated();
            addTexture("tinker_armor/metal_golem_"+slot.getName()+"/small_plating2_armor", PlatingMaterialStats.TYPES.get(slot.ordinal()).getId()).disallowAnimated();
            addPart(slot.getName() + "_metal_golem_plating",
                    PlatingMaterialStats.TYPES.get(slot.ordinal()).getId());
        }
        buildTool("armor/metal_golem/" + gloves).disallowAnimated()
                .addBreakablePart("huge_plating", PlatingMaterialStats.TYPES.get(1).getId())
                .addBreakablePart("small_plating1", PlatingMaterialStats.TYPES.get(1).getId())
                .addBreakablePart("small_plating2", PlatingMaterialStats.TYPES.get(1).getId());
        addTexture("tinker_armor/metal_golem_"+gloves+"/huge_plating_armor", PlatingMaterialStats.TYPES.get(1).getId()).disallowAnimated();
        addTexture("tinker_armor/metal_golem_"+gloves+"/small_plating1_armor", PlatingMaterialStats.TYPES.get(1).getId()).disallowAnimated();
        addTexture("tinker_armor/metal_golem_"+gloves+"/small_plating2_armor", PlatingMaterialStats.TYPES.get(1).getId()).disallowAnimated();
        addTexture("item/tool/weapon/cannon/barrel_1", LimbMaterialStats.ID);
        addTexture("item/tool/weapon/cannon/barrel_2", LimbMaterialStats.ID);
        addTexture("item/tool/weapon/cannon/core", CannonCoreMaterialStats.ID);
        buildTool("weapon/golem_sword")
                //.addHead("golem_sword").addHead("golem_sword_large")
                .addHead("blade1").addHead("blade1_large")
                .addHandle("handle").addHandle("handle_large")
                .addHead("guard").addHead("guard_large")
                .addHead("blade2").addHead("blade2_large");
        buildTool("weapon/golem_axe")
                //.addHead("golem_axe").addHead("golem_axe_large")
                .addHead("head").addHead("head_large")
                .addHandle("handle").addHandle("handle_large")
                .addHead("blade").addHead("blade_large")
                .addBinding("binding").addBinding("binding_large");
        buildTool("weapon/golem_spear")
                //.addHead("golem_spear").addHead("golem_spear_large")
                .addHead("head").addHead("head_large")
                .addHandle("handle1").addHandle("handle1_large")
                .addBinding("binding").addBinding("binding_large")
                .addHandle("handle2").addHandle("handle2_large");
    }

    @Override
    public String getName() {
        return "Tinkers' Golem Parts";
    }
}
