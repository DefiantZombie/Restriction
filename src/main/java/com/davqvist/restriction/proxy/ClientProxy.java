package com.davqvist.restriction.proxy;

import com.davqvist.restriction.commands.client.CmdMain;
import com.davqvist.restriction.handler.TooltipHandler;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;

public class ClientProxy extends CommonProxy{

    @Override
    public void init( FMLInitializationEvent e ){
        super.init( e );
        MinecraftForge.EVENT_BUS.register( new TooltipHandler() );

        ClientCommandHandler.instance.registerCommand(new CmdMain());
    }
}
