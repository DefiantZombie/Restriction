package com.davqvist.restriction.commands.client.Main;

import com.davqvist.restriction.Restriction;
import com.davqvist.restriction.commands.BaseCommand;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;

import java.io.File;

public class CmdReload extends BaseCommand
{

    @Override
    public String getCommand() {
        return "reload";
    }

    @Override
    public String getUsageSuffix() { return "- Reload the restriction config file"; }

    @Override
    public void runCommand(CommandBase command, ICommandSender sender, String[] args)
    {
        File configdir = new File(Restriction.modConfigDir, "restriction");
        if(!configdir.exists()) {
            configdir.mkdir();
        }

        File file = new File(configdir, "restriction.json");

        Restriction.proxy.rr.readRestrictions(file);
    }
}
