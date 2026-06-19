package golemknights.tinkersgolem.data;

import golemknights.tinkersgolem.register.TGItems;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.ExistingFileHelper;
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
        this.armor(METAL_GOLEM, TGItems.metalGolemArmor,
                "huge_plating", "small_plating1", "small_plating2"
        );
    }

    @Override
    public String getName() {
        return "Tinkers Golem Tool Item Model Provider";
    }
}
