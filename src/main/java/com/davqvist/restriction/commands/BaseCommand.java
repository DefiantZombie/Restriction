package com.davqvist.restriction.commands;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;

public abstract class BaseCommand
{
    public abstract String getCommand();

    public String getUsageSuffix()
    {
        return "";
    }

    public boolean validArgs(String[] args)
    {
        return args.length == 1;
    }

    public List<String> autoComplete(ICommandSender sender, String[] args)
    {
        return new ArrayList<String>();
    }

    public abstract void runCommand(CommandBase command, ICommandSender sender, String[] args);

    public final WrongUsageException getException(CommandBase command)
    {
        String txt = command.getName() + " " + getCommand();

        if(getUsageSuffix().length() > 0)
        {
            txt += " " + getUsageSuffix();
        }

        return new WrongUsageException(txt);
    }
}
