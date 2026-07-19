package golemknights.tinkersgolem.entity;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.control.LookControl;

public class SlimeLookControl extends LookControl {

	public SlimeLookControl(Mob mob) {
		super(mob);
	}

	@Override
	protected void clampHeadRotationToBody() {
		if (!mob.getNavigation().isDone()) return;
		mob.setYRot(mob.yHeadRot);
	}

}
