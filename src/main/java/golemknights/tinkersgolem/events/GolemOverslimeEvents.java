package golemknights.tinkersgolem.events;

import dev.xkmc.modulargolems.init.registrate.GolemTypes;
import golemknights.tinkersgolem.recipes.OverslimeRecoverRecipe;
import golemknights.tinkersgolem.recipes.OverslimeRecoverRecipeCache;
import golemknights.tinkersgolem.register.TGAttributes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;

import static golemknights.tinkersgolem.TinkersGolem.MODID;

@Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class GolemOverslimeEvents {
	public static final String OVERSLIME_KEY = "golem_overslime";

	public static boolean isGolem(LivingEntity entity) {
		return entity.getType().equals(GolemTypes.ENTITY_GOLEM.get())
				|| entity.getType().equals(GolemTypes.ENTITY_HUMANOID.get())
				|| entity.getType().equals(GolemTypes.ENTITY_DOG.get());
	}

	public static float getOverslime(LivingEntity entity) {
		if (isGolem(entity)) {
			return entity.getPersistentData().getFloat(OVERSLIME_KEY);
		} else {
			return 0;
		}
	}

	public static void setOverslime(LivingEntity entity, float amount) {
		if (isGolem(entity)) {
			AttributeInstance attribute = entity.getAttribute(TGAttributes.MAX_OVERSLIME.get());
			if (attribute == null)
				return;
			float value = (float) attribute.getValue();
			entity.getPersistentData().putFloat(OVERSLIME_KEY, Math.min(Math.max(amount, 0), value));
		}
	}

	public static void addOverslime(LivingEntity entity, float amount) {
		setOverslime(entity, getOverslime(entity) + amount);
	}

	public static float onGolemHurt(LivingEntity entity, float damage) {
		if (isGolem(entity)) {
			float slime = getOverslime(entity);
			float newDamage = Math.max(damage - slime, 0);
			addOverslime(entity, newDamage - damage);
			return newDamage;
		}
		return damage;
	}

	@SubscribeEvent
	public static void recoverOverslime(PlayerInteractEvent.EntityInteract event) {
		Entity entity = event.getTarget();
		if (entity instanceof LivingEntity target && isGolem(target)) {
			AttributeInstance attribute = target.getAttribute(TGAttributes.MAX_OVERSLIME.get());
			if (attribute == null)
				return;
			float value = (float) attribute.getValue();
			if (value <= 0)
				return;
			if (value == getOverslime(target))
				return;
			ItemStack item = event.getItemStack();
			List<OverslimeRecoverRecipe> recipes = OverslimeRecoverRecipeCache.findRecipe(target.level().getRecipeManager(), item);
			if (!recipes.isEmpty()) {
				float amount = 0;
				for (OverslimeRecoverRecipe recipe : recipes) {
					if (!recipe.matches(item)) {
						continue;
					}
					amount += recipe.getOutput();
				}
				addOverslime(target, amount);

				ItemStack remaining = item.getCraftingRemainingItem();
				item.shrink(1);
				if (remaining.isEmpty()) {
					return;
				}
				var player = event.getEntity();
				if (item.isEmpty()) {
					player.setItemInHand(event.getHand(), remaining);
					return;
				}
				if (!player.getInventory().add(remaining)) {
					player.drop(remaining, false);
				}
			}
		}
	}
}
