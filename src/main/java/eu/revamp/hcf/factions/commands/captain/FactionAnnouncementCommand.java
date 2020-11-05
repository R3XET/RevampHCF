package eu.revamp.hcf.factions.commands.captain;

import eu.revamp.hcf.Language;
import eu.revamp.hcf.factions.type.PlayerFaction;
import eu.revamp.spigot.utils.chat.color.CC;
import org.apache.commons.lang.StringUtils;
import eu.revamp.hcf.factions.utils.struction.Role;
import org.bukkit.entity.Player;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import eu.revamp.hcf.RevampHCF;
import eu.revamp.hcf.utils.command.CommandArgument;

public class FactionAnnouncementCommand extends CommandArgument
{
    public FactionAnnouncementCommand(RevampHCF plugin) {
        super("announcement", "Sey your faction's announcement", new String[] { "announce", "motd" });
    }

    @Override
    public String getUsage(String label) {
        return '/' + label + ' ' + this.getName() + " <newAnnouncement>";
    }

    @Override @SuppressWarnings("deprecation")
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 2) {
            sender.sendMessage(CC.translate("&cCorrect Usage: " + this.getUsage(label)));
            return true;
        }
        Player player = (Player)sender;
        PlayerFaction playerFaction = RevampHCF.getInstance().getFactionManager().getPlayerFaction(player);
        if (playerFaction == null) {
            sender.sendMessage(Language.FACTIONS_NOFACTION.toString());
            return true;
        }
        if (playerFaction.getMember(player.getUniqueId()).getRole() == Role.MEMBER) {
            sender.sendMessage(CC.translate("&cYour cannot do this with this role."));
            return true;
        }
        String oldAnnouncement = playerFaction.getAnnouncement();
        String newAnnouncement;
        if (args[1].equalsIgnoreCase("clear") || args[1].equalsIgnoreCase("none") || args[1].equalsIgnoreCase("remove")) {
            newAnnouncement = null;
        }
        else {
            newAnnouncement = StringUtils.join(args, ' ', 1, args.length);
        }
        if (oldAnnouncement == null && newAnnouncement == null) {
            sender.sendMessage(CC.translate("&cYour factions' announcement is already unset."));
            return true;
        }
        if (oldAnnouncement != null && newAnnouncement != null && oldAnnouncement.equals(newAnnouncement)) {
            sender.sendMessage(CC.translate("&cYour faction's announcement is already &l" + newAnnouncement + "&c"));
            return true;
        }
        playerFaction.setAnnouncement(newAnnouncement);
        if (newAnnouncement == null) {
            playerFaction.broadcast(CC.translate("&f" + sender.getName() + " &ehas cleared the faction's announcement."));
            return true;
        }
        playerFaction.broadcast(CC.translate("&f" + player.getName() + " &ehas updated the faction's announcement from &f" + ((oldAnnouncement != null) ? oldAnnouncement : "none") + " &eto &f" + newAnnouncement));
        return true;
    }
}
