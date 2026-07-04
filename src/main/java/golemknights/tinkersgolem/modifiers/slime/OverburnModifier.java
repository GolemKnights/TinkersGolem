package golemknights.tinkersgolem.modifiers.slime;

import dev.xkmc.modulargolems.content.core.StatFilterType;
import dev.xkmc.modulargolems.content.entity.common.AbstractGolemEntity;
import golemknights.tinkersgolem.data.TGConfig;
import golemknights.tinkersgolem.entity.SlimeGolemEntity;
import golemknights.tinkersgolem.events.GolemOverslimeEvents;
import golemknights.tinkersgolem.register.TGAttributes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.recipe.fuel.MeltingFuel;
import slimeknights.tconstruct.library.recipe.fuel.MeltingFuelLookup;
import slimeknights.tconstruct.library.tools.nbt.ModDataNBT;

import javax.annotation.Nullable;

public class OverburnModifier extends SlimeModifier {
	public OverburnModifier() {
		super(StatFilterType.MASS, 5);
	}

	/**
	 * Keeps track of fuel consumed on the slime
	 *
	 * @param expiration Tick when this fuel info is discarded
	 * @param rate       Fuel rate, gives more or less overslime
	 */
	private record FuelInfo(long expiration, int rate) {
		private static final String EXPIRATION = "expiration";
		private static final String RATE = "rate";

		/**
		 * Reads the info from the slime
		 */
		@Nullable
		public static FuelInfo read(SlimeGolemEntity slime, ResourceLocation location) {
			ModDataNBT persistentData = ModDataNBT.readFromNBT(slime.getPersistentData());
			if (persistentData.contains(location, Tag.TAG_COMPOUND)) {
				CompoundTag tag = persistentData.getCompound(location);
				return new FuelInfo(tag.getLong(EXPIRATION), tag.getInt(RATE));
			}
			return null;
		}

		/**
		 * Writes the info to the slime
		 */
		public void write(SlimeGolemEntity slime, ResourceLocation location) {
			CompoundTag tag = new CompoundTag();
			tag.putLong(EXPIRATION, expiration);
			tag.putInt(RATE, rate);
			slime.getPersistentData().put(String.valueOf(location), tag);
		}
	}

	@Override
	public void onAiStep(AbstractGolemEntity<?, ?> golem, int level) {
		SlimeGolemEntity slime = (SlimeGolemEntity) golem;
		int interval = TGConfig.COMMON.overburnInterval.get();
		int updateInterval = interval << (6 - level);
		if (golem.tickCount % updateInterval == 0 && GolemOverslimeEvents.getOverslime(golem) < golem.getAttribute(TGAttributes.MAX_OVERSLIME.get()).getValue()) {
			// find current fuel info
			ResourceLocation key = this.getRegistryName();
			FuelInfo info = FuelInfo.read(slime, key);

			// if we have no fuel, try and find some
			boolean neededFuel = info == null;
			// since we will write this to NBT, use game time as that is global
			long time = golem.level().getGameTime();
			if (neededFuel || info.expiration < time) {
				info = null;
				FluidStack fluid = slime.getFluid();
				if (!fluid.isEmpty()) {
					MeltingFuel fuel = MeltingFuelLookup.findFuel(fluid.getFluid());
					if (fuel != null) {
						// scale amount consumed by level
						int amount = fuel.getAmount(fluid.getFluid());
						// scale up fuel duration so we always get the same amount of overslime per fuel bucket
						// if we didn't do this, lower levels would consume way more than higher ones
						// this works out to equivalent to the alloyer/melter at level 4
						// note this does mean changing trait levels multiplies fuel efficiency, but the part swap costs more than just adding a slimeball so its fine
						int burnRate = TGConfig.COMMON.overburnSpeed.get();
						int duration = fuel.getDuration() * updateInterval / burnRate;
						// if we don't have a full recipe, use what is left but scale down the duration
						if (amount > fluid.getAmount()) {
							slime.setFluid(FluidStack.EMPTY);
							duration = duration * fluid.getAmount() / amount;
						} else {
							// if we have a complete recipe, just decrease fluid in the tank
							fluid.shrink(amount);
							slime.setFluid(fluid);
						}
						info = new FuelInfo(time + duration, fuel.getRate());
					}
				}
				if (info != null && info.expiration >= time + updateInterval) {
					info.write(slime, key);
				} else if (!neededFuel) {
					// no need to remove if we had nothing, saves some hash map modifications
					slime.getPersistentData().remove(String.valueOf(key));
				}
			}
			if (info != null) {
				double rate = TGConfig.COMMON.overburnHealRate.get();
				double amount = info.rate * rate;
				int restore = (int) amount;
				if (Modifier.RANDOM.nextDouble() < amount - restore) {
					restore++;
				}
				GolemOverslimeEvents.addOverslime(golem, restore);
			}
		}
	}
}
