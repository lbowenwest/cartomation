package tweakyllama.cartomation.rail.block;

import net.minecraft.state.BooleanProperty;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.properties.RailShape;
import tweakyllama.cartomation.rail.state.RailDirection;

public class RailBlockStateProperties {
    public static final EnumProperty<RailShape> RAIL_SHAPE_STRAIGHT_FLAT = EnumProperty.create(
            "shape",
            RailShape.class, railShape -> railShape == RailShape.NORTH_SOUTH || railShape == RailShape.EAST_WEST
    );

    public static final EnumProperty<RailDirection> RAIL_DIRECTION = EnumProperty.create("rail_direction", RailDirection.class);

    public static final BooleanProperty OCCUPIED = BooleanProperty.create("occupied");

}
