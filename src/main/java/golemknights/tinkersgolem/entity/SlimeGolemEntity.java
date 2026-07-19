package golemknights.tinkersgolem.entity;

import dev.xkmc.l2library.util.math.MathHelper;
import dev.xkmc.l2serial.serialization.SerialClass;
import dev.xkmc.modulargolems.content.config.GolemMaterial;
import dev.xkmc.modulargolems.content.config.GolemMaterialConfig;
import dev.xkmc.modulargolems.content.core.GolemType;
import dev.xkmc.modulargolems.content.entity.common.AbstractGolemEntity;
import dev.xkmc.modulargolems.content.item.equipments.IGolemEquipmentItem;
import dev.xkmc.modulargolems.content.item.upgrade.AddSlotTemplate;
import dev.xkmc.modulargolems.content.item.upgrade.IUpgradeItem;
import dev.xkmc.modulargolems.init.advancement.GolemTriggers;
import dev.xkmc.modulargolems.init.data.MGConfig;
import golemknights.tinkersgolem.TinkersGolem;
import golemknights.tinkersgolem.client.DynamicBreakParticleOption;
import golemknights.tinkersgolem.events.GolemOverslimeEvents;
import golemknights.tinkersgolem.register.TGAttributes;
import golemknights.tinkersgolem.register.TGEntities;
import golemknights.tinkersgolem.register.TGGolemModifiers;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
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
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import slimeknights.mantle.fluid.FluidTransferHelper;
import slimeknights.tconstruct.library.modifiers.fluid.FluidEffectContext;
import slimeknights.tconstruct.library.modifiers.fluid.FluidEffectManager;
import slimeknights.tconstruct.library.modifiers.fluid.FluidEffects;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;

import java.util.*;

import static slimeknights.tconstruct.tools.modifiers.ability.fluid.UseFluidOnHitModifier.spawnParticles;

@SerialClass
public class SlimeGolemEntity extends AbstractGolemEntity<SlimeGolemEntity, SlimeGolemPartType> {

	private static final EntityDataAccessor<Integer> ID_SIZE = SynchedEntityData.defineId(SlimeGolemEntity.class,
			EntityDataSerializers.INT);
	private static final EntityDataAccessor<Boolean> ID_SHAKE = SynchedEntityData.defineId(SlimeGolemEntity.class,
			EntityDataSerializers.BOOLEAN);

	public float targetSquish;
	public float squish;
	public float oSquish;
	private boolean wasOnGround;

	@SerialClass.SerialField
	public long shakeTimestamp;

	@SerialClass.SerialField
	public final SlimeTank tank = new SlimeTank(this, 1);

	public SlimeGolemEntity(EntityType<SlimeGolemEntity> type, Level level) {
		super(type, level);
		this.fixupDimensions();
		this.moveControl = new SlimeMoveControl(this);
		this.waterMoveControl = new SlimeSwimMoveControl(this);
		if (!level.isClientSide()) {
			tank.add(() -> TinkersGolem.HANDLER.toTrackingPlayers(new SlimeTankSyncPacket(this, tank), this));
		}
	}

	private void tryAddAttribute(Attribute attribute, AttributeModifier modifier) {
		AttributeInstance instance = getAttribute(attribute);
		if (instance != null) {
			instance.removeModifier(modifier.getId());
			instance.addPermanentModifier(modifier);
		}
	}

	public int getTankCapacity() {
		return (int) getAttributeValue(TGAttributes.TANK_CAPACITY.get());
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
		this.entityData.define(ID_SHAKE, false);
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
		float hp = i * i / 16f - 1;
		this.entityData.set(ID_SIZE, i);
		this.reapplyPosition();
		this.refreshDimensions();
		var uuid = MathHelper.getUUIDFromString("slime_golem_bonus");
		tryAddAttribute(Attributes.MAX_HEALTH, new AttributeModifier(uuid, "tinkers_golem.size_health_bonus", hp,
				AttributeModifier.Operation.MULTIPLY_TOTAL));
		tryAddAttribute(Attributes.MOVEMENT_SPEED, new AttributeModifier(uuid, "tinkers_golem.size_speed_bonus", p / 2F,
				AttributeModifier.Operation.MULTIPLY_TOTAL));
		tryAddAttribute(Attributes.ATTACK_DAMAGE, new AttributeModifier(uuid, "tinkers_golem.size_damage_bonus", p,
				AttributeModifier.Operation.MULTIPLY_TOTAL));
		tryAddAttribute(TGAttributes.MAX_OVERSLIME.get(), new AttributeModifier(uuid,
				"tinkers_golem.size_overslime_bonus", p, AttributeModifier.Operation.MULTIPLY_TOTAL));
		tryAddAttribute(TGAttributes.TANK_CAPACITY.get(), new AttributeModifier(uuid,
				"tinkers_golem.tank_capacity_bonus", p, AttributeModifier.Operation.MULTIPLY_TOTAL));
		tryAddAttribute(ForgeMod.ENTITY_REACH.get(), new AttributeModifier(uuid, "tinkers_golem.size_reach_bonus", p,
				AttributeModifier.Operation.MULTIPLY_TOTAL));
		if (resetHealth) {
			this.setGuardedDataImpl(this.getMaxHealth(), true, true);
			float overslime = (float) getAttributeValue(TGAttributes.MAX_OVERSLIME.get());
			GolemOverslimeEvents.setOverslime(this, overslime);
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
		ListTag helmetModifiers = new ListTag();
		cachedHelmetModifiers.forEach((attribute, modifier) -> {
			var tag = modifier.save();
			tag.putString("AttributeName", BuiltInRegistries.ATTRIBUTE.getKey(attribute).toString());
			helmetModifiers.add(tag);
		});
		p_33619_.put("HelmetModifiers", helmetModifiers);
	}

	public void readAdditionalSaveData(CompoundTag p_33607_) {
		super.readAdditionalSaveData(p_33607_);
		this.setSize(p_33607_.getInt("Size") + 1, false);
		this.wasOnGround = p_33607_.getBoolean("wasOnGround");

		if (p_33607_.contains("HelmetModifiers", 9)) {
			cachedHelmetModifiers = HashMultimap.create();
			ListTag listtag = p_33607_.getList("HelmetModifiers", 10);
			for (int i = 0; i < listtag.size(); ++i) {
				CompoundTag compoundtag = listtag.getCompound(i);
				Optional<Attribute> optional = BuiltInRegistries.ATTRIBUTE
						.getOptional(ResourceLocation.tryParse(compoundtag.getString("AttributeName")));
				if (optional.isPresent()) {
					AttributeModifier attributemodifier = AttributeModifier.load(compoundtag);
					if (attributemodifier != null && attributemodifier.getId().getLeastSignificantBits() != 0L
							&& attributemodifier.getId().getMostSignificantBits() != 0L) {
						cachedHelmetModifiers.put(optional.get(), attributemodifier);
					}
				}
			}
		}
	}

	public boolean isTiny() {
		return this.getSize() <= 1;
	}

	public boolean wasOnGround() {
		return wasOnGround;
	}

	@Override
	protected InteractionResult mobInteractImpl(Player player, InteractionHand hand) {
		ItemStack itemstack = player.getItemInHand(hand);
		if (MGConfig.COMMON.strictInteract.get() && !itemstack.isEmpty()) {
			return InteractionResult.PASS;
		} else if (!this.canModify(player)) {
			return super.mobInteractImpl(player, hand);
		} else if (!player.isShiftKeyDown()) {
			if (itemstack.isEmpty()) {
				return super.mobInteractImpl(player, hand);
			}

			boolean success = false;
			if (!FluidTransferHelper.interactWithContainer(level(), this.getOnPos(), tank, player, hand)
					.didTransfer()) {
				success = true;
				FluidTransferHelper.interactWithFilledBucket(level(), this.getOnPos(), tank, player, hand,
						player.getDirection().getOpposite());
			}

			if (itemstack.canEquip(EquipmentSlot.HEAD, this)) {
				success = true;
				dropHelmet();
				this.setItemSlot(EquipmentSlot.HEAD, itemstack.split(1));
				if (player instanceof ServerPlayer mp) {
					GolemTriggers.EQUIP.trigger(mp, 1);
				}
			}

			return success ? (level().isClientSide() ? InteractionResult.SUCCESS : InteractionResult.CONSUME)
					: InteractionResult.FAIL;
		}
		dropHelmet();
		return super.mobInteractImpl(player, hand);
	}

	private void dropHelmet() {
		if (!this.getItemBySlot(EquipmentSlot.HEAD).isEmpty()) {
			this.dropSlot(EquipmentSlot.HEAD, false);
		}
	}

	@Override
	public void tick() {
		this.squish += (this.targetSquish - this.squish) * 0.5F;
		this.oSquish = this.squish;
		super.tick();
		if (this.onGround() && !wasOnGround()) {
			int i = this.getSize();
			for (int j = 0; j < i * 8; ++j) {
				float f = this.random.nextFloat() * ((float) Math.PI * 2F);
				float f1 = this.random.nextFloat() * 0.5F + 0.5F;
				float f2 = Mth.sin(f) * (float) i * 0.5F * f1;
				float f3 = Mth.cos(f) * (float) i * 0.5F * f1;
				ArrayList<GolemMaterial> mat = getMaterials();
				if (!mat.isEmpty()) {
					Item item = GolemMaterialConfig.get().getCraftIngredient(mat.get(1).id()).getItems()[0].getItem();
					this.level().addParticle(new DynamicBreakParticleOption(item.getDefaultInstance()),
							this.getX() + (double) f2, this.getY(), this.getZ() + (double) f3, 0.0F, 0.0F, 0.0F);
				}
			}

			this.playSound(this.getSquishSound(), this.getSoundVolume(),
					((this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F) / 0.8F);
			this.targetSquish = -0.5F;
		} else if (!this.onGround() && this.wasOnGround()) {
			this.targetSquish = 1.0F;
		}

		this.wasOnGround = this.onGround();
		this.decreaseSquish();
		if (!level().isClientSide()) {
			entityData.set(ID_SHAKE, shakeTimestamp > level().getGameTime());
		}
	}

	public boolean isShaking() {
		return entityData.get(ID_SHAKE);
	}

	@Override
	public boolean canAttackType(EntityType<?> type) {
		return !isShaking() && super.canAttackType(type);
	}

	protected void decreaseSquish() {
		this.targetSquish *= 0.6F;
	}

	public double getToTargetDist() {
		if (navigation.isDone())
			return 0;
		var path = navigation.getPath();
		if (path == null)
			return 0;
		return path.getTarget().getCenter().distanceTo(position());
	}

	protected int getJumpDelay() {
		if (getToTargetDist() > 4)
			return this.random.nextInt(5) + 5;
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

	public float getJumpPower() {
		return 0.42F * this.getBlockJumpFactor() + this.getJumpBoostPower();
	}

	@Override
	protected void jumpFromGround() {
		Vec3 vec3 = this.getDeltaMovement();
		this.setDeltaMovement(vec3.x, this.getJumpPower(), vec3.z);
		this.hasImpulse = true;
	}

	@Nullable
	@Override
	public SpawnGroupData finalizeSpawn(ServerLevelAccessor p_33601_, DifficultyInstance p_33602_,
			MobSpawnType p_33603_, @Nullable SpawnGroupData p_33604_, @Nullable CompoundTag p_33605_) {
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
		if (!isTiny())
			return;
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
		if (!this.level().isClientSide && size > 1 && this.isDeadOrDying() && reason == RemovalReason.KILLED
				&& !getTags().contains("NoSplit")) {
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

			FluidStack fluid = this.getFluid();
			if (!fluid.isEmpty()) {
				fluid.setAmount(fluid.getAmount() / n);
			}

			for (int i = 0; i < n; ++i) {
				float f1 = ((i & 1) - 0.5F) * offset;
				float f2 = -0.5F * offset;
				SlimeGolemEntity slime = this.getType().create(this.level());
				if (slime == null)
					continue;

				slime.onCreate(getMaterials(), split.get(i), getOwnerUUID());
				slime.setCustomName(component);
				slime.setNoAi(flag);
				slime.setInvulnerable(this.isInvulnerable());
				slime.setSize(nextSize, true);

				if (i == helmetIndex) {
					slime.setItemSlot(EquipmentSlot.HEAD, helmet.copy());
				}
				slime.setFluid(fluid.copy());

				// TODO curios split

				slime.moveTo(this.getX() + (double) f1, this.getY() + (double) 0.5F, this.getZ() + (double) f2,
						this.random.nextFloat() * 360.0F, 0.0F);
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
	public void doEnchantDamageEffects(LivingEntity attacker, Entity target) {
		int lv = getModifiers().getOrDefault(TGGolemModifiers.SPILLING.get(), 0) * getSize();
		if (lv > 0) {
			FluidStack fluid = this.getFluid();
			LivingEntity livingTarget = target instanceof LivingEntity living ? living : null;
			if (!fluid.isEmpty()) {
				FluidEffects recipe = FluidEffectManager.INSTANCE.find(fluid.getFluid());
				if (recipe.hasEntityEffects()) {
					int consumed = recipe.applyToEntity(fluid, lv, FluidEffectContext.builder(attacker.level())
							.user(attacker, null).target(target, livingTarget), IFluidHandler.FluidAction.EXECUTE);
					if (consumed > 0) {
						spawnParticles(target, fluid);
						fluid.shrink(consumed);
						setFluid(fluid);
					}
				}
			}
		}
		super.doEnchantDamageEffects(attacker, target);
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
		if (!isAddedToWorld())
			return 60;
		return 60 / getSize();
	}

	public FluidStack getFluid() {
		return this.tank.getFluidInTank(0);
	}

	public void setFluid(FluidStack fluid) {
		if (fluid.getAmount() > getTankCapacity())
			fluid.setAmount(getTankCapacity());
		this.tank.set(0, 0, fluid);
		this.tank.setChanged();
	}

	@Override
	public <T> LazyOptional<T> getCapability(Capability<T> capability, @Nullable Direction facing) {
		if (capability == ForgeCapabilities.FLUID_HANDLER) {
			return LazyOptional.of(() -> tank).cast();
		}
		return super.getCapability(capability, facing);
	}

	@Override
	public void checkRide(LivingEntity target) {
		if (!(target instanceof SlimeGolemEntity other) || !target.isAlive())
			return;

		// size check
		if (getSize() != other.getSize())
			return;
		if (getSize() > 2)
			return;

		// material check
		for (int i = 0; i < getMaterials().size(); i++) {
			if (!getMaterials().get(i).id().equals(other.getMaterials().get(i).id()))
				return;
		}

		// upgrade check
		var upgrades = new ArrayList<IUpgradeItem>();
		for (var e : getUpgrades())
			if (e instanceof IUpgradeItem item)
				upgrades.add(item);
		for (var e : other.getUpgrades())
			if (e instanceof IUpgradeItem item)
				upgrades.add(item);
		int rem = TGEntities.HOLDER_SLIME.get().getRemaining(getMaterials(), upgrades);
		if (rem < 0)
			return;

		// check for duplicate templates
		Set<AddSlotTemplate> set = new LinkedHashSet<>();
		for (var e : upgrades) {
			if (e instanceof AddSlotTemplate t) {
				if (set.contains(t))
					return;
				set.add(t);
			}
		}

		// reforge check
		int reforge = getReforgeCount();
		int otherReforge = other.getReforgeCount();
		if (reforge + otherReforge > other.getMaxReforge())
			return;

		// apply data merge
		other.updateAttributes(other.getMaterials(), upgrades, other.getOwnerUUID());
		other.setSize(getSize() * 2, false);
		if (reforge > 0)
			other.updateReforge(reforge + otherReforge);
		float hp = Math.min(other.getMaxHealth(), other.getGuardedDataImpl() + getGuardedDataImpl());
		other.setGuardedDataImpl(hp, true, true);
		GolemOverslimeEvents.setOverslime(other,
				GolemOverslimeEvents.getOverslime(this) + GolemOverslimeEvents.getOverslime(other));

		// equipment merge
		for (var e : EquipmentSlot.values()) {
			var stack = getItemBySlot(e);
			if (stack.isEmpty())
				continue;
			if (other.getItemBySlot(e).isEmpty())
				other.setItemSlot(e, stack);
			else
				spawnAtLocation(stack);
		}

		var fluid = getFluid();
		var otherFluid = other.getFluid();
		if (!fluid.isEmpty()) {
			if (otherFluid.isEmpty()) {
				other.setFluid(fluid.copy());
			} else if (fluid.isFluidEqual(otherFluid)) {
				var ans = otherFluid.copy();
				ans.setAmount(otherFluid.getAmount() + fluid.getAmount());
				other.setFluid(ans);
			}
		}

		// TODO curios merge

		other.shakeTimestamp = level().getGameTime() + 100;

		toItem(getOwner());
	}

	@Nonnull
	private Multimap<Attribute, AttributeModifier> cachedHelmetModifiers = HashMultimap.create();;

	@Override
	@Nullable
	public Map<EquipmentSlot, ItemStack> collectEquipmentChanges() {
		Map<EquipmentSlot, ItemStack> map = null;

		for (EquipmentSlot equipmentslot : EquipmentSlot.values()) {
			ItemStack itemstack;
			switch (equipmentslot.getType()) {
				case HAND:
					itemstack = super.getLastHandItem(equipmentslot);
					break;
				case ARMOR:
					itemstack = super.getLastArmorItem(equipmentslot);
					break;
				default:
					continue;
			}

			ItemStack itemstack1 = this.getItemBySlot(equipmentslot);
			if (this.equipmentHasChanged(itemstack, itemstack1)) {
				net.minecraftforge.common.MinecraftForge.EVENT_BUS
						.post(new net.minecraftforge.event.entity.living.LivingEquipmentChangeEvent(this, equipmentslot,
								itemstack, itemstack1));
				if (map == null) {
					map = Maps.newEnumMap(EquipmentSlot.class);
				}

				map.put(equipmentslot, itemstack1);
				if (!itemstack.isEmpty()) {
					if (equipmentslot == EquipmentSlot.HEAD && !cachedHelmetModifiers.isEmpty())
						this.getAttributes().removeAttributeModifiers(cachedHelmetModifiers);
					else if (itemstack.getItem() instanceof IGolemEquipmentItem gItem)
						this.getAttributes()
								.removeAttributeModifiers(gItem.getGolemModifiers(itemstack, this, equipmentslot));
					else
						this.getAttributes().removeAttributeModifiers(itemstack.getAttributeModifiers(equipmentslot));
				}

				if (!itemstack1.isEmpty()) {
					var modifiers = itemstack1.getAttributeModifiers(equipmentslot);
					if (itemstack1.getItem() instanceof IGolemEquipmentItem gItem)
						modifiers = gItem.getGolemModifiers(itemstack1, this, equipmentslot);
					if (equipmentslot == EquipmentSlot.HEAD) {
						ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
						for (var entry : modifiers.entries()) {
							if (entry.getKey() == Attributes.ARMOR || entry.getKey() == Attributes.ARMOR_TOUGHNESS) {
								var modifier = entry.getValue();
								builder.put(entry.getKey(), new AttributeModifier(modifier.getId(), modifier.getName(),
										modifier.getAmount() * 4, modifier.getOperation()));
							} else
								builder.put(entry.getKey(), entry.getValue());
						}
						cachedHelmetModifiers = builder.build();
						this.getAttributes().addTransientAttributeModifiers(cachedHelmetModifiers);
					} else
						this.getAttributes()
								.addTransientAttributeModifiers(modifiers);
				}
			}
		}

		return map;
	}
}
