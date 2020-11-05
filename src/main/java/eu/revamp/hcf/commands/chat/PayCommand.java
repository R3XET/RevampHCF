package eu.revamp.hcf.commands.chat;

import eu.revamp.hcf.Language;
import eu.revamp.hcf.RevampHCF;
import eu.revamp.hcf.commands.BaseCommand;
import eu.revamp.hcf.playerdata.HCFPlayerData;
import eu.revamp.spigot.utils.generic.ConversionUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PayCommand extends BaseCommand
{
    public PayCommand(RevampHCF plugin) {
        super(plugin);
        this.command = "pay";
        this.permission = "revamphcf.command.pay";
    }
    
    @Override
    public void execute(CommandSender sender, String[] args) {
        Player player = (Player)sender;
        if (args.length < 2) {
            sender.sendMessage(Language.PAY_COMMAND_USAGE.toString());
            return;
        }
        Player target = Bukkit.getPlayer(args[0]);
        if (target == player) {
            sender.sendMessage(Language.PAY_PAY_YOURSELF.toString());
            return;
        }
        if (target == null) {
            sender.sendMessage(Language.COMMANDS_PLAYER_NOT_FOUND.toString());
            return;
        }
        HCFPlayerData data = this.getInstance().getHandlerManager().getHCFPlayerDataHandler().getPlayer(player);
        if (!ConversionUtils.isInteger(args[1])){
            sender.sendMessage(Language.COMMANDS_INVALID_NUMBER.toString());
            return;
        }
        int amount = Integer.parseInt(args[1]);
        if (amount <= 0) {
            sender.sendMessage(Language.PAY_INVALID_MONEY.toString());
            return;
        }
        if (data.getBalance() < amount) {
            sender.sendMessage(Language.PAY_NOT_ENOUGH_MONEY.toString().replace("%money%", String.valueOf(data.getBalance())));
            return;
        }
        HCFPlayerData targetData = this.getInstance().getHandlerManager().getHCFPlayerDataHandler().getPlayer(target);
        data.setBalance(data.getBalance() - amount);
        targetData.addBalance(amount);
        target.sendMessage(Language.PAY_MONEY_RECEIVED.toString().replace("%money%", String.valueOf(amount)).replace("%player%", sender.getName()));
        sender.sendMessage(Language.PAY_MONEY_SENT.toString().replace("%money%", String.valueOf(amount)).replace("%player%",target.getName()));
    }
}
