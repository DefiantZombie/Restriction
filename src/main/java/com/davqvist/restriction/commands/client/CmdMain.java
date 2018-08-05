package com.davqvist.restriction.commands.client;

import com.davqvist.restriction.commands.BaseCommand;
import com.davqvist.restriction.commands.client.Main.CmdReload;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;

import java.util.ArrayList;
import java.util.List;

public class CmdMain extends CommandBase
{
    public static ArrayList<BaseCommand> commands = new ArrayList<BaseCommand>();

    public CmdMain()
    {
        commands.add(new CmdReload());
    }

    @Override
    public String getName() {
        return "restriction";
    }

    @Override
    public List<String> getAliases()
    {
        ArrayList<String> strings = new ArrayList<String>();
        strings.add("restrict");

        return strings;
    }

    @Override
    public String getUsage(ICommandSender sender)
    {
        for(int i = 0; i < commands.size(); i++)
        {
            BaseCommand c = commands.get(i);

            String txt = c.getCommand();

            if(c.getUsageSuffix().length() > 0)
            {
                txt += " " + c.getUsageSuffix();
            }

            if(i < commands.size() - 1)
            {
                txt += ", ";
            }

            sender.sendMessage(new TextComponentTranslation(txt));
        }

        return "</restrict, /restriction>";
    }

    @Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] strings, BlockPos pos)
    {
        if(strings.length == 1)
        {
            ArrayList<String> cmds = new ArrayList<String>();
            for(BaseCommand c : commands)
            {
                cmds.add(c.getCommand());
            }

            return getListOfStringsMatchingLastWord(strings, cmds.toArray(new String[0]));
        }
        else if(strings.length > 1)
        {
            for(BaseCommand c : commands)
            {
                if(c.getCommand().equalsIgnoreCase(strings[0]))
                {
                    return c.autoComplete(sender, strings);
                }
            }
        }

        return new ArrayList<String>();
    }

    @Override
    public int getRequiredPermissionLevel() { return 2; }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
    {
        if(args.length < 1)
        {
            throw new WrongUsageException(this.getUsage(sender));
        }

        for(BaseCommand c : commands)
        {
            if(c.getCommand().equalsIgnoreCase(args[0]))
            {
                if(c.validArgs(args))
                {
                    c.runCommand(this, sender, args);
                    return;
                }
                else
                {
                    throw c.getException(this);
                }
            }
        }

        throw new WrongUsageException(this.getUsage(sender));
    }
}
