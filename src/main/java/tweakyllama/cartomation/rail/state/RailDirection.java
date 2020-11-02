package tweakyllama.cartomation.rail.state;

import net.minecraft.util.Direction;
import net.minecraft.util.IStringSerializable;

public enum RailDirection implements IStringSerializable {
    NONE("none"),
    FORWARDS("forwards"),
    BACKWARDS("backwards");

    private final String name;

    RailDirection(String name) {
        this.name = name;
    }

    public String toString() {
        return this.name;
    }

    @Override
    public String getString() {
        return this.name;
    }

    public int getMagnitude() {
        switch (this) {
            case NONE:
            default:
                return 0;
            case FORWARDS:
                return 1;
            case BACKWARDS:
                return -1;
        }
    }

    /**
     * Returns this direction as an AxisDirection
     *
     * NONE defaults to FORWARDS for this
     */
    public Direction.AxisDirection asDirection() {
        switch (this) {
            case NONE:
            case FORWARDS:
            default:
                return Direction.AxisDirection.POSITIVE;
            case BACKWARDS:
                return Direction.AxisDirection.NEGATIVE;
        }
    }

    /**
     * Cycle through the directions
     */
    public RailDirection nextState() {
        switch (this) {
            case NONE:
                return FORWARDS;
            case FORWARDS:
                return BACKWARDS;
            case BACKWARDS:
            default:
                return NONE;
        }
    }
}
