package eu.revamp.hcf.factions.commands.captain;

import eu.revamp.hcf.Language;
import eu.revamp.hcf.RevampHCF;
import eu.revamp.spigot.utils.chat.color.CC;
import org.bukkit.OfflinePlayer;
import org.bukkit.Bukkit;
import java.util.Map;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import eu.revamp.hcf.factions.utils.FactionMember;
import eu.revamp.hcf.factions.type.PlayerFaction;
import java.util.UUID;
import eu.revamp.hcf.factions.utils.struction.Role;
import org.bukkit.entity.Player;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import eu.revamp.hcf.utils.command.CommandArgument;

public class FactionPromoteCommand extends CommandArgument
{
    private RevampHCF plugin;
    
    public FactionPromoteCommand(RevampHCF plugin) {
        super("promote", "Promotes a player to a captain.");
        this.plugin = plugin;
        this.aliases = new String[] { "captain", "officer", "mod", "moderator" };
    }

    @Override
    public String getUsage(String label) {
        return '/' + label + ' ' + this.getName() + " <playerName>";
    }
    
    @Override @SuppressWarnings("deprecation")
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 2) {
            sender.sendMessage(CC.translate("&cCorrect Usage: " + this.getUsage(label)));
            return true;
        }
        Player player = (Player)sender;
        UUID uuid = player.getUniqueId();
        PlayerFaction playerFaction = this.plugin.getFactionManager().getPlayerFaction(uuid);
        if (playerFaction == null) {
            sender.sendMessage(Language.FACTIONS_NOFACTION.toString());
            return true;
        }
        if (playerFaction.getMember(uuid).getRole() != Role.LEADER) {
            sender.sendMessage(CC.translate("&cYour cannot do this with this role."));
            return true;
        }
        FactionMember targetMember = playerFaction.getMember(args[1]);
        if (targetMember == null) {
            sender.sendMessage(CC.translate("&cThat player is not in your faction."));
            return true;
        }
        Role role = targetMember.getRole();
        if (role == Role.COLEADER) {
            sender.sendMessage(CC.translate("&cThat player is already the highest rank possible."));
            return true;
        }
        if (role == Role.MEMBER) {
            role = Role.CAPTAIN;
        }
        else if (role == Role.CAPTAIN) {
            role = Role.COLEADER;
        }
        targetMember.setRole(role);
        playerFaction.broadcast("&7" + role.getAstrix() + "&2" + targetMember.getName() + " &ehas been assigned as a faction &f" + role.name().toLowerCase() + "&e.");
        return true;
    }
    
    @Override @SuppressWarnings("deprecation")
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (args.length != 2 || !(sender instanceof Player)) {
            return Collections.emptyList();
        }
        Player player = (Player)sender;
        PlayerFaction playerFaction = this.plugin.getFactionManager().getPlayerFaction(player);
        if (playerFaction == null || playerFaction.getMember(player.getUniqueId()).getRole() != Role.LEADER) {
            return Collections.emptyList();
        }
        List<String> results = new ArrayList<>();
        for (Map.Entry<UUID, FactionMember> entry : playerFaction.getMembers().entrySet()) {
            if (entry.getValue().getRole() == Role.MEMBER) {
                OfflinePlayer target = Bukkit.getOfflinePlayer(entry.getKey());
                String targetName = target.getName();
                if (targetName == null) {
                    continue;
                }
                results.add(targetName);
            }
        }
        return results;
    }
}
