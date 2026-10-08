package golemknights.tinkersgolem.content.entity;

import dev.xkmc.l2library.base.BaseEntity;
import dev.xkmc.l2serial.serialization.SerialClass;
import dev.xkmc.modulargolems.content.entity.metalgolem.MetalGolemEntity;
import dev.xkmc.modulargolems.content.item.ranged.CannonPoseUtil;
import dev.xkmc.modulargolems.init.data.MGConfig;
import dev.xkmc.modulargolems.init.data.MGDamageTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.OwnableEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.entity.EntityTypeTest;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import slimeknights.tconstruct.common.Sounds;
import slimeknights.tconstruct.library.modifiers.hook.build.ConditionalStatModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.interaction.EntityInteractionModifierHook;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.helper.ModifierUtil;
import slimeknights.tconstruct.library.tools.helper.ToolAttackUtil;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.stat.ToolStats;

import java.util.*;

public class ModifiableBeaconLaserEntity extends BaseEntity implements OwnableEntity {
    public static final EntityDataAccessor<Integer> OWNER_ID;
    @SerialClass.SerialField
    public UUID owner;
    @SerialClass.SerialField
    public int life;
    @SerialClass.SerialField
    public float len;
    @SerialClass.SerialField
    public boolean right;
    @SerialClass.SerialField
    private Vec3 lastTarget;
    public LivingEntity ownerCache;
    private Set<LivingEntity> hit;
    @Nullable
    private IToolStackView tool;
    private ItemStack item = ItemStack.EMPTY;
    private float velocity = 1;
    private float power = 2;

    public ModifiableBeaconLaserEntity(EntityType<?> type, Level level) {
        super(type, level);
        this.lastTarget = Vec3.ZERO;
        this.hit = new HashSet();
        this.tool = null;
    }

    public ModifiableBeaconLaserEntity(EntityType<?> type, Level level, LivingEntity owner, int life, boolean right, IToolStackView tool, ItemStack stack) {
        super(type, level);
        this.lastTarget = Vec3.ZERO;
        this.hit = new HashSet();
        this.tool = tool;
        this.item = stack;
        this.owner = owner.getUUID();
        this.ownerCache = owner;
        this.entityData.set(OWNER_ID, owner.getId());
        this.life = life;
        this.right = right;
        this.setup(owner);
    }

    public void setup(LivingEntity living) {
        if (living instanceof MetalGolemEntity golem) {
            InteractionHand hand = this.right ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND;
            Vec3 pos = CannonPoseUtil.BEACON_CANNON.getOrigin(golem, hand);
            float[] rot = CannonPoseUtil.BEACON_CANNON.getAngle(golem, hand);
            Random random = new Random();
            if (tool != null) {
                // 使用精准度偏移角度
                float inaccuracy = ModifierUtil.getInaccuracy(tool, golem);
                rot[0] += (float) (random.nextGaussian() * inaccuracy);
                rot[1] += (float) (random.nextGaussian() * inaccuracy);
            }
            Vec3 dst = golem.getTargetAimPos().add(golem.position()).subtract(pos).normalize().scale(35.0).add(pos);
            BlockHitResult hit = golem.level().clip(new ClipContext(pos, dst, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, (Entity)null));
            this.len = (float)hit.getLocation().subtract(pos).length();
            this.lastTarget = hit.getLocation();
            this.setPos(pos);
            this.setYRot(rot[0] * 57.295776F);
            this.setXRot(rot[1] * 57.295776F);
        }
    }

    protected void defineSynchedData() {
        this.entityData.define(OWNER_ID, -1);
    }

    public void tick() {
        LivingEntity owner = this.getOwner();
        if (this.level().isClientSide() || owner != null && owner.isAlive() && this.tickCount <= this.life) {
            if (owner != null && this.tickCount == 0) {
                this.setup(owner);
            }

            super.tick();
            if (!this.level().isClientSide() && owner instanceof MetalGolemEntity golem) {
                Vec3 pos = this.position();
                Vec3 dst = this.lastTarget;
                List<LivingEntity> list = this.getEntityHitResult(this.level(), golem, pos, dst, golem.getScale() * 0.2F);
                if (tool != null) {
                    // 获取初速度与弹射物力量并保证同一条激光只获取一次
                    if (hit.isEmpty()) {
                        velocity = ConditionalStatModifierHook.getModifiedStat(tool, golem, ToolStats.VELOCITY);
                        power = ConditionalStatModifierHook.getModifiedStat(tool, golem, ToolStats.PROJECTILE_DAMAGE);
                    }
                    for (LivingEntity target : list) {
                        if (ToolAttackUtil.canPerformAttack(tool) && ToolAttackUtil.isAttackable(owner, target)) {
                            if (EntityInteractionModifierHook.meleeDisabled(tool)) {
                                owner.playSound(Sounds.TOY_SQUEAK.getSound());
                            } else {
                                ItemStack offhand = owner.getOffhandItem();
                                boolean notSelf = owner != target;
                                if (notSelf) {
                                    // 不确定工具是否还能回到原来的槽位
                                    owner.setItemInHand(InteractionHand.OFF_HAND, this.item);
                                }
                                ToolAttackContext.Builder builder = ToolAttackContext
                                        .attacker(owner).target(target).hand(InteractionHand.OFF_HAND)
                                        .baseDamage(((float)golem.getAttributeValue(Attributes.ATTACK_DAMAGE) + power - 2)
                                                * velocity * MGConfig.COMMON.beaconCannonDamageFactor.get().floatValue())
                                        .baseKnockback(0);
                                // 为了兼容一些特性，一条激光攻击的第一个生物不视为额外攻击
                                if (!hit.isEmpty())builder.extraAttack();
                                ToolAttackContext context = builder.build();
                                if (ToolAttackUtil.performAttack(tool, context)) {
                                    this.hit.add(target);
                                }
                                if (notSelf) {
                                    owner.setItemInHand(InteractionHand.OFF_HAND, offhand);
                                }
                            }
                        }
                    }
                } else {// 由于没有存储工具好物品至NBT，用原来的逻辑做好兜底
                    DamageSource source = new DamageSource(this.level().registryAccess().lookupOrThrow(Registries.DAMAGE_TYPE).getOrThrow(MGDamageTypes.BEACON), this, owner);
                    float dmg = (float) golem.getAttributeValue(Attributes.ATTACK_DAMAGE) * MGConfig.COMMON.beaconCannonDamageFactor.get().floatValue();

                    for (LivingEntity target : list) {
                        if (target.hurt(source, dmg)) {
                            this.hit.add(target);
                        }
                    }
                }
            }

        } else {
            this.discard();
        }
    }

    public List<LivingEntity> getEntityHitResult(Level level, MetalGolemEntity owner, Vec3 start, Vec3 end, float radius) {
        List<LivingEntity> list = level.getEntities(
                EntityTypeTest.forClass(LivingEntity.class),
                (new AABB(start, end)).inflate(radius),
                (x) -> !this.hit.contains(x) && owner.canAttack(x) && owner.predicateTarget(x)
        );
        ArrayList<LivingEntity> ans = new ArrayList<>();
        Iterator<LivingEntity> var8 = list.iterator();

        while(true) {
            LivingEntity e;
            AABB aabb;
            do {
                if (!var8.hasNext()) {
                    return ans;
                }

                e = var8.next();
                aabb = e.getBoundingBox().inflate(radius);
            } while(!aabb.intersects(start, end) && !aabb.contains(start) && !aabb.contains(end));

            ans.add(e);
        }
    }

    public @org.jetbrains.annotations.Nullable UUID getOwnerUUID() {
        return this.owner;
    }

    public @Nullable LivingEntity getOwner() {
        if (this.ownerCache != null) {
            return this.ownerCache;
        } else {
            Entity ans = this.level().getEntity(this.entityData.get(OWNER_ID));
            if (ans instanceof LivingEntity) {
                LivingEntity le = (LivingEntity)ans;
                this.ownerCache = le;
            }

            return this.ownerCache;
        }
    }

    public boolean shouldRender(double p_20296_, double p_20297_, double p_20298_) {
        return true;
    }

    public boolean shouldRenderAtSqrDistance(double p_19883_) {
        return true;
    }

    static {
        OWNER_ID = SynchedEntityData.defineId(ModifiableBeaconLaserEntity.class, EntityDataSerializers.INT);
    }
}
