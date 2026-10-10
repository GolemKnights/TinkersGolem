package golemknights.tinkersgolem.register;

import golemknights.tinkersgolem.TinkersGolem;
import golemknights.tinkersgolem.content.item.weapon.ModifiableShoulderCannonItem;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.RegistryObject;
import slimeknights.tconstruct.common.registration.CastItemObject;
import slimeknights.tconstruct.library.materials.MaterialRegistry;
import slimeknights.tconstruct.library.materials.definition.IMaterial;
import slimeknights.tconstruct.library.materials.definition.MaterialVariant;
import slimeknights.tconstruct.library.tools.definition.ToolDefinition;
import slimeknights.tconstruct.library.tools.helper.ToolBuildHandler;
import slimeknights.tconstruct.library.tools.item.IModifiable;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;
import slimeknights.tconstruct.library.tools.part.IMaterialItem;
import slimeknights.tconstruct.tools.TinkerTools;

import java.util.function.Consumer;

import org.slf4j.IMarkerFactory;

import static golemknights.tinkersgolem.TinkersGolem.TABS;

public class TGTabs {
    public static void load() {
    }

    /*
     * public static RegistryObject<CreativeModeTab> item_tab = TABS.register(
     * "items",
     * () -> CreativeModeTab.builder()
     * .title(Component.translatable("tabs." + TinkersGolem.MODID + ".items"))
     * .icon(()->TGItems.metalGolemArmor.get(ArmorItem.Type.HELMET).getRenderTool())
     * .displayItems(
     * (displayParameters, output) ->{
     * ;
     * }
     * )
     * .withTabsBefore(TinkerTools.tabTools.getId())
     * .build()
     * );
     */
    public static RegistryObject<CreativeModeTab> tool_tab = TABS.register(
            "tools",
            () -> CreativeModeTab.builder()
                    .title(Component.translatable("tabs." + TinkersGolem.MODID + ".tools"))
                    .icon(() -> TGItems.metalGolemArmor.get(ArmorItem.Type.HELMET).getRenderTool())
                    .displayItems(
                            (displayParameters, output) -> {
                                acceptCast(output, TGItems.helmetMetalGolemPlatingCast);
                                acceptCast(output, TGItems.chestplateMetalGolemPlatingCast);
                                acceptCast(output, TGItems.leggingsMetalGolemPlatingCast);
                                acceptCast(output, TGItems.bootsMetalGolemPlatingCast);
                                TGItems.metalGolemPlating.forEach((item) -> acceptPart(output::accept, item));
                                TGItems.metalGolemArmor.forEach((item) -> acceptTool(output::accept, item));
                                acceptTool(output::accept, TGItems.metalGolemGloves.get());
                                acceptCannon(output::accept, TGItems.cannonItem.get());
                            })
                    .withTabsBefore(TinkerTools.tabTools.getId())
                    .build());

    private static void acceptCannon(Consumer<ItemStack> tab, ModifiableShoulderCannonItem cannon) {
        ToolDefinition definition = cannon.getToolDefinition();
        boolean hasMaterials = definition.hasMaterials();
        if (!definition.isDataLoaded() || (hasMaterials && !MaterialRegistry.isFullyLoaded())) {
            // not loaded? cannot properly build it
            tab.accept(new ItemStack(cannon));
            return;
        }
        var matReg = MaterialRegistry.getInstance();
        var materials = matReg.getVisibleMaterials();
        for (IMaterial core : materials.stream().filter(m -> matReg.isInTag(m.getIdentifier(), TGMaterials.cannoncore)).toList())
            for (IMaterial material : materials.stream().filter(m -> !matReg.isInTag(m.getIdentifier(), TGMaterials.cannoncore)).toList()) {
                // if we added it and we want a single material, we are done
                ItemStack tool = ToolBuildHandler.createSingleMaterial(cannon, MaterialVariant.of(material));
                if (!tool.isEmpty()) {
                    ToolStack.from(tool).replaceMaterial(0, core.getIdentifier());
                    tab.accept(tool);
                }
            }
    }

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
