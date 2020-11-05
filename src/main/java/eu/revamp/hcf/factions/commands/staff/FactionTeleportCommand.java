package eu.revamp.hcf.factions.commands.staff;

import eu.revamp.hcf.RevampHCF;
import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.bukkit.Location;
import eu.revamp.hcf.factions.Faction;
import org.bukkit.entity.Player;
import eu.revamp.hcf.factions.type.PlayerFaction;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import eu.revamp.hcf.utils.command.CommandArgument;

public class FactionTeleportCommand extends CommandArgument
{
    private final RevampHCF plugin;
    
    public FactionTeleportCommand(RevampHCF plugin) {
        super("tp", "apply a punishment to a faction");
        this.plugin = plugin;
        this.permission = "HCF.staff";
    }

    @Override
    public String getUsage(String label) {
        return '/' + label + ' ' + this.getName() + " <factionName>";
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 2) {
            sender.sendMessage(ChatColor.RED + "Usage: " + this.getUsage(label));
            return true;
        }
        Faction faction = this.plugin.getFactionManager().getContainingFaction(args[1]);
        PlayerFaction playerFaction = (PlayerFaction)faction;
        Location location = playerFaction.getHome();
        if (faction == null) {
            sender.sendMessage(ChatColor.RED + "Player faction named or containing member with IGN or UUID " + args[1] + " not found.");
            return true;
        }
        if (!(faction instanceof PlayerFaction)) {
            sender.sendMessage(ChatColor.RED + "Player faction named or containing member with IGN or UUID " + args[1] + " not found.");
            return true;
        }
        if (location == null) {
            sender.sendMessage(ChatColor.RED + args[1] + " HQ not found.");
            return true;
        }
        ((Player)sender).teleport(location);
        sender.sendMessage(ChatColor.GREEN + "Teleported you to the home point of the faction: " + faction.getName() + '.');
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
        Player player = (Player)sender;
        List<String> results = new ArrayList<>(this.plugin.getFactionManager().getFactionNameMap().keySet());
        for (Player players : Bukkit.getServer().getOnlinePlayers()) {
            if (player.canSee(players) && !results.contains(players.getName())) {
                results.add(players.getName());
            }
        }
        return results;
    }
}
