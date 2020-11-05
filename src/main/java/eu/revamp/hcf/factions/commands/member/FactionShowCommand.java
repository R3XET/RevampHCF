package eu.revamp.hcf.factions.commands.member;

import eu.revamp.hcf.Language;
import eu.revamp.hcf.RevampHCF;
import eu.revamp.hcf.factions.Faction;
import eu.revamp.hcf.utils.command.CommandArgument;
import eu.revamp.spigot.utils.chat.color.CC;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FactionShowCommand extends CommandArgument
{
    private final RevampHCF plugin;
    
    public FactionShowCommand(RevampHCF plugin) {
        super("show", "Get details about a faction.", new String[] { "i", "info", "who" });
        this.plugin = plugin;
    }

    @Override
    public String getUsage(String label) {
        return '/' + label + ' ' + this.getName() + " (playerName|factionName)";
    }


    @Override @SuppressWarnings("deprecation")
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Faction playerFaction = null;
        Faction namedFaction;
        if (args.length < 2) {
            if (!(sender instanceof Player)) {
                sender.sendMessage(CC.translate("&cCorrect Usage: " + this.getUsage(label)));
                return true;
            }
            namedFaction = this.plugin.getFactionManager().getPlayerFaction((Player)sender);
            if (namedFaction == null) {
                sender.sendMessage(Language.FACTIONS_NOFACTION.toString());
                return true;
            }
        }
        else {
            namedFaction = this.plugin.getFactionManager().getFaction(args[1]);
            playerFaction = this.plugin.getFactionManager().getContainingPlayerFaction(args[1]);
            if (namedFaction == null && playerFaction == null) {
                sender.sendMessage(Language.FACTIONS_FACTION_NOT_FOUND.toString());
                return true;
            }
        }
        if (namedFaction != null) {
            namedFaction.printDetails(sender);
        }
        if (playerFaction != null && namedFaction != playerFaction) {
            playerFaction.printDetails(sender);
        }
        return true;
    }
    
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (args.length != 2 || !(sender instanceof Player)) {
            return Collections.emptyList();
        }
        if (args[1].isEmpty()) {
            return null;
        }
        final Player player = (Player)sender;
        final List<String> results = new ArrayList<>(this.plugin.getFactionManager().getFactionNameMap().keySet());
        for (Player target : Bukkit.getOnlinePlayers()) {
            if (player.canSee(target) && !results.contains(target.getName())) {
                results.add(target.getName());
            }
        }
        return results;
    }
}
