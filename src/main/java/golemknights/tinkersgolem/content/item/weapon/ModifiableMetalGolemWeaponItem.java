package golemknights.tinkersgolem.content.item.weapon;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import dev.xkmc.l2library.util.math.MathHelper;
import dev.xkmc.modulargolems.content.item.equipments.GolemEquipmentItem;
import dev.xkmc.modulargolems.init.registrate.GolemTypes;
import golemknights.tinkersgolem.register.TGStats;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.*;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.common.ToolAction;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import slimeknights.mantle.client.SafeClientAccess;
import slimeknights.tconstruct.common.Sounds;
import slimeknights.tconstruct.common.TinkerTags;
import slimeknights.tconstruct.library.client.item.ModifiableItemClientExtension;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.behavior.AttributesModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.behavior.EnchantmentModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.display.DurabilityDisplayModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.interaction.*;
import slimeknights.tconstruct.library.modifiers.modules.build.RarityModule;
import slimeknights.tconstruct.library.tools.IndestructibleItemEntity;
import slimeknights.tconstruct.library.tools.capability.ToolCapabilityProvider;
import slimeknights.tconstruct.library.tools.capability.inventory.ToolInventoryCapability;
import slimeknights.tconstruct.library.tools.definition.ToolDefinition;
import slimeknights.tconstruct.library.tools.definition.module.display.ToolNameHook;
import slimeknights.tconstruct.library.tools.definition.module.mining.IsEffectiveToolHook;
import slimeknights.tconstruct.library.tools.definition.module.mining.MiningSpeedToolHook;
import slimeknights.tconstruct.library.tools.helper.*;
import slimeknights.tconstruct.library.tools.item.IModifiableDisplay;
import slimeknights.tconstruct.library.tools.nbt.IModDataView;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.StatsNBT;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;
import slimeknights.tconstruct.tools.TinkerToolActions;

import javax.annotation.Nullable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;

public class ModifiableMetalGolemWeaponItem extends GolemEquipmentItem
        implements IModifiableDisplay {
    public ModifiableMetalGolemWeaponItem(Item.Properties properties, ToolDefinition toolDefinition) {
        this(properties, toolDefinition, 1);
    }

    public ModifiableMetalGolemWeaponItem(Item.Properties properties, ToolDefinition toolDefinition, int maxStackSize) {
        super(properties, EquipmentSlot.MAINHAND, GolemTypes.ENTITY_GOLEM::get, builder -> {});
        this.toolDefinition = toolDefinition;
        this.maxStackSize = maxStackSize;
    }
    private final ToolDefinition toolDefinition;
    @Override
    public ToolDefinition getToolDefinition() {
        return toolDefinition;
    }

    /**
     * Cache of the tool built for rendering
     */
    private ItemStack toolForRendering = null;

    /* Basic properties */

    private final int maxStackSize;
    @Override
    public int getMaxStackSize(ItemStack stack) {
        return stack.isDamaged() ? 1 : this.maxStackSize;
    }
    public boolean isNotReplaceableByPickAction(ItemStack stack, Player player, int inventorySlot) {
        return true;
    }

    @Nullable
    public EquipmentSlot getEquipmentSlot(ItemStack stack) {
        return stack.is(TinkerTags.Items.HELD_ARMOR) ? EquipmentSlot.OFFHAND : null;
    }

    public boolean isEnchantable(ItemStack stack) {
        return false;
    }

    public boolean isBookEnchantable(ItemStack stack, ItemStack book) {
        return false;
    }

    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
        return enchantment.isCurse() && super.canApplyAtEnchantingTable(stack, enchantment);
    }

    public int getEnchantmentLevel(ItemStack stack, Enchantment enchantment) {
        return EnchantmentModifierHook.getEnchantmentLevel(stack, enchantment);
    }

    public Map<Enchantment, Integer> getAllEnchantments(ItemStack stack) {
        return EnchantmentModifierHook.getAllEnchantments(stack);
    }

    @Nullable
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundTag nbt) {
        return new ToolCapabilityProvider(stack);
    }

    public void verifyTagAfterLoad(CompoundTag nbt) {
        ToolStack.verifyTag(this, nbt, this.getToolDefinition());
    }

    public void onCraftedBy(ItemStack stack, Level worldIn, Player playerIn) {
        ToolStack.ensureInitialized(stack, this.getToolDefinition());
    }

    public boolean isFoil(ItemStack stack) {
        return ModifierUtil.checkVolatileFlag(stack, SHINY);
    }

    public Rarity getRarity(ItemStack stack) {
        return RarityModule.getRarity(stack);
    }

    public boolean hasCustomEntity(ItemStack stack) {
        return IndestructibleItemEntity.hasCustomEntity(stack);
    }

    @Nullable
    public Entity createEntity(Level world, Entity original, ItemStack stack) {
        return IndestructibleItemEntity.createFrom(world, original, stack);
    }

    /** @deprecated */
    @Deprecated
    public void onDestroyed(ItemEntity entity) {
        ToolInventoryCapability.onDestroyed(entity);
    }

    public boolean isRepairable(ItemStack stack) {
        return false;
    }

    public boolean isValidRepairItem(ItemStack pToRepair, ItemStack pRepair) {
        return false;
    }

    public boolean canBeDepleted() {
        return true;
    }

    public int getMaxDamage(ItemStack stack) {
        return ToolDamageUtil.getFakeMaxDamage(stack);
    }

    public int getDamage(ItemStack stack) {
        return !this.canBeDepleted() ? 0 : ToolStack.from(stack).getDamage();
    }

    public void setDamage(ItemStack stack, int damage) {
        if (this.canBeDepleted()) {
            ToolStack.from(stack).setDamage(damage);
        }

    }

    public <T extends LivingEntity> int damageItem(ItemStack stack, int amount, T damager, Consumer<T> onBroken) {
        ToolDamageUtil.handleDamageItem(stack, amount, damager, onBroken);
        return 0;
    }

    public boolean isBarVisible(ItemStack stack) {
        return stack.getCount() == 1 && DurabilityDisplayModifierHook.showDurabilityBar(stack);
    }

    public int getBarColor(ItemStack pStack) {
        return DurabilityDisplayModifierHook.getDurabilityRGB(pStack);
    }

    public int getBarWidth(ItemStack pStack) {
        return DurabilityDisplayModifierHook.getDurabilityWidth(pStack);
    }

    public boolean onLeftClickEntity(ItemStack stack, Player player, Entity target) {
        if (stack.getCount() > 1) return true;
        ToolStack tool = ToolStack.from(stack);
        //boolean noMelee = EntityInteractionModifierHook.meleeDisabled(tool);
        if (stack.is(TinkerTags.Items.INTERACTABLE_LEFT) && !player.getCooldowns().isOnCooldown(stack.getItem())) {
            List<ModifierEntry> modifiers = tool.getModifierList();
            for (ModifierEntry entry : modifiers) {
                if (entry.getHook(ModifierHooks.ENTITY_INTERACT).beforeEntityUse(tool, entry, player, target, InteractionHand.MAIN_HAND, InteractionSource.LEFT_CLICK).consumesAction()) {
                    return true;
                }
            }
            if (target instanceof LivingEntity living) {
                for (ModifierEntry entry : modifiers) {
                    if (entry.getHook(ModifierHooks.ENTITY_INTERACT).afterEntityUse(tool, entry, player, living, InteractionHand.MAIN_HAND, InteractionSource.LEFT_CLICK).consumesAction()) {
                        return true;
                    }
                }
            }
            // 原先这里有判断noMelee和近战Tag的逻辑
            for (ModifierEntry entry : modifiers) {
                if (entry.getHook(ModifierHooks.GENERAL_INTERACT).onToolUse(tool, entry, player, InteractionHand.MAIN_HAND, InteractionSource.LEFT_CLICK).consumesAction()) {
                    return true;
                }
            }
        }
        // 原先这里有判断noMelee的逻辑
        player.playSound(Sounds.TOY_SQUEAK.getSound());
        return true;
    }

    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(IToolStackView tool, EquipmentSlot slot) {
        if (slot != getSlot()) {
            return ImmutableMultimap.of();
        }

        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
        if (!tool.isBroken()) {
            StatsNBT statsNBT = tool.getStats();
            UUID uuid = UUID.get(slot);
            UUID perc = MathHelper.getUUIDFromString("golem_percent_attack");
            if (slot == EquipmentSlot.MAINHAND && EntityInteractionModifierHook.isMeleeWeapon(tool)) {
                float value = statsNBT.get(TGStats.ENTITY_REACH);
                if (value > 0) {
                    builder.put(ForgeMod.ENTITY_REACH.get(), new AttributeModifier(perc, "tconstruct.tool.golem_reach", value, AttributeModifier.Operation.MULTIPLY_BASE));
                }
                value = statsNBT.get(TGStats.GOLEM_SWEEP);
                if (value > 0) {
                    builder.put(GolemTypes.GOLEM_SWEEP.get(), new AttributeModifier(uuid, "tconstruct.tool.golem_sweep", value, AttributeModifier.Operation.MULTIPLY_BASE));
                }
                value = statsNBT.get(TGStats.ATTACK_DAMAGE_PERCENT);
                if (value > 0) {
                    builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(uuid, "tconstruct.tool.attack_damage_percent", value, AttributeModifier.Operation.MULTIPLY_BASE));
                }
            }

            builder.putAll(AttributesModifierHook.getHeldAttributeModifiers(tool, slot));
        }
        return builder.build();
    }

    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot, ItemStack stack) {
        CompoundTag nbt = stack.getTag();
        return nbt != null && slot.getType() == EquipmentSlot.Type.HAND ? this.getAttributeModifiers(ToolStack.from(stack), slot) : ImmutableMultimap.of();
    }

    public boolean canDisableShield(ItemStack stack, ItemStack shield, LivingEntity entity, LivingEntity attacker) {
        return this.canPerformAction(stack, TinkerToolActions.SHIELD_DISABLE);
    }

    public boolean isCorrectToolForDrops(ItemStack stack, BlockState state) {
        return IsEffectiveToolHook.isEffective(ToolStack.from(stack), state);
    }

    public boolean mineBlock(ItemStack stack, Level worldIn, BlockState state, BlockPos pos, LivingEntity entityLiving) {
        return ToolHarvestLogic.mineBlock(stack, worldIn, state, pos, entityLiving);
    }

    public float getDestroySpeed(ItemStack stack, BlockState state) {
        return stack.getCount() == 1 ? MiningSpeedToolHook.getDestroySpeed(stack, state) : 0.0F;
    }

    public boolean onBlockStartBreak(ItemStack stack, BlockPos pos, Player player) {
        return stack.getCount() > 1 || ToolHarvestLogic.handleBlockBreak(stack, pos, player);
    }

    public void inventoryTick(ItemStack stack, Level worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        InventoryTickModifierHook.heldInventoryTick(stack, worldIn, entityIn, itemSlot, isSelected);
    }

    public boolean overrideStackedOnOther(ItemStack held, Slot slot, ClickAction action, Player player) {
        return SlotStackModifierHook.overrideStackedOnOther(held, slot, action, player);
    }

    public boolean overrideOtherStackedOnMe(ItemStack slotStack, ItemStack held, Slot slot, ClickAction action, Player player, SlotAccess access) {
        return SlotStackModifierHook.overrideOtherStackedOnMe(slotStack, held, slot, action, player, access);
    }

    protected static boolean shouldInteract(@Nullable LivingEntity player, ToolStack toolStack, InteractionHand hand) {
        IModDataView volatileData = toolStack.getVolatileData();
        if (volatileData.getBoolean(NO_INTERACTION)) {
            return false;
        } else if (hand == InteractionHand.OFF_HAND) {
            return true;
        } else {
            return player == null || !volatileData.getBoolean(DEFER_OFFHAND) || player.getOffhandItem().isEmpty();
        }
    }

    public InteractionResult onItemUseFirst(ItemStack stack, UseOnContext context) {
        if (stack.getCount() == 1) {
            ToolStack tool = ToolStack.from(stack);
            InteractionHand hand = context.getHand();
            if (shouldInteract(context.getPlayer(), tool, hand)) {
                Iterator var5 = tool.getModifierList().iterator();

                while(var5.hasNext()) {
                    ModifierEntry entry = (ModifierEntry)var5.next();
                    InteractionResult result = entry.getHook(ModifierHooks.BLOCK_INTERACT).beforeBlockUse(tool, entry, context, InteractionSource.RIGHT_CLICK);
                    if (result.consumesAction()) {
                        return result;
                    }
                }
            }
        }

        return InteractionResult.PASS;
    }

    public InteractionResult useOn(UseOnContext context) {
        ItemStack stack = context.getItemInHand();
        if (stack.getCount() == 1) {
            ToolStack tool = ToolStack.from(stack);
            InteractionHand hand = context.getHand();
            if (shouldInteract(context.getPlayer(), tool, hand)) {
                for (ModifierEntry entry : tool.getModifierList()) {
                    InteractionResult result = entry.getHook(ModifierHooks.BLOCK_INTERACT).afterBlockUse(tool, entry, context, InteractionSource.RIGHT_CLICK);
                    if (result.consumesAction()) {
                        return result;
                    }
                }
            }
        }

        return InteractionResult.PASS;
    }

    public InteractionResult interactLivingEntity(ItemStack stack, Player playerIn, LivingEntity target, InteractionHand hand) {
        ToolStack tool = ToolStack.from(stack);
        if (shouldInteract(playerIn, tool, hand)) {
            Iterator var6 = tool.getModifierList().iterator();

            while(var6.hasNext()) {
                ModifierEntry entry = (ModifierEntry)var6.next();
                InteractionResult result = entry.getHook(ModifierHooks.ENTITY_INTERACT).afterEntityUse(tool, entry, playerIn, target, hand, InteractionSource.RIGHT_CLICK);
                if (result.consumesAction()) {
                    return result;
                }
            }
        }

        return InteractionResult.PASS;
    }

    public InteractionResultHolder<ItemStack> use(Level worldIn, Player playerIn, InteractionHand hand) {
        ItemStack stack = playerIn.getItemInHand(hand);
        if (stack.getCount() > 1) {
            return InteractionResultHolder.pass(stack);
        } else {
            ToolStack tool = ToolStack.from(stack);
            if (shouldInteract(playerIn, tool, hand)) {
                for (ModifierEntry entry : tool.getModifierList()) {
                    InteractionResult result = entry.getHook(ModifierHooks.GENERAL_INTERACT).onToolUse(tool, entry, playerIn, hand, InteractionSource.RIGHT_CLICK);
                    if (result.consumesAction()) {
                        return new InteractionResultHolder<>(result, stack);
                    }
                }
            }
            return InteractionResultHolder.pass(stack);
        }
    }

    public void onUseTick(Level pLevel, LivingEntity entityLiving, ItemStack stack, int timeLeft) {
        ToolStack tool = ToolStack.from(stack);
        ModifierEntry activeModifier = GeneralInteractionModifierHook.getActiveModifier(tool);
        GeneralInteractionModifierHook hook = activeModifier.getHook(ModifierHooks.GENERAL_INTERACT);
        int duration = hook.getUseDuration(tool, activeModifier);
        for (ModifierEntry entry : tool.getModifiers()) {
            entry.getHook(ModifierHooks.TOOL_USING).onUsingTick(tool, entry, entityLiving, duration, timeLeft, activeModifier);
        }

        hook.onUsingTick(tool, activeModifier, entityLiving, timeLeft);
    }

    public boolean canContinueUsing(ItemStack oldStack, ItemStack newStack) {
        if (super.canContinueUsing(oldStack, newStack) && oldStack != newStack) {
            GeneralInteractionModifierHook.finishUsing(ToolStack.from(oldStack));
        }

        return super.canContinueUsing(oldStack, newStack);
    }

    public ItemStack finishUsingItem(ItemStack stack, Level worldIn, LivingEntity entityLiving) {
        ToolStack tool = ToolStack.from(stack);
        ModifierEntry activeModifier = GeneralInteractionModifierHook.getActiveModifier(tool);
        GeneralInteractionModifierHook hook = activeModifier.getHook(ModifierHooks.GENERAL_INTERACT);
        int duration = hook.getUseDuration(tool, activeModifier);
        for (ModifierEntry entry : tool.getModifiers()) {
            entry.getHook(ModifierHooks.TOOL_USING).beforeReleaseUsing(tool, entry, entityLiving, duration, 0, activeModifier);
        }
        hook.onFinishUsing(tool, activeModifier, entityLiving);
        return stack;
    }

    public void releaseUsing(ItemStack stack, Level worldIn, LivingEntity entityLiving, int timeLeft) {
        ToolStack tool = ToolStack.from(stack);
        ModifierEntry activeModifier = GeneralInteractionModifierHook.getActiveModifier(tool);
        GeneralInteractionModifierHook hook = activeModifier.getHook(ModifierHooks.GENERAL_INTERACT);
        int duration = hook.getUseDuration(tool, activeModifier);
        for (ModifierEntry entry : tool.getModifiers()) {
            entry.getHook(ModifierHooks.TOOL_USING).beforeReleaseUsing(tool, entry, entityLiving, duration, timeLeft, activeModifier);
        }
        hook.onStoppedUsing(tool, activeModifier, entityLiving, timeLeft);
    }

    public void onStopUsing(ItemStack stack, LivingEntity entity, int timeLeft) {
        ToolStack tool = ToolStack.from(stack);
        UsingToolModifierHook.afterStopUsing(tool, entity, timeLeft);
        GeneralInteractionModifierHook.finishUsing(tool);
    }

    public int getUseDuration(ItemStack stack) {
        ToolStack tool = ToolStack.from(stack);
        ModifierEntry activeModifier = GeneralInteractionModifierHook.getActiveModifier(tool);
        return activeModifier != ModifierEntry.EMPTY ? activeModifier.getHook(ModifierHooks.GENERAL_INTERACT).getUseDuration(tool, activeModifier) : 0;
    }

    public UseAnim getUseAnimation(ItemStack stack) {
        ToolStack tool = ToolStack.from(stack);
        ModifierEntry activeModifier = GeneralInteractionModifierHook.getActiveModifier(tool);
        return activeModifier != ModifierEntry.EMPTY ? activeModifier.getHook(ModifierHooks.GENERAL_INTERACT).getUseAction(tool, activeModifier) : UseAnim.NONE;
    }

    public boolean canPerformAction(ItemStack stack, ToolAction toolAction) {
        return stack.getCount() == 1 && ModifierUtil.canPerformAction(ToolStack.from(stack), toolAction);
    }

    public Component getName(ItemStack stack) {
        return ToolNameHook.getName(this.getToolDefinition(), stack);
    }

    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        TooltipUtil.addInformation(this, stack, level, tooltip, SafeClientAccess.getTooltipKey(), flag);
    }

    public int getDefaultTooltipHideFlags(ItemStack stack) {
        return TooltipUtil.getModifierHideFlags(this.getToolDefinition());
    }

    public ItemStack getRenderTool() {
        if (this.toolForRendering == null) {
            this.toolForRendering = ToolBuildHandler.buildToolForRendering(this, this.getToolDefinition());
        }

        return this.toolForRendering;
    }

    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(ModifiableItemClientExtension.INSTANCE);
    }

    public static boolean shouldCauseReequip(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        if (oldStack == newStack) {
            return false;
        } else if (!slotChanged && oldStack.getItem() == newStack.getItem()) {
            ToolStack oldTool = ToolStack.from(oldStack);
            ToolStack newTool = ToolStack.from(newStack);
            if (!oldTool.getMaterials().equals(newTool.getMaterials())) {
                return true;
            } else if (!oldTool.getModifierList().equals(newTool.getModifierList())) {
                return true;
            } else {
                Multimap<Attribute, AttributeModifier> attributesNew = newStack.getAttributeModifiers(EquipmentSlot.MAINHAND);
                Multimap<Attribute, AttributeModifier> attributesOld = oldStack.getAttributeModifiers(EquipmentSlot.MAINHAND);
                if (attributesNew.size() != attributesOld.size()) {
                    return true;
                } else {
                    for (Attribute attribute : attributesOld.keySet()) {
                        if (!attributesNew.containsKey(attribute)) {
                            return true;
                        }

                        Iterator<AttributeModifier> iter1 = attributesNew.get(attribute).iterator();
                        Iterator<AttributeModifier> iter2 = attributesOld.get(attribute).iterator();

                        while (iter1.hasNext() && iter2.hasNext()) {
                            if (!iter1.next().equals(iter2.next())) {
                                return true;
                            }
                        }
                    }

                    return false;
                }
            }
        } else {
            return true;
        }
    }

    public boolean shouldCauseBlockBreakReset(ItemStack oldStack, ItemStack newStack) {
        return this.shouldCauseReequipAnimation(oldStack, newStack, false);
    }

    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        return shouldCauseReequip(oldStack, newStack, slotChanged);
    }

    public static BlockHitResult blockRayTrace(Level worldIn, Player player, ClipContext.Fluid fluidMode) {
        return Item.getPlayerPOVHitResult(worldIn, player, fluidMode);
    }

    public boolean canAttackBlock(BlockState state, Level worldIn, BlockPos pos, Player player) {
        return false;
    }
}
