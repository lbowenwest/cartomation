package tweakyllama.cartomation.mixin;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.item.minecart.AbstractMinecartEntity;
import net.minecraft.entity.item.minecart.ChestMinecartEntity;
import net.minecraft.entity.item.minecart.ContainerMinecartEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.ChestContainer;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.Items;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tweakyllama.cartomation.cart.module.CartModule;

@Mixin(ChestMinecartEntity.class)
public class ChestMinecartEntityMixin extends ContainerMinecartEntity {
    public ChestMinecartEntityMixin(EntityType<? extends ChestMinecartEntity> type, World world) {
        super(type, world);
    }

    private static final String drop_injection_target = "Lnet/minecraft/entity/item/minecart/ChestMinecartEntity;entityDropItem(Lnet/minecraft/util/IItemProvider;)Lnet/minecraft/entity/item/ItemEntity;";

    @Inject(method = "killMinecart", at = @At(value = "INVOKE", target = drop_injection_target), cancellable = true)
    public void killMinecartHook(DamageSource source, CallbackInfo ci) {
        if (CartModule.overwriteDrops) {
            this.entityDropItem(Items.CHEST_MINECART);
            ci.cancel();
        }
    }

    public int getSizeInventory() {
        return 27;
    }

    @Shadow
    public AbstractMinecartEntity.Type getMinecartType() {
        return AbstractMinecartEntity.Type.CHEST;
    }

    @Shadow
    public Container createContainer(int id, PlayerInventory playerInventoryIn) {
        return ChestContainer.createGeneric9X3(id, playerInventoryIn, this);
    }
}
