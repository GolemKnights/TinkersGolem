package golemknights.tinkersgolem.content.item.weapon;

import dev.xkmc.modulargolems.content.entity.common.AbstractGolemEntity;
import dev.xkmc.modulargolems.content.item.ranged.IGenericShoulderCannonAnimated;
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

public class ModifiableShoulderCannonItem extends ModifiableLauncherItem implements IGenericShoulderCannonAnimated {
   public ModifiableShoulderCannonItem(Properties properties, ToolDefinition toolDefinition) {
      super(properties, toolDefinition);
   }

   @Override
   public void onTick(AbstractGolemEntity<?, ?> e, ItemStack stack, int slot) {
      if (stack.getItem() instanceof IModifiable) {
         ToolStack tool = ToolStack.from(stack);
         if (tool.isBroken())
            return;
         for (ModifierEntry modifier : tool.getModifierList()) {
            modifier.getHook(TGTinkersModifiers.SHOLDER_WEAPON_MODIFIER_HOOK).onTick(tool, modifier, e, stack, slot);
         }
      }
   }

   @Override
   public @NotNull Predicate<ItemStack> getAllSupportedProjectiles() {
      return stack -> true;
   }

   @Override
   public @Nullable ResourceLocation getModel(int slot) {
      return slot < 0 ? TGModelPaths.CANNON_LEFT : TGModelPaths.CANNON_RIGHT;
   }

   @Override
   public boolean emissive() {
      return true;
   }

   @Override
   public ResourceLocation getModelTexture(AbstractGolemEntity<?, ?> abstractGolemEntity, ItemStack itemStack, int i) {
      // 不会被用到
      ResourceLocation id = ForgeRegistries.ITEMS.getKey(this);
      assert id != null;
      return id.withPath((e) -> "textures/equipments/" + e + ".png");
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
            .get((GolemTypes.ENTITY_GOLEM.get()).getDescription().copy().withStyle(ChatFormatting.GOLD))
            .withStyle(ChatFormatting.UNDERLINE));
      tooltip.add(MGLangData.SHOULDER_WEAPON.get());
   }

}
