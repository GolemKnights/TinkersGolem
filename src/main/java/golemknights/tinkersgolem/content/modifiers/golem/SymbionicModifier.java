package golemknights.tinkersgolem.content.modifiers.golem;

import dev.xkmc.modulargolems.content.core.StatFilterType;
import dev.xkmc.modulargolems.content.entity.common.AbstractGolemEntity;
import dev.xkmc.modulargolems.content.modifier.base.GolemModifier;
import net.minecraft.world.entity.player.Player;

public class SymbionicModifier extends GolemModifier {
    public SymbionicModifier() {
        super(StatFilterType.MASS, 5);
    }
    private static boolean recursive = false;

    @Override
    public void onHealPost(float heal, AbstractGolemEntity<?, ?> golem, int value) {
        if (!recursive) {
            recursive = true;
            float amount = heal * value * 0.2f;
            Player player = golem.getOwner();
            if (player != null) {
                player.heal(amount);
            }
            recursive = false;
        }
    }
}
