package golemknights.tinkersgolem.content.entity;

import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.Goal;

import java.util.EnumSet;

class SlimeRandomDirectionGoal extends Goal {
    private final SlimeGolemEntity slime;
    private float chosenDegrees;
    private int nextRandomizeTime;

    public SlimeRandomDirectionGoal(SlimeGolemEntity p_33679_) {
        this.slime = p_33679_;
        this.setFlags(EnumSet.of(Flag.LOOK));
    }

    public boolean canUse() {
        return this.slime.getTarget() == null && (this.slime.onGround() || this.slime.isInWater() || this.slime.isInLava() || this.slime.hasEffect(MobEffects.LEVITATION)) && this.slime.getMoveControl() instanceof SlimeMoveControl;
    }

    public void tick() {
        if (--this.nextRandomizeTime <= 0) {
            this.nextRandomizeTime = this.adjustedTickDelay(40 + this.slime.getRandom().nextInt(60));
            this.chosenDegrees = (float)this.slime.getRandom().nextInt(360);
        }

        MoveControl movecontrol = this.slime.getMoveControl();
        if (movecontrol instanceof SlimeMoveControl slimemovecontrol) {
            slimemovecontrol.setDirection(this.chosenDegrees, false);
        }

    }
}
