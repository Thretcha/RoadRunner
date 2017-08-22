package thretcha.roadrunner.proxy;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import thretcha.roadrunner.Config;
import thretcha.roadrunner.RoadHandler;

import java.io.File;

public abstract class CommonProxy {

    public static Configuration config;
    public void preInit(FMLPreInitializationEvent event)
    {
        File directory = event.getModConfigurationDirectory();
        config = new Configuration(new File(directory.getPath(),"RoadRunner.cfg"));
        Config.readConfig();
        RoadHandler.addRoadBlocksFromConfig();
    }

    public void init(FMLInitializationEvent event)
    {

        MinecraftForge.EVENT_BUS.register(new RoadHandler());
    }

    public void postInit(FMLPostInitializationEvent event)
    {
        if (config.hasChanged()) {
            config.save();
        }
    }



}
