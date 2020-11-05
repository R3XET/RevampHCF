package eu.revamp.hcf.factions.commands.captain;

import java.util.Collection;

import eu.revamp.hcf.Language;
import eu.revamp.hcf.RevampHCF;
import eu.revamp.hcf.factions.Faction;
import org.bukkit.ChatColor;
import eu.revamp.hcf.factions.utils.zone.ClaimZone;
import eu.revamp.hcf.factions.type.PlayerFaction;
import eu.revamp.spigot.utils.chat.color.CC;
import eu.revamp.hcf.factions.type.ClaimableFaction;
import org.bukkit.entity.Player;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import eu.revamp.hcf.utils.command.CommandArgument;

public class FactionClaimsCommand extends CommandArgument
{
    public FactionClaimsCommand(RevampHCF plugin) {
        super("claims", "View all claims for a faction");
    }

    @Override
    public String getUsage(String label) {
        return '/' + label + ' ' + this.getName() + " [factionName]";
    }

    @Override @SuppressWarnings("deprecation")
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = (Player)sender;
        PlayerFaction selfFaction = (sender instanceof Player) ? RevampHCF.getInstance().getFactionManager().getPlayerFaction(player) : null;
        ClaimableFaction targetFaction;
        if (args.length < 2) {
            if (!(sender instanceof Player)) {
                sender.sendMessage(CC.translate("&cCorrect Usage: " + this.getUsage(label)));
                return true;
            }
            if (selfFaction == null) {
                sender.sendMessage(Language.FACTIONS_NOFACTION.toString());
                return true;
            }
            targetFaction = selfFaction;
        }
        else {
            Faction faction = RevampHCF.getInstance().getFactionManager().getContainingFaction(args[1]);
            if (faction == null) {
                sender.sendMessage(Language.FACTIONS_FACTION_NOT_FOUND.toString());
                return true;
            }
            if (!(faction instanceof ClaimableFaction)) {
                sender.sendMessage(CC.translate("&cYou can only check the claims of factions that can have claims."));
                return true;
            }
            targetFaction = (ClaimableFaction)faction;
        }
        Collection<ClaimZone> claims = targetFaction.getClaims();
        if (claims.isEmpty()) {
            sender.sendMessage(CC.translate("&cFaction &l" + targetFaction.getDisplayName(sender) + " &chas no claimed land."));
            return true;
        }
        if (sender instanceof Player && !sender.isOp() && targetFaction instanceof PlayerFaction && ((PlayerFaction)targetFaction).getHome() == null && selfFaction != targetFaction) {
            sender.sendMessage(CC.translate("&cYou cannot view the claims of &l" + targetFaction.getDisplayName(sender) + " &cbecause their home is unset."));
            return true;
        }
        sender.sendMessage(CC.translate("&eClaims of &f" + targetFaction.getDisplayName(sender) + "&e (&f" + claims.size() + "&e):"));
        for (ClaimZone claim : claims) {
            sender.sendMessage(ChatColor.GRAY + " " + claim.getFormattedName());
        }
        return true;
    }
}
