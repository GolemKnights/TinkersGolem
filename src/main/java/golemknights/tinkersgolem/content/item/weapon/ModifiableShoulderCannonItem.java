package golemknights.tinkersgolem.content.item.weapon;

import dev.xkmc.modulargolems.content.entity.metalgolem.MetalGolemEntity;
import dev.xkmc.modulargolems.content.item.ranged.IShoulderCannonAnimated;
import dev.xkmc.modulargolems.init.data.MGLangData;
import dev.xkmc.modulargolems.init.registrate.GolemTypes;
import golemknights.tinkersgolem.client.TGModelPaths;
import golemknights.tinkersgolem.register.TGTinkersModifiers;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.capability.inventory.ToolInventoryCapability;
import slimeknights.tconstruct.library.tools.definition.ToolDefinition;
import slimeknights.tconstruct.library.tools.item.IModifiable;
import slimeknights.tconstruct.library.tools.item.ranged.ModifiableLauncherItem;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;
import slimeknights.tconstruct.library.utils.Util;

import java.util.List;
import java.util.function.Predicate;

public class ModifiableShoulderCannonItem extends ModifiableLauncherItem implements IShoulderCannonAnimated {
   public ModifiableShoulderCannonItem(Properties properties, ToolDefinition toolDefinition) {
      super(properties, toolDefinition);
   }

   @Override
   public void onTick(MetalGolemEntity e, ItemStack stack, InteractionHand hand) {
      if (stack.getItem() instanceof IModifiable) {
         ToolStack tool = ToolStack.from(stack);
         if (tool.isBroken())
            return;
         for (ModifierEntry modifier : tool.getModifierList()) {
            modifier.getHook(TGTinkersModifiers.SHOLDER_WEAPON_MODIFIER_HOOK).onTick(tool, modifier, e, stack, hand);
         }
      }
   }

   @Override
   public @NotNull Predicate<ItemStack> getAllSupportedProjectiles() {
      return stack -> true;
   }

   @Override
   public @Nullable ResourceLocation getModelForHand(InteractionHand hand) {
      return hand == InteractionHand.MAIN_HAND ? TGModelPaths.CANNON_RIGHT : TGModelPaths.CANNON_LEFT;
   }

   @Override
   public @Nullable ResourceLocation getAnimBaseId(MetalGolemEntity user, ItemStack stack, InteractionHand hand) {
      return hand == InteractionHand.MAIN_HAND ? TGModelPaths.CANNON_RIGHT : TGModelPaths.CANNON_LEFT;
   }

   @Override
   public @NotNull ResourceLocation getEmissiveTexture(MetalGolemEntity entity, ItemStack stack, InteractionHand hand) {
      // 不会被用到
      ResourceLocation id = ForgeRegistries.ITEMS.getKey(this);
      assert id != null;
      return id.withPath((e) -> "textures/equipments/" + e + "_emissive.png");
   }

   @Override
   public @NotNull ResourceLocation getModelTexture(MetalGolemEntity metalGolemEntity, ItemStack itemStack,
         InteractionHand interactionHand) {
      // 不会被用到
      ResourceLocation id = ForgeRegistries.ITEMS.getKey(this);
      assert id != null;
      return id.withPath((e) -> "textures/equipments/" + e + ".png");
   }

   @Override
   public boolean emissive() {
      return true;
   }

   @Override
   public @NotNull UseAnim getUseAnimation(ItemStack pStack) {
      return UseAnim.CUSTOM;
   }

   @Override
   public int getDefaultProjectileRange() {
      return 15;
   }

   @Override
   public InteractionResultHolder<ItemStack> use(Level levelIn, Player playerIn, InteractionHand handIn) {
      if (playerIn.isCrouching()) {
         ItemStack stack = playerIn.getItemInHand(handIn);
         InteractionResult result = ToolInventoryCapability.tryOpenContainer(stack, null, getToolDefinition(), playerIn,
               Util.getSlotType(handIn));
         if (result.consumesAction()) {
            return new InteractionResultHolder<>(result, stack);
         }
      }
      return super.use(levelIn, playerIn, handIn);
   }

   public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
      super.appendHoverText(stack, level, tooltip, flag);
      tooltip.add(MGLangData.GOLEM_EQUIPMENT
            .get(new Object[] {
                  ((EntityType) GolemTypes.ENTITY_GOLEM.get()).getDescription().copy().withStyle(ChatFormatting.GOLD) })
            .withStyle(ChatFormatting.UNDERLINE));
      tooltip.add(MGLangData.SHOULDER_WEAPON.get(new Object[0]));
   }

}
