package eu.revamp.hcf.factions.commands.member;

import eu.revamp.hcf.Language;
import eu.revamp.hcf.RevampHCF;
import eu.revamp.hcf.factions.Faction;
import eu.revamp.hcf.utils.command.CommandArgument;
import eu.revamp.hcf.factions.type.PlayerFaction;
import eu.revamp.hcf.factions.utils.FactionMember;
import eu.revamp.hcf.factions.utils.struction.Relation;
import eu.revamp.hcf.factions.utils.struction.Role;
import eu.revamp.spigot.utils.chat.color.CC;
import eu.revamp.system.enums.ChatChannel;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class FactionAcceptCommand extends CommandArgument
{
    private RevampHCF plugin;
    
    public FactionAcceptCommand(RevampHCF plugin) {
        super("accept", "Accept a pending invitation", new String[] { "join", "a" });
        this.plugin = plugin;
    }

    @Override
    public String getUsage(String label) {
        return '/' + label + ' ' + this.getName() + " <playerName|factionName>";
    }

    @Override @SuppressWarnings("deprecation")
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 2) {
            sender.sendMessage(CC.translate("&cCorrect Usage: " + this.getUsage(label)));
            return true;
        }
        Player player = (Player)sender;
        if (this.plugin.getFactionManager().getPlayerFaction(player) != null) {
            sender.sendMessage(Language.FACTIONS_ALREADY_IN_A_FACTION.toString());
            return true;
        }
        Faction faction = this.plugin.getFactionManager().getContainingFaction(args[1]);
        if (faction == null) {
            sender.sendMessage(Language.FACTIONS_FACTION_NOT_FOUND.toString());
            return true;
        }
        if (!(faction instanceof PlayerFaction)) {
            sender.sendMessage(CC.translate("&cYou cannot join system factions."));
            return true;
        }
        PlayerFaction targetFaction = (PlayerFaction)faction;
        if (targetFaction.getMembers().size() >= RevampHCF.getInstance().getConfiguration().getMaxMembers()) {
            sender.sendMessage(CC.translate("&f" + faction.getDisplayName(sender) + " &eis currently full."));
            sender.sendMessage(CC.translate("&cFaction limits are &f" + RevampHCF.getInstance().getConfiguration().getMaxMembers() + "&c."));
            return true;
        }
        if (!targetFaction.isOpen() && !targetFaction.getInvitedPlayerNames().contains(player.getName())) {
            sender.sendMessage(CC.translate("&f" + faction.getDisplayName(sender) + " &ehas not invited you."));
            return true;
        }
        if (targetFaction.addMember(player, player, player.getUniqueId(), new FactionMember(player, ChatChannel.PUBLIC, Role.MEMBER))) {
            targetFaction.broadcast(CC.translate(Relation.MEMBER.toChatColour() + sender.getName() + " &ehas joined the faction."));
        }
        return true;
    }
    
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (args.length != 2 || !(sender instanceof Player)) {
            return Collections.emptyList();
        }
        return this.plugin.getFactionManager().getFactions().stream().filter(faction -> faction instanceof PlayerFaction && ((PlayerFaction) faction).getInvitedPlayerNames().contains(sender.getName())).map(faction -> sender.getName()).collect(Collectors.toList());
    }
}
