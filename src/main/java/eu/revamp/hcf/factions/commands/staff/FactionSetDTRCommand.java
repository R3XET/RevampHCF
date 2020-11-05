package eu.revamp.hcf.factions.commands.staff;

import eu.revamp.hcf.Language;
import eu.revamp.hcf.RevampHCF;
import eu.revamp.spigot.utils.chat.color.CC;
import eu.revamp.spigot.utils.chat.message.MessageUtils;
import eu.revamp.spigot.utils.generic.ConversionUtils;
import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.Collections;
import org.bukkit.entity.Player;
import java.util.List;

import eu.revamp.hcf.factions.type.PlayerFaction;
import eu.revamp.hcf.factions.Faction;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import eu.revamp.hcf.utils.command.CommandArgument;

public class FactionSetDTRCommand extends CommandArgument
{
    private final RevampHCF plugin;
    
    public FactionSetDTRCommand(RevampHCF plugin) {
        super("setdtr", "Sets the DTR of a faction.", new String[] { "dtr" });
        this.plugin = plugin;
        this.permission = "hcf.staffplus";
    }

    @Override
    public String getUsage(String label) {
        return '/' + label + ' ' + this.getName() + " <playerName|factionName> <newDTR>.";
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 3) {
            sender.sendMessage(CC.translate("&cCorrect Usage: " + this.getUsage(label)));
            return true;
        }
        if (!ConversionUtils.isDouble(args[2])){
            sender.sendMessage(Language.COMMANDS_INVALID_NUMBER.toString());
            return true;
        }
        double newDTR = Double.parseDouble(args[2]);
        if (args[1].equalsIgnoreCase("all")) {
            for (Faction faction : this.plugin.getFactionManager().getFactions()) {
                if (faction instanceof PlayerFaction) {
                    ((PlayerFaction)faction).setDeathsUntilRaidable(newDTR);
                }
            }
            MessageUtils.sendMessage(CC.translate("&eSet DTR of all factions to &f" + newDTR), "hcf.admin");
            return true;
        }
        Faction faction = this.plugin.getFactionManager().getContainingFaction(args[1]);
        if (faction == null) {
            sender.sendMessage(Language.FACTIONS_FACTION_NOT_FOUND.toString());
            return true;
        }
        if (!(faction instanceof PlayerFaction)) {
            sender.sendMessage(CC.translate("&cYou can only set DTR of player factions."));
            return true;
        }
        PlayerFaction playerFaction = (PlayerFaction)faction;
        double previousDtr = playerFaction.getDeathsUntilRaidable();
        newDTR = playerFaction.setDeathsUntilRaidable(newDTR);
        MessageUtils.sendMessage(CC.translate("&eSet DTR of &f" + faction.getName() + " &efrom &f" + previousDtr + " &eto &f" + newDTR), "hcf.admin");
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
        for (Player target : Bukkit.getOnlinePlayers()) {
            if (player.canSee(target) && !results.contains(target.getName())) {
                results.add(target.getName());
            }
        }
        return results;
    }
}
