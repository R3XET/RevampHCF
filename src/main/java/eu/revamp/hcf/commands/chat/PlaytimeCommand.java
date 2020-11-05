package eu.revamp.hcf.commands.chat;

import org.bukkit.Bukkit;
import eu.revamp.spigot.utils.chat.color.CC;
import org.apache.commons.lang.time.DurationFormatUtils;
import org.bukkit.Statistic;
import org.bukkit.entity.Player;
import org.bukkit.command.CommandSender;
import eu.revamp.hcf.RevampHCF;
import eu.revamp.hcf.commands.BaseCommand;

public class PlaytimeCommand extends BaseCommand
{
    public PlaytimeCommand(RevampHCF plugin) {
        super(plugin);
        this.command = "playtime";
        this.permission = "revamphcf.command.playtime";
        this.forPlayerUseOnly = true;
    }
    
    @Override
    public void execute(CommandSender sender, String[] args) {
        Player player = (Player)sender;
        if (args.length == 0) {
            long l = player.getStatistic(Statistic.PLAY_ONE_TICK);
            player.sendMessage(CC.translate("&eYour playtime is &a" + DurationFormatUtils.formatDurationWords(l * 50L, true, true) + " &ethis map."));
            return;
        }
        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            sender.sendMessage("Target offline.");
            return;
        }
        long i = target.getStatistic(Statistic.PLAY_ONE_TICK);
        player.sendMessage(CC.translate("&b" + target.getName() + "'s &eplaytime is &a" + DurationFormatUtils.formatDurationWords(i * 50L, true, true) + " &e &ethis map."));
    }
    
    public void sendUsage(CommandSender sender) {
        sender.sendMessage(CC.translate("&cCorrect Usage: /playtime (playerName)."));
    }
}
