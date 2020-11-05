package eu.revamp.hcf.commands.admin;

import eu.revamp.hcf.Language;
import eu.revamp.hcf.RevampHCF;
import eu.revamp.hcf.commands.BaseCommand;
import eu.revamp.hcf.timers.KeySaleHandler;
import eu.revamp.spigot.utils.chat.color.CC;
import eu.revamp.spigot.utils.generic.ConversionUtils;
import org.apache.commons.lang.time.DurationFormatUtils;
import org.bukkit.command.CommandSender;

public class KeySaleCommand extends BaseCommand
{
    public KeySaleCommand(RevampHCF plugin) {
        super(plugin);
        this.command = "keysale";
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
                sender.sendMessage(CC.translate("&c&lKeySale &atime must last for at least 20 ticks."));
                return;
            }
            KeySaleHandler.keysaleRunnable KeySaleRunnable = RevampHCF.getInstance().getHandlerManager().getKeysaleHandler().getKeysaleRunnable();
            if (KeySaleRunnable != null) {
                sender.sendMessage(CC.translate("&c&lKeySale &ais already enabled, use /Sale cancel to end it."));
                return;
            }
            RevampHCF.getInstance().getHandlerManager().getKeysaleHandler().start(duration);
            sender.sendMessage(CC.translate("&eStarted &e&lKeySale&a for &l" + DurationFormatUtils.formatDurationWords(duration, true, true) + "&c."));
        }
        else {
            if (!args[0].equalsIgnoreCase("end") && !args[0].equalsIgnoreCase("cancel")) return;
            if (RevampHCF.getInstance().getHandlerManager().getKeysaleHandler().cancel()) {
                sender.sendMessage(CC.translate("&cYou have successfully cancelled &e&lKeySale&c."));
                return;
            }
            sender.sendMessage(CC.translate("&c&lKeySale &cis not active."));
        }
    }
    
    public void sendUsage(CommandSender sender) {
        sender.sendMessage(CC.translate("&cKeySale - Help Commands"));
        sender.sendMessage(CC.translate("&c/KeySale start <time> - Start Key-Sale."));
        sender.sendMessage(CC.translate("&c/KeySale end - Stop Key-Sale."));
    }
}
