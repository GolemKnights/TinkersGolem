package golemknights.tinkersgolem.compat;

import dev.xkmc.modulargolems.content.entity.common.AbstractGolemEntity;
import snownee.jade.api.IWailaClientRegistration;
import snownee.jade.api.IWailaCommonRegistration;
import snownee.jade.api.IWailaPlugin;
import snownee.jade.api.WailaPlugin;

@WailaPlugin
public class JadeTGPlugin implements IWailaPlugin {

	@Override
	public void registerClient(IWailaClientRegistration registration) {
		registration.registerEntityComponent(OverslimeProvider.INSTANCE, AbstractGolemEntity.class);
	}

	@Override
	public void register(IWailaCommonRegistration registration) {
	}

}
