package tweakyllama.cartomation.tool.item;

import net.minecraft.block.AbstractRailBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.minecart.AbstractMinecartEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import tweakyllama.cartomation.base.handler.RegistryHandler;
import tweakyllama.cartomation.base.module.Module;
import tweakyllama.cartomation.rail.module.RailModule;

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
    public boolean doesSneakBypassUse(ItemStack stack, IWorldReader world, BlockPos pos, PlayerEntity player) {
        return true;
    }

    @Override
    public ActionResultType onItemUseFirst(ItemStack stack, ItemUseContext context) {
        World world = context.getWorld();
        if (world.isRemote) {
            return ActionResultType.PASS;
        }

        PlayerEntity player = context.getPlayer();
        if (player != null && player.isSneaking()) {
            BlockState state = world.getBlockState(context.getPos());
            Block block = state.getBlock();
            if (block instanceof AbstractRailBlock) {
                ItemStack drop = RailModule.getKitFromRail((AbstractRailBlock) block);
                if (!drop.isEmpty()) {
                    Block.spawnAsEntity(world, context.getPos(), drop);
                    Block.replaceBlock(state, Blocks.RAIL.getDefaultState(), world, context.getPos(), 515);
                    return ActionResultType.CONSUME;
                }
            }
        }
        return super.onItemUse(context);
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
