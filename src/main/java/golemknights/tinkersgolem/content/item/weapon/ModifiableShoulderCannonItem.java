package golemknights.tinkersgolem.content.item.weapon;

import dev.xkmc.modulargolems.content.client.armor.GolemModelPaths;
import dev.xkmc.modulargolems.content.entity.metalgolem.MetalGolemEntity;
import dev.xkmc.modulargolems.content.item.ranged.IShoulderCannonAnimated;
import golemknights.tinkersgolem.register.TGTinkersModifiers;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.UseAnim;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.definition.ToolDefinition;
import slimeknights.tconstruct.library.tools.item.IModifiable;
import slimeknights.tconstruct.library.tools.item.ranged.ModifiableLauncherItem;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;

import java.util.function.Predicate;

public class ModifiableShoulderCannonItem extends ModifiableLauncherItem implements IShoulderCannonAnimated {
   public ModifiableShoulderCannonItem(Properties properties, ToolDefinition toolDefinition) {
      super(properties, toolDefinition);
   }

   public void onTick(MetalGolemEntity e, ItemStack stack, InteractionHand hand) {
      if (stack.getItem() instanceof IModifiable) {
         ToolStack tool = ToolStack.from(stack);
         if(tool.isBroken()) return;
         for (ModifierEntry modifier : tool.getModifierList()) {
            modifier.getHook(TGTinkersModifiers.SHOLDER_WEAPON_MODIFIER_HOOK).onTick(tool, modifier, e, stack, hand);
         }
      }
   }

   public @NotNull Predicate<ItemStack> getAllSupportedProjectiles() {
      return stack -> true;
   }

   public @Nullable ResourceLocation getModelForHand(InteractionHand hand) {
      return hand == InteractionHand.MAIN_HAND ? GolemModelPaths.FLAME_RIGHT : GolemModelPaths.FLAME_LEFT;
   }

   public @Nullable ResourceLocation getAnimBaseId(MetalGolemEntity user, ItemStack stack, InteractionHand hand) {
      return hand == InteractionHand.MAIN_HAND ? GolemModelPaths.BEACON_RIGHT : GolemModelPaths.BEACON_LEFT;
   }

   public @NotNull ResourceLocation getEmissiveTexture(MetalGolemEntity entity, ItemStack stack, InteractionHand hand) {
      ResourceLocation id = ForgeRegistries.ITEMS.getKey(this);

      assert id != null;

      return id.withPath((e) -> "textures/equipments/" + e + "_emissive.png");
   }

   @Override
   public @NotNull ResourceLocation getModelTexture(MetalGolemEntity metalGolemEntity, ItemStack itemStack, InteractionHand interactionHand) {
      ResourceLocation id = ForgeRegistries.ITEMS.getKey(this);
      assert id != null;
      return id.withPath((e) -> "textures/equipments/" + e + ".png");
   }

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
}
