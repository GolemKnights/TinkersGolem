package golemknights.tinkersgolem.item.weapon;

import dev.xkmc.modulargolems.content.client.armor.GolemModelPaths;
import dev.xkmc.modulargolems.content.entity.metalgolem.MetalGolemEntity;
import dev.xkmc.modulargolems.content.item.ranged.IShoulderCannonAnimated;
import golemknights.tinkersgolem.register.TGTinkersModifiers;

import java.util.function.Predicate;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.UseAnim;
import net.minecraftforge.registries.ForgeRegistries;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.definition.ToolDefinition;
import slimeknights.tconstruct.library.tools.item.IModifiable;
import slimeknights.tconstruct.library.tools.item.ranged.ModifiableLauncherItem;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;

import org.jetbrains.annotations.Nullable;

public class SlimeFireballShooterItem extends ModifiableLauncherItem implements IShoulderCannonAnimated {
   public SlimeFireballShooterItem(Properties properties, ToolDefinition toolDefinition) {
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

   private static boolean supports(ItemStack stack) {
      return stack.is(Items.BLAZE_POWDER) || stack.is(Items.FIRE_CHARGE) || stack.is(Items.TNT);
   }

   public Predicate<ItemStack> getAllSupportedProjectiles() {
      return SlimeFireballShooterItem::supports;
   }

   public @Nullable ResourceLocation getModelForHand(InteractionHand hand) {
      return hand == InteractionHand.MAIN_HAND ? GolemModelPaths.FLAME_RIGHT : GolemModelPaths.FLAME_LEFT;
   }

   public @Nullable ResourceLocation getAnimBaseId(MetalGolemEntity user, ItemStack stack, InteractionHand hand) {
      return hand == InteractionHand.MAIN_HAND ? GolemModelPaths.BEACON_RIGHT : GolemModelPaths.BEACON_LEFT;
   }

   public boolean emissive() {
      return true;
   }

   public ResourceLocation getEmissiveTexture(MetalGolemEntity entity, ItemStack stack, InteractionHand hand) {
      ResourceLocation id = ForgeRegistries.ITEMS.getKey(this);

      assert id != null;

      return id.withPath((e) -> "textures/equipments/" + e + "_emissive.png");
   }

   public ResourceLocation getModelTexture(MetalGolemEntity entity, ItemStack stack, InteractionHand hand) {
      ResourceLocation id = ForgeRegistries.ITEMS.getKey(this);

      assert id != null;

      return id.withPath((e) -> "textures/equipments/" + e + ".png");
   }

   @Override
   public UseAnim getUseAnimation(ItemStack pStack) {
      return UseAnim.CUSTOM;
   }

   @Override
   public int getDefaultProjectileRange() {
      return 15;
   }
}
