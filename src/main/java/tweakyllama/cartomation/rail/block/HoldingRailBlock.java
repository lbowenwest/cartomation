package tweakyllama.cartomation.rail.block;

import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.entity.item.minecart.AbstractMinecartEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.Property;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.state.properties.RailShape;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import tweakyllama.cartomation.base.handler.RegistryHandler;
import tweakyllama.cartomation.tool.item.CrowbarItem;

public class HoldingRailBlock extends AbstractRailBlock {
    public static final EnumProperty<RailShape> SHAPE = RailBlockStateProperties.RAIL_SHAPE_STRAIGHT_FLAT;
    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
    public static final BooleanProperty AXIS_DIRECTION = RailBlockStateProperties.AXIS_DIRECTION;

    public HoldingRailBlock() {
        // default rail properties
        this(Properties.create(Material.MISCELLANEOUS)
                .doesNotBlockMovement()
                .hardnessAndResistance(0.7f)
                .sound(SoundType.METAL)
        );
    }

    public HoldingRailBlock(Properties properties) {
        super(true, properties);
        this.setDefaultState(this.stateContainer.getBaseState()
                .with(POWERED, false)
                .with(SHAPE, RailShape.NORTH_SOUTH)
                .with(AXIS_DIRECTION, true)
        );
        RegistryHandler.registerBlock(this, "holding_rail");
    }

    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(SHAPE, POWERED, AXIS_DIRECTION);
    }

    /**
     * Called when a neighbor is changed
     *
     * @param state neighbor block state
     * @param world the world
     * @param pos   our position
     * @param block updated block
     */
    protected void updateState(BlockState state, World world, BlockPos pos, Block block) {
        boolean powered = state.get(POWERED);
        boolean worldPowered = world.isBlockPowered(pos);
        if (powered != worldPowered) {
            world.setBlockState(pos, state.with(POWERED, worldPowered), 3);
            world.notifyNeighborsOfStateChange(pos.down(), this);
        }
        super.updateState(state, world, pos, block);
    }

    /**
     * Called by any minecart that passes over this rail.
     * Called once per update thick that the minecart is on the rail.
     *
     * @param state block state
     * @param world the world
     * @param pos   block's position in the world
     * @param cart  the cart on the rail
     */
    @Override
    public void onMinecartPass(BlockState state, World world, BlockPos pos, AbstractMinecartEntity cart) {
        if (!state.get(POWERED)) {
            cart.setMotion(Vector3d.ZERO);
        }
//        if (cart instanceof HoldableMinecartEntity) {
//            ((HoldableMinecartEntity) cart).onHoldingRailPass(pos.getX(), pos.getY(), pos.getZ(), state.get(POWERED), getImpulseDirection(state));
//        }
    }

    /**
     * Returns the direction a holding cart is facing from the block state
     *
     * @param state block state
     * @return facing direction
     */
    public Direction getImpulseDirection(BlockState state) {
        Direction.AxisDirection axisDirection = state.get(AXIS_DIRECTION) ? Direction.AxisDirection.POSITIVE : Direction.AxisDirection.NEGATIVE;
        return state.get(SHAPE) == RailShape.NORTH_SOUTH
                ? Direction.getFacingFromAxisDirection(Direction.Axis.Z, axisDirection.inverted())
                : Direction.getFacingFromAxisDirection(Direction.Axis.X, axisDirection.inverted());
    }

    /**
     * Called when the block is activated
     * We flip the direction when the rail is activated with a crowbar
     *
     * @param state block state
     * @param worldIn world
     * @param pos block position
     * @param player player that used the item
     * @param handIn which hand the item is held
     * @param hit where on the block the item was used
     * @return
     */
    @Override
    @SuppressWarnings("deprecation")
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        if (worldIn.isRemote) {
            // force processing to happen on server side
            return ActionResultType.SUCCESS;
        }
        ItemStack heldItem = player.getHeldItem(handIn);
        if (heldItem.getItem() instanceof CrowbarItem) {
            worldIn.setBlockState(pos, state.with(AXIS_DIRECTION, !state.get(AXIS_DIRECTION)));
            return ActionResultType.CONSUME;
        }
        return ActionResultType.PASS;
    }

    @Override
    public boolean canMakeSlopes(BlockState state, IBlockReader world, BlockPos pos) {
        return false;
    }

    @SuppressWarnings("deprecated")
    public Property<RailShape> getShapeProperty() {
        return SHAPE;
    }

    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        // TODO
        return super.getStateForPlacement(context);
    }
}
