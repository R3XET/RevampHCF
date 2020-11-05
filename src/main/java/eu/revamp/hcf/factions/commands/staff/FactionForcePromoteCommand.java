package eu.revamp.hcf.factions.commands.staff;

import java.util.Collections;
import java.util.List;

import eu.revamp.hcf.Language;
import eu.revamp.hcf.RevampHCF;
import eu.revamp.hcf.factions.utils.FactionMember;
import eu.revamp.hcf.factions.type.PlayerFaction;
import eu.revamp.spigot.utils.chat.color.CC;
import org.bukkit.ChatColor;
import eu.revamp.hcf.factions.utils.struction.Role;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import eu.revamp.hcf.utils.command.CommandArgument;

public class FactionForcePromoteCommand extends CommandArgument
{
    private final RevampHCF plugin;
    
    public FactionForcePromoteCommand(RevampHCF plugin) {
        super("forcepromote", "Forces the promotion status of a player.");
        this.plugin = plugin;
        this.permission = "faction.forcepromote";
    }

    @Override
    public String getUsage(String label) {
        return '/' + label + ' ' + this.getName() + " <playerName>";
    }
    
    @Override @SuppressWarnings("deprecation")
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 2) {
            sender.sendMessage(CC.translate("Correct Usage: /f forcepromote <playerName>."));
            return true;
        }
        PlayerFaction playerFaction = this.plugin.getFactionManager().getContainingPlayerFaction(args[1]);
        if (playerFaction == null) {
            sender.sendMessage(Language.FACTIONS_FACTION_NOT_FOUND.toString());
            return true;
        }
        FactionMember factionMember = playerFaction.getMember(args[1]);
        if (factionMember == null) {
            sender.sendMessage(Language.FACTIONS_FACTION_NOT_FOUND.toString());
            return true;
        }
        if (factionMember.getRole() != Role.MEMBER) {
            sender.sendMessage(CC.translate("&cYour cannot do this with this role."));
            return true;
        }
        factionMember.setRole(Role.CAPTAIN);
        playerFaction.broadcast(String.valueOf(ChatColor.GOLD.toString()) + ChatColor.BOLD + sender.getName() + " has been forcefully assigned as a captain.");
        return true;
    }
    
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        return (args.length == 2) ? null : Collections.emptyList();
    }
}
