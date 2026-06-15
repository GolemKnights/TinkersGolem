package golemknights.tinkersgolem.item.armor;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import dev.xkmc.modulargolems.content.item.equipments.DogGolemArmorItem;
import dev.xkmc.modulargolems.content.item.equipments.DogGolemArmorSpecialRenderer;
import golemknights.tinkersgolem.client.render.ModifiableDogGolemArmorRender;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
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
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.ToolAction;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import slimeknights.mantle.client.SafeClientAccess;
import slimeknights.mantle.client.TooltipKey;
import slimeknights.tconstruct.common.Sounds;
import slimeknights.tconstruct.library.client.armor.ArmorModelManager;
import slimeknights.tconstruct.library.client.armor.ArmorModelManager.ArmorModel;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.behavior.EnchantmentModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.display.DurabilityDisplayModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.interaction.SlotStackModifierHook;
import slimeknights.tconstruct.library.modifiers.modules.build.RarityModule;
import slimeknights.tconstruct.library.tools.IndestructibleItemEntity;
import slimeknights.tconstruct.library.tools.capability.ToolCapabilityProvider;
import slimeknights.tconstruct.library.tools.capability.inventory.ToolInventoryCapability;
import slimeknights.tconstruct.library.tools.definition.ModifiableArmorMaterial;
import slimeknights.tconstruct.library.tools.definition.ToolDefinition;
import slimeknights.tconstruct.library.tools.definition.module.display.ToolNameHook;
import slimeknights.tconstruct.library.tools.helper.ModifierUtil;
import slimeknights.tconstruct.library.tools.helper.ToolBuildHandler;
import slimeknights.tconstruct.library.tools.helper.ToolDamageUtil;
import slimeknights.tconstruct.library.tools.helper.TooltipUtil;
import slimeknights.tconstruct.library.tools.item.IModifiableDisplay;
import slimeknights.tconstruct.library.tools.item.armor.DummyArmorMaterial;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.StatsNBT;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;
import slimeknights.tconstruct.library.tools.stat.ToolStats;
import slimeknights.tconstruct.library.utils.Util;

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import static golemknights.tinkersgolem.TinkersGolem.LOGGER;
import static golemknights.tinkersgolem.TinkersGolem.getResource;
import static golemknights.tinkersgolem.register.TGItems.DOG_GOLEM_ARMOR;
import static slimeknights.tconstruct.library.tools.item.armor.ModifiableArmorItem.*;

public class ModifiableDogGolemArmorItem extends DogGolemArmorItem
		implements IModifiableDisplay, DogGolemArmorSpecialRenderer.ProviderItem {

	private static final DummyArmorMaterial material = new DummyArmorMaterial(getResource(DOG_GOLEM_ARMOR),
			Sounds.EQUIP_PLATE.getSound());

	public ModifiableDogGolemArmorItem(Properties properties,
	                                   ToolDefinition toolDefinition, ResourceLocation textureModel) {
		super(properties, material, 0, 0);
		this.toolDefinition = toolDefinition;
		this.textureModel = textureModel;
	}

	public ModifiableDogGolemArmorItem(Properties properties, ModifiableArmorMaterial material,
	                                   ResourceLocation textureModel) {
		this(properties, Objects.requireNonNull(material.getArmorDefinition(ArmorItem.Type.CHESTPLATE),
				"Missing tool definition for chest armor"), textureModel);
	}

	private final ResourceLocation textureModel;

	// @Getter
	private final ToolDefinition toolDefinition;

	public ToolDefinition getToolDefinition() {
		return toolDefinition;
	}

	/**
	 * Cache of the tool built for rendering
	 */
	private ItemStack toolForRendering = null;

	/* Basic properties */

	@Override
	public int getMaxStackSize(ItemStack stack) {

		return 1;
	}

	@Override
	public boolean makesPiglinsNeutral(ItemStack stack, LivingEntity wearer) {
		return ModifierUtil.checkVolatileFlag(stack, PIGLIN_NEUTRAL);
	}

	@Override
	public boolean canWalkOnPowderedSnow(ItemStack stack, LivingEntity wearer) {
		return getSlot() == EquipmentSlot.FEET && ModifierUtil.checkVolatileFlag(stack, SNOW_BOOTS);
	}

	@Override
	public boolean isEnderMask(ItemStack stack, Player player, EnderMan endermanEntity) {
		return getSlot() == EquipmentSlot.HEAD && ModifierUtil.checkVolatileFlag(stack, ENDERMASK);
	}

	@Override
	public boolean canPerformAction(ItemStack stack, ToolAction toolAction) {
		return ModifierUtil.canPerformAction(ToolStack.from(stack), toolAction);
	}

	@Override
	public boolean isNotReplaceableByPickAction(ItemStack stack, Player player, int inventorySlot) {
		return true;
	}

	/* Enchantments */

	@Override
	public boolean isEnchantable(ItemStack stack) {
		return false;
	}

	@Override
	public boolean isBookEnchantable(ItemStack stack, ItemStack book) {
		return false;
	}

	@Override
	public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
		return enchantment.isCurse() && super.canApplyAtEnchantingTable(stack, enchantment);
	}

	@Override
	public int getEnchantmentLevel(ItemStack stack, Enchantment enchantment) {
		return EnchantmentModifierHook.getEnchantmentLevel(stack, enchantment);
	}

	@Override
	public Map<Enchantment, Integer> getAllEnchantments(ItemStack stack) {
		return EnchantmentModifierHook.getAllEnchantments(stack);
	}

	/* Loading */

	@Nullable
	@Override
	public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundTag nbt) {
		return new ToolCapabilityProvider(stack);
	}

	@Override
	public void verifyTagAfterLoad(CompoundTag nbt) {
		ToolStack.verifyTag(this, nbt, getToolDefinition());
	}

	@Override
	public void onCraftedBy(ItemStack stack, Level levelIn, Player playerIn) {
		ToolStack.ensureInitialized(stack, getToolDefinition());
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level levelIn, Player playerIn, InteractionHand handIn) {
		if (playerIn.isCrouching()) {
			ItemStack stack = playerIn.getItemInHand(handIn);
			InteractionResult result = ToolInventoryCapability.tryOpenContainer(stack, null, getToolDefinition(),
					playerIn, Util.getSlotType(handIn));
			if (result.consumesAction()) {
				return new InteractionResultHolder<>(result, stack);
			}
		}
		return super.use(levelIn, playerIn, handIn);
	}

	/* Display */

	@Override
	public boolean isFoil(ItemStack stack) {
		// we use enchantments to handle some modifiers, so don't glow from them
		// however, if a modifier wants to glow let them
		return ModifierUtil.checkVolatileFlag(stack, SHINY);
	}

	@Override
	public Rarity getRarity(ItemStack stack) {
		return RarityModule.getRarity(stack);
	}

	/* Indestructible items */

	@Override
	public boolean hasCustomEntity(ItemStack stack) {
		return IndestructibleItemEntity.hasCustomEntity(stack);
	}

	@Nullable
	@Override
	public Entity createEntity(Level level, Entity original, ItemStack stack) {
		return IndestructibleItemEntity.createFrom(level, original, stack);
	}

	/* Damage/Durability */

	@Override
	public boolean isRepairable(ItemStack stack) {
		// handle in the tinker station
		return false;
	}

	@Override
	public boolean canBeDepleted() {
		return true;
	}

	@Override
	public int getMaxDamage(ItemStack stack) {
		return ToolDamageUtil.getFakeMaxDamage(stack);
	}

	@Override
	public int getDamage(ItemStack stack) {
		if (!canBeDepleted()) {
			return 0;
		}
		return ToolStack.from(stack).getDamage();
	}

	@Override
	public void setDamage(ItemStack stack, int damage) {
		if (canBeDepleted()) {
			ToolStack.from(stack).setDamage(damage);
		}
	}

	@Override
	public <T extends LivingEntity> int damageItem(ItemStack stack, int amount, T damager, Consumer<T> onBroken) {
		// We basically emulate Itemstack.damageItem here. We always return 0 to skip
		// the handling in ItemStack.
		// If we don't tools ignore our damage logic
		if (canBeDepleted() && ToolDamageUtil.damage(ToolStack.from(stack), amount, damager, stack)) {
			onBroken.accept(damager);
		}

		return 0;
	}

	/* Durability display */

	@Override
	public boolean isBarVisible(ItemStack pStack) {
		return DurabilityDisplayModifierHook.showDurabilityBar(pStack);
	}

	@Override
	public int getBarColor(ItemStack pStack) {
		return DurabilityDisplayModifierHook.getDurabilityRGB(pStack);
	}

	@Override
	public int getBarWidth(ItemStack pStack) {
		return DurabilityDisplayModifierHook.getDurabilityWidth(pStack);
	}

	/* Armor properties */

	@Override
	public boolean isValidRepairItem(ItemStack toRepair, ItemStack repair) {
		return false;
	}

	@Override
	public Multimap<Attribute, AttributeModifier> getAttributeModifiers(IToolStackView tool, EquipmentSlot slot) {
		if (slot != getSlot()) {
			return ImmutableMultimap.of();
		}

		ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
		if (!tool.isBroken()) {
			// base stats
			StatsNBT statsNBT = tool.getStats();
			UUID uuid = UUID.get(slot);
			builder.put(Attributes.ARMOR, new AttributeModifier(uuid, "tconstruct.armor.armor",
					statsNBT.get(ToolStats.ARMOR), AttributeModifier.Operation.ADDITION));
			builder.put(Attributes.ARMOR_TOUGHNESS, new AttributeModifier(uuid, "tconstruct.armor.toughness",
					statsNBT.get(ToolStats.ARMOR_TOUGHNESS), AttributeModifier.Operation.ADDITION));
			double knockbackResistance = statsNBT.get(ToolStats.KNOCKBACK_RESISTANCE);
			if (knockbackResistance != 0) {
				builder.put(Attributes.KNOCKBACK_RESISTANCE,
						new AttributeModifier(uuid, "tconstruct.armor.knockback_resistance", knockbackResistance,
								AttributeModifier.Operation.ADDITION));
			}
			// grab attributes from modifiers
			BiConsumer<Attribute, AttributeModifier> attributeConsumer = builder::put;
			for (ModifierEntry entry : tool.getModifierList()) {
				entry.getHook(ModifierHooks.ATTRIBUTES).addAttributes(tool, entry, slot, attributeConsumer);
			}
		}

		return builder.build();
	}

	@Override
	public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot, ItemStack stack) {
		CompoundTag nbt = stack.getTag();
		if (slot != getEquipmentSlot(stack) || nbt == null) {
			return ImmutableMultimap.of();
		}
		return getAttributeModifiers(ToolStack.from(stack), slot);
	}

	/* Ticking */

	@Override
	public void inventoryTick(ItemStack stack, Level levelIn, Entity entityIn, int itemSlot, boolean isSelected) {
		super.inventoryTick(stack, levelIn, entityIn, itemSlot, isSelected);

		// don't care about non-living, they skip most tool context
		if (entityIn instanceof LivingEntity living) {
			ToolStack tool = ToolStack.from(stack);
			if (!levelIn.isClientSide) {
				tool.ensureHasData();
			}
			List<ModifierEntry> modifiers = tool.getModifierList();
			if (!modifiers.isEmpty()) {
				var slot = getEquipmentSlot(stack);
				boolean isCorrectSlot = slot != null && living.getItemBySlot(slot) == stack;
				// we pass in the stack for most custom context, but for the sake of armor its
				// easier to tell them that this is the correct slot for effects
				for (ModifierEntry entry : modifiers) {
					entry.getHook(ModifierHooks.INVENTORY_TICK).onInventoryTick(tool, entry, levelIn, living, itemSlot,
							isSelected, isCorrectSlot, stack);
				}
			}
		}
	}

	@Override
	public boolean overrideStackedOnOther(ItemStack held, Slot slot, ClickAction action, Player player) {
		return SlotStackModifierHook.overrideStackedOnOther(held, slot, action, player)
				|| super.overrideStackedOnOther(held, slot, action, player);
	}

	@Override
	public boolean overrideOtherStackedOnMe(ItemStack slotStack, ItemStack held, Slot slot, ClickAction action,
	                                        Player player, SlotAccess access) {
		return SlotStackModifierHook.overrideOtherStackedOnMe(slotStack, held, slot, action, player, access)
				|| super.overrideOtherStackedOnMe(slotStack, held, slot, action, player, access);
	}

	/* Tooltips */

	@Override
	public Component getName(ItemStack stack) {
		return ToolNameHook.getName(getToolDefinition(), stack);
	}

	@Override
	public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
		TooltipUtil.addInformation(this, stack, level, tooltip, SafeClientAccess.getTooltipKey(), flag);
	}

	@Override
	public List<Component> getStatInformation(IToolStackView tool, @Nullable Player player, List<Component> tooltips,
	                                          TooltipKey key, TooltipFlag tooltipFlag) {
		tooltips = TooltipUtil.getArmorStats(tool, player, tooltips, key, tooltipFlag);
		TooltipUtil.addAttributes(this, tool, player, tooltips, TooltipUtil.SHOW_ARMOR_ATTRIBUTES, getSlot());
		return tooltips;
	}

	@Override
	public int getDefaultTooltipHideFlags(ItemStack stack) {
		return TooltipUtil.getModifierHideFlags(getToolDefinition());
	}

	/* Display items */

	@Override
	public ItemStack getRenderTool() {
		if (toolForRendering == null) {
			toolForRendering = ToolBuildHandler.buildToolForRendering(this, this.getToolDefinition());
		}
		return toolForRendering;
	}

	@Override
	public ResourceLocation getModelTexture(LivingEntity user, boolean override) {
		LOGGER.warn("getModelTexture not implemented");
		return null;//FIXME
	}

	@Override
	public int getColor(ItemStack stack) {
		LOGGER.warn("getColor not implemented");
		return -1;
	}

	@Override
	public Optional<DogGolemArmorSpecialRenderer> getSpecialRenderer() {
		return Optional.of(ModifiableDogGolemArmorRender.INSTANCE);

	}

	private ArmorModel armorModel;

	protected ResourceLocation getName() {
		return textureModel;
	}

	/**
	 * Fetches the model from the cache
	 */
	public ArmorModel getModel(ItemStack stack) {
		if (armorModel == null) {
			armorModel = ArmorModelManager.INSTANCE.getModel(getName());
			if (armorModel == ArmorModel.EMPTY) {
				LOGGER.warn("Failed to find armor model {}, will skip rendering {}", getName(), stack);
			}
		}
		return armorModel;
	}
}
