package golemknights.tinkersgolem.register;

import com.mojang.serialization.Codec;
import golemknights.tinkersgolem.client.DynamicBreakParticleOption;
import net.minecraft.core.particles.ParticleType;
import net.minecraftforge.registries.RegistryObject;

import static golemknights.tinkersgolem.TinkersGolem.PARTICLES;

public class TGParticles {

     public static final RegistryObject<ParticleType<DynamicBreakParticleOption>> DynamicItemBreak =
            PARTICLES.register("dynamic_item_break", () -> new ParticleType<>(false, DynamicBreakParticleOption.DESERIALIZER) {
                @Override
                public Codec<DynamicBreakParticleOption> codec() {
                    return DynamicBreakParticleOption.codec();
                }
            });

    public static void load() {
    }
}
