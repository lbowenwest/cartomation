package tweakyllama.cartomation.rail.item;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.RailBlock;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResultType;
import net.minecraft.world.World;
import tweakyllama.cartomation.base.handler.RegistryHandler;

public abstract class AbstractRailKit extends Item {

    public AbstractRailKit(String name) {
        this(name, new Properties());
    }

    public AbstractRailKit(String name, Properties properties) {
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
}
