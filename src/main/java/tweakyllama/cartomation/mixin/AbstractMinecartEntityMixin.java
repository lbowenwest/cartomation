package tweakyllama.cartomation.mixin;

import net.minecraft.entity.item.minecart.AbstractMinecartEntity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.extensions.IForgeEntityMinecart;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import tweakyllama.cartomation.cart.module.CartModule;

@Mixin(AbstractMinecartEntity.class)
public abstract class AbstractMinecartEntityMixin implements IForgeEntityMinecart {

    private static final String drop_injection_target = "Lnet/minecraft/entity/item/minecart/AbstractMinecartEntity;entityDropItem(Lnet/minecraft/item/ItemStack;)Lnet/minecraft/entity/item/ItemEntity;";

    /**
     * Overwrite the item stack that the minecart would drop
     *
     * @param stack ItemStack to be dropped
     * @return
     */
    @ModifyArg(method = "killMinecart", at = @At(value = "INVOKE", target = drop_injection_target))
    public ItemStack modifyDrop(ItemStack stack) {
        if (CartModule.overwriteDrops) {
            return this.getCartItem();
        }
        return stack;
    }
}
