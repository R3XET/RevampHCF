package eu.revamp.hcf.commands.games;

import eu.revamp.hcf.Language;
import eu.revamp.hcf.RevampHCF;
import eu.revamp.hcf.playerdata.HCFPlayerData;
import eu.revamp.hcf.timers.SOTWHandler;
import eu.revamp.spigot.utils.chat.color.CC;
import eu.revamp.spigot.utils.generic.ConversionUtils;
import org.apache.commons.lang.time.DurationFormatUtils;
import org.bukkit.entity.Player;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import eu.revamp.hcf.commands.BaseCommand;

public class SOTWCommand extends BaseCommand
{
    public SOTWCommand(RevampHCF plugin) {
        super(plugin);
        this.command = "sotw";
        this.forPlayerUseOnly = false;
    }
    
    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length == 0) {
            this.sendUsage(sender);
            return;
        }
        if (args[0].equalsIgnoreCase("start") && sender.hasPermission("*")) {
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
                sender.sendMessage(CC.translate("&a&lSOTW Protection&c time must last for at least 20 ticks."));
                return;
            }
            SOTWHandler.SotwRunnable sotwRunnable = RevampHCF.getInstance().getHandlerManager().getSotwHandler().getSotwRunnable();
            if (sotwRunnable != null) {
                sender.sendMessage(CC.translate("&a&lSOTW Protection&c is already enabled, use /sotw cancel to end it."));
                return;
            }
            RevampHCF.getInstance().getHandlerManager().getSotwHandler().start(duration);
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (!this.getInstance().getConfiguration().isKitMap() && this.getInstance().getHandlerManager().getTimerManager().getPvpTimerHandler().getRemaining(player) > 0L) {
                    this.getInstance().getHandlerManager().getTimerManager().getPvpTimerHandler().clearCooldown(player);
                    HCFPlayerData data = this.getInstance().getHandlerManager().getHCFPlayerDataHandler().getPlayer(player);
                    data.setPvpTimerCooldown(0);
                }
                HCFPlayerData data = this.getInstance().getHandlerManager().getHCFPlayerDataHandler().getPlayer(player);
                if (data.getSpawnTagCooldown() > 0L) {
                    data.setSpawnTagCooldown(0L);
                    this.getInstance().getHandlerManager().getTimerManager().getSpawnTagHandler().clearCooldown(player);
                }
            }
            sender.sendMessage(CC.translate("&cStarted &a&lSOTW Protection&c for &l" + DurationFormatUtils.formatDurationWords(duration, true, true) + "&c."));
        }
        else if (args[0].equalsIgnoreCase("enable") || (args[0].equalsIgnoreCase("pvp") && sender.hasPermission("sotw.enable"))) {
            if (RevampHCF.getInstance().getHandlerManager().getSotwHandler().sotwRunnable != null && !RevampHCF.getInstance().getHandlerManager().getSotwHandler().isSotwEnabled((Player)sender)) {
                RevampHCF.getInstance().getHandlerManager().getSotwHandler().getEnabledSotws().add(((Player)sender).getUniqueId());
                sender.sendMessage(CC.translate("&aYour sotw protection timer has been enabled, you can now take deal damage to players who don't have thier SOTW timer."));
                return;
            }
            sender.sendMessage(CC.translate("&a&lSOTW Protection &cis not active."));
        }
        else {
            if ((!args[0].equalsIgnoreCase("end") || !sender.hasPermission("*")) && (!args[0].equalsIgnoreCase("cancel") || !sender.hasPermission("*"))) {
                if (args[0].equalsIgnoreCase("start2") && sender.hasPermission("*")) {
                    if (!RevampHCF.getInstance().getHandlerManager().getSotwHandler().isRunning()) {
                        long duration = Long.parseLong(args[1]);
                        RevampHCF.getInstance().getHandlerManager().getSotwHandler().start(duration);
                        sender.sendMessage(CC.translate("&cStarted &a&lSOTW Protection&c for &l" + DurationFormatUtils.formatDurationWords(duration, true, true) + "&c."));
                    }
                    else {
                        sender.sendMessage(CC.translate("&a&lSOTW Protection&c is already enabled, use /sotw cancel to end it."));
                    }
                }
                else if (args[0].equalsIgnoreCase("startnosb") && sender.hasPermission("*")) {
                    if (!RevampHCF.getInstance().getHandlerManager().getSotwHandler().isRunning()) {
                        RevampHCF.getInstance().getHandlerManager().getSotwHandler().startSOTW(3600);
                        Bukkit.broadcastMessage("§f§m-------------------------------");
                        Bukkit.broadcastMessage(" ");
                        Bukkit.broadcastMessage(" ");
                        Bukkit.broadcastMessage("§a§lSOTW has been activated!");
                        Bukkit.broadcastMessage(" §7§ §c120 Minutes");
                        Bukkit.broadcastMessage(" ");
                        Bukkit.broadcastMessage(" ");
                        Bukkit.broadcastMessage("§f§m-------------------------------");
                    }
                    else {
                        sender.sendMessage(CC.translate("&a&lSOTW Protection&c is already enabled, use /sotw cancel to end it."));
                    }
                }
                else if ((args[0].equalsIgnoreCase("endnosb") && sender.hasPermission("*")) || (args[0].equalsIgnoreCase("stopnosb") && sender.hasPermission("*"))) {
                    if (RevampHCF.getInstance().getHandlerManager().getSotwHandler().isRunning()) {
                        RevampHCF.getInstance().getHandlerManager().getSotwHandler().getEnabledSotws().clear();
                        RevampHCF.getInstance().getHandlerManager().getSotwHandler().stopSOTW();
                        Bukkit.broadcastMessage("§f§m-------------------------------");
                        Bukkit.broadcastMessage(" ");
                        Bukkit.broadcastMessage(" ");
                        Bukkit.broadcastMessage("§a§lSOTW has been deactivated!");
                        Bukkit.broadcastMessage(" ");
                        Bukkit.broadcastMessage(" ");
                        Bukkit.broadcastMessage("§f§m-------------------------------");
                    }
                    else {
                        sender.sendMessage(CC.translate("&a&lSOTW Protection &cis not active."));
                    }
                }
                return;
            }
            if (RevampHCF.getInstance().getHandlerManager().getSotwHandler().cancel()) {
                RevampHCF.getInstance().getHandlerManager().getSotwHandler().getEnabledSotws().clear();
                sender.sendMessage(CC.translate("&cYou have successfully cancelled &a&lSOTW Protection&c."));
                return;
            }
            sender.sendMessage(CC.translate("&a&lSOTW Protection &cis not active."));
        }
    }
    
    public void sendUsage(CommandSender sender) {
        if (sender.hasPermission("*")) {
            sender.sendMessage(CC.translate("&cSOTW - Help Commands"));
            sender.sendMessage(CC.translate("&c/sotw start <time> - Start SOTW Prot."));
            sender.sendMessage(CC.translate("&c/sotw enable - Disable your sotw timer."));
            sender.sendMessage(CC.translate("&c/sotw end - Stop SOTW Prot."));
            sender.sendMessage(CC.translate("&c/sotw startnosb - Start SOTW without scoreboard."));
            sender.sendMessage(CC.translate("&c/sotw endnosb - Stop SOTW with scoreboard."));
        }
        else if (sender.hasPermission("sotw.enable")) {
            sender.sendMessage(CC.translate("&9&m---------------------------------------"));
            sender.sendMessage(CC.translate("&c&l/sotw enable &7- &cDisable your sotw timer."));
            sender.sendMessage(CC.translate("&9&m---------------------------------------"));
        }
    }
}
