package com.davqvist.restriction.proxy;

import com.davqvist.restriction.config.RestrictionReader;
import com.davqvist.restriction.handler.RightClickHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import java.io.File;

public abstract class CommonProxy {

    public RestrictionReader rr = new RestrictionReader();

    public void preInit( FMLPreInitializationEvent e ){
        File configdir = new File( e.getModConfigurationDirectory(), "restriction" );
        if( !configdir.exists() ){
            configdir.mkdir();
        }
        File file = new File( configdir, "restriction.json");
        rr.readRestrictions( file );
    }

    public void init( FMLInitializationEvent e ) {
        MinecraftForge.EVENT_BUS.register( new RightClickHandler() );
    }
}
