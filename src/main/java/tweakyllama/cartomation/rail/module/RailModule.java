package tweakyllama.cartomation.rail.module;

import net.minecraft.block.Block;
import tweakyllama.cartomation.base.module.Module;
import tweakyllama.cartomation.rail.block.HoldingRailBlock;

public class RailModule extends Module {

    public static Block holdingRail;

    @Override
    public void construct() {
        holdingRail = new HoldingRailBlock();
    }
}
