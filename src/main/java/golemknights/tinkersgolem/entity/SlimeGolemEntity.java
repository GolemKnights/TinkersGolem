package golemknights.tinkersgolem.entity;

import dev.xkmc.l2serial.serialization.SerialClass;
import dev.xkmc.modulargolems.content.config.GolemMaterial;
import dev.xkmc.modulargolems.content.config.GolemMaterialConfig;
import dev.xkmc.modulargolems.content.core.GolemType;
import dev.xkmc.modulargolems.content.entity.common.AbstractGolemEntity;
import dev.xkmc.modulargolems.content.item.upgrade.IUpgradeItem;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.util.random.SimpleWeightedRandomList;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.*;


@SerialClass
public class SlimeGolemEntity extends AbstractGolemEntity<SlimeGolemEntity, SlimeGolemPartType> {

	private static final EntityDataAccessor<Integer> ID_SIZE = SynchedEntityData.defineId(SlimeGolemEntity.class, EntityDataSerializers.INT);

	public float targetSquish;
	public float squish;
	public float oSquish;
	private boolean wasOnGround;

	public SlimeGolemEntity(EntityType<SlimeGolemEntity> type, Level level) {
		super(type, level);
		this.fixupDimensions();
		this.moveControl = new SlimeMoveControl(this);
	}

	@Override
	public void onCreate(ArrayList<GolemMaterial> materials, ArrayList<IUpgradeItem> upgrades, @Nullable UUID owner) {
		super.onCreate(materials, upgrades, owner);
		setSize(4, true);
	}

	@Override
	protected void defineSynchedData() {
		super.defineSynchedData();
		this.entityData.define(ID_SIZE, 1);
	}

	@Override
	protected void registerGoals() {
		super.registerGoals();
		this.goalSelector.addGoal(2, meleeGoal);
		this.goalSelector.addGoal(9, new SlimeKeepOnJumpingGoal(this));
	}

	public void setSize(int size, boolean resetHealth) {
		int i = Mth.clamp(size, 1, 127);
		this.entityData.set(ID_SIZE, i);
		this.reapplyPosition();
		this.refreshDimensions();
		this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(i * i);
		this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.2F + 0.1F * (float) i);
		this.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(i);
		if (resetHealth) {
			this.setHealth(this.getMaxHealth());
		}

		this.xpReward = i;
	}

	public int getSize() {
		return this.entityData.get(ID_SIZE);
	}

	public void addAdditionalSaveData(CompoundTag p_33619_) {
		super.addAdditionalSaveData(p_33619_);
		p_33619_.putInt("Size", this.getSize() - 1);
		p_33619_.putBoolean("wasOnGround", this.wasOnGround);
	}

	public void readAdditionalSaveData(CompoundTag p_33607_) {
		this.setSize(p_33607_.getInt("Size") + 1, false);
		super.readAdditionalSaveData(p_33607_);
		this.wasOnGround = p_33607_.getBoolean("wasOnGround");
	}

	public boolean isTiny() {
		return this.getSize() <= 1;
	}

	protected ParticleOptions getParticleType() {
		return ParticleTypes.ITEM_SLIME;
	}

	public void tick() {
		this.squish += (this.targetSquish - this.squish) * 0.5F;
		this.oSquish = this.squish;
		super.tick();
		if (this.onGround() && !this.wasOnGround) {
			int i = this.getSize();
			for (int j = 0; j < i * 8; ++j) {
				float f = this.random.nextFloat() * ((float) Math.PI * 2F);
				float f1 = this.random.nextFloat() * 0.5F + 0.5F;
				float f2 = Mth.sin(f) * (float) i * 0.5F * f1;
				float f3 = Mth.cos(f) * (float) i * 0.5F * f1;
				this.level().addParticle(this.getParticleType(), this.getX() + (double) f2, this.getY(), this.getZ() + (double) f3, 0.0F, 0.0F, 0.0F);
			}

			this.playSound(this.getSquishSound(), this.getSoundVolume(), ((this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F) / 0.8F);
			this.targetSquish = -0.5F;
		} else if (!this.onGround() && this.wasOnGround) {
			this.targetSquish = 1.0F;
		}

		this.wasOnGround = this.onGround();
		this.decreaseSquish();
	}

	protected void decreaseSquish() {
		this.targetSquish *= 0.6F;
	}

	protected int getJumpDelay() {
		return this.random.nextInt(20) + 10;
	}

	@Override
	protected float getSoundVolume() {
		return 0.4F * (float) this.getSize();
	}

	@Override
	public int getMaxHeadXRot() {
		return 0;
	}

	@Override
	protected void jumpFromGround() {
		Vec3 vec3 = this.getDeltaMovement();
		this.setDeltaMovement(vec3.x, this.getJumpPower(), vec3.z);
		this.hasImpulse = true;
	}

	@Nullable
	@Override
	public SpawnGroupData finalizeSpawn(ServerLevelAccessor p_33601_, DifficultyInstance p_33602_, MobSpawnType p_33603_, @Nullable SpawnGroupData p_33604_, @Nullable CompoundTag p_33605_) {
		this.setSize(4, true);
		return super.finalizeSpawn(p_33601_, p_33602_, p_33603_, p_33604_, p_33605_);
	}

	@Override
	public void onSyncedDataUpdated(EntityDataAccessor<?> p_33609_) {
		if (ID_SIZE.equals(p_33609_)) {
			this.refreshDimensions();
			this.setYRot(this.yHeadRot);
			this.yBodyRot = this.yHeadRot;
			if (this.isInWater() && this.random.nextInt(20) == 0) {
				this.doWaterSplashEffect();
			}
		}

		super.onSyncedDataUpdated(p_33609_);
	}


	@Override
	protected void dropCustomDeathLoot(DamageSource source, int i, boolean b) {
		if (!isTiny()) return;
		Map<Item, Integer> drop = new HashMap<>();
		for (GolemMaterial mat : getMaterials()) {
			Item item = GolemMaterialConfig.get().getCraftIngredient(mat.id()).getItems()[0].getItem();
			drop.compute(item, (e, old) -> (old == null ? 0 : old) + 1);
		}
		drop.forEach((k, v) -> spawnAtLocation(new ItemStack(k, v)));
		if (!isHostile()) {
			for (EquipmentSlot slot : EquipmentSlot.values()) {
				dropSlot(slot, true);
			}
		}
	}

	protected List<ArrayList<IUpgradeItem>> splitUpgrades(int n) {
		var upgrades = getUpgrades();
		var ans = new ArrayList<ArrayList<IUpgradeItem>>();
		for (int i = 0; i < n; i++)
			ans.add(new ArrayList<>());
		var holder = GolemType.getGolemHolder(getType());
		for (var e : upgrades) {
			if (!(e instanceof IUpgradeItem item)) {
				spawnAtLocation(e.getDefaultInstance());
				continue;
			}
			var builder = new SimpleWeightedRandomList.Builder<Integer>();
			for (int i = 0; i < n; i++) {
				var copy = new ArrayList<>(ans.get(i));
				copy.add(item);
				int rem = holder.getRemaining(getMaterials(), copy);
				if (rem >= 0)
					builder.add(i, rem + 1);
			}
			var rand = builder.build();

			var index = rand.getRandom(getRandom());
			if (index.isEmpty()) {
				spawnAtLocation(e.getDefaultInstance());
				continue;
			}
			ans.get(index.get().getData()).add(item);
		}
		return ans;
	}

	@Override
	public void remove(Entity.RemovalReason reason) {
		int size = this.getSize();
		if (!this.level().isClientSide && size > 1 && this.isDeadOrDying()) {
			Component component = this.getCustomName();
			boolean flag = this.isNoAi();
			float offset = (float) size / 4.0F;
			int nextSize = size / 2;
			int n = 2;
			var split = splitUpgrades(n);
			for (int i = 0; i < n; ++i) {
				float f1 = ((i & 1) - 0.5F) * offset;
				float f2 = ((i >> 1) - 0.5F) * offset;
				SlimeGolemEntity slime = this.getType().create(this.level());
				if (slime == null) continue;
				slime.onCreate(getMaterials(), split.get(i), getOwnerUUID());
				slime.setCustomName(component);
				slime.setNoAi(flag);
				slime.setInvulnerable(this.isInvulnerable());
				slime.setSize(nextSize, true);
				slime.moveTo(this.getX() + (double) f1, this.getY() + (double) 0.5F, this.getZ() + (double) f2, this.random.nextFloat() * 360.0F, 0.0F);
				this.level().addFreshEntity(slime);
			}
		}

		super.remove(reason);
	}

	protected float getSoundPitch() {
		float f = this.isTiny() ? 1.4F : 0.8F;
		return ((this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F) * f;
	}

	protected SoundEvent getJumpSound() {
		return this.isTiny() ? SoundEvents.SLIME_JUMP_SMALL : SoundEvents.SLIME_JUMP;
	}

	@Override
	public EntityDimensions getDimensions(Pose pose) {
		return super.getDimensions(pose).scale(0.255F * (float) this.getSize());
	}


	protected boolean doPlayJumpSound() {
		return this.getSize() > 0;
	}

	@Override
	protected float getStandingEyeHeight(Pose pose, EntityDimensions dimensions) {
		return 0.625F * dimensions.height;
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource source) {
		return this.isTiny() ? SoundEvents.SLIME_HURT_SMALL : SoundEvents.SLIME_HURT;
	}

	@Override
	protected SoundEvent getDeathSound() {
		return this.isTiny() ? SoundEvents.SLIME_DEATH_SMALL : SoundEvents.SLIME_DEATH;
	}

	protected SoundEvent getSquishSound() {
		return this.isTiny() ? SoundEvents.SLIME_SQUISH_SMALL : SoundEvents.SLIME_SQUISH;
	}

	static class SlimeMoveControl extends MoveControl {
		private int jumpDelay;
		private final SlimeGolemEntity slime;
		private boolean isAggressive;

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
			// 暂不清楚作用，先注释掉
			// this.operation = Operation.WAIT;
			if (this.mob.onGround()) {
				this.mob.setSpeed((float) (this.speedModifier * this.mob.getAttributeValue(Attributes.MOVEMENT_SPEED)));
				if (this.jumpDelay-- <= 0) {
					this.jumpDelay = this.slime.getJumpDelay();
					if (this.isAggressive) {
						this.jumpDelay /= 3;
					}
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

	static class SlimeKeepOnJumpingGoal extends Goal {
		private final SlimeGolemEntity slime;

		public SlimeKeepOnJumpingGoal(SlimeGolemEntity golem) {
			this.slime = golem;
			this.setFlags(EnumSet.of(Flag.JUMP, Flag.MOVE));
		}

		@Override
		public boolean canUse() {
			return !this.slime.isPassenger();
		}

		@Override
		public void tick() {
			MoveControl movecontrol = this.slime.getMoveControl();
			if (movecontrol instanceof SlimeMoveControl slimemovecontrol) {
				slimemovecontrol.setWantedMovement(1.0F);
			}

		}
	}
}
