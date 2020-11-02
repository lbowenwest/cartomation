package tweakyllama.cartomation.rail.item;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;

public class DetectorRailKitItem extends AbstractRailKit {
    public DetectorRailKitItem() {
        super("detector_rail_kit");
    }

    @Override
    public Block getRailBlock() {
        return Blocks.DETECTOR_RAIL;
    }
}
