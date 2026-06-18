package golemknights.tinkersgolem.modifiers.golem;

import dev.xkmc.modulargolems.content.core.StatFilterType;
import dev.xkmc.modulargolems.content.entity.common.AbstractGolemEntity;
import dev.xkmc.modulargolems.content.item.golem.GolemPart;
import dev.xkmc.modulargolems.content.modifier.base.GolemModifier;
import golemknights.tinkersgolem.events.GolemOverslimeEvents;
import golemknights.tinkersgolem.register.TGEntities;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.state.BlockState;

public class OversmeltModifier extends GolemModifier {
    public OversmeltModifier() {
        super(StatFilterType.MASS, 5);
    }

    @Override
    public boolean canExistOn(GolemPart<?, ?> part) {
        return part.getEntityType() != TGEntities.TYPE_SLIME.get();
    }

    @Override
    public void onAiStep(AbstractGolemEntity<?, ?> golem, int level) {
        if (golem.tickCount % 20 == 0 ) {
            BlockState blockstate = golem.level().getBlockState(golem.blockPosition());
            if (golem.getRandom().nextFloat() < 0.5 && blockstate.is(BlockTags.STRIDER_WARM_BLOCKS)) {
                GolemOverslimeEvents.addOverslime(golem, level);
            }
        }
    }
}
