package tweakyllama.cartomation.mixin;

import net.minecraft.entity.item.minecart.HopperMinecartEntity;
import net.minecraft.util.DamageSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tweakyllama.cartomation.cart.module.CartModule;

@Mixin(HopperMinecartEntity.class)
public class HopperMinecartEntityMixin {

    private static final String drop_injection_target = "Lnet/minecraft/entity/item/minecart/HopperMinecartEntity;entityDropItem(Lnet/minecraft/util/IItemProvider;)Lnet/minecraft/entity/item/ItemEntity;";

    @Inject(method = "killMinecart", at = @At(value = "INVOKE", target = drop_injection_target), cancellable = true)
    public void killMinecartHook(DamageSource source, CallbackInfo ci) {
        if (CartModule.overwriteDrops) {
            ci.cancel();
        }
    }
}
