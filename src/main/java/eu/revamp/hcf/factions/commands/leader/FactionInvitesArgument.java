package eu.revamp.hcf.factions.commands.leader;

import eu.revamp.hcf.RevampHCF;
import eu.revamp.hcf.factions.Faction;
import eu.revamp.hcf.utils.command.CommandArgument;
import eu.revamp.hcf.factions.type.PlayerFaction;
import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class FactionInvitesArgument extends CommandArgument
{
    private RevampHCF plugin;
    
    public FactionInvitesArgument(RevampHCF plugin) {
        super("invites", "Disband your faction.");
        this.plugin = plugin;
    }
    
    public String getUsage(String label) {
        return '/' + label + ' ' + this.getName();
    }
    
    @Override @SuppressWarnings("deprecation")
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Only players can have faction invites.");
            return true;
        }
        List<String> receivedInvites = new ArrayList<>();
        for (Faction faction : this.plugin.getFactionManager().getFactions()) {
            if (faction instanceof PlayerFaction) {
                PlayerFaction targetPlayerFaction = (PlayerFaction)faction;
                if (!targetPlayerFaction.getInvitedPlayerNames().contains(sender.getName())) {
                    continue;
                }
                receivedInvites.add(targetPlayerFaction.getDisplayName(sender));
            }
        }
        PlayerFaction playerFaction = this.plugin.getFactionManager().getPlayerFaction((Player)sender);
        String delimiter = ChatColor.WHITE + ", " + ChatColor.GRAY;
        if (playerFaction != null) {
            Set<String> sentInvites = playerFaction.getInvitedPlayerNames();
            sender.sendMessage(ChatColor.AQUA + "Sent by " + playerFaction.getDisplayName(sender) + ChatColor.AQUA + " (" + sentInvites.size() + ')' + ChatColor.DARK_AQUA + ": " + ChatColor.GRAY + (sentInvites.isEmpty() ? "Your faction has not invited anyone." : (String.valueOf(StringUtils.join(sentInvites, delimiter)) + '.')));
        }
        sender.sendMessage(ChatColor.AQUA + "Requested (" + receivedInvites.size() + ')' + ChatColor.DARK_AQUA + ": " + ChatColor.GRAY + (receivedInvites.isEmpty() ? "No factions have invited you." : (String.valueOf(StringUtils.join(receivedInvites, new StringBuilder().append(ChatColor.WHITE).append(delimiter).toString())) + '.')));
        return true;
    }
}
