package golemknights.tinkersgolem.entity;

import dev.xkmc.l2serial.serialization.SerialClass;
import dev.xkmc.modulargolems.content.config.GolemMaterial;
import dev.xkmc.modulargolems.content.config.GolemMaterialConfig;
import dev.xkmc.modulargolems.content.core.GolemType;
import dev.xkmc.modulargolems.content.entity.common.AbstractGolemEntity;
import dev.xkmc.modulargolems.content.item.upgrade.IUpgradeItem;
import dev.xkmc.modulargolems.init.advancement.GolemTriggers;
import dev.xkmc.modulargolems.init.data.MGConfig;
import golemknights.tinkersgolem.client.DynamicBreakParticleOption;
import golemknights.tinkersgolem.events.GolemOverslimeEvents;
import golemknights.tinkersgolem.register.TGAttributes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.util.random.SimpleWeightedRandomList;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
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

	private void tryAddAttribute(Attribute attribute, AttributeModifier modifier) {
		AttributeInstance instance = getAttribute(attribute);
		if (instance != null) {
			instance.removeModifier(modifier.getId());
			instance.addPermanentModifier(modifier);
		}
	}

	@Override
	public void onCreate(ArrayList<GolemMaterial> materials, ArrayList<IUpgradeItem> upgrades, @Nullable UUID owner) {
		super.onCreate(materials, upgrades, owner);
		setSize(4, true);
		float overslime = (float) getAttributeValue(TGAttributes.MAX_OVERSLIME.get());
		GolemOverslimeEvents.setOverslime(this, overslime);
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
		float p = i / 4F - 1;
		this.entityData.set(ID_SIZE, i);
		this.reapplyPosition();
		this.refreshDimensions();
		tryAddAttribute(Attributes.MAX_HEALTH, new AttributeModifier("tinkers_golem.size_health_bonus", p, AttributeModifier.Operation.MULTIPLY_TOTAL));
		tryAddAttribute(Attributes.MOVEMENT_SPEED, new AttributeModifier("tinkers_golem.size_speed_bonus", p / 2F, AttributeModifier.Operation.MULTIPLY_TOTAL));
		tryAddAttribute(Attributes.ATTACK_DAMAGE, new AttributeModifier("tinkers_golem.size_damage_bonus", p, AttributeModifier.Operation.MULTIPLY_TOTAL));
		tryAddAttribute(TGAttributes.MAX_OVERSLIME.get(), new AttributeModifier("tinkers_golem.size_overslime_bonus", p, AttributeModifier.Operation.MULTIPLY_TOTAL));
		if (resetHealth) {
			this.setHealth(this.getMaxHealth());
			GolemOverslimeEvents.setOverslime(this, 99999);
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

	@Override
	protected InteractionResult mobInteractImpl(Player player, InteractionHand hand) {
		ItemStack itemstack = player.getItemInHand(hand);
		if (MGConfig.COMMON.strictInteract.get() && !itemstack.isEmpty()) {
			return InteractionResult.PASS;
		} else if (itemstack.isEmpty() || !this.canModify(player)) {
			return super.mobInteractImpl(player, hand);
		} else if (!player.isShiftKeyDown()) {
			if (itemstack.canEquip(EquipmentSlot.HEAD, this)) {
				if (this.level().isClientSide()) {
					return InteractionResult.SUCCESS;
				}
				if (!this.getItemBySlot(EquipmentSlot.HEAD).isEmpty()) {
					this.dropSlot(EquipmentSlot.HEAD, false);
				}

				this.setItemSlot(EquipmentSlot.HEAD, itemstack.split(1));
				GolemTriggers.EQUIP.trigger((ServerPlayer) player, 1);
				return InteractionResult.CONSUME;
			} else {
				return InteractionResult.FAIL;
			}
		} else {
			for (EquipmentSlot slot : EquipmentSlot.values()) {
				this.dropSlot(slot, false);
			}

			return InteractionResult.SUCCESS;
		}
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
				GolemMaterial mat = getMaterials().get(1);
				Item item = GolemMaterialConfig.get().getCraftIngredient(mat.id()).getItems()[0].getItem();

				this.level().addParticle(new DynamicBreakParticleOption(item.getDefaultInstance()), this.getX() + (double) f2, this.getY(), this.getZ() + (double) f3, 0.0F, 0.0F, 0.0F);

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
			ItemStack helmet = getItemBySlot(EquipmentSlot.HEAD);
			int helmetIndex = -1;
			if (!helmet.isEmpty()) {
				helmetIndex = this.random.nextInt(n);
			}

			for (int i = 0; i < n; ++i) {
				float f1 = ((i & 1) - 0.5F) * offset;
				float f2 = -0.5F * offset;
				SlimeGolemEntity slime = this.getType().create(this.level());
				if (slime == null) continue;
				slime.onCreate(getMaterials(), split.get(i), getOwnerUUID());
				slime.setCustomName(component);
				slime.setNoAi(flag);
				slime.setInvulnerable(this.isInvulnerable());
				slime.setSize(nextSize, true);
				if (i == helmetIndex) {
					slime.setItemSlot(EquipmentSlot.HEAD, helmet.copy());
				}
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

	@Override
	public int getPreviewScale() {
		if (!isAddedToWorld()) return 60;
		return 60 / getSize();
	}

}
