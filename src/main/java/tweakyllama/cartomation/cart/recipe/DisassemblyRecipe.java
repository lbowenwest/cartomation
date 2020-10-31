package tweakyllama.cartomation.cart.recipe;

import com.google.gson.JsonObject;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.*;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.registries.ForgeRegistryEntry;
import tweakyllama.cartomation.cart.module.CartModule;

import javax.annotation.Nullable;

public class DisassemblyRecipe implements ICraftingRecipe {

    public static final Serializer serializer = new Serializer();

    protected final ResourceLocation id;
    protected final Ingredient ingredient;
    protected final ItemStack result;
    protected final ItemStack remaining;

    public DisassemblyRecipe(ResourceLocation id, Ingredient ingredient, ItemStack result, ItemStack remaining) {
        this.id = id;
        this.ingredient = ingredient;
        this.result = result;
        this.remaining = remaining;
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        return NonNullList.from(this.ingredient);
    }

    @Override
    public boolean matches(CraftingInventory inv, World worldIn) {
        if (!CartModule.disassemblyRecipes)
            return false;

        boolean foundTarget = false;

        for (int i = 0; i < inv.getSizeInventory(); ++i) {
            if (this.ingredient.test(inv.getStackInSlot(i))) {
                if (foundTarget)
                    // only allow one
                    return false;
                foundTarget = true;
            }
        }
        return foundTarget;
    }

    @Override
    public NonNullList<ItemStack> getRemainingItems(CraftingInventory inv) {
        NonNullList<ItemStack> list = NonNullList.withSize(inv.getSizeInventory(), ItemStack.EMPTY);
        for (int i = 0; i < inv.getSizeInventory(); ++i) {
            if (this.ingredient.test(inv.getStackInSlot(i))) {
                list.set(i, this.remaining.copy());
            }
        }
        return list;
    }

    @Override
    public ItemStack getCraftingResult(CraftingInventory inv) {
        return this.result.copy();
    }

    @Override
    public boolean canFit(int width, int height) {
        return width * height >= 1;
    }

    @Override
    public ItemStack getRecipeOutput() {
        return this.result;
    }

    @Override
    public ResourceLocation getId() {
        return this.id;
    }

    @Override
    public IRecipeSerializer<?> getSerializer() {
        return serializer;
    }

    public static class Serializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<DisassemblyRecipe> {

        @Override
        public DisassemblyRecipe read(ResourceLocation recipeId, JsonObject json) {
            Ingredient ingredient = Ingredient.deserialize(JSONUtils.getJsonObject(json, "ingredient"));
            ItemStack result = ShapedRecipe.deserializeItem(JSONUtils.getJsonObject(json, "result"));
            ItemStack remaining = ShapedRecipe.deserializeItem(JSONUtils.getJsonObject(json, "remaining"));

            return new DisassemblyRecipe(recipeId, ingredient, result, remaining);
        }

        @Nullable
        @Override
        public DisassemblyRecipe read(ResourceLocation recipeId, PacketBuffer buffer) {
            Ingredient ingredient = Ingredient.read(buffer);
            ItemStack result = buffer.readItemStack();
            ItemStack remaining = buffer.readItemStack();

            return new DisassemblyRecipe(recipeId, ingredient, result, remaining);
        }

        @Override
        public void write(PacketBuffer buffer, DisassemblyRecipe recipe) {
            recipe.ingredient.write(buffer);
            buffer.writeItemStack(recipe.result);
            buffer.writeItemStack(recipe.remaining);
        }
    }
}
