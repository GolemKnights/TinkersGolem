package golemknights.tinkersgolem.content.modifiers.tool;

import dev.xkmc.modulargolems.content.entity.metalgolem.MetalGolemEntity;
import dev.xkmc.modulargolems.content.item.ranged.CannonPoseUtil;
import golemknights.tinkersgolem.library.hooks.ShoulderWeaponModifierHook;
import golemknights.tinkersgolem.register.TGStats;
import golemknights.tinkersgolem.register.TGTinkersModifiers;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.FireworkRocketEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ProjectileWeaponItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import slimeknights.mantle.data.loadable.record.RecordLoadable;
import slimeknights.mantle.data.loadable.record.SingletonLoader;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.build.ConditionalStatModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.ranged.BowAmmoModifierHook;
import slimeknights.tconstruct.library.modifiers.modules.ModifierModule;
import slimeknights.tconstruct.library.module.HookProvider;
import slimeknights.tconstruct.library.module.ModuleHook;
import slimeknights.tconstruct.library.tools.capability.EntityModifierCapability;
import slimeknights.tconstruct.library.tools.capability.PersistentDataCapability;
import slimeknights.tconstruct.library.tools.helper.ModifierUtil;
import slimeknights.tconstruct.library.tools.helper.ToolDamageUtil;
import slimeknights.tconstruct.library.tools.item.ranged.ModifiableLauncherItem;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ModDataNBT;
import slimeknights.tconstruct.library.tools.nbt.ModifierNBT;
import slimeknights.tconstruct.library.tools.stat.ToolStats;

import java.util.List;

public enum ShootingModule implements ModifierModule, ShoulderWeaponModifierHook.Delayed {
    INSTANCE;
    public static final RecordLoadable<ShootingModule> LOADER =  new SingletonLoader<>(INSTANCE);

    @Override
    public RecordLoadable<? extends ModifierModule> getLoader() {
        return LOADER;
    }

    @Override
    public List<ModuleHook<?>> getDefaultHooks() {
        return HookProvider.defaultHooks(TGTinkersModifiers.SHOLDER_WEAPON_MODIFIER_HOOK);
    }

    @Override
    public void onShoot(IToolStackView tool, ModifierEntry modifier, MetalGolemEntity golem, ItemStack toolItem, InteractionHand hand) {
        LivingEntity target = golem.getTarget();
        boolean hostile = golem.isHostile();
        if (target != null && target.isAlive()) {
            if (!CannonPoseUtil.FLAME_THROWER.isOutOfRange(golem, hand, ConditionalStatModifierHook.getModifiedStat(tool, golem, TGStats.CANNON_RANGE))) {
                int count = BowAmmoModifierHook.getDesiredProjectiles(tool);
                ItemStack ammo = BowAmmoModifierHook.consumeAmmo(tool, ItemStack.EMPTY, golem, null, ProjectileWeaponItem.ARROW_OR_FIREWORK, count);
                if (!ammo.isEmpty()) {
                    float velocity = ConditionalStatModifierHook.getModifiedStat(tool, golem, ToolStats.VELOCITY);
                    float inaccuracy = ModifierUtil.getInaccuracy(tool, golem);
                    float startAngle = ModifiableLauncherItem.getAngleStart(ammo.getCount());
                    int primaryIndex = ammo.getCount() / 2;
                    Level level = golem.level();

                    Vec3 pos = CannonPoseUtil.FLAME_THROWER.getOrigin(golem, hand);

                    Vec3 dst = target.position().add(0.0F, target.getBbHeight() / 2.0F,
                            0.0F);
                    Vec3 dir = dst.subtract(pos).normalize();
                    int damage = 0;
                    for (int arrowIndex = 0; arrowIndex < ammo.getCount(); ++arrowIndex) {
                        AbstractArrow arrow = null;
                        Projectile projectile;
                        float speed;
                        float angle;
                        if (ammo.is(Items.FIREWORK_ROCKET)) {
                            projectile = new FireworkRocketEntity(level, ammo, golem, golem.getX(), golem.getEyeY() - 0.15000000596046448, golem.getZ(), true);
                            speed = 1.5F;
                            damage += 3;
                        } else {
                            ArrowItem arrowItem;
                            if (ammo.getItem() instanceof ArrowItem aItem) {
                                arrowItem = aItem;
                            } else {
                                arrowItem = (ArrowItem) Items.ARROW;
                            }

                            arrow = arrowItem.createArrow(level, ammo, golem);
                            projectile = arrow;
                            arrow.setCritArrow(true);
                            arrow.setSoundEvent(SoundEvents.CROSSBOW_HIT);
                            arrow.setShotFromCrossbow(true);
                            speed = 3.0F;
                            ++damage;
                            angle = (float) (arrow.getBaseDamage() - 2.0 + tool.getStats().get(ToolStats.PROJECTILE_DAMAGE));
                            arrow.setBaseDamage(ConditionalStatModifierHook.getModifiedStat(tool, golem, ToolStats.PROJECTILE_DAMAGE, angle));
                            if (hostile) {
                                arrow.pickup = AbstractArrow.Pickup.CREATIVE_ONLY;
                            }
                        }

                        angle = startAngle + 10 * arrowIndex;
                        projectile.shoot(dir.x(), dir.y(), dir.z(), velocity * speed, inaccuracy);
                        ModifierNBT modifiers = tool.getModifiers();
                        EntityModifierCapability.getCapability(projectile).addModifiers(modifiers);
                        ModDataNBT projectileData = PersistentDataCapability.getOrWarn(projectile);

                        for (ModifierEntry entry : modifiers.getModifiers()) {
                            entry.getHook(ModifierHooks.PROJECTILE_LAUNCH).onProjectileLaunch(tool, entry, golem, ammo, projectile, arrow, projectileData, arrowIndex == primaryIndex);
                        }

                        level.addFreshEntity(projectile);
                        level.playSound(null, golem.getX(), golem.getY(), golem.getZ(), SoundEvents.CROSSBOW_SHOOT, golem.getSoundSource(), 1.0F, getRandomShotPitch(angle, golem.getRandom()));
                    }
                    if (!hostile) {
                        ToolDamageUtil.damageAnimated(tool, damage, golem);
                        ammo.shrink(count);
                    }
                }
            }
        }
    }

    private static float getRandomShotPitch(float angle, RandomSource pRandom) {
        return angle == 0.0F ? 1.0F : 1.0F / (pRandom.nextFloat() * 0.5F + 1.8F) + 0.53F + angle / 10.0F;
    }
}