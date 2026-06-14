package golemknights.tinkersgolem.register;

import net.minecraft.core.registries.Registries;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.RegisterEvent;

public class TGModifiers {
    @SubscribeEvent
    void registerSerializers(RegisterEvent event) {
        if (event.getRegistryKey() == Registries.RECIPE_SERIALIZER) {

        }
    }

    public static void registers(IEventBus bus) {
        bus.register(new TGModifiers());
    }
}
