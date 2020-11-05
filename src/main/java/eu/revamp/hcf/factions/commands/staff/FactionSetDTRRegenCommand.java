package eu.revamp.hcf.factions.commands.staff;

import eu.revamp.hcf.Language;
import eu.revamp.hcf.RevampHCF;
import eu.revamp.spigot.utils.chat.color.CC;
import eu.revamp.spigot.utils.chat.message.MessageUtils;
import eu.revamp.spigot.utils.generic.ConversionUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import eu.revamp.hcf.factions.Faction;
import org.apache.commons.lang.time.DurationFormatUtils;
import eu.revamp.hcf.factions.type.PlayerFaction;
import eu.revamp.hcf.factions.FactionManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import eu.revamp.hcf.utils.command.CommandArgument;

public class FactionSetDTRRegenCommand extends CommandArgument
{
    private final RevampHCF plugin;
    
    public FactionSetDTRRegenCommand(RevampHCF plugin) {
        super("setdtrregen", "Sets the DTR cooldown of a faction.", new String[] { "setdtrregeneration" });
        this.plugin = plugin;
        this.permission = "hcf.staffplus";
    }

    @Override
    public String getUsage(String label) {
        return '/' + label + ' ' + this.getName() + " <playerName|factionName> <newDTRRegen>.";
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 3) {
            sender.sendMessage(CC.translate("&cCorrect Usage: " + this.getUsage(label)));
            return true;
        }
        if (!ConversionUtils.isLong(args[2])){
            sender.sendMessage(Language.COMMANDS_INVALID_NUMBER.toString());
            return true;
        }
        long newRegen = Long.parseLong(args[2]);
        if (newRegen > FactionManager.MAX_DTR_REGEN_MILLIS) {
            sender.sendMessage(CC.translate("&cYou cannot set factions DTR regen above &l" + FactionManager.MAX_DTR_REGEN_WORDS + "&c."));
            return true;
        }
        Faction faction = this.plugin.getFactionManager().getContainingFaction(args[1]);
        if (faction == null) {
            sender.sendMessage(Language.FACTIONS_FACTION_NOT_FOUND.toString());
            return true;
        }
        if (!(faction instanceof PlayerFaction)) {
            sender.sendMessage(CC.translate("&cThis type of faction does not use DTR."));
            return true;
        }
        PlayerFaction playerFaction = (PlayerFaction)faction;
        long previousRegenRemaining = playerFaction.getRemainingRegenerationTime();
        playerFaction.setRemainingRegenerationTime(newRegen);
        MessageUtils.sendMessage(CC.translate("&eSet DTR regen of &f" + faction.getName() + ((previousRegenRemaining > 0L) ? (" &efrom &f" + DurationFormatUtils.formatDurationWords(previousRegenRemaining, true, true)) : "") + " &eto &f" + DurationFormatUtils.formatDurationWords(newRegen, true, true)), "hcf.admin");
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (args.length != 2) {
            return Collections.emptyList();
        } else if (args[1].isEmpty()) {
            return null;
        } else {
            List<String> results = new ArrayList<>(plugin.getFactionManager().getFactionNameMap().keySet());
            Player senderPlayer = sender instanceof Player ? ((Player) sender) : null;
            for (Player player : Bukkit.getOnlinePlayers()) {
                // Make sure the player can see.
                if (senderPlayer == null || senderPlayer.canSee(player)) {
                    results.add(player.getName());
                }
            }

            return results;
        }
    }
}
