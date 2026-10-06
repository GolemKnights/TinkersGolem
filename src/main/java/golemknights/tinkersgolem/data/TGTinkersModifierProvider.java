package golemknights.tinkersgolem.data;

import golemknights.tinkersgolem.content.modifiers.tool.SpewingModule;
import golemknights.tinkersgolem.register.TGTinkersModifiers;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.crafting.conditions.IConditionBuilder;
import slimeknights.tconstruct.library.data.tinkering.AbstractModifierProvider;
import slimeknights.tconstruct.library.json.LevelingInt;
import slimeknights.tconstruct.library.modifiers.modules.build.StatBoostModule;
import slimeknights.tconstruct.library.tools.capability.fluid.ToolTankHelper;

public class TGTinkersModifierProvider extends AbstractModifierProvider implements IConditionBuilder {
    public TGTinkersModifierProvider(PackOutput packOutput) {
        super(packOutput);
    }

    @Override
    protected void addModifiers() {
        buildModifier(TGTinkersModifiers.spewing).priority(120)
                .addModule(new SpewingModule(LevelingInt.eachLevel(1)))
                .addModule(ToolTankHelper.TANK_HANDLER)
                .addModule(StatBoostModule.add(ToolTankHelper.CAPACITY_STAT).eachLevel(1000.0F))
        ;
    }

    @Override
    public String getName() {
        return "Tinkers' Golem Modifiers";
    }
}
