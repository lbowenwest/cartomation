package tweakyllama.cartomation.rail.block;

import net.minecraft.state.BooleanProperty;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.properties.RailShape;

public class RailBlockStateProperties {
    public static final EnumProperty<RailShape> RAIL_SHAPE_STRAIGHT_FLAT = EnumProperty.create(
            "shape",
            RailShape.class, railShape -> railShape == RailShape.NORTH_SOUTH || railShape == RailShape.EAST_WEST
    );

    // TODO custom property for this?
    public static final BooleanProperty AXIS_DIRECTION = BooleanProperty.create("axis_direction");

}
