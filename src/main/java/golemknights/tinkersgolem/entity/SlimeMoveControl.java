package golemknights.tinkersgolem.entity;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;

class SlimeMoveControl extends MoveControl {
	private int jumpDelay;
	private final SlimeGolemEntity slime;
	//private boolean isAggressive;

	public SlimeMoveControl(SlimeGolemEntity golem) {
		super(golem);
		this.slime = golem;
	}

	public void setWantedMovement(double p_33671_) {
		this.speedModifier = p_33671_;
		this.operation = Operation.MOVE_TO;
	}

	@Override
	public void tick() {
		if (this.operation == Operation.MOVE_TO) {
			double d0 = this.wantedX - this.mob.getX();
			double d1 = this.wantedZ - this.mob.getZ();
			float targetYRot = (float) (Mth.atan2(d1, d0) * (180F / Math.PI)) - 90.0F;
			this.mob.setYRot(this.rotlerp(this.mob.getYRot(), targetYRot, 90.0F));
		}
		this.mob.yHeadRot = this.mob.getYRot();
		this.mob.yBodyRot = this.mob.getYRot();

		if (this.operation != Operation.MOVE_TO) {
			this.mob.setZza(0.0F);
			return;
		}
		if (this.mob.onGround()) {
			this.mob.setSpeed((float) (this.speedModifier * this.mob.getAttributeValue(Attributes.MOVEMENT_SPEED)));
			if (this.jumpDelay-- <= 0) {
				this.jumpDelay = this.slime.getJumpDelay();
				this.slime.getJumpControl().jump();
				if (this.slime.doPlayJumpSound()) {
					this.slime.playSound(this.slime.getJumpSound(), this.slime.getSoundVolume(), this.slime.getSoundPitch());
				}
			} else {
				this.slime.xxa = 0.0F;
				this.slime.zza = 0.0F;
				this.mob.setSpeed(0.0F);
			}
		} else {
			this.mob.setSpeed((float) (this.speedModifier * this.mob.getAttributeValue(Attributes.MOVEMENT_SPEED)));
		}
	}
}
