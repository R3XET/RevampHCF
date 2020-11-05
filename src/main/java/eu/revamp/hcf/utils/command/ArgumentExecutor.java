package eu.revamp.hcf.utils.command;

import eu.revamp.spigot.utils.chat.color.CC;
import eu.revamp.hcf.utils.inventory.BukkitUtils;
import org.bukkit.ChatColor;
import org.apache.commons.lang.WordUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import com.google.common.collect.ImmutableList;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.command.TabCompleter;
import org.bukkit.command.CommandExecutor;

public abstract class ArgumentExecutor implements CommandExecutor, TabCompleter
{
    protected List<CommandArgument> arguments;
    protected String label;
    
    public ArgumentExecutor(String label) {
        this.arguments = new ArrayList<>();
        this.label = label;
    }
    
    public boolean containsArgument(CommandArgument argument) {
        return this.arguments.contains(argument);
    }
    
    public void addArgument(CommandArgument argument) {
        this.arguments.add(argument);
    }
    
    public void removeArgument(CommandArgument argument) {
        this.arguments.remove(argument);
    }
    
    public CommandArgument getArgument(String id) {
        for (CommandArgument argument : this.arguments) {
            String name = argument.getName();
            if (name.equalsIgnoreCase(id) || Arrays.asList(argument.getAliases()).contains(id.toLowerCase())) {
                return argument;
            }
        }
        return null;
    }
    
    public List<CommandArgument> getArguments() {
        return ImmutableList.copyOf(this.arguments);
    }
    
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 1) {
            sender.sendMessage(CC.translate("&c" + WordUtils.capitalizeFully(this.label) + " - Help Commands"));
            for (CommandArgument argument : this.arguments) {
                String permission = argument.getPermission();
                if (permission != null) {
                    sender.hasPermission(permission);
                }
            }
            return true;
        }
        CommandArgument argument2 = this.getArgument(args[0]);
        String permission2 = (argument2 == null) ? null : argument2.getPermission();
        if (argument2 == null || (permission2 != null && !sender.hasPermission(permission2))) {
            sender.sendMessage(ChatColor.RED + WordUtils.capitalizeFully(this.label) + " sub-command " + args[0] + " not found.");
            return true;
        }
        argument2.onCommand(sender, command, label, args);
        return true;
    }
    
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        List<String> results = new ArrayList<>();
        if (args.length < 2) {
            for (CommandArgument argument : this.arguments) {
                String permission = argument.getPermission();
                if (permission == null || sender.hasPermission(permission)) {
                    results.add(argument.getName());
                }
            }
        }
        else {
            CommandArgument argument2 = this.getArgument(args[0]);
            if (argument2 == null) {
                return results;
            }
            String permission2 = argument2.getPermission();
            if (permission2 == null || sender.hasPermission(permission2)) {
                results = argument2.onTabComplete(sender, command, label, args);
                if (results == null) {
                    return null;
                }
            }
        }
        return BukkitUtils.getCompletions(args, results);
    }
}
