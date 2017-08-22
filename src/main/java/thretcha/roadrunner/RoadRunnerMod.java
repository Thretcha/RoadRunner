package thretcha.roadrunner;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.Logger;
import thretcha.roadrunner.proxy.CommonProxy;

//physical server - server.jar
//Logical server - (world.isRemote==false)The logical server is what runs game logic: mob spawning, weather, updating inventories, health, AI, and all other game mechanics.

//physical client - minecraft.exe
//logical client - (world.isRemote==true)The logical client is what accepts input from the player and relays it to the logical server.
//In addition, it also receives information from the logical server and makes it available graphically to the player.
// The logical client runs in the Client Thread, though often several other threads are spawned to handle things like audio and chunk render batching.

//communication between logical sides only using network packets

@Mod(modid = RoadRunnerMod.MODID, version = RoadRunnerMod.VERSION, name = RoadRunnerMod.NAME, useMetadata = true)
public class RoadRunnerMod {
    public static final String MODID = "roadrunner";
    public static final String VERSION = "1.0";
    public static final String NAME = "Road Runner";

    @SidedProxy(clientSide = "thretcha.roadrunner.proxy.ClientProxy", serverSide = "thretcha.roadrunner.proxy.ServerProxy")
    public static CommonProxy proxy;
    /*
        All mods go through an initialization step before the next step is started !
        PreInitialization - "Run before anything else. Read your config, create blocks, items, etc, and register them with the GameRegistry."
        Initialization - "Do your mod setup. Build whatever data structures you care about. Register recipes."
        PostInitialization -  "Handle interaction with other mods, complete your setup based on this.
     */
    @Mod.Instance(MODID)
    public static RoadRunnerMod instance;

    public static Logger logger;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        //System.out.println(UUID.randomUUID());
        logger = event.getModLog();
        this.proxy.preInit(event);
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event)
    {
        this.proxy.init(event);
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event)
    {
        this.proxy.postInit(event);
    }


}
