package eu.revamp.hcf.factions.commands.staff;

import eu.revamp.hcf.Language;
import eu.revamp.hcf.RevampHCF;
import eu.revamp.hcf.factions.type.PlayerFaction;
import eu.revamp.hcf.utils.chat.JavaUtils;
import org.bukkit.Bukkit;
import eu.revamp.spigot.utils.chat.color.CC;
import org.bukkit.entity.Player;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import eu.revamp.hcf.utils.command.CommandArgument;

public class FactionSetBalanceCommand extends CommandArgument
{
    public FactionSetBalanceCommand(RevampHCF plugin) {
        super("givebal", "Gives balance to a player's faction.", new String[] { "setbalance" });
    }

    @Override
    public String getUsage(String label) {
        return '/' + label + ' ' + this.getName() + " (factionName) (balance).";
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(CC.translate("&cNo console."));
            return true;
        }
        Player player = (Player)sender;
        if (!player.hasPermission("*")) {
            sender.sendMessage(Language.COMMANDS_NO_PERMISSION_MESSAGE.toString());
            return true;
        }
        if (args.length < 3) {
            sender.sendMessage(CC.translate("&cCorrect Usage: " + this.getUsage(label)));
            return true;
        }
        if (Bukkit.getPlayer(args[1]) == null) {
            sender.sendMessage(Language.COMMANDS_PLAYER_NOT_FOUND.toString());
            return true;
        }
        Player target = Bukkit.getPlayerExact(args[1]);
        PlayerFaction playerFaction = RevampHCF.getInstance().getFactionManager().getPlayerFaction(target.getUniqueId());
        if (playerFaction == null) {
            sender.sendMessage(Language.FACTIONS_NOFACTION.toString());
            return true;
        }
        if (!args[2].chars().allMatch(Character::isDigit)) {
            sender.sendMessage(Language.COMMANDS_INVALID_NUMBER.toString());
            return true;
        }
        int factionBalance = playerFaction.getBalance();
        int addedBalance = Integer.parseInt(args[2]);
        int newBalance = factionBalance + addedBalance;
        playerFaction.setBalance(newBalance);
        playerFaction.broadcast(CC.translate("&f" + player.getName() + " &ehas given your faction &f$" + JavaUtils.format(addedBalance) + "&e."));
        for (Player all : Bukkit.getOnlinePlayers()) {
            if (all.hasPermission("*") || all.isOp()) {
                sender.sendMessage(CC.translate("&7[&2&lStaff&7] &f" + player.getName() + " &ahas given the faction &2" + playerFaction.getName() + " &f$" + addedBalance + "&e."));
            }
        }
        return true;
    }
}
