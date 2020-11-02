package tweakyllama.cartomation.rail.item;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;

public class PoweredRailKitItem extends AbstractRailKit {
    public PoweredRailKitItem() {
        super("powered_rail_kit");
    }

    @Override
    public Block getRailBlock() {
        return Blocks.POWERED_RAIL;
    }
}
