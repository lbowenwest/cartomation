package tweakyllama.cartomation.rail.block;

import net.minecraft.block.AbstractRailBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.minecart.AbstractMinecartEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.Property;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.state.properties.RailShape;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import tweakyllama.cartomation.base.handler.RegistryHandler;
import tweakyllama.cartomation.rail.module.RailModule;
import tweakyllama.cartomation.rail.state.RailDirection;
import tweakyllama.cartomation.tool.item.CrowbarItem;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;
import java.util.function.Predicate;

public class HoldingRailBlock extends AbstractRailBlock {
    public static final EnumProperty<RailShape> SHAPE = RailBlockStateProperties.RAIL_SHAPE_STRAIGHT_FLAT;
    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
    public static final EnumProperty<RailDirection> DIRECTION = RailBlockStateProperties.RAIL_DIRECTION;
    public static final BooleanProperty OCCUPIED = RailBlockStateProperties.OCCUPIED;
//    public static final BooleanProperty AXIS_DIRECTION = RailBlockStateProperties.AXIS_DIRECTION;

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
                .with(DIRECTION, RailDirection.NONE)
                .with(OCCUPIED, false)
        );
        RegistryHandler.registerBlock(this, "holding_rail");
    }

    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(SHAPE, POWERED, DIRECTION, OCCUPIED);
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
        } else {
            if (state.get(DIRECTION) != RailDirection.NONE) {
                float speedIncrease = RailModule.boostSpeed;
                Vector3d motion = cart.getMotion();
                cart.setMotion(motion.add(getImpulseVector(state, speedIncrease)));
            }
        }
//        if (cart instanceof HoldableMinecartEntity) {
//            ((HoldableMinecartEntity) cart).onHoldingRailPass(pos.getX(), pos.getY(), pos.getZ(), state.get(POWERED), getImpulseDirection(state));
//        }
        world.updateComparatorOutputLevel(pos, this);
    }

    /**
     * Returns the direction a holding cart should move from the block
     * <p>
     * If rail direction is NONE defaults to positive, so check that before running this
     *
     * @param state block state
     * @return facing direction
     */
    public Direction getImpulseDirection(BlockState state) {
        Direction.AxisDirection axisDirection = state.get(DIRECTION).asDirection().inverted();
        return state.get(SHAPE) == RailShape.NORTH_SOUTH
                ? Direction.getFacingFromAxisDirection(Direction.Axis.Z, axisDirection)
                : Direction.getFacingFromAxisDirection(Direction.Axis.X, axisDirection);

    }

    /**
     * Gets the impulse vector to add to a minecart from the block state
     *
     * @param state         block state
     * @param speedIncrease magnitude of speed increase along axis
     * @return vector of impulse
     */
    public Vector3d getImpulseVector(BlockState state, float speedIncrease) {
        Direction direction = getImpulseDirection(state);
        return new Vector3d(
                direction.getXOffset() * speedIncrease,
                direction.getYOffset() * speedIncrease,
                direction.getZOffset() * speedIncrease
        );
    }

    /**
     * Called when the block is activated
     * We flip the direction when the rail is activated with a crowbar
     *
     * @param state   block state
     * @param worldIn world
     * @param pos     block position
     * @param player  player that used the item
     * @param handIn  which hand the item is held
     * @param hit     where on the block the item was used
     * @return ActionResultType
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
            worldIn.setBlockState(pos, state.with(DIRECTION, state.get(DIRECTION).nextState()));
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

    @Override
    @SuppressWarnings("deprecation")
    public boolean hasComparatorInputOverride(BlockState state) {
        return true;
    }

    @Override
    @SuppressWarnings("deprecation")
    public int getComparatorInputOverride(BlockState blockState, World worldIn, BlockPos pos) {
        List<AbstractMinecartEntity> carts = this.findMinecarts(worldIn, pos, AbstractMinecartEntity.class, EntityPredicates.HAS_INVENTORY);
//        if (!carts.isEmpty() && carts.get(0).getComparatorLevel() > -1) return carts.get(0).getComparatorLevel();
        if (!carts.isEmpty()) {
            return Container.calcRedstoneFromInventory((IInventory) carts.get(0));
        }
        return 0;
    }

    protected <T extends AbstractMinecartEntity> List<T> findMinecarts(World worldIn, BlockPos pos, Class<T> cartType, @Nullable Predicate<Entity> filter) {
        return worldIn.getEntitiesWithinAABB(cartType, this.getDetectionBox(pos), filter);
    }

    private AxisAlignedBB getDetectionBox(BlockPos pos) {
        double delta = 0.2D;
        return new AxisAlignedBB(
                (double) pos.getX() + delta, pos.getY(), (double) pos.getZ() + delta,
                (double) (pos.getX() + 1) - delta, (double) (pos.getY() + 1) - delta, (double) (pos.getZ() + 1) - delta
        );
    }

    @Override
    @SuppressWarnings("deprecation")
    public void onEntityCollision(BlockState state, World worldIn, BlockPos pos, Entity entityIn) {
        if (!worldIn.isRemote) {
            if (!state.get(OCCUPIED)) {
                updateOccupiedState(worldIn, pos, state);
            }
        }
    }

    @Override
    @SuppressWarnings("deprecation")
    public void tick(BlockState state, ServerWorld worldIn, BlockPos pos, Random rand) {
        if (state.get(OCCUPIED)) {
            this.updateOccupiedState(worldIn, pos, state);
        }
    }

    public void updateOccupiedState(World world, BlockPos pos, BlockState state) {
        if (!this.isValidPosition(state, world, pos))
            return;

        boolean previouslyOccupied = state.get(OCCUPIED);
        boolean currentlyOccupied = false;
        List<AbstractMinecartEntity> carts = this.findMinecarts(world, pos, AbstractMinecartEntity.class, null);
        if (!carts.isEmpty()) {
            currentlyOccupied = true;
        }

        if (previouslyOccupied != currentlyOccupied) {
            world.setBlockState(pos, state.with(OCCUPIED, currentlyOccupied), 3);
            world.notifyNeighborsOfStateChange(pos, this);
        }

        if (currentlyOccupied) {
            // schedule a tick to check if we need to unset OCCUPIED
            world.getPendingBlockTicks().scheduleTick(pos, this, 20);
        }

        world.updateComparatorOutputLevel(pos, this);

    }

    /**
     * Returns the blockstate with the given rotation from the passed blockstate. If inapplicable returns the
     * passed blockstate
     */
    @Override
    @SuppressWarnings("deprecation")
    public BlockState rotate(BlockState state, Rotation rot) {
        switch (rot) {
            case CLOCKWISE_180:
                switch (state.get(DIRECTION)) {
                    case NONE:
                        return state;
                    case FORWARDS:
                        return state.with(DIRECTION, RailDirection.BACKWARDS);
                    case BACKWARDS:
                        return state.with(DIRECTION, RailDirection.FORWARDS);
                }
            case CLOCKWISE_90:
                switch (state.get(SHAPE)) {
                    case NORTH_SOUTH:
                        return state.with(SHAPE, RailShape.EAST_WEST);
                    case EAST_WEST:
                        return state.with(SHAPE, RailShape.NORTH_SOUTH);
                }
            case COUNTERCLOCKWISE_90:
                switch (state.get(SHAPE)) {
                    case NORTH_SOUTH:
                        return state.with(SHAPE, RailShape.EAST_WEST);
                    case EAST_WEST:
                        return state.with(SHAPE, RailShape.NORTH_SOUTH);
                }
            default:
                return state;
        }
    }

    /**
     * Returns the blockstate with the given mirror of the passed blockstate. If inapplicable, returns the passed
     * blockstate.
     */
    @Override
    @SuppressWarnings("deprecation")
    public BlockState mirror(BlockState state, Mirror mirrorIn) {
        // TODO this aint right
        switch (mirrorIn) {
            case LEFT_RIGHT:
            case FRONT_BACK:
                switch (state.get(DIRECTION)) {
                    case NONE:
                        return state;
                    case FORWARDS:
                        return state.with(DIRECTION, RailDirection.BACKWARDS);
                    case BACKWARDS:
                        return state.with(DIRECTION, RailDirection.FORWARDS);
                }
            default:
                return state;
        }
    }
}
