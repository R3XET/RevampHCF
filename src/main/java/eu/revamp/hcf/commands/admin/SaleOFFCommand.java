package eu.revamp.hcf.commands.admin;

import eu.revamp.hcf.Language;
import eu.revamp.hcf.RevampHCF;
import eu.revamp.hcf.timers.SaleOFFHandler;
import eu.revamp.spigot.utils.chat.color.CC;
import eu.revamp.spigot.utils.generic.ConversionUtils;
import org.apache.commons.lang.time.DurationFormatUtils;
import eu.revamp.hcf.utils.chat.JavaUtils;
import org.bukkit.command.CommandSender;
import eu.revamp.hcf.commands.BaseCommand;

public class SaleOFFCommand extends BaseCommand
{
    public SaleOFFCommand(RevampHCF plugin) {
        super(plugin);
        this.command = "sale";
        this.permission = "*";
        this.forPlayerUseOnly = false;
    }
    
    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length == 0) {
            this.sendUsage(sender);
            return;
        }
        if (args[0].equalsIgnoreCase("start")) {
            if (args.length < 2) {
                this.sendUsage(sender);
                return;
            }
            if (!ConversionUtils.isLong(args[1])){
                sender.sendMessage(Language.COMMANDS_INVALID_DURATION.toString());
                return;
            }
            long duration = Long.parseLong(args[1]);
            if (duration < 1000L) {
                sender.sendMessage(CC.translate("&c&lSaleOFF &atime must last for at least 20 ticks."));
                return;
            }
            SaleOFFHandler.saleoffRunnable SaleOFFRunnable = RevampHCF.getInstance().getHandlerManager().getSaleoffHandler().getSaleoffRunnable();
            if (SaleOFFRunnable != null) {
                sender.sendMessage(CC.translate("&c&lSaleOFF &ais already enabled, use /Sale cancel to end it."));
                return;
            }
            RevampHCF.getInstance().getHandlerManager().getSaleoffHandler().start(duration);
            sender.sendMessage(CC.translate("&cStarted &c&lSaleOFF&a for &l" + DurationFormatUtils.formatDurationWords(duration, true, true) + "&c."));
        }
        else {
            if (!args[0].equalsIgnoreCase("end") && !args[0].equalsIgnoreCase("cancel")) return;
            if (RevampHCF.getInstance().getHandlerManager().getSaleoffHandler().cancel()) {
                sender.sendMessage(CC.translate("&cYou have successfully cancelled &c&lSale OFF&c."));
                return;
            }
            sender.sendMessage(CC.translate("&c&lSaleOFF &cis not active."));
        }
    }
    
    public void sendUsage(CommandSender sender) {
        sender.sendMessage(CC.translate("&cSaleOFF - Help Commands"));
        sender.sendMessage(CC.translate("&c/SaleOFF start <time> - Start SaleOFF."));
        sender.sendMessage(CC.translate("&c/SaleOFF end - Stop SaleOFF."));
    }
}
