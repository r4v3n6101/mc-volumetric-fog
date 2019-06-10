package fog;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import fog.events.EventHandler;

@Mod(modid = "volumetric-fog")
public class ModMain {

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        new EventHandler();
    }
}
