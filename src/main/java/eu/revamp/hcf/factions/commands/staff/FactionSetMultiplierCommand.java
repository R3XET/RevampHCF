package eu.revamp.hcf.factions.commands.staff;

import eu.revamp.hcf.RevampHCF;
import eu.revamp.spigot.utils.chat.color.CC;
import eu.revamp.spigot.utils.chat.message.MessageUtils;
import eu.revamp.spigot.utils.generic.ConversionUtils;
import org.bukkit.entity.Player;
import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import eu.revamp.hcf.factions.Faction;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import eu.revamp.hcf.utils.command.CommandArgument;

public class FactionSetMultiplierCommand extends CommandArgument
{
    private RevampHCF plugin;
    
    public FactionSetMultiplierCommand(RevampHCF plugin) {
        super("setdeathbanmultiplier", "Sets the deathban multiplier of a faction.");
        this.plugin = plugin;
        this.permission = "*";
    }

    @Override
    public String getUsage(String label) {
        return '/' + label + ' ' + this.getName() + " <playerName|factionName> <newMultiplier>.";
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 3) {
            sender.sendMessage(CC.translate("&cCorrect Usage: " + this.getUsage(label)));
            return true;
        }
        Faction faction = this.plugin.getFactionManager().getContainingFaction(args[1]);
        if (faction == null) {
            sender.sendMessage(ChatColor.RED + "Faction named or containing member with IGN or UUID " + args[1] + " not found.");
            return true;
        }
        if (!ConversionUtils.isDouble(args[2])){
            sender.sendMessage(ChatColor.RED + "'" + args[2] + "' is not a valid number.");
            return true;
        }

        double multiplier = Double.parseDouble(args[2]);
        if (multiplier < 0.0) {
            sender.sendMessage(ChatColor.RED + "Deathban multipliers may not be less than " + 0.0 + '.');
            return true;
        }
        if (multiplier > 5.0) {
            sender.sendMessage(ChatColor.RED + "Deathban multipliers may not be more than " + 5.0 + '.');
            return true;
        }
        double previousMultiplier = faction.getDeathbanMultiplier();
        faction.setDeathbanMultiplier(multiplier);
        MessageUtils.sendMessage(CC.translate("&eSet deathban multiplier of &f" + faction.getName() + "&e from &f" + previousMultiplier + "&e to &f" + multiplier + "&e."), "hcf.admin");
        return true;
    }
    
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (args.length != 2) {
            return Collections.emptyList();
        }
        if (args[1].isEmpty()) {
            return null;
        }
        List<String> results = new ArrayList<>(this.plugin.getFactionManager().getFactionNameMap().keySet());
        for (Player player : Bukkit.getOnlinePlayers()) {
            results.add(player.getName());
        }
        return results;
    }
}
