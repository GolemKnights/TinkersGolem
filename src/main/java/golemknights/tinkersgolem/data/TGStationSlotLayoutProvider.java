package golemknights.tinkersgolem.data;

import golemknights.tinkersgolem.TinkersGolem;
import golemknights.tinkersgolem.register.TGItems;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import slimeknights.tconstruct.library.data.tinkering.AbstractStationSlotLayoutProvider;
import slimeknights.tconstruct.library.tools.layout.Patterns;
import slimeknights.tconstruct.tools.TinkerToolParts;

public class TGStationSlotLayoutProvider extends AbstractStationSlotLayoutProvider {
    public TGStationSlotLayoutProvider(PackOutput packOutput) {
        super(packOutput);
    }

    @Override
    protected void addLayouts() {
        this.definePattern(TGItems.METAL_GOLEM_ARMOR)
                .sortIndex(17)
                .translationKey(TinkersGolem.makeTranslationKey("gui", "metal_golem_armor"))
                .addInputPattern(
                        TGItems.METAL_GOLEM_PLATING,
                        33, 29,
                        Ingredient.of(TGItems.metal_golem_plating.values().toArray(new Item[0]))
                ).addInputPattern(
                        Patterns.PLATING,
                        23, 49,
                        Ingredient.of(TinkerToolParts.plating.values().toArray(new Item[0]))
                ).addInputPattern(
                        Patterns.PLATING,
                        43, 49,
                        Ingredient.of(TinkerToolParts.plating.values().toArray(new Item[0]))
                ).build();
    }

    @Override
    public String getName() {
        return "Tinkers' Golem Tinker Station Slot Layouts";
    }
}
