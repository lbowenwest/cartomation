package tweakyllama.cartomation.tool.module;

import net.minecraft.item.Item;
import tweakyllama.cartomation.base.module.Module;
import tweakyllama.cartomation.tool.item.CrowbarItem;

public class CrowbarModule extends Module {

    public static Item crowbar;

    @Override
    public void construct() {
        crowbar = new CrowbarItem(this);
    }
}
