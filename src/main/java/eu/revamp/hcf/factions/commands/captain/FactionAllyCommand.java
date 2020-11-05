package eu.revamp.hcf.factions.commands.captain;

import java.util.UUID;
import java.util.Collection;

import eu.revamp.hcf.Language;
import eu.revamp.hcf.RevampHCF;
import eu.revamp.spigot.utils.chat.color.CC;
import org.bukkit.Bukkit;
import eu.revamp.hcf.factions.events.FactionRelationCreateEvent;
import eu.revamp.hcf.factions.Faction;
import eu.revamp.hcf.factions.type.PlayerFaction;
import eu.revamp.hcf.factions.utils.struction.Role;
import org.bukkit.entity.Player;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import eu.revamp.hcf.factions.utils.struction.Relation;
import eu.revamp.hcf.utils.command.CommandArgument;

public class FactionAllyCommand extends CommandArgument
{
    private static final Relation RELATION = Relation.ALLY;
    
    public FactionAllyCommand(RevampHCF plugin) {
        super("ally", "Make an ally pact with other factions.", new String[] { "alliance" });
    }
    @Override
    public String getUsage(String label) {
        return '/' + label + ' ' + this.getName() + " <factionName>";
    }
    
    @Override @SuppressWarnings("deprecation")
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = (Player)sender;
        if (RevampHCF.getInstance().getConfiguration().getMaxAllysPerFaction() <= 0) {
            player.sendMessage(CC.translate("&cAllies are currently disabled."));
            return true;
        }
        if (args.length < 2) {
            sender.sendMessage(CC.translate("&cCorrect Usage: " + this.getUsage(label)));
            return true;
        }
        PlayerFaction playerFaction = RevampHCF.getInstance().getFactionManager().getPlayerFaction(player);
        if (playerFaction == null) {
            player.sendMessage(Language.FACTIONS_NOFACTION.toString());
            return true;
        }
        if (playerFaction.getMember(player.getName()).getRole() == Role.MEMBER) {
            player.sendMessage(CC.translate("&cYour cannot do this with this role."));
            return true;
        }
        Faction containingFaction = RevampHCF.getInstance().getFactionManager().getContainingFaction(args[1]);
        if (!(containingFaction instanceof PlayerFaction)) {
            player.sendMessage(Language.FACTIONS_FACTION_NOT_FOUND.toString());
            return true;
        }
        PlayerFaction targetFaction = (PlayerFaction)containingFaction;
        if (playerFaction == targetFaction) {
            player.sendMessage(CC.translate("&cYou can't send requests to your own faction!"));
            return true;
        }
        Collection<UUID> allied = playerFaction.getAllied();
        if (allied.size() >= RevampHCF.getInstance().getConfiguration().getMaxAllysPerFaction()) {
            player.sendMessage(CC.translate("&cYour faction has already reached the alliance limit."));
            return true;
        }
        if (targetFaction.getAllied().size() >= RevampHCF.getInstance().getConfiguration().getMaxAllysPerFaction()) {
            player.sendMessage(CC.translate("&c&l" + targetFaction.getDisplayName(sender) + " &chas reached their maximum alliance limit!"));
            return true;
        }
        if (allied.contains(targetFaction.getUniqueID())) {
            player.sendMessage(CC.translate("&cYour faction is already in relation ship with &l" + FactionAllyCommand.RELATION.getDisplayName() + targetFaction.getDisplayName(playerFaction) + "&c!"));
            return true;
        }
        if (targetFaction.getRequestedRelations().remove(playerFaction.getUniqueID()) != null) {
            FactionRelationCreateEvent event = new FactionRelationCreateEvent(playerFaction, targetFaction, FactionAllyCommand.RELATION);
            Bukkit.getPluginManager().callEvent(event);
            targetFaction.getRelations().put(playerFaction.getUniqueID(), FactionAllyCommand.RELATION);
            targetFaction.broadcast("&eYour faction is now &d" + FactionAllyCommand.RELATION.getDisplayName() + " &ewith &d" + playerFaction.getDisplayName(targetFaction) + "&e!");
            playerFaction.getRelations().put(targetFaction.getUniqueID(), FactionAllyCommand.RELATION);
            playerFaction.broadcast("&eYour faction is now &d" + FactionAllyCommand.RELATION.getDisplayName() + " &ewith &d" + targetFaction.getDisplayName(playerFaction) + "&e!");
            return true;
        }
        if (playerFaction.getRequestedRelations().putIfAbsent(targetFaction.getUniqueID(), FactionAllyCommand.RELATION) != null) {
            player.sendMessage("&eYour faction has already requested to " + FactionAllyCommand.RELATION.getDisplayName() + " &ewith " + targetFaction.getDisplayName(playerFaction) + "&e!");
            return true;
        }
        playerFaction.broadcast(targetFaction.getDisplayName(playerFaction) + " &ewere informed that you wish to be &d" + FactionAllyCommand.RELATION.getDisplayName());
        targetFaction.broadcast(playerFaction.getDisplayName(targetFaction) + " &ehas sent a request to be " + FactionAllyCommand.RELATION.getDisplayName() + ". Use " + RevampHCF.getInstance().getConfiguration().getAllyColor() + "&e/faction ally " + playerFaction.getName() + " &eto accept!");
        return true;
    }
}
