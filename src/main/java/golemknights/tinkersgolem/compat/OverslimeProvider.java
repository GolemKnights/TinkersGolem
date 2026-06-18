package golemknights.tinkersgolem.compat;

import dev.xkmc.modulargolems.content.entity.common.AbstractGolemEntity;
import golemknights.tinkersgolem.TinkersGolem;
import golemknights.tinkersgolem.cap.OverslimeCap;
import golemknights.tinkersgolem.data.TGLang;
import golemknights.tinkersgolem.register.TGAttributes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.Vec2;
import snownee.jade.api.EntityAccessor;
import snownee.jade.api.IEntityComponentProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;
import snownee.jade.api.ui.IElement;
import snownee.jade.api.ui.IElementHelper;

public enum OverslimeProvider implements IEntityComponentProvider {

	INSTANCE;

	private static final ResourceLocation UID = TinkersGolem.getResource("overslime");

	@Override
	public void appendTooltip(ITooltip tooltip, EntityAccessor accessor, IPluginConfig config) {
		if (accessor.getEntity() instanceof AbstractGolemEntity<?, ?> golem) {
            var attr = golem.getAttribute(TGAttributes.MAX_OVERSLIME.get());
			if (attr == null || attr.getValue() == 0) return;
            IElementHelper elements = IElementHelper.get();
            IElement icon = elements.item(new ItemStack(Items.SLIME_BALL), 0.5F).size(new Vec2(10, 10)).translate(new Vec2(-0.75F, -1));
            tooltip.add(icon);
			int max = (int) attr.getValue();
			var val = (int) OverslimeCap.HOLDER.get(golem).overslime;
			tooltip.append(TGLang.OVERSLIME_INFO.get(val, max));
		}
	}

	@Override
	public ResourceLocation getUid() {
		return UID;
	}

}
