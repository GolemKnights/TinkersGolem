package golemknights.tinkersgolem.client;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.BreakingItemParticle;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

@OnlyIn(Dist.CLIENT)
public class DynamicItemBreakParticle extends BreakingItemParticle {
    public DynamicItemBreakParticle(ClientLevel level, double x, double y, double z, ItemStack stack) {
        super(level, x, y, z, stack);
    }

    public DynamicItemBreakParticle(ClientLevel level, double x, double y, double z,
                                    double xSpeed, double ySpeed, double zSpeed, ItemStack stack) {
        super(level, x, y, z, xSpeed, ySpeed, zSpeed, stack);
    }

    static class Provider implements ParticleProvider<DynamicBreakParticleOption> {
        @Override
        public Particle createParticle(DynamicBreakParticleOption option, @NotNull ClientLevel level,
                                       double x, double y, double z,
                                       double xSpeed, double ySpeed, double zSpeed) {
            ItemStack stack = option.getItemStack();
            if (stack.isEmpty()) return null;
            return new DynamicItemBreakParticle(level, x, y, z, xSpeed, ySpeed, zSpeed, stack);
        }
    }
}
