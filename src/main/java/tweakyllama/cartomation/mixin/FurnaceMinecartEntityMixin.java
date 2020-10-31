package tweakyllama.cartomation.mixin;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.item.minecart.AbstractMinecartEntity;
import net.minecraft.entity.item.minecart.FurnaceMinecartEntity;
import net.minecraft.item.Items;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tweakyllama.cartomation.Cartomation;
import tweakyllama.cartomation.cart.module.CartModule;

@Mixin(FurnaceMinecartEntity.class)
public class FurnaceMinecartEntityMixin extends AbstractMinecartEntity {
    public FurnaceMinecartEntityMixin(EntityType<? extends FurnaceMinecartEntityMixin> cart, World world) {
        super(cart, world);
    }

    private static final String drop_injection_target = "Lnet/minecraft/entity/item/minecart/FurnaceMinecartEntity;entityDropItem(Lnet/minecraft/util/IItemProvider;)Lnet/minecraft/entity/item/ItemEntity;";

    @Shadow
    public Type getMinecartType() {
        return Type.FURNACE;
    }

    @Inject(method = "killMinecart", at = @At(value = "INVOKE", target = drop_injection_target), cancellable = true)
    public void killMinecartHook(DamageSource source, CallbackInfo ci) {
        if (CartModule.overwriteDrops) {
            Cartomation.LOGGER.info("overriding furnace minecraft drop");
            this.entityDropItem(Items.FURNACE_MINECART);
            ci.cancel();
        }
    }
}
