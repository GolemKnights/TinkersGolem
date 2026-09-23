package golemknights.tinkersgolem.mixinhelper;

import java.util.function.Predicate;

import javax.annotation.Nullable;

import dev.xkmc.modulargolems.content.entity.metalgolem.MetalGolemEntity;
import dev.xkmc.modulargolems.content.item.ranged.CannonPoseUtil;
import golemknights.tinkersgolem.library.hooks.ShoulderWeaponModifierHook;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ProjectileWeaponItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.fluid.entity.DamageFluidEffect.DamageTypePair;
import slimeknights.tconstruct.library.modifiers.hook.build.ConditionalStatModifierHook;
import slimeknights.tconstruct.library.tools.capability.EntityModifierCapability;
import slimeknights.tconstruct.library.tools.capability.PersistentDataCapability;
import slimeknights.tconstruct.library.tools.helper.ToolDamageUtil;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ModDataNBT;
import slimeknights.tconstruct.library.tools.nbt.ModifierNBT;
import slimeknights.tconstruct.library.tools.stat.ToolStats;
import slimeknights.tconstruct.tools.entity.CustomFireball;
import slimeknights.tconstruct.tools.modules.interaction.FireballModule;

public interface FireballModule_ShoulderWeaponModifierHook extends ShoulderWeaponModifierHook {

    private FireballModule self() {
        return (FireballModule) (Object) this;
    }

    @Nullable
    private FireballTypeAccessor getFireballTypeAccessor(ItemStack stack) {
        try {
            return (FireballTypeAccessor) FireballTypeHelper.getFireballTypeHandle().invoke(this, stack);
        } catch (Throwable t) {
            throw new RuntimeException("Failed to invoke FireballModule#getFireballType", t);
        }
    }

    private ItemStack getProjectile(MetalGolemEntity e) {
        Predicate<ItemStack> predicate = self();
        ItemStack stack = ProjectileWeaponItem.getHeldProjectile(e, predicate);
        ItemStack arrowSlot = e.getArrowSlot().getItem();
        if (stack.isEmpty() && !arrowSlot.isEmpty() && predicate.test(arrowSlot)) {
            stack = arrowSlot;
        }

        if (e.isHostile()) {
            stack = stack.copy();
        }

        return stack;
    }

    default void onTick(IToolStackView tool, ModifierEntry modifier, MetalGolemEntity entity, ItemStack toolItem,
            InteractionHand hand) {
        if (entity.tickCount % 40 == (hand == InteractionHand.MAIN_HAND ? 10 : 30)) {
            LivingEntity target = entity.getTarget();
            if (target != null && target.isAlive()) {
                if (!CannonPoseUtil.FLAME_THROWER.isOutOfRange(entity, hand, 15.0F)) {
                    ItemStack fireball = getProjectile(entity);
                    if (fireball.isEmpty())
                        return;
                    Vec3 pos = CannonPoseUtil.FLAME_THROWER.getOrigin(entity, hand);

                    Vec3 dst = target.position().add(0.0F, target.getBbHeight() / 2.0F,
                            0.0F);
                    Vec3 dir = dst.subtract(pos).normalize();

                    Level level = entity.level();
                    // if we found a fireball, fire it
                    if (!level.isClientSide) {
                        // fetch stats
                        float power = ConditionalStatModifierHook.getModifiedStat(tool, entity,
                                ToolStats.PROJECTILE_DAMAGE);
                        float velocity = ConditionalStatModifierHook.getModifiedStat(tool, entity, ToolStats.VELOCITY);

                        CustomFireball projectile = new CustomFireball(level, entity, dir.x, dir.y, dir.z);
                        projectile.xPower *= velocity;
                        projectile.yPower *= velocity;
                        projectile.zPower *= velocity;
                        projectile.setPower(power);
                        projectile.setPos(pos);

                        // add in type specific behavior
                        projectile.setItem(fireball);
                        FireballTypeAccessor type = getFireballTypeAccessor(fireball);
                        projectile.setDamageMultiplier(self().damageMultiplier() * type.damageMultiplier());
                        DamageTypePair damageTypes = type.damageType(self().damageType());
                        projectile.setDamageType(damageTypes.ranged(), damageTypes.melee());

                        // set projectile modifiers
                        ModifierNBT modifiers = tool.getModifiers();
                        if (!self().ammoModifiers().isEmpty()) {
                            ModifierNBT.Builder builder = ModifierNBT.builder();
                            builder.add(modifiers);
                            builder.add(self().ammoModifiers());
                            builder.add(type.ammoModifiers());
                            modifiers = builder.build();
                        }
                        EntityModifierCapability.getCapability(projectile).setModifiers(modifiers);

                        // fetch the persistent data for the fireball as modifiers may want to store
                        // data
                        ModDataNBT projectileData = PersistentDataCapability.getOrWarn(projectile);
                        // let modifiers set properties
                        for (ModifierEntry entry : tool.getModifierList()) {
                            entry.getHook(ModifierHooks.PROJECTILE_LAUNCH).onProjectileLaunch(tool, entry, entity,
                                    ItemStack.EMPTY, projectile, null, projectileData, true);
                        }

                        // finally, release the projectile
                        level.addFreshEntity(projectile);

                        // damage tool if not creative
                        ToolDamageUtil.damage(tool, self().durability().compute(modifier), entity, toolItem,
                                modifier.getId());

                    }

                    if (!entity.isSilent()) {
                        entity.playSound(self().sound(), 2.0F,
                                (level.random.nextFloat() - level.random.nextFloat()) * 0.2F + 1.0F);
                    }

                    if (!entity.isHostile()) {
                        fireball.shrink(1);
                    }

                }
            }
        }
    }

}
