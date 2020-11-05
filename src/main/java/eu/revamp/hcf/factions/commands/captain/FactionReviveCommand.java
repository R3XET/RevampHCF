package eu.revamp.hcf.factions.commands.captain;

import eu.revamp.hcf.Language;
import eu.revamp.hcf.RevampHCF;
import eu.revamp.hcf.factions.utils.FactionMember;
import eu.revamp.hcf.factions.type.PlayerFaction;
import org.bukkit.OfflinePlayer;
import java.io.File;
import eu.revamp.hcf.factions.utils.struction.Role;
import org.bukkit.Bukkit;
import eu.revamp.spigot.utils.chat.color.CC;
import org.bukkit.entity.Player;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import eu.revamp.hcf.utils.command.CommandArgument;

public class FactionReviveCommand extends CommandArgument
{
    public FactionReviveCommand(RevampHCF plugin) {
        super("revive", "Revive a player using faction lives.");
    }

    @Override
    public String getUsage(String label) {
        return String.valueOf('/') + label + ' ' + this.getName() + " <playerName>";
    }

    @Override @SuppressWarnings("deprecation")
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(CC.translate("&cNo console."));
            return true;
        }
        if (args.length < 2) {
            sender.sendMessage(CC.translate("&cCorrect Usage: " + this.getUsage(label)));
            return true;
        }
        OfflinePlayer target = Bukkit.getOfflinePlayer(args[1]);
        if (!target.hasPlayedBefore() && !target.isOnline()) {
            sender.sendMessage(Language.COMMANDS_PLAYER_NOT_FOUND.toString());
            return true;
        }
        Player player = (Player)sender;
        PlayerFaction playerFaction = RevampHCF.getInstance().getFactionManager().getPlayerFaction(player);
        if (playerFaction == null) {
            sender.sendMessage(Language.FACTIONS_NOFACTION.toString());
            return true;
        }
        FactionMember factionMember = playerFaction.getMember(player);
        if (factionMember.getRole() == Role.MEMBER) {
            sender.sendMessage(CC.translate("&cYour cannot do this with this role."));
            return true;
        }
        if (playerFaction.getMember(target.getName()) == null) {
            sender.sendMessage(CC.translate("&cThat player is not in your faction."));
            return true;
        }
        if (playerFaction.getLives() <= 0) {
            sender.sendMessage(CC.translate("&cYour faction doesn't have enough lives to revive that member!"));
            return true;
        }
        if (playerFaction.getLives() - 1 < 0) {
            sender.sendMessage(CC.translate("&cYour faction doesn't have enough lives to revive that member!"));
            return true;
        }
        File targetFile = new File(RevampHCF.getInstance().getHandlerManager().getDeathBanHandler().getDeathbansFolder(), target.getUniqueId().toString() + ".yml");
        if (targetFile.exists()) {
            targetFile.delete();
            playerFaction.setLives(playerFaction.getLives() - 1);
            playerFaction.broadcast(CC.translate("&2" + factionMember.getRole().getAstrix() + sender.getName() + " &ehas revived &2" + target.getName() + " &eusing &f1 &efaction life."));
            sender.sendMessage(CC.translate("&aYou have successfully revived &l" + target.getName() + "&a."));
        }
        else {
            sender.sendMessage(CC.translate("&c&l" + target.getName() + " &cis not death-banned."));
        }
        return true;
    }
}
