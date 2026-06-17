package golemknights.tinkersgolem.register;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.RegistryObject;
import slimeknights.tconstruct.common.registration.CastItemObject;
import slimeknights.tconstruct.library.tools.helper.ToolBuildHandler;
import slimeknights.tconstruct.library.tools.item.IModifiable;
import slimeknights.tconstruct.library.tools.part.IMaterialItem;
import slimeknights.tconstruct.tools.TinkerTools;

import java.util.function.Consumer;

import static golemknights.tinkersgolem.TinkersGolem.TABS;

public class TGTabs {
    public static RegistryObject<CreativeModeTab> item_tab = TABS.register(
            "items",
            () -> CreativeModeTab.builder()
                    .title(Component.translatable("tabs.tinkerdgolem.items"))
                    .icon(()->TGItems.metalGolemArmor.get(ArmorItem.Type.HELMET).getRenderTool())
                    .displayItems(
                            (displayParameters, output) ->{
                                ;
                            }
                    )
                    .withTabsBefore(TinkerTools.tabTools.getId())
                    .build()
    );
    public static RegistryObject<CreativeModeTab> tool_tab = TABS.register(
            "tools",
            () -> CreativeModeTab.builder()
                    .title(Component.translatable("tabs.tinkerdgolem.tools"))
                    .icon(()->TGItems.metalGolemArmor.get(ArmorItem.Type.HELMET).getRenderTool())
                    .displayItems(
                            (displayParameters, output) -> {
                                acceptCast(output, TGItems.helmetMetalGolemPlatingCast);
                                acceptCast(output, TGItems.chestplateMetalGolemPlatingCast);
                                acceptCast(output, TGItems.leggingsMetalGolemPlatingCast);
                                acceptCast(output, TGItems.bootsMetalGolemPlatingCast);
                                TGItems.metal_golem_plating.forEach((item) -> acceptPart(output::accept, item));
                                TGItems.metalGolemArmor.forEach((item) -> acceptTool(output::accept, item));
                            }
                    )
                    .withTabsBefore(TinkerTools.tabTools.getId())
                    .build()
    );
    private static void acceptTool(Consumer<ItemStack> output, IModifiable tool) {
        ToolBuildHandler.addVariants(output, tool, "");
    }
    private static void acceptPart(Consumer<ItemStack> output, IMaterialItem item) {
        item.addVariants(output, "");
    }
    private static void acceptCast(CreativeModeTab.Output output, CastItemObject cast) {
        output.accept(cast.get().getDefaultInstance());
        output.accept(cast.getSand().getDefaultInstance());
        output.accept(cast.getRedSand().getDefaultInstance());
    }
}
