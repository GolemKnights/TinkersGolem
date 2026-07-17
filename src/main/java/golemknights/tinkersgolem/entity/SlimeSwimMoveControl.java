package golemknights.tinkersgolem.entity;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;

public class SlimeSwimMoveControl extends SlimeMoveControl{

	public SlimeSwimMoveControl(SlimeGolemEntity golem) {
		super(golem);
	}

	@Override
	public void tick() {
		LivingEntity livingentity = this.slime.getTarget();
		if (this.slime.isInWater()) {
			if (livingentity != null && livingentity.getY() > this.slime.getY()) {
				this.slime.setDeltaMovement(this.slime.getDeltaMovement().add(0.0D, 0.002D, 0.0D));
			}
			if (this.operation != MoveControl.Operation.MOVE_TO || this.slime.getNavigation().isDone()) {
				this.slime.setSpeed(0.0F);
				return;
			}

			double d0 = this.wantedX - this.slime.getX();
			double d1 = this.wantedY - this.slime.getY();
			double d2 = this.wantedZ - this.slime.getZ();
			double d3 = Math.sqrt(d0 * d0 + d1 * d1 + d2 * d2);
			d1 /= d3;
			float f = (float) (Mth.atan2(d2, d0) * (double) (180F / (float) Math.PI)) - 90.0F;
			this.slime.setYRot(this.rotlerp(this.slime.getYRot(), f, 90.0F));
			this.slime.yBodyRot = this.slime.getYRot();
			float f1 = (float) (this.speedModifier * this.slime.getAttributeValue(Attributes.MOVEMENT_SPEED));
			float f2 = Mth.lerp(0.125F, this.slime.getSpeed(), f1);
			this.slime.setSpeed(f2);
			this.slime.setDeltaMovement(this.slime.getDeltaMovement().add((double) f2 * d0 * 0.005D, (double) f2 * d1 * 0.1D, (double) f2 * d2 * 0.005D));
			return;
		}

		super.tick();
	}
}
