package eu.revamp.hcf.commands.factions;

import eu.revamp.hcf.Language;
import eu.revamp.hcf.RevampHCF;
import eu.revamp.hcf.commands.BaseCommand;
import eu.revamp.hcf.playerdata.HCFPlayerData;
import eu.revamp.spigot.utils.generic.ConversionUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BalanceCommand extends BaseCommand
{
    public BalanceCommand(RevampHCF plugin) {
        super(plugin);
        this.command = "balance";
        this.forPlayerUseOnly = true;
    }
    
    @Override
    public void execute(CommandSender sender, String[] args) {
        Player player = (Player)sender;
        if (args.length == 0) {
            HCFPlayerData data = RevampHCF.getInstance().getHandlerManager().getHCFPlayerDataHandler().getPlayer(player);
            sender.sendMessage(Language.BALANCE_OWN.toString().replace("%balance%", String.valueOf(data.getBalance())));
            return;
        }
        if (args.length == 1) {
            Player target = Bukkit.getPlayer(args[0]);
            if (target == null) {
                sender.sendMessage(Language.COMMANDS_PLAYER_NOT_FOUND.toString());
                return;
            }
            HCFPlayerData targetData = RevampHCF.getInstance().getHandlerManager().getHCFPlayerDataHandler().getPlayer(target);
            sender.sendMessage(Language.BALANCE_OTHER.toString().replace("%player%", target.getName()).replace("%balance%", String.valueOf(targetData.getBalance())));
        }
        else if (args[0].equalsIgnoreCase("give") || args[0].equalsIgnoreCase("add")) {
            if (!player.hasPermission("revamphcf.op")) {
                sender.sendMessage(Language.COMMANDS_NO_PERMISSION_MESSAGE.toString());
                return;
            }
            if (args.length <= 2) {
                sender.sendMessage(Language.COMMANDS_SOMETHING_WRONG.toString());
                return;
            }
            Player target = Bukkit.getPlayer(args[1]);
            if (target == null) {
                sender.sendMessage(Language.COMMANDS_PLAYER_NOT_FOUND.toString());
                return;
            }
            if (!ConversionUtils.isInteger(args[2])){
                sender.sendMessage(Language.COMMANDS_INVALID_NUMBER.toString());
                return;
            }
            int amount = Integer.parseInt(args[2]);
            HCFPlayerData targetData2 = RevampHCF.getInstance().getHandlerManager().getHCFPlayerDataHandler().getPlayer(player);
            targetData2.addBalance(amount);
            sender.sendMessage(Language.BALANCE_ADDED.toString().replace("%money%", String.valueOf(amount)).replace("%player%", target.getName()));
            target.sendMessage(Language.BALANCE_ADDED_OTHER.toString().replace("%money%", String.valueOf(amount)).replace("%player%", sender.getName()));
        }
        else if (args[0].equalsIgnoreCase("set")) {
            if (!player.hasPermission("revamphcf.op")) {
                sender.sendMessage(Language.COMMANDS_NO_PERMISSION_MESSAGE.toString());
                return;
            }
            if (args.length <= 2) {
                sender.sendMessage(Language.COMMANDS_SOMETHING_WRONG.toString());
                return;
            }
            Player target = Bukkit.getPlayer(args[1]);
            if (target == null) {
                sender.sendMessage(Language.COMMANDS_PLAYER_NOT_FOUND.toString());
                return;
            }
            if (!ConversionUtils.isInteger(args[2])){
                sender.sendMessage(Language.COMMANDS_INVALID_NUMBER.toString());
                return;
            }
            Integer amount = Integer.parseInt(args[2]);
            HCFPlayerData targetData2 = RevampHCF.getInstance().getHandlerManager().getHCFPlayerDataHandler().getPlayer(target);
            targetData2.setBalance(amount);
            sender.sendMessage(Language.BALANCE_SET.toString().replace("%money%", String.valueOf(amount)).replace("%player%", target.getName()));
            sender.sendMessage(Language.BALANCE_SET_OTHER.toString().replace("%money%", String.valueOf(amount)).replace("%player%", sender.getName()));
        }
        else if (args[0].equalsIgnoreCase("giveall")) {
            if (!player.hasPermission("revamphcf.op")) {
                sender.sendMessage(Language.COMMANDS_NO_PERMISSION_MESSAGE.toString());
                return;
            }
            if (args.length <= 1) {
                sender.sendMessage(Language.COMMANDS_SOMETHING_WRONG.toString());
                return;
            }
            if (!ConversionUtils.isInteger(args[1])){
                sender.sendMessage(Language.COMMANDS_INVALID_NUMBER.toString());
                return;
            }
            int amount2 = Integer.parseInt(args[1]);
            for (Player online : Bukkit.getOnlinePlayers()) {
                HCFPlayerData onlineData = RevampHCF.getInstance().getHandlerManager().getHCFPlayerDataHandler().getPlayer(online);
                onlineData.setBalance(onlineData.getBalance() + amount2);
            }
            Bukkit.broadcastMessage(Language.BALANCE_GIVEALL.toString().replace("%money%", String.valueOf(amount2)).replace("%player%", sender.getName()));
        }
    }
}
