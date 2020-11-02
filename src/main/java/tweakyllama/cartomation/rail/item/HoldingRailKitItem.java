package tweakyllama.cartomation.rail.item;

import net.minecraft.block.Block;
import tweakyllama.cartomation.rail.module.RailModule;

public class HoldingRailKitItem extends AbstractRailKit {
    public HoldingRailKitItem() {
        super("holding_rail_kit");
    }

    @Override
    public Block getRailBlock() {
        return RailModule.holdingRail;
    }
}
