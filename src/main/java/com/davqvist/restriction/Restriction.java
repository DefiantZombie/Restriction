package com.davqvist.restriction;

import com.davqvist.restriction.proxy.CommonProxy;
import com.davqvist.restriction.reference.Reference;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import java.io.File;

@Mod(modid = Reference.MOD_ID, name = Reference.MOD_NAME, version = Reference.VERSION)
public class Restriction{
    public static File modConfigDir;

    @Mod.Instance(Reference.MOD_ID)
    public static Restriction instance;

    @SidedProxy(clientSide = Reference.CLIENT_PROXY_CLASS, serverSide = Reference.SERVER_PROXY_CLASS)
    public static CommonProxy proxy;

    @Mod.EventHandler
    public void init( FMLInitializationEvent event ){
        proxy.init( event );
    }

    @Mod.EventHandler
    public void preInit( FMLPreInitializationEvent event )
    {
        modConfigDir = event.getModConfigurationDirectory();
        proxy.preInit( event );
    }

}
