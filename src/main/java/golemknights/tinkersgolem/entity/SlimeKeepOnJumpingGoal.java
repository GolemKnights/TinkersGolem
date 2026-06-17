package golemknights.tinkersgolem.entity;

import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.Goal;

import java.util.EnumSet;

class SlimeKeepOnJumpingGoal extends Goal {
	private final SlimeGolemEntity slime;

	public SlimeKeepOnJumpingGoal(SlimeGolemEntity golem) {
		this.slime = golem;
		this.setFlags(EnumSet.of(Flag.JUMP, Flag.MOVE));
	}

	@Override
	public boolean canUse() {
		return !slime.isPassenger() && slime.isMovable();
	}

	@Override
	public void tick() {
		MoveControl movecontrol = this.slime.getMoveControl();
		if (movecontrol instanceof SlimeMoveControl slimemovecontrol) {
			slimemovecontrol.setWantedMovement(1.0F);
		}

	}
}
