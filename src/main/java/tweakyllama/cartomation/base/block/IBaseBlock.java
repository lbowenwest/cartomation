package tweakyllama.cartomation.base.block;

import net.minecraftforge.common.extensions.IForgeBlock;
import tweakyllama.cartomation.base.module.Module;

import javax.annotation.Nullable;
import java.util.function.BooleanSupplier;

public interface IBaseBlock extends IForgeBlock {
    @Nullable
    Module getModule();

    IBaseBlock setCondition(BooleanSupplier condition);

    boolean doesConditionApply();

    default boolean isEnabled() {
        Module module = getModule();
        return module != null && module.enabled && doesConditionApply();

    }

}
