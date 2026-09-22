package golemknights.tinkersgolem.mixinhelper;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Method;

import net.minecraft.world.item.ItemStack;
import slimeknights.tconstruct.tools.modules.interaction.FireballModule;

public class FireballTypeHelper {
    /** 缓存的 getFireballType 方法句柄 */
    private static MethodHandle getFireballTypeHandle;

    /** 懒加载方法句柄 */
    public static MethodHandle getFireballTypeHandle() {
        if (getFireballTypeHandle == null) {
            try {
                Method m = FireballModule.class.getDeclaredMethod("getFireballType", ItemStack.class);
                m.setAccessible(true);
                getFireballTypeHandle = MethodHandles.lookup().unreflect(m);
            } catch (ReflectiveOperationException e) {
                throw new RuntimeException("Failed to bind FireballModule#getFireballType", e);
            }
        }
        return getFireballTypeHandle;
    }
}
