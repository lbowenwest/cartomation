package tweakyllama.cartomation.rail.item;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.RailBlock;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResultType;
import net.minecraft.world.World;
import tweakyllama.cartomation.base.handler.RegistryHandler;
import tweakyllama.cartomation.rail.module.RailModule;

public abstract class RailKit extends Item {

    public RailKit(String name) {
        this(name, new Properties());
    }

    public RailKit(String name, Properties properties) {
        super(properties);
        RegistryHandler.registerItem(this, name);
    }

    public abstract Block getRailBlock();


    @Override
    public ActionResultType onItemUseFirst(ItemStack stack, ItemUseContext context) {
        World world = context.getWorld();
        if (world.isRemote) {
            return ActionResultType.PASS;
        }

        BlockState state = world.getBlockState(context.getPos());
        Block block = state.getBlock();
        if (block instanceof RailBlock) {
            stack.shrink(1);
            Block.replaceBlock(state, getRailBlock().getDefaultState(), world, context.getPos(), 515);
            return ActionResultType.CONSUME;
        }

        return ActionResultType.PASS;
    }

    public static class PoweredRailKitItem extends RailKit {
        public PoweredRailKitItem() {
            super("powered_rail_kit");
        }

        @Override
        public Block getRailBlock() {
            return Blocks.POWERED_RAIL;
        }
    }

    public static class HoldingRailKitItem extends RailKit {
        public HoldingRailKitItem() {
            super("holding_rail_kit");
        }

        @Override
        public Block getRailBlock() {
            return RailModule.holdingRail;
        }
    }

    public static class DetectorRailKitItem extends RailKit {
        public DetectorRailKitItem() {
            super("detector_rail_kit");
        }

        @Override
        public Block getRailBlock() {
            return Blocks.DETECTOR_RAIL;
        }
    }

    public static class ActivatorRailKitItem extends RailKit {
        public ActivatorRailKitItem() {
            super("activator_rail_kit");
        }

        @Override
        public Block getRailBlock() {
            return Blocks.ACTIVATOR_RAIL;
        }
    }
}
