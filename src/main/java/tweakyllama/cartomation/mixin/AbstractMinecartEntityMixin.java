package tweakyllama.cartomation.mixin;

import net.minecraft.entity.item.minecart.AbstractMinecartEntity;
import net.minecraft.util.DamageSource;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tweakyllama.cartomation.cart.module.CartModule;

@Mixin(AbstractMinecartEntity.class)
public abstract class AbstractMinecartEntityMixin {

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

}
