package tweakyllama.cartomation.cart.entity;

import net.minecraft.util.Direction;

public interface HoldableMinecartEntity {
    /**
     * Called every tick the minecart is on a holding rail.
     */
    void onHoldingRailPass(int x, int y, int z, boolean receivingPower, Direction direction);
}
