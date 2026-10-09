package golemknights.tinkersgolem.content.modifiers.tool;

import dev.xkmc.modulargolems.content.entity.metalgolem.MetalGolemEntity;
import dev.xkmc.modulargolems.content.item.ranged.CannonPoseUtil;
import golemknights.tinkersgolem.library.hooks.ShoulderWeaponModifierHook;
import golemknights.tinkersgolem.register.TGStats;
import golemknights.tinkersgolem.register.TGTinkersModifiers;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.fluids.FluidStack;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import slimeknights.mantle.data.loadable.record.RecordLoadable;
import slimeknights.tconstruct.common.Sounds;
import slimeknights.tconstruct.library.json.LevelingInt;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.fluid.FluidEffectManager;
import slimeknights.tconstruct.library.modifiers.fluid.FluidEffects;
import slimeknights.tconstruct.library.modifiers.hook.build.ConditionalStatModifierHook;
import slimeknights.tconstruct.library.modifiers.modules.ModifierModule;
import slimeknights.tconstruct.library.module.HookProvider;
import slimeknights.tconstruct.library.module.ModuleHook;
import slimeknights.tconstruct.library.tools.capability.EntityModifierCapability;
import slimeknights.tconstruct.library.tools.capability.PersistentDataCapability;
import slimeknights.tconstruct.library.tools.capability.fluid.ToolTankHelper;
import slimeknights.tconstruct.library.tools.helper.ModifierUtil;
import slimeknights.tconstruct.library.tools.helper.ToolDamageUtil;
import slimeknights.tconstruct.library.tools.item.ranged.ModifiableLauncherItem;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ModDataNBT;
import slimeknights.tconstruct.library.tools.stat.ToolStats;
import slimeknights.tconstruct.tools.entity.FluidEffectProjectile;

import java.util.List;

public record SpewingModule(LevelingInt shots) implements ModifierModule, ShoulderWeaponModifierHook.Delayed {
    public static final RecordLoadable<SpewingModule> LOADER = RecordLoadable.create(
            LevelingInt.LOADABLE.requiredField("shots", SpewingModule::shots),
            SpewingModule::new);

    @Override
    public RecordLoadable<? extends ModifierModule> getLoader() {
        return LOADER;
    }

    @Override
    public List<ModuleHook<?>> getDefaultHooks() {
        return HookProvider.defaultHooks(TGTinkersModifiers.SHOLDER_WEAPON_MODIFIER_HOOK);
    }

    @Override
    public void onShoot(IToolStackView tool, ModifierEntry modifier, MetalGolemEntity golem, ItemStack toolItem,
            InteractionHand hand) {
        LivingEntity target = golem.getTarget();
        if (target != null && target.isAlive()) {
            if (!CannonPoseUtil.FLAME_THROWER.isOutOfRange(golem, hand, ConditionalStatModifierHook.getModifiedStat(tool, golem, TGStats.CANNON_RANGE))) {
                FluidStack fluid = ToolTankHelper.TANK_HELPER.getFluid(tool);
                if (!fluid.isEmpty()) {
                    FluidEffects recipe = FluidEffectManager.INSTANCE.find(fluid.getFluid());
                    if (recipe.hasEffects()) {
                        float power = ConditionalStatModifierHook.getModifiedStat(tool, golem,
                                ToolStats.PROJECTILE_DAMAGE);
                        int shots = this.shots.compute(modifier.getEffectiveLevel());
                        int amount = Math.min(fluid.getAmount(),
                                (int) ((float) recipe.getAmount(fluid.getFluid()) * power) * shots) / shots;
                        if (amount > 0) {
                            float velocity = ConditionalStatModifierHook.getModifiedStat(tool, golem,
                                    ToolStats.VELOCITY) * 3.0F;
                            float inaccuracy = ModifierUtil.getInaccuracy(tool, golem);
                            float startAngle = ModifiableLauncherItem.getAngleStart(shots);
                            int primaryIndex = shots / 2;
                            Level world = golem.level();

                            Vec3 pos = CannonPoseUtil.FLAME_THROWER.getOrigin(golem, hand);

                            Vec3 dst = target.position().add(0.0F, target.getBbHeight() / 2.0F,
                                    0.0F);
                            Vec3 dir = dst.subtract(pos).normalize();

                            for (int shotIndex = 0; shotIndex < shots; ++shotIndex) {
                                FluidEffectProjectile spit = new FluidEffectProjectile(world, golem,
                                        new FluidStack(fluid, amount), power);
                                spit.setWaterInertia(ConditionalStatModifierHook.getModifiedStat(tool, golem,
                                        ToolStats.WATER_INERTIA));
                                Vec3 upVector = golem.getUpVector(1.0F);
                                float angle = startAngle + (float) (10 * shotIndex);
                                Vector3f targetVector = golem.getViewVector(1.0F).toVector3f()
                                        .rotate((new Quaternionf()).setAngleAxis((double) angle * Math.PI / 180.0,
                                                upVector.x, upVector.y, upVector.z));
                                spit.shoot(dir.x(), dir.y(), dir.z(), velocity, inaccuracy);
                                EntityModifierCapability.getCapability(spit).setModifiers(tool.getModifiers());
                                ModDataNBT arrowData = PersistentDataCapability.getOrWarn(spit);

                                for (ModifierEntry entry : tool.getModifierList()) {
                                    entry.getHook(ModifierHooks.PROJECTILE_LAUNCH).onProjectileLaunch(tool, entry,
                                            golem, ItemStack.EMPTY, spit, (AbstractArrow) null, arrowData,
                                            shotIndex == primaryIndex);
                                }

                                world.addFreshEntity(spit);
                                world.playSound(null, golem.getX(), golem.getY(), golem.getZ(), Sounds.SPIT.getSound(),
                                        SoundSource.PLAYERS, 1.0F,
                                        1.0F / (world.getRandom().nextFloat() * 0.4F + 1.2F) + 0.5F + angle / 10.0F);
                            }

                            if (ModifierUtil.consumesResources(golem)) {
                                fluid.shrink(amount * shots);
                                ToolTankHelper.TANK_HELPER.setFluid(tool, fluid);
                            }

                            ToolDamageUtil.damageAnimated(tool, shots, golem, modifier.getId());
                        }
                    }
                }
            }
        }
    }
}
