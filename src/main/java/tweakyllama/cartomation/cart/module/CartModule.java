package tweakyllama.cartomation.cart.module;

import tweakyllama.cartomation.base.config.Config;
import tweakyllama.cartomation.base.handler.RegistryHandler;
import tweakyllama.cartomation.base.module.Module;
import tweakyllama.cartomation.cart.recipe.DisassemblyRecipe;

public class CartModule extends Module {

    @Config(description = "Overwrite default minecart drops")
    public static boolean overwriteDrops = true;

    @Config(description = "Enable convenience recipes for special minecarts")
    public static boolean convenienceRecipes = true;

    @Config(description = "Enable disassembly recipes")
    public static boolean disassemblyRecipes = true;

    public String description = "Cart module";

    @Override
    public void construct() {
        RegistryHandler.register(DisassemblyRecipe.serializer, "disassembly");
    }
}
