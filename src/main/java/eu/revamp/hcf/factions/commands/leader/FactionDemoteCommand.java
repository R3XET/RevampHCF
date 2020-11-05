package eu.revamp.hcf.factions.commands.leader;

import eu.revamp.hcf.Language;
import eu.revamp.hcf.RevampHCF;
import eu.revamp.hcf.factions.utils.FactionMember;
import eu.revamp.hcf.factions.type.PlayerFaction;
import eu.revamp.hcf.factions.utils.struction.Relation;
import eu.revamp.spigot.utils.chat.color.CC;
import org.bukkit.ChatColor;
import eu.revamp.hcf.factions.utils.struction.Role;
import org.bukkit.entity.Player;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import eu.revamp.hcf.utils.command.CommandArgument;

public class FactionDemoteCommand extends CommandArgument
{
    private final RevampHCF plugin;
    
    public FactionDemoteCommand(RevampHCF plugin) {
        super("demote", "Demotes a player to a member.", new String[] { "uncaptain", "delcaptain", "delofficer" });
        this.plugin = plugin;
    }

    @Override
    public String getUsage(String label) {
        return '/' + label + ' ' + this.getName() + " <playerName>";
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(CC.translate("&cNo console."));
            return true;
        }
        if (args.length < 2) {
            sender.sendMessage(CC.translate("&cCorrect Usage: " + this.getUsage(label)));
            return true;
        }
        Player player = (Player)sender;
        PlayerFaction playerFaction = this.plugin.getFactionManager().getPlayerFaction(player);
        if (playerFaction == null) {
            sender.sendMessage(Language.FACTIONS_NOFACTION.toString());
            return true;
        }
        if (playerFaction.getMember(player.getUniqueId()).getRole() != Role.LEADER) {
            sender.sendMessage(CC.translate("&cYour cannot do this with this role."));
            return true;
        }
        FactionMember targetMember = playerFaction.getMember(args[1]);
        if (targetMember == null) {
            sender.sendMessage(ChatColor.RED + "That player is not in your faction.");
            return true;
        }
        Role role = targetMember.getRole();
        if (role == Role.MEMBER) {
            sender.sendMessage(ChatColor.RED + "That player is already the lowest rank possible.");
            return true;
        }
        if (role == Role.CAPTAIN) {
            role = Role.MEMBER;
        }
        else if (role == Role.COLEADER) {
            role = Role.CAPTAIN;
        }
        targetMember.setRole(role);
        playerFaction.broadcast(Relation.MEMBER.toChatColour() + targetMember.getName() + ChatColor.YELLOW + " has been demoted to a faction " + role.name().toLowerCase() + ".");
        return true;
    }
}
