package tweakyllama.cartomation.tool.item;

import net.minecraft.block.BlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.minecart.AbstractMinecartEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.DamageSource;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import tweakyllama.cartomation.base.handler.RegistryHandler;
import tweakyllama.cartomation.base.module.Module;

import javax.annotation.Nullable;
import java.util.List;

public class CrowbarItem extends Item {

    private final Module module;

    public CrowbarItem(Module module) {
        super(new Properties().maxStackSize(1));
        this.module = module;
        RegistryHandler.registerItem(this, "crowbar");
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        tooltip.add(new TranslationTextComponent("message.crowbar"));
    }

    @Override
    public float getDestroySpeed(ItemStack stack, BlockState state) {
        return state.isIn(BlockTags.RAILS) ? 15f : super.getDestroySpeed(stack, state);
    }

    @Override
    public boolean onLeftClickEntity(ItemStack stack, PlayerEntity player, Entity entity) {
        if (entity instanceof AbstractMinecartEntity) {
            ((AbstractMinecartEntity) entity).killMinecart(new DamageSource("crowbar"));
            return true;
        }
        return false;
    }
}
