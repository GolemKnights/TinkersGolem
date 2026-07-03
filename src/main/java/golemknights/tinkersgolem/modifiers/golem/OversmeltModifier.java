package golemknights.tinkersgolem.modifiers.golem;

import dev.xkmc.modulargolems.content.core.StatFilterType;
import dev.xkmc.modulargolems.content.entity.common.AbstractGolemEntity;
import dev.xkmc.modulargolems.content.item.golem.GolemPart;
import dev.xkmc.modulargolems.content.modifier.base.GolemModifier;
import golemknights.tinkersgolem.data.TGConfig;
import golemknights.tinkersgolem.events.GolemOverslimeEvents;
import golemknights.tinkersgolem.register.TGEntities;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

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
		if (golem.tickCount % 20 == 0) {
			BlockState blockstate = golem.level().getBlockState(golem.blockPosition());
			if (blockstate.is(BlockTags.STRIDER_WARM_BLOCKS)) {
				GolemOverslimeEvents.addOverslime(golem, level * TGConfig.COMMON.oversmeltRate.get().floatValue());
			}
		}
	}

	@Override
	public List<MutableComponent> getDetail(int v) {
		return List.of(Component.translatable(this.getDescriptionId() + ".desc", TGConfig.COMMON.oversmeltRate.get().floatValue() * v).withStyle(ChatFormatting.GREEN));
	}

}
