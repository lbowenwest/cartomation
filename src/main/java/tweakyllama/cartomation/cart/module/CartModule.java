package tweakyllama.cartomation.cart.module;

import tweakyllama.cartomation.base.handler.RegistryHandler;
import tweakyllama.cartomation.base.module.Module;
import tweakyllama.cartomation.cart.recipe.DisassemblyRecipe;

public class CartModule extends Module {

    public static boolean overwriteDrops = true;
    public static boolean convenienceRecipes = true;
    public static boolean disassemblyRecipes = true;


    @Override
    public void construct() {
        RegistryHandler.register(DisassemblyRecipe.serializer, "disassembly");
    }
}
