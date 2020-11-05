package eu.revamp.hcf.factions.commands.captain;

import eu.revamp.hcf.Language;
import eu.revamp.hcf.RevampHCF;
import eu.revamp.spigot.utils.chat.color.CC;
import org.bukkit.OfflinePlayer;
import org.bukkit.Bukkit;
import java.util.UUID;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import eu.revamp.hcf.factions.utils.FactionMember;
import eu.revamp.hcf.factions.type.PlayerFaction;
import eu.revamp.hcf.factions.utils.struction.Role;
import org.bukkit.entity.Player;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import eu.revamp.hcf.utils.command.CommandArgument;

public class FactionKickCommand extends CommandArgument
{
    public FactionKickCommand(RevampHCF plugin) {
        super("kick", "Kick a player from the faction.");
    }

    @Override
    public String getUsage(String label) {
        return '/' + label + ' ' + this.getName() + " <playerName>";
    }

    @Override @SuppressWarnings("deprecation")
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(CC.translate("&cNo console."));
            return true;
        }
        if (args.length < 2) {
            sender.sendMessage(Language.F_KICK_COMMAND_USAGE.toString());
            return true;
        }
        Player player = (Player)sender;
        PlayerFaction playerFaction = RevampHCF.getInstance().getFactionManager().getPlayerFaction(player);
        if (playerFaction == null) {
            sender.sendMessage(Language.FACTIONS_NOFACTION.toString());
            return true;
        }
        if (playerFaction.isRaidable() && !RevampHCF.getInstance().getHandlerManager().getEotwUtils().isEndOfTheWorld()) {
            sender.sendMessage(Language.F_KICK_COMMAND_USAGE.toString());
            return true;
        }
        FactionMember targetMember = playerFaction.getMember(args[1]);
        if (targetMember == null) {
            sender.sendMessage(Language.COMMANDS_PLAYER_NOT_FOUND.toString());
            return true;
        }
        Role selfRole = playerFaction.getMember(player.getUniqueId()).getRole();
        if (selfRole != Role.CAPTAIN && selfRole != Role.COLEADER && selfRole != Role.LEADER) {
            sender.sendMessage(CC.translate("&cYour cannot do this with this role."));
            return true;
        }
        Role targetRole = targetMember.getRole();
        if (targetRole == Role.LEADER) {
            sender.sendMessage(CC.translate("&cYour cannot do this with this role."));
            return true;
        }
        if (targetRole == Role.CAPTAIN && selfRole == Role.CAPTAIN) {
            sender.sendMessage(CC.translate("&cYour cannot do this with this role."));
            return true;
        }
        Player onlineTarget = targetMember.toOnlinePlayer();
        if (playerFaction.removeMember(sender, onlineTarget, targetMember.getUniqueID(), true, true)) {
            if (onlineTarget != null) {
                onlineTarget.sendMessage(Language.F_KICK_KICKED.toString().replace("%player%", sender.getName()));
            }
            playerFaction.broadcast(Language.F_KICK_KICKED_OTHER.toString().replace("%player%", targetMember.getName()).replace("%kicker%", playerFaction.getMember(player).getRole().getAstrix() + sender.getName()));
        }
        return true;
    }
    
    @Override @SuppressWarnings("deprecation")
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (args.length != 2 || !(sender instanceof Player)) {
            return Collections.emptyList();
        }
        Player player = (Player)sender;
        PlayerFaction playerFaction = RevampHCF.getInstance().getFactionManager().getPlayerFaction(player);
        if (playerFaction == null) {
            return Collections.emptyList();
        }
        Role memberRole = playerFaction.getMember(player.getUniqueId()).getRole();
        if (memberRole == Role.MEMBER) {
            return Collections.emptyList();
        }
        List<String> results = new ArrayList<>();
        for (UUID entry : playerFaction.getMembers().keySet()) {
            Role targetRole = playerFaction.getMember(entry).getRole();
            if (targetRole != Role.LEADER) {
                if (targetRole == Role.CAPTAIN && memberRole != Role.LEADER) {
                    continue;
                }
                OfflinePlayer target = Bukkit.getOfflinePlayer(entry);
                String targetName = target.getName();
                if (targetName == null || results.contains(targetName)) {
                    continue;
                }
                results.add(targetName);
            }
        }
        return results;
    }
}
