package eu.revamp.hcf.factions.commands.leader;

import eu.revamp.hcf.Language;
import eu.revamp.hcf.factions.utils.FactionMember;
import java.util.UUID;
import eu.revamp.hcf.factions.type.PlayerFaction;
import eu.revamp.hcf.factions.utils.struction.Role;
import eu.revamp.spigot.utils.chat.color.CC;
import org.bukkit.entity.Player;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import eu.revamp.hcf.RevampHCF;
import eu.revamp.hcf.utils.command.CommandArgument;

public class FactionCoLeaderArgument extends CommandArgument
{
    public FactionCoLeaderArgument(RevampHCF plugin) {
        super("coleader", "Sets an member as an coleader.");
        this.aliases = new String[] { "coleader", "colead", "coleaderr" };
    }

    @Override
    public String getUsage(String label) {
        return String.valueOf('/') + label + ' ' + this.getName() + " <playerName>";
    }


    @Override @SuppressWarnings("deprecation")
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 2) {
            sender.sendMessage(CC.translate("&cCorrect Usage: /f coleader <playerName>."));
            return true;
        }
        Player player = (Player)sender;
        PlayerFaction playerFaction = RevampHCF.getInstance().getFactionManager().getPlayerFaction(player.getUniqueId());
        if (playerFaction == null) {
            sender.sendMessage(Language.COMMANDS_PLAYER_NOT_FOUND.toString());
            return true;
        }
        UUID uuid = player.getUniqueId();
        FactionMember selfMember = playerFaction.getMember(uuid);
        Role selfRole = selfMember.getRole();
        if (selfRole != Role.LEADER) {
            sender.sendMessage(CC.translate("&cYou must be an leader to assign the coleader role to an member."));
            return true;
        }
        FactionMember targetMember = playerFaction.getMember(args[1]);
        if (targetMember == null) {
            sender.sendMessage(Language.COMMANDS_PLAYER_NOT_FOUND.toString());
            return true;
        }
        if (targetMember.getRole().equals(Role.COLEADER)) {
            sender.sendMessage(CC.translate("&cThis member is already a co-leader!"));
            return true;
        }
        if (targetMember.getUniqueID().equals(uuid)) {
            sender.sendMessage(CC.translate("&cYou are the leader, which means you cannot co-leader yourself."));
            return true;
        }
        targetMember.setRole(Role.COLEADER);
        playerFaction.broadcast(CC.translate("&2" + targetMember.getName() + " &ehas been promoted to a co leader."));
        return true;
    }
}
