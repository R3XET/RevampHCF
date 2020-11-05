package eu.revamp.hcf.commands.admin;

import eu.revamp.hcf.Language;
import eu.revamp.hcf.RevampHCF;
import eu.revamp.hcf.timers.RebootHandler;
import eu.revamp.spigot.utils.chat.color.CC;
import eu.revamp.spigot.utils.generic.ConversionUtils;
import org.apache.commons.lang.time.DurationFormatUtils;
import eu.revamp.hcf.utils.chat.JavaUtils;
import org.bukkit.command.CommandSender;
import eu.revamp.hcf.commands.BaseCommand;

public class RebootCommand extends BaseCommand {
    public RebootCommand(RevampHCF plugin) {
        super(plugin);
        this.command = "reboot";
        this.permission = "*";
        this.forPlayerUseOnly = false;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length == 0) {
            sender.sendMessage(Language.REBOOT_COMMAND_USAGE.toString());
            return;
        }
        if (args[0].equalsIgnoreCase("start")) {
            if (args.length < 2) {
                sender.sendMessage(Language.REBOOT_COMMAND_USAGE.toString());
                return;
            }
            if (!ConversionUtils.isLong(args[1])){
                sender.sendMessage(Language.COMMANDS_INVALID_DURATION.toString());
                return;
            }
            long duration = Long.parseLong(args[1]);
            if (duration < 1000L) {
                sender.sendMessage(Language.REBOOT_TOO_SHORT.toString());
                return;
            }
            RebootHandler.rebootRunnable REBOOTRunnable = RevampHCF.getInstance().getHandlerManager().getRebootHandler().getRebootRunnable();
            if (REBOOTRunnable != null) {
                sender.sendMessage(Language.REBOOT_RUNNING.toString());
                return;
            }
            RevampHCF.getInstance().getHandlerManager().getRebootHandler().start(duration);
            sender.sendMessage(Language.REBOOT_STARTED.toString().replace("%time%", DurationFormatUtils.formatDurationWords(duration, true, true)));
        } else {
            if (!args[0].equalsIgnoreCase("end") && !args[0].equalsIgnoreCase("cancel")) return;
            if (RevampHCF.getInstance().getHandlerManager().getRebootHandler().cancel()) {
                sender.sendMessage(Language.REBOOT_CANCELLED.toString());
                return;
            }
            sender.sendMessage(Language.REBOOT_NOT_ACTIVE.toString());
        }
    }
}