package eu.revamp.hcf.deathban.lives.argument;

import eu.revamp.hcf.RevampHCF;
import eu.revamp.hcf.deathban.Deathban;
import eu.revamp.hcf.factions.utils.FactionMember;
import eu.revamp.hcf.utils.command.CommandArgument;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

/**
 * An {@link CommandArgument} used to clear all {@link Deathban}s.
 *//*
public class LivesClearDeathbansArgument extends CommandArgument {

    private final RevampHCF plugin;

    public LivesClearDeathbansArgument(RevampHCF plugin) {
        super("cleardeathbans", "Clears the global deathbans");
        this.plugin = plugin;
        this.aliases = new String[]{"resetdeathbans"};
        this.permission = "hcf.command.lives.argument." + getName();
    }

    @Override
    public String getUsage(String label) {
        return '/' + label + ' ' + getName();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        for (FactionMember user : plugin.getUserManager().getUsers().values()) {
            user.removeDeathban();
        }

        Command.broadcastCommandMessage(sender, ChatColor.translateAlternateColorCodes('&', RevampHCF.getInstance().getConfig().getString("cmd-messages.lives-clear-deathbans")));
        return true;
    }
}
*/