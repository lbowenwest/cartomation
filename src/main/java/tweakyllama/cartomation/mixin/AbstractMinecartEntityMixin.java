package tweakyllama.cartomation.mixin;

import net.minecraft.entity.item.minecart.AbstractMinecartEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Direction;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tweakyllama.cartomation.Cartomation;
import tweakyllama.cartomation.cart.entity.HoldableMinecartEntity;
import tweakyllama.cartomation.cart.module.CartModule;

@Mixin(AbstractMinecartEntity.class)
public abstract class AbstractMinecartEntityMixin implements HoldableMinecartEntity {

    private static final String drop_injection_target = "Lnet/minecraft/entity/item/minecart/AbstractMinecartEntity;entityDropItem(Lnet/minecraft/item/ItemStack;)Lnet/minecraft/entity/item/ItemEntity;";

    /**
     * Stop the abstract minecart from dropping anything if option set in config
     * Concrete classes are responsible for dropping the correct item
     */
    @Inject(method = "killMinecart", at = @At(value = "INVOKE", target = drop_injection_target), cancellable = true)
    public void killMinecartHook(DamageSource source, CallbackInfo ci) {
        if (CartModule.overwriteDrops) {
            ci.cancel();
        }
    }


    @Inject(method = "onBlockActivate")

    public void onHoldingRailPass(int x, int y, int z, boolean receivingPower, Direction direction) {
        if (receivingPower) {
            go(direction);
        } else {
            stop();
        }
    }

    public void go(Direction direction) {
        Cartomation.LOGGER.info("go " + direction);
    }

    public void stop() {
        Cartomation.LOGGER.info("stop");

    }

}
