package tweakyllama.cartomation.rail.module;

import net.minecraft.block.AbstractRailBlock;
import net.minecraft.block.Block;
import net.minecraft.block.DetectorRailBlock;
import net.minecraft.block.PoweredRailBlock;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import tweakyllama.cartomation.base.module.Module;
import tweakyllama.cartomation.rail.block.HoldingRailBlock;
import tweakyllama.cartomation.rail.item.DetectorRailKitItem;
import tweakyllama.cartomation.rail.item.HoldingRailKitItem;
import tweakyllama.cartomation.rail.item.PoweredRailKitItem;

public class RailModule extends Module {

    public static Block holdingRail;

    public static Item holdingRailKit;
    public static Item poweredRailKit;
    public static Item detectorRailKit;

    @Override
    public void construct() {
        holdingRail = new HoldingRailBlock();

        holdingRailKit = new HoldingRailKitItem();
        poweredRailKit = new PoweredRailKitItem();
        detectorRailKit = new DetectorRailKitItem();
    }

    public static ItemStack getKitFromRail(AbstractRailBlock railBlock) {

        // Class<? extends AbstractRailBlock> rail_class = railBlock.getClass();

        // TODO use a hashmap because this is rubbish
        if (railBlock instanceof HoldingRailBlock) {
            return new ItemStack(holdingRailKit);
        } else if (railBlock instanceof PoweredRailBlock) {
            return new ItemStack(poweredRailKit);
        } else if (railBlock instanceof DetectorRailBlock) {
            return new ItemStack(detectorRailKit);
        }
        return ItemStack.EMPTY;
    }
}
