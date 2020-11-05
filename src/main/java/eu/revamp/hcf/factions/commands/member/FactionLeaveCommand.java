package eu.revamp.hcf.factions.commands.member;

import java.util.UUID;

import eu.revamp.hcf.Language;
import eu.revamp.hcf.RevampHCF;
import eu.revamp.hcf.factions.type.PlayerFaction;
import eu.revamp.hcf.factions.utils.struction.Role;
import org.bukkit.entity.Player;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import eu.revamp.hcf.utils.command.CommandArgument;

public class FactionLeaveCommand extends CommandArgument
{
    private final RevampHCF plugin;
    
    public FactionLeaveCommand(RevampHCF plugin) {
        super("leave", "Leave your current faction.");
        this.plugin = plugin;
    }

    @Override
    public String getUsage(String label) {
        return '/' + label + ' ' + this.getName();
    }


    @Override @SuppressWarnings("deprecation")
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = (Player)sender;
        PlayerFaction playerFaction = this.plugin.getFactionManager().getPlayerFaction(player);
        if (playerFaction == null) {
            sender.sendMessage(Language.FACTIONS_NOFACTION.toString());
            return true;
        }
        UUID uuid = player.getUniqueId();
        if (playerFaction.getMember(uuid).getRole() == Role.LEADER) {
            sender.sendMessage(Language.LEAVE_CAPTAIN.toString());
            return true;
        }
        if (playerFaction.removeMember(player, player, player.getUniqueId(), false, false)) {
            sender.sendMessage(Language.LEAVE_LEFT.toString());
            playerFaction.broadcast(Language.LEAVE_LEFT_OTHER.toString().replace("%player%", sender.getName()));
        }
        return true;
    }
}
