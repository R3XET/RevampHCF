package eu.revamp.hcf.factions.commands.member;

import java.util.List;

import eu.revamp.hcf.Language;
import eu.revamp.hcf.RevampHCF;
import eu.revamp.spigot.utils.chat.color.CC;
import org.bukkit.Location;
import eu.revamp.hcf.factions.utils.FactionMember;
import eu.revamp.hcf.factions.type.PlayerFaction;
import java.util.Collections;
import java.util.Collection;
import eu.revamp.hcf.factions.utils.zone.ClaimZone;
import java.util.ArrayList;

import eu.revamp.hcf.factions.utils.struction.Role;
import org.bukkit.entity.Player;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import com.google.common.collect.ImmutableList;
import eu.revamp.hcf.utils.command.CommandArgument;

public class FactionUnclaimCommand extends CommandArgument
{
    private final RevampHCF plugin;
    private static final ImmutableList<String> COMPLETIONS = ImmutableList.of("all");

    public FactionUnclaimCommand(RevampHCF plugin) {
        super("unclaim", "Unclaims land from your faction.");
        this.plugin = plugin;
    }

    @Override
    public String getUsage(String label) {
        return '/' + label + ' ' + this.getName() + " ";
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
        if (factionMember.getRole() != Role.LEADER) {
            sender.sendMessage(CC.translate("&cYour cannot do this with this role."));
            return true;
        }
        Collection<ClaimZone> factionClaims = playerFaction.getClaims();
        if (factionClaims.isEmpty()) {
            sender.sendMessage(CC.translate("Your faction don't have any claims."));
            return true;
        }
        Collection<ClaimZone> removingClaims;
        if (args.length > 1 && args[1].equalsIgnoreCase("all")) {
            removingClaims = new ArrayList<>(factionClaims);
        }
        else {
            Location location = player.getLocation();
            ClaimZone claimAt = this.plugin.getFactionManager().getClaimAt(location);
            if (claimAt == null || !factionClaims.contains(claimAt)) {
                sender.sendMessage(CC.translate("&cYour faction don't have any claims."));
                return true;
            }
            removingClaims = Collections.singleton(claimAt);
        }
        if (!playerFaction.removeClaims(removingClaims, player)) {
            sender.sendMessage(CC.translate("&c&lError when removing claims, please contact an Administrator."));
            return true;
        }
        int removingAmount = removingClaims.size();
        playerFaction.broadcast(CC.translate("&7" + factionMember.getRole().getAstrix() + "&2" + sender.getName() + " &ehas removed &c&l" + removingAmount + " &eclaim" + ((removingAmount > 1) ? "&es" : "") + '.'));
        return true;
    }
    
    @Override @SuppressWarnings("unchecked")
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        return (List<String>)((args.length == 2) ? FactionUnclaimCommand.COMPLETIONS : Collections.emptyList());
    }
}
