package com.sarahk.togglyfiers;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(Togglyfiers.MODID)
public class Togglyfiers
{
    public static final String MODID = "togglyfiers";

    private static final Logger LOGGER = LogManager.getLogger();

    public Togglyfiers()
    {
        MinecraftForge.EVENT_BUS.register(this);
    }


}
