package tweakyllama.cartomation.mixin;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.item.minecart.AbstractMinecartEntity;
import net.minecraft.entity.item.minecart.MinecartEntity;
import net.minecraft.item.Items;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import tweakyllama.cartomation.cart.module.CartModule;

@Mixin(MinecartEntity.class)
public class MinecartEntityMixin extends AbstractMinecartEntity {
    public MinecartEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    public void killMinecart(DamageSource source) {
        super.killMinecart(source);
        if (CartModule.overwriteDrops) {
            this.entityDropItem(Items.MINECART);
        }
    }

    @Override
    public Type getMinecartType() {
        return Type.RIDEABLE;
    }

}
