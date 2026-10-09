package golemknights.tinkersgolem.data;

import golemknights.tinkersgolem.content.modifiers.tool.MetalGolemAmmoFinderModule;
import golemknights.tinkersgolem.content.modifiers.tool.ShootingModule;
import golemknights.tinkersgolem.content.modifiers.tool.SpewingModule;
import golemknights.tinkersgolem.content.modifiers.tool.SpotlightModule;
import golemknights.tinkersgolem.register.TGTinkersModifiers;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.crafting.conditions.IConditionBuilder;
import slimeknights.tconstruct.library.data.tinkering.AbstractModifierProvider;
import slimeknights.tconstruct.library.json.LevelingValue;
import slimeknights.tconstruct.library.modifiers.impl.BasicModifier.TooltipDisplay;
import slimeknights.tconstruct.library.modifiers.modules.build.StatBoostModule;
import slimeknights.tconstruct.library.modifiers.util.ModifierLevelDisplay;
import slimeknights.tconstruct.library.tools.capability.fluid.ToolTankHelper;

public class TGTinkersModifierProvider extends AbstractModifierProvider implements IConditionBuilder {
    public TGTinkersModifierProvider(PackOutput packOutput) {
        super(packOutput);
    }

    @Override
    protected void addModifiers() {
        buildModifier(TGTinkersModifiers.spewing)
                .priority(120).levelDisplay(ModifierLevelDisplay.SINGLE_LEVEL)
                .addModule(new SpewingModule(LevelingValue.eachLevel(0.5f)))
                .addModule(ToolTankHelper.TANK_HANDLER)
                .addModule(StatBoostModule.add(ToolTankHelper.CAPACITY_STAT).eachLevel(1000.0F));
        buildModifier(TGTinkersModifiers.ammoFinder)
                .priority(200).tooltipDisplay(TooltipDisplay.NEVER)
                .addModule(MetalGolemAmmoFinderModule.INSTANCE);
        buildModifier(TGTinkersModifiers.spotlight)
                .priority(120).levelDisplay(ModifierLevelDisplay.NO_LEVELS)
                .addModule(SpotlightModule.INSTANCE);
        buildModifier(TGTinkersModifiers.shooting)
                .priority(120).levelDisplay(ModifierLevelDisplay.NO_LEVELS)
                .addModule(ShootingModule.INSTANCE);
    }

    @Override
    public String getName() {
        return "Tinkers' Golem Modifiers";
    }
}
