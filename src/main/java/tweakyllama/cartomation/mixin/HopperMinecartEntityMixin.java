package tweakyllama.cartomation.mixin;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.item.minecart.ChestMinecartEntity;
import net.minecraft.entity.item.minecart.ContainerMinecartEntity;
import net.minecraft.entity.item.minecart.HopperMinecartEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.ChestContainer;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.HopperContainer;
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

@Mixin(HopperMinecartEntity.class)
public class HopperMinecartEntityMixin extends ContainerMinecartEntity {
    public HopperMinecartEntityMixin(EntityType<? extends HopperMinecartEntity> type, World world) {
        super(type, world);
    }

    private static final String drop_injection_target = "Lnet/minecraft/entity/item/minecart/HopperMinecartEntity;entityDropItem(Lnet/minecraft/util/IItemProvider;)Lnet/minecraft/entity/item/ItemEntity;";

    @Inject(method = "killMinecart", at = @At(value = "INVOKE", target = drop_injection_target), cancellable = true)
    public void killMinecartHook(DamageSource source, CallbackInfo ci) {
        if (CartModule.overwriteDrops) {
            Cartomation.LOGGER.info("overriding drops for hopper minecart");
            this.entityDropItem(Items.HOPPER_MINECART);
            ci.cancel();
        }
    }

    public int getSizeInventory() {
        return 27;
    }

    @Shadow
    public Type getMinecartType() {
        return Type.HOPPER;
    }

    @Shadow
    public Container createContainer(int id, PlayerInventory playerInventoryIn) {
        return new HopperContainer(id, playerInventoryIn, this);
    }
}
