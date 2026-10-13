package golemknights.tinkersgolem.data;

import com.google.gson.JsonObject;
import golemknights.tinkersgolem.register.TGItems;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.ExistingFileHelper;
import slimeknights.tconstruct.TConstruct;
import slimeknights.tconstruct.library.data.AbstractToolItemModelProvider;

import java.io.IOException;

import static golemknights.tinkersgolem.TinkersGolem.MODID;
import static golemknights.tinkersgolem.register.TGItems.METAL_GOLEM;

public class TGToolItemModelProvider extends AbstractToolItemModelProvider {
    public TGToolItemModelProvider(PackOutput packOutput, ExistingFileHelper existingFileHelper) {
        super(packOutput, existingFileHelper, MODID);
    }

    @Override
    protected void addModels() throws IOException {
        JsonObject toolBlocking = this.readJson(TConstruct.getResource("base/tool_blocking"));
        this.armor(METAL_GOLEM, TGItems.metalGolemArmor,
                "huge_plating", "small_plating1", "small_plating2"
        );
        this.armor(METAL_GOLEM + "/gloves", TGItems.metalGolemGloves,
                "huge_plating", "small_plating1", "small_plating2"
        );
        this.tool(TGItems.SWORD_TOOL, toolBlocking);
        this.tool(TGItems.AXE_TOOL, toolBlocking);
        this.tool(TGItems.SPEAR_TOOL, toolBlocking);
    }

    @Override
    public String getName() {
        return "Tinkers Golem Tool Item Model Provider";
    }
}
