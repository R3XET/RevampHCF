package eu.revamp.hcf.factions.commands.captain;

import eu.revamp.hcf.Language;
import eu.revamp.hcf.RevampHCF;
import org.bukkit.Location;
import eu.revamp.hcf.factions.utils.FactionMember;
import eu.revamp.hcf.factions.type.PlayerFaction;
import eu.revamp.spigot.utils.chat.color.CC;
import eu.revamp.hcf.factions.utils.zone.ClaimZone;
import eu.revamp.hcf.factions.utils.struction.Role;
import org.bukkit.entity.Player;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import eu.revamp.hcf.utils.command.CommandArgument;

public class FactionSetHQCommand extends CommandArgument
{
    private final RevampHCF plugin;
    
    public FactionSetHQCommand(RevampHCF plugin) {
        super("sethome", "Sets the faction home location.", new String[] { "sethq" });
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
        FactionMember factionMember = playerFaction.getMember(player);
        if (factionMember.getRole() == Role.MEMBER) {
            sender.sendMessage(CC.translate("&cYour cannot do this with this role."));
            return true;
        }
        Location location = player.getLocation();
        boolean insideTerritory = false;
        for (ClaimZone claim : playerFaction.getClaims()) {
            if (claim.contains(location)) {
                insideTerritory = true;
                break;
            }
        }
        if (!insideTerritory) {
            player.sendMessage(CC.translate("&cYou may only set your home in your territory."));
            return true;
        }
        playerFaction.setHome(location);
        playerFaction.broadcast(CC.translate("&a" + factionMember.getRole().getAstrix() + sender.getName() + " &ehas updated the faction home."));
        return true;
    }
}
