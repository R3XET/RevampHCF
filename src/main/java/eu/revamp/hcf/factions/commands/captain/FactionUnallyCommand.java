package eu.revamp.hcf.factions.commands.captain;

import com.google.common.collect.Lists;
import com.google.common.collect.Iterables;

import java.util.stream.Collectors;
import java.util.Collections;
import java.util.List;

import eu.revamp.hcf.Language;
import eu.revamp.hcf.RevampHCF;
import org.bukkit.Bukkit;
import eu.revamp.hcf.factions.events.FactionRelationRemoveEvent;
import eu.revamp.hcf.factions.Faction;
import java.util.Collection;
import eu.revamp.hcf.factions.type.PlayerFaction;
import java.util.HashSet;
import eu.revamp.hcf.factions.utils.struction.Relation;
import eu.revamp.hcf.factions.utils.struction.Role;
import org.bukkit.entity.Player;
import eu.revamp.spigot.utils.chat.color.CC;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import com.google.common.collect.ImmutableList;
import eu.revamp.hcf.utils.command.CommandArgument;

public class FactionUnallyCommand extends CommandArgument
{
    private final RevampHCF plugin;
    private static final ImmutableList<String> COMPLETIONS  = ImmutableList.of("all");

    public FactionUnallyCommand(RevampHCF plugin) {
        super("unally", "Remove an ally pact with other factions.");
        this.plugin = plugin;
        this.aliases = new String[] { "neutral" };
    }

    @Override
    public String getUsage(String label) {
        return '/' + label + ' ' + this.getName() + " <all|factionName>";
    }

    @Override @SuppressWarnings("deprecation")
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (RevampHCF.getInstance().getConfiguration().getMaxAllysPerFaction() <= 0) {
            sender.sendMessage(CC.translate("&cAllies are currently disabled this map."));
            return true;
        }
        if (args.length < 2) {
            sender.sendMessage(Language.UNALLY_COMMAND_USAGE.toString());
            return true;
        }
        Player player = (Player)sender;
        PlayerFaction playerFaction = this.plugin.getFactionManager().getPlayerFaction(player);
        if (playerFaction == null) {
            sender.sendMessage(Language.FACTIONS_NOFACTION.toString());
            return true;
        }
        if (playerFaction.getMember(player.getUniqueId()).getRole() == Role.MEMBER) {
            sender.sendMessage(CC.translate("&cYour cannot do this with this role."));
            return true;
        }
        Relation relation = Relation.ALLY;
        Collection<PlayerFaction> targetFactions = new HashSet<>();
        if (args[1].equalsIgnoreCase("all")) {
            Collection<PlayerFaction> allies = playerFaction.getAlliedFactions();
            if (allies.isEmpty()) {
                sender.sendMessage(Language.UNALLY_NO_ALLIES.toString());
                return true;
            }
            targetFactions.addAll(allies);
        }
        else {
            Faction searchedFaction = this.plugin.getFactionManager().getContainingFaction(args[1]);
            if (!(searchedFaction instanceof PlayerFaction)) {
                sender.sendMessage(Language.FACTIONS_NOFACTION.toString());
                return true;
            }
            targetFactions.add((PlayerFaction)searchedFaction);
        }
        for (PlayerFaction targetFaction : targetFactions) {
            if (playerFaction.getRelations().remove(targetFaction.getUniqueID()) == null || targetFaction.getRelations().remove(playerFaction.getUniqueID()) == null) {
                sender.sendMessage(Language.UNALLY_ERROR.toString().replace("%faction%", targetFaction.getDisplayName(playerFaction)).replace("%relation%", relation.getDisplayName()));
                return true;
            }
            FactionRelationRemoveEvent event = new FactionRelationRemoveEvent(playerFaction, targetFaction, Relation.ALLY);
            Bukkit.getPluginManager().callEvent(event);
            if (event.isCancelled()) {
                sender.sendMessage(Language.UNALLY_EVENT_CANCELLED.toString().replace("%faction%", targetFaction.getDisplayName(playerFaction)).replace("%relation%", relation.getDisplayName()));
                return true;
            }
            playerFaction.broadcast(Language.UNALLY_DROPPED.toString().replace("%faction%", targetFaction.getDisplayName(playerFaction)).replace("%relation%", relation.getDisplayName()));
            targetFaction.broadcast(Language.UNALLY_DROPPED_OTHER.toString().replace("%faction%", targetFaction.getDisplayName(playerFaction)).replace("%relation%", relation.getDisplayName()));
        }
        return true;
    }
    
    @Override @SuppressWarnings("deprecation")
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (args.length != 2 || !(sender instanceof Player)) {
            return Collections.emptyList();
        }
        Player player = (Player)sender;
        PlayerFaction playerFaction = this.plugin.getFactionManager().getPlayerFaction(player);
        if (playerFaction == null) {
            return Collections.emptyList();
        }
        return Lists.newArrayList(Iterables.concat(FactionUnallyCommand.COMPLETIONS, playerFaction.getAlliedFactions().stream().map(Faction::getName).collect(Collectors.toList())));
    }
}
