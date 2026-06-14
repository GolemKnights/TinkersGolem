package golemknights.tinkersgolem.register;

import dev.xkmc.modulargolems.init.registrate.GolemTypes;
import golemknights.tinkersgolem.TinkersGolem;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.minecraftforge.event.entity.EntityAttributeModificationEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import static golemknights.tinkersgolem.TinkersGolem.MODID;

public class TGAttributes {
    public static final DeferredRegister<Attribute> ATTRIBUTES = DeferredRegister.create(ForgeRegistries.ATTRIBUTES, MODID);
    public static final RegistryObject<Attribute> MAX_OVERSLIME = ATTRIBUTES.register("max_overslime", () -> new RangedAttribute(MODID + ".max_overslime", 0, 0, Integer.MAX_VALUE).setSyncable(true));
    /**
     * Adds attributes.
     */
    public static void setupAttributes(EntityAttributeModificationEvent event) {
        if (event.getTypes().contains(GolemTypes.ENTITY_GOLEM.get())) {
            event.add(GolemTypes.ENTITY_GOLEM.get(), MAX_OVERSLIME.get());
        }if (event.getTypes().contains(GolemTypes.ENTITY_HUMANOID.get())) {
            event.add(GolemTypes.ENTITY_HUMANOID.get(), MAX_OVERSLIME.get());
        }if (event.getTypes().contains(GolemTypes.ENTITY_DOG.get())) {
            event.add(GolemTypes.ENTITY_DOG.get(), MAX_OVERSLIME.get());
        }
    }
}
