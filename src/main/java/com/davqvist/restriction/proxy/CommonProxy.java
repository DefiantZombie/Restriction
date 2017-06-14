package com.davqvist.restriction.proxy;

import com.davqvist.restriction.handler.RightClickHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;

public abstract class CommonProxy {

    public void init( FMLInitializationEvent e ) {
        MinecraftForge.EVENT_BUS.register( new RightClickHandler() );
    }
}
