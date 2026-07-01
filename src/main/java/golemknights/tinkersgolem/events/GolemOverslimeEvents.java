package golemknights.tinkersgolem.events;

import dev.xkmc.modulargolems.content.entity.common.AbstractGolemEntity;
import golemknights.tinkersgolem.TinkersGolem;
import golemknights.tinkersgolem.cap.OverslimeCap;
import golemknights.tinkersgolem.entity.SlimeGolemEntity;
import golemknights.tinkersgolem.entity.SlimeTankSyncPacket;
import golemknights.tinkersgolem.recipes.OverslimeRecoverRecipe;
import golemknights.tinkersgolem.recipes.OverslimeRecoverRecipeCache;
import golemknights.tinkersgolem.register.TGAttributes;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;

import static golemknights.tinkersgolem.TinkersGolem.MODID;

@Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class GolemOverslimeEvents {
	public static final String OVERSLIME_KEY = "golem_overslime";

	public static boolean isGolem(LivingEntity entity) {
		return entity instanceof AbstractGolemEntity<?, ?>;
	}

	public static float getOverslime(LivingEntity entity) {
		if (entity instanceof AbstractGolemEntity<?, ?> golem) {
			return OverslimeCap.HOLDER.get(golem).overslime;
		} else {
			return 0;
		}
	}

	public static void setOverslime(LivingEntity entity, float amount) {
		if (entity instanceof AbstractGolemEntity<?, ?> golem) {
			AttributeInstance attribute = entity.getAttribute(TGAttributes.MAX_OVERSLIME.get());
			if (attribute == null)
				return;
			float value = (float) attribute.getValue();
			var cap = OverslimeCap.HOLDER.get(golem);
			cap.overslime = Math.min(Math.max(amount, 0), value);
			if (!entity.level().isClientSide()) {
				cap.sync(golem);
			}
		}
	}

	public static void addOverslime(LivingEntity entity, float amount) {
		if (amount > 0) {
			var ins = entity.getAttribute(TGAttributes.OVERSLIME_RECOVERY.get());
			if (ins != null)
				amount *= (float) ins.getValue();
		}
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
		if (!(entity instanceof AbstractGolemEntity<?, ?> target)) return;
		AttributeInstance attribute = target.getAttribute(TGAttributes.MAX_OVERSLIME.get());
		if (attribute == null) return;
		float value = (float) attribute.getValue();
		if (value <= 0) return;
		if (value <= getOverslime(target)) return;
		ItemStack item = event.getItemStack();
		if (item.isEmpty()) return;
		List<OverslimeRecoverRecipe> recipes = OverslimeRecoverRecipeCache.findRecipe(target.level().getRecipeManager(), item);
		if (recipes.isEmpty()) return;
		float amount = 0;
		for (OverslimeRecoverRecipe recipe : recipes) {
			if (!recipe.matches(item)) {
				continue;
			}
			amount = Math.max(amount, recipe.getOutput());
		}
		if (amount <= 0) return;
		addOverslime(target, amount);
		ItemStack remaining = item.getCraftingRemainingItem();
		item.shrink(1);
		event.setCanceled(true);
		event.setCancellationResult(InteractionResult.SUCCESS);
		if (remaining.isEmpty()) return;
		var player = event.getEntity();
		if (item.isEmpty()) {
			player.setItemInHand(event.getHand(), remaining);
			return;
		}
		if (!player.getInventory().add(remaining)) {
			player.drop(remaining, false);
		}
	}

	@SubscribeEvent
	public static void onStartTracking(PlayerEvent.StartTracking event) {
		if (event.getTarget() instanceof AbstractGolemEntity<?, ?> entity) {
			if (event.getEntity() instanceof ServerPlayer player) {
				OverslimeCap.HOLDER.get(entity).syncToPlayer(entity, player);
				if (entity instanceof SlimeGolemEntity slime) {
					TinkersGolem.HANDLER.toClientPlayer(new SlimeTankSyncPacket(slime, slime.tank), player);
				}
			}
		}
	}

}
