package tweakyllama.cartomation.base.proxy;

import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.config.ModConfig.ModConfigEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import tweakyllama.cartomation.Cartomation;
import tweakyllama.cartomation.base.handler.RegistryHandler;
import tweakyllama.cartomation.base.module.ModuleLoader;

public class CommonProxy {

    protected ModuleLoader loader = new ModuleLoader();

    public void start() {

        loader.start();

        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        bus.register(RegistryHandler.class);
        registerListeners(bus);
    }

    public void registerListeners(IEventBus bus) {
        bus.addListener(this::setup);
        bus.addListener(this::loadComplete);
        bus.addListener(this::configChanged);
    }


    public void setup(FMLCommonSetupEvent event) {
        Cartomation.LOGGER.info("Hello from Automation mod");
        loader.setup(event);
    }

    public void loadComplete(FMLLoadCompleteEvent event) {
        loader.loadComplete(event);
    }

    public void configChanged(ModConfigEvent event) {
        // TODO
    }
}
