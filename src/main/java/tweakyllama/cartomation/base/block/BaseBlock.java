package tweakyllama.cartomation.base.block;

import net.minecraft.block.Block;
import tweakyllama.cartomation.base.handler.RegistryHandler;
import tweakyllama.cartomation.base.module.Module;

import javax.annotation.Nullable;
import java.util.function.BooleanSupplier;

public abstract class BaseBlock extends Block implements IBaseBlock {
    private final Module module;
    private BooleanSupplier enabledSupplier = () -> true;


    public BaseBlock(String name, Module module, Properties properties) {
        super(properties);
        this.module = module;
        RegistryHandler.registerBlock(this, name);
    }

    @Override
    public BaseBlock setCondition(BooleanSupplier enabledSupplier) {
        this.enabledSupplier = enabledSupplier;
        return this;
    }

    @Override
    public boolean doesConditionApply() {
        return enabledSupplier.getAsBoolean();
    }

    @Nullable
    @Override
    public Module getModule() {
        return module;
    }
}

