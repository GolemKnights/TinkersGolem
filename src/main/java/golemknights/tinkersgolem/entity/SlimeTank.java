package golemknights.tinkersgolem.entity;

import dev.xkmc.l2library.base.tile.BaseContainerListener;
import dev.xkmc.l2serial.serialization.codec.AliasCollection;
import net.minecraft.core.NonNullList;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BooleanSupplier;
import java.util.function.Predicate;

public class SlimeTank implements IFluidHandler, AliasCollection<FluidStack> {

	private final int size;
	private final SlimeGolemEntity golem;
	private final List<BaseContainerListener> listeners = new ArrayList<>();
	private Predicate<FluidStack> allowInsert = (e) -> true;
	private BooleanSupplier allowExtract = () -> true;
	public NonNullList<FluidStack> list;
	private int maxDrain;

	public SlimeTank(SlimeGolemEntity golem, int size) {
		this.size = size;
		this.golem = golem;
		this.list = NonNullList.withSize(size, FluidStack.EMPTY);
	}

	public SlimeTank add(BaseContainerListener listener) {
		this.listeners.add(listener);
		return this;
	}

	public SlimeTank setAllowInsert(Predicate<FluidStack> predicate) {
		this.allowInsert = predicate;
		return this;
	}

	public SlimeTank setAllowExtract(BooleanSupplier allowExtract) {
		this.allowExtract = allowExtract;
		return this;
	}

	public SlimeTank setMaxDrain(int max) {
		this.maxDrain = max;
		return this;
	}

	public int getTanks() {
		return this.size;
	}

	public @NotNull FluidStack getFluidInTank(int tank) {
		return (FluidStack) this.list.get(tank).copy();
	}

	public int getTankCapacity(int tank) {
		return this.golem.getTankCapacity();
	}

	public boolean isFluidValid(int tank, @NotNull FluidStack stack) {
		return true;
	}

	public int fill(FluidStack resource, IFluidHandler.FluidAction action) {
		if (resource.isEmpty()) {
			return 0;
		} else if (!this.allowInsert.test(resource)) {
			return 0;
		} else {
			int to_fill = this.maxDrain == 0 ? resource.getAmount() : (resource.getAmount() >= this.maxDrain ? this.maxDrain : 0);
			if (to_fill == 0) {
				return 0;
			} else {
				int filled = 0;

				for (int i = 0; i < this.size; ++i) {
					FluidStack stack = this.list.get(i);
					if (stack.isFluidEqual(resource)) {
						int remain = this.getTankCapacity(i) - stack.getAmount();
						int fill = Math.min(to_fill, remain);
						filled += fill;
						to_fill -= fill;
						if (action == FluidAction.EXECUTE) {
							resource.shrink(fill);
							stack.grow(fill);
						}
					} else if (stack.isEmpty()) {
						int fill = Math.min(to_fill, this.getTankCapacity(i));
						filled += fill;
						to_fill -= fill;
						if (action == FluidAction.EXECUTE) {
							FluidStack rep = resource.copy();
							rep.setAmount(fill);
							this.list.set(i, rep);
							resource.shrink(fill);
						}
					}

					if (resource.isEmpty() || to_fill == 0) {
						break;
					}
				}

				if (action == FluidAction.EXECUTE && filled > 0) {
					this.setChanged();
				}

				return filled;
			}
		}
	}

	public @NotNull FluidStack drain(FluidStack resource, IFluidHandler.FluidAction action) {
		if (resource.isEmpty()) {
			return resource;
		} else if (!this.allowExtract.getAsBoolean()) {
			return FluidStack.EMPTY;
		} else {
			int to_drain = resource.getAmount();
			if (this.maxDrain > 0) {
				if (to_drain < this.maxDrain) {
					return FluidStack.EMPTY;
				}

				to_drain = this.maxDrain;
			}

			int drained = 0;

			for (int i = 0; i < this.size; ++i) {
				FluidStack stack = (FluidStack) this.list.get(i);
				if (stack.isFluidEqual(resource)) {
					int remain = stack.getAmount();
					int drain = Math.min(to_drain, remain);
					drained += drain;
					to_drain -= drain;
					if (action == FluidAction.EXECUTE) {
						stack.shrink(drain);
					}
				}

				if (to_drain == 0) {
					break;
				}
			}

			if (action == FluidAction.EXECUTE && drained > 0) {
				this.setChanged();
			}

			FluidStack ans = resource.copy();
			ans.setAmount(drained);
			return ans;
		}
	}

	public @NotNull FluidStack drain(int maxDrain, IFluidHandler.FluidAction action) {
		if (!this.allowExtract.getAsBoolean()) {
			return FluidStack.EMPTY;
		} else {
			FluidStack ans = null;
			int to_drain = maxDrain;
			if (this.maxDrain > 0) {
				if (maxDrain < this.maxDrain) {
					return FluidStack.EMPTY;
				}

				to_drain = this.maxDrain;
			}

			int drained = 0;

			for (int i = 0; i < this.size; ++i) {
				FluidStack stack = (FluidStack) this.list.get(i);
				if (!stack.isEmpty() && (ans == null || stack.isFluidEqual(ans))) {
					int remain = stack.getAmount();
					int drain = Math.min(to_drain, remain);
					drained += drain;
					to_drain -= drain;
					if (ans == null) {
						ans = stack.copy();
					}

					if (action == FluidAction.EXECUTE) {
						stack.shrink(drain);
					}
				}

				if (to_drain == 0) {
					break;
				}
			}

			if (action == FluidAction.EXECUTE && drained > 0) {
				this.setChanged();
			}

			if (ans == null) {
				return FluidStack.EMPTY;
			} else {
				ans.setAmount(drained);
				return ans;
			}
		}
	}

	public void setChanged() {
		this.listeners.forEach(BaseContainerListener::notifyTile);
	}

	public List<FluidStack> getAsList() {
		return this.list;
	}

	public void clear() {
		this.list.clear();
	}

	public void set(int n, int i, FluidStack elem) {
		this.list.set(i, elem);
	}

	public Class<FluidStack> getElemClass() {
		return FluidStack.class;
	}

	public boolean isEmpty() {
		for (FluidStack stack : this.list) {
			if (!stack.isEmpty()) {
				return false;
			}
		}

		return true;
	}
}
