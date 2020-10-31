package tweakyllama.cartomation;

import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import tweakyllama.cartomation.base.proxy.ClientProxy;
import tweakyllama.cartomation.base.proxy.CommonProxy;

@Mod(Cartomation.MODID)
public class Cartomation {

    public static final String MODID = "cartomation";
    public static final Logger LOGGER = LogManager.getLogger(MODID);

    public static CommonProxy proxy;

    public Cartomation() {
        proxy = DistExecutor.safeRunForDist(() -> ClientProxy::new, () -> CommonProxy::new);
        proxy.start();
    }

}
