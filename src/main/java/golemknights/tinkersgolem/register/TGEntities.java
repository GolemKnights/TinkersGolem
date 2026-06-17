package golemknights.tinkersgolem.register;

import com.tterrag.registrate.util.entry.EntityEntry;
import com.tterrag.registrate.util.entry.ItemEntry;
import com.tterrag.registrate.util.entry.RegistryEntry;
import dev.xkmc.l2library.base.L2Registrate;
import dev.xkmc.modulargolems.content.core.GolemType;
import dev.xkmc.modulargolems.content.item.golem.GolemHolder;
import dev.xkmc.modulargolems.content.item.golem.GolemPart;
import dev.xkmc.modulargolems.init.ModularGolems;
import dev.xkmc.modulargolems.init.data.MGTagGen;
import dev.xkmc.modulargolems.init.registrate.GolemItems;
import dev.xkmc.modulargolems.init.registrate.GolemTypes;
import golemknights.tinkersgolem.TinkersGolem;
import golemknights.tinkersgolem.data.TGTagGen;
import golemknights.tinkersgolem.entity.*;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.ForgeMod;

@SuppressWarnings("removal")
public class TGEntities {

    public static final EntityEntry<SlimeGolemEntity> ENTITY_SLIME;
    public static final RegistryEntry<SlimeGolemType> TYPE_SLIME;
    public static final ItemEntry<GolemPart<SlimeGolemEntity, SlimeGolemPartType>> SLIME_CORE, SLIME_SHELL;
    public static final ItemEntry<GolemHolder<SlimeGolemEntity, SlimeGolemPartType>> HOLDER_SLIME;


    static {
        ENTITY_SLIME = TinkersGolem.REGISTRATE.entity("slime_golem", SlimeGolemEntity::new, MobCategory.MISC)
                .properties(e -> e.sized(2.04F, 2.04F).clientTrackingRange(20))
                .renderer(() -> SlimeGolemRenderer::new)
                .attributes(() -> Mob.createMobAttributes()
                        .add(Attributes.MAX_HEALTH, 20)
                        .add(Attributes.ATTACK_DAMAGE, 4)
                        //可能是因为寻路问题，史莱姆傀儡移动速度异常缓慢，为此适当提高了基础移速
                        .add(Attributes.MOVEMENT_SPEED, 0.8D)
                        .add(Attributes.KNOCKBACK_RESISTANCE, 0)
                        .add(Attributes.ATTACK_SPEED, 4)
                        .add(Attributes.ATTACK_KNOCKBACK, 1)
                        .add(ForgeMod.ENTITY_REACH.get(), 1)
                        .add(Attributes.FOLLOW_RANGE, 35)
                        .add(GolemTypes.GOLEM_REGEN.get())
                        .add(GolemTypes.GOLEM_SIZE.get(), 3)
                        .add(GolemTypes.DYNAMIC_REDUCTION.get())
                        .add(TGAttributes.MAX_OVERSLIME.get())
                        .add(TGAttributes.OVERSLIME_RECOVERY.get())
                ).tag(MGTagGen.GOLEM_FRIENDLY).register();

        var cls = new L2Registrate.RegistryInstance<GolemType<?, ?>>(() -> null, ResourceKey.createRegistryKey(new ResourceLocation(ModularGolems.MODID, "golem_type")));

        TYPE_SLIME = TinkersGolem.REGISTRATE.generic(cls, "slime_golem",
                        () -> new SlimeGolemType(ENTITY_SLIME, () -> SlimeGolemModel::new))
                .defaultLang().register();


        HOLDER_SLIME = TinkersGolem.REGISTRATE.item("slime_golem_holder", p ->
                        new GolemHolder<>(p.fireResistant(), TYPE_SLIME))
                .model((ctx, pvd) -> pvd.getBuilder(ctx.getName()).parent(new ModelFile.UncheckedModelFile("builtin/entity"))
                        .texture("particle", "minecraft:block/clay"))
                .transform(e -> e.tab(GolemItems.GOLEMS.getKey(), x -> e.getEntry().fillItemCategory(x)))
                .tag(MGTagGen.GOLEM_HOLDERS).defaultLang().register();

        SLIME_CORE = TinkersGolem.REGISTRATE.item("slime_golem_core", p ->
                        new GolemPart<>(p.fireResistant(), TYPE_SLIME, SlimeGolemPartType.CORE, 9))
                .model((ctx, pvd) -> pvd.getBuilder(ctx.getName()).parent(new ModelFile.UncheckedModelFile("builtin/entity"))
                        .texture("particle", "minecraft:block/clay"))
                .tab(GolemItems.ITEMS.getKey())
                .transform(e -> e.tab(GolemItems.GOLEMS.getKey(), x -> e.getEntry().fillItemCategory(x)))
                .tag(MGTagGen.GOLEM_PARTS, TGTagGen.SLIME_PART).defaultLang().register();

        SLIME_SHELL = TinkersGolem.REGISTRATE.item("slime_golem_shell", p ->
                        new GolemPart<>(p.fireResistant(), TYPE_SLIME, SlimeGolemPartType.SHELL, 9))
                .model((ctx, pvd) -> pvd.getBuilder(ctx.getName()).parent(new ModelFile.UncheckedModelFile("builtin/entity"))
                        .texture("particle", "minecraft:block/clay"))
                .tab(GolemItems.ITEMS.getKey())
                .transform(e -> e.tab(GolemItems.GOLEMS.getKey(), x -> e.getEntry().fillItemCategory(x)))
                .tag(MGTagGen.GOLEM_PARTS, TGTagGen.SLIME_PART).defaultLang().register();
    }

    public static void load() {
    }
}