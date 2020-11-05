package eu.revamp.hcf.factions.commands.captain;

import java.util.Set;
import java.util.List;
import eu.revamp.spigot.utils.chat.color.CC;
import java.util.Collection;
import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import eu.revamp.hcf.factions.type.PlayerFaction;
import eu.revamp.hcf.factions.Faction;
import java.util.ArrayList;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import eu.revamp.hcf.RevampHCF;
import eu.revamp.hcf.utils.command.CommandArgument;

public class FactionInvitesCommand extends CommandArgument
{
    public FactionInvitesCommand(RevampHCF plugin) {
        super("invites", "View faction invitations.");
    }

    @Override
    public String getUsage(String label) {
        return '/' + label + ' ' + this.getName();
    }


    @Override @SuppressWarnings("deprecation")
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        List<String> receivedInvites = new ArrayList<>();
        for (Faction faction : RevampHCF.getInstance().getFactionManager().getFactions()) {
            if (faction instanceof PlayerFaction) {
                PlayerFaction targetPlayerFaction = (PlayerFaction)faction;
                if (!targetPlayerFaction.getInvitedPlayerNames().contains(sender.getName())) {
                    continue;
                }
                receivedInvites.add(targetPlayerFaction.getDisplayName(sender));
            }
        }
        PlayerFaction playerFaction = RevampHCF.getInstance().getFactionManager().getPlayerFaction((Player)sender);
        String delimiter = ChatColor.WHITE + ", " + ChatColor.GRAY;
        if (playerFaction != null) {
            Set<String> sentInvites = playerFaction.getInvitedPlayerNames();
            sender.sendMessage(CC.translate("&eSent by &f" + playerFaction.getDisplayName(sender) + "&e (&f" + sentInvites.size() + "): &e" + (sentInvites.isEmpty() ? "Your faction has not invited anyone." : (String.valueOf(StringUtils.join((Collection)sentInvites, delimiter)) + '.'))));
        }
        sender.sendMessage(CC.translate("&eRequested (&f" + receivedInvites.size() + "&e): &e" + (receivedInvites.isEmpty() ? "No factions have invited you." : (String.valueOf(StringUtils.join((Collection)receivedInvites, new StringBuilder().append(ChatColor.WHITE).append(delimiter).toString())) + '.'))));
        return true;
    }
}
