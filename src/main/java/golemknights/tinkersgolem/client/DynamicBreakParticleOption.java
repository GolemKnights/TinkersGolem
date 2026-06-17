package golemknights.tinkersgolem.client;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import golemknights.tinkersgolem.register.TGParticles;
import net.minecraft.commands.arguments.item.ItemParser;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

public class DynamicBreakParticleOption implements ParticleOptions {

    public static final Deserializer<DynamicBreakParticleOption> DESERIALIZER = new Deserializer<>() {
        @Override
        public @NotNull DynamicBreakParticleOption fromCommand(@NotNull ParticleType<DynamicBreakParticleOption> type, StringReader reader) throws CommandSyntaxException {
            reader.expect(' ');
            return new DynamicBreakParticleOption(ItemParser.parseForItem(BuiltInRegistries.ITEM.asLookup(), reader).item().get().getDefaultInstance());
        }

        @Override
        public @NotNull DynamicBreakParticleOption fromNetwork(@NotNull ParticleType<DynamicBreakParticleOption> type, FriendlyByteBuf buf) {
            return new DynamicBreakParticleOption(buf.readItem());
        }
    };


    public static Codec<DynamicBreakParticleOption> codec() {
        return RecordCodecBuilder.create(inst -> inst.group(
                ItemStack.CODEC.fieldOf("item").forGetter(p -> p.stack)
        ).apply(inst, DynamicBreakParticleOption::new));
    }

    private final ItemStack stack;
    private final ParticleType<DynamicBreakParticleOption> type;

    public DynamicBreakParticleOption(ItemStack stack) {
        this.stack = stack.copy();
        this.type = TGParticles.DynamicItemBreak.get();
    }

    public ItemStack getItemStack() {
        return stack;
    }

    @Override
    public @NotNull ParticleType<?> getType() {
        return type;
    }

    @Override
    public void writeToNetwork(FriendlyByteBuf buf) {
        buf.writeItem(stack);
    }

    @Override
    public @NotNull String writeToString() {
        return getType() + " " + stack;
    }

    @Override
    public String toString() {
        return "DynamicBreakParticle{item=" + ForgeRegistries.ITEMS.getKey(stack.getItem()) + "}";
    }
}
