package golemknights.tinkersgolem.mixinhelper;

import dev.xkmc.modulargolems.content.entity.common.AbstractGolemEntity;
import dev.xkmc.modulargolems.content.item.ranged.CannonPoseUtil;
import golemknights.tinkersgolem.library.hooks.ShoulderWeaponModifierHook;
import golemknights.tinkersgolem.register.TGStats;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.fluid.entity.DamageFluidEffect.DamageTypePair;
import slimeknights.tconstruct.library.modifiers.hook.build.ConditionalStatModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.ranged.BowAmmoModifierHook;
import slimeknights.tconstruct.library.tools.capability.EntityModifierCapability;
import slimeknights.tconstruct.library.tools.capability.PersistentDataCapability;
import slimeknights.tconstruct.library.tools.helper.ModifierUtil;
import slimeknights.tconstruct.library.tools.helper.ToolDamageUtil;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ModDataNBT;
import slimeknights.tconstruct.library.tools.nbt.ModifierNBT;
import slimeknights.tconstruct.library.tools.stat.ToolStats;
import slimeknights.tconstruct.tools.entity.CustomFireball;
import slimeknights.tconstruct.tools.modules.interaction.FireballModule;

import javax.annotation.Nullable;

public interface FireballModule_ShoulderWeaponModifierHook extends ShoulderWeaponModifierHook.Delayed {

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

    @Override
    default void onShoot(IToolStackView tool, ModifierEntry modifier, AbstractGolemEntity<?, ?> golem, ItemStack toolItem,
                         int slot) {
        LivingEntity target = golem.getTarget();
        if (target != null && target.isAlive()) {
            if (!golem.getCannonPose(CannonPoseUtil.DOG_FLAME_THROWER).isOutOfRange(golem, slot, ConditionalStatModifierHook.getModifiedStat(tool, golem, TGStats.CANNON_RANGE))) {
                ItemStack fireball = BowAmmoModifierHook.consumeAmmo(tool, ItemStack.EMPTY, golem, null, self(), 1);
                if (fireball.isEmpty())
                    return;
                Vec3 pos = golem.getCannonPose(CannonPoseUtil.DOG_FLAME_THROWER).getOrigin(golem, slot);

                Vec3 dst = target.position().add(0.0F, target.getBbHeight() / 2.0F,
                        0.0F);
                Vec3 dir = dst.subtract(pos).normalize();

                Level level = golem.level();
                // if we found a fireball, fire it
                if (!level.isClientSide) {
                    // fetch stats
                    float power = ConditionalStatModifierHook.getModifiedStat(tool, golem,
                            ToolStats.PROJECTILE_DAMAGE);
                    float velocity = ConditionalStatModifierHook.getModifiedStat(tool, golem, ToolStats.VELOCITY);
                    float inaccuracy = ModifierUtil.getInaccuracy(tool, golem) / 16.0F;
                    RandomSource random = golem.getRandom();

                    CustomFireball projectile = new CustomFireball(level, golem,
                            dir.x + random.nextGaussian() * inaccuracy,
                            dir.y,
                            dir.z + random.nextGaussian() * inaccuracy);
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
                        entry.getHook(ModifierHooks.PROJECTILE_LAUNCH).onProjectileLaunch(tool, entry, golem,
                                ItemStack.EMPTY, projectile, null, projectileData, true);
                    }

                    // finally, release the projectile
                    level.addFreshEntity(projectile);

                    // damage tool if not creative
                    ToolDamageUtil.damage(tool, self().durability().compute(modifier), golem, toolItem,
                            modifier.getId());

                }

                if (!golem.isSilent()) {
                    golem.playSound(self().sound(), 2.0F,
                            (level.random.nextFloat() - level.random.nextFloat()) * 0.2F + 1.0F);
                }

                if (!golem.isHostile()) {
                    fireball.shrink(1);
                }

            }
        }
    }
}
