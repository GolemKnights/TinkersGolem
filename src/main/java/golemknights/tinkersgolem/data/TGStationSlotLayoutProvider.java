package golemknights.tinkersgolem.data;

import dev.xkmc.modulargolems.init.registrate.GolemItems;
import golemknights.tinkersgolem.TinkersGolem;
import golemknights.tinkersgolem.register.TGItems;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import slimeknights.tconstruct.library.data.tinkering.AbstractStationSlotLayoutProvider;
import slimeknights.tconstruct.library.tools.layout.Patterns;
import slimeknights.tconstruct.library.tools.part.ToolPartItem;
import slimeknights.tconstruct.tools.TinkerToolParts;

import java.util.ArrayList;
import java.util.List;

public class TGStationSlotLayoutProvider extends AbstractStationSlotLayoutProvider {
    public TGStationSlotLayoutProvider(PackOutput packOutput) {
        super(packOutput);
    }

    @Override
    protected void addLayouts() {
        List<ToolPartItem> platings = new ArrayList<>(TinkerToolParts.plating.values());
        platings.add(TinkerToolParts.largePlate.get());
        this.definePattern(TGItems.METAL_GOLEM_ARMOR_PATTERN)
                .sortIndex(17)
                .translationKey(TinkersGolem.makeTranslationKey("gui", "metal_golem_armor"))
                .addInputPattern(
                        TGItems.METAL_GOLEM_PLATING_PATTERN,
                        33, 53,
                        Ingredient.of(TGItems.metalGolemPlating.values().toArray(new Item[0]))
                ).addInputPattern(
                        Patterns.PLATING,
                        23, 29,
                        Ingredient.of(platings.toArray(new Item[0]))
                ).addInputPattern(
                        Patterns.PLATING,
                        43, 29,
                        Ingredient.of(platings.toArray(new Item[0]))
                ).addInputItem(
                        GolemItems.GOLEM_TEMPLATE.get(),
                        13, 49
                ).addInputItem(
                        GolemItems.GOLEM_TEMPLATE.get(),
                        53, 49
                ).build();
        this.defineModifiable(TGItems.swordItem)
                .sortIndex(18)
                .addInputItem(
                        TinkerToolParts.broadBlade,
                        30, 21
                ).addInputItem(
                        TinkerToolParts.toughHandle,
                        7, 62
                ).addInputItem(
                        TinkerToolParts.largePlate,
                        25, 46
                ).addInputItem(
                        TinkerToolParts.broadBlade,
                        50, 41
                ).addInputItem(
                        GolemItems.GOLEM_TEMPLATE.get(),
                        83, 20
                ).build();
        this.defineModifiable(TGItems.axeItem)
                .sortIndex(19)
                .addInputItem(
                        TinkerToolParts.broadAxeHead,
                        44, 29
                ).addInputItem(
                        TinkerToolParts.toughHandle,
                        21, 52
                ).addInputItem(
                        TinkerToolParts.broadBlade,
                        25, 20
                ).addInputItem(
                        TinkerToolParts.toughBinding,
                        50, 48
                ).addInputItem(
                        GolemItems.GOLEM_TEMPLATE.get(),
                        83, 20
                ).build();
        this.defineModifiable(TGItems.spearItem)
                .sortIndex(20)
                .addInputItem(
                        TinkerToolParts.broadBlade,
                        45, 26
                ).addInputItem(
                        TinkerToolParts.toughHandle,
                        25, 46
                ).addInputItem(
                        TinkerToolParts.toughBinding,
                        45, 46
                ).addInputItem(
                        TinkerToolParts.toughHandle,
                        7, 62
                ).addInputItem(
                        GolemItems.GOLEM_TEMPLATE.get(),
                        83, 20
                ).build();
    }

    @Override
    public String getName() {
        return "Tinkers' Golem Tinker Station Slot Layouts";
    }
}
