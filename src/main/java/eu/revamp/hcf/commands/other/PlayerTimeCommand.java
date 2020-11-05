package eu.revamp.hcf.commands.other;

import eu.revamp.hcf.RevampHCF;
import eu.revamp.spigot.utils.generic.Tasks;
import eu.revamp.hcf.utils.DescParseTickFormat;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.command.CommandSender;

import java.text.SimpleDateFormat;
import eu.revamp.hcf.commands.BaseCommand;

public class PlayerTimeCommand extends BaseCommand {
    private SimpleDateFormat format;

    public PlayerTimeCommand(RevampHCF plugin) {
        super(plugin);
        this.format = new SimpleDateFormat("HH:mm");
        this.command = "ptime";
        this.permission = "revamphcf.command.ptime";
        this.forPlayerUseOnly = true;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Tasks.runAsync(this.getInstance(), () -> {
            Player player = (Player) sender;
            long offset = player.getPlayerTimeOffset();
            long time = player.getPlayerTime();
            if (args.length == 0) {
                if (offset != 0L) {
                    sender.sendMessage(ChatColor.YELLOW + "Your current time is " + ChatColor.GRAY + DescParseTickFormat.formatDateFormat(time, this.format));
                    sender.sendMessage(ChatColor.YELLOW + "To reset your offset type " + ChatColor.GRAY + "/ptime" + " reset");
                } else {
                    sender.sendMessage(ChatColor.YELLOW + "You currently have no time offset");
                    sender.sendMessage(ChatColor.YELLOW + "To set your time use the args " + ChatColor.GRAY + "/ptime" + " <time>");
                }
            } else if (args[0].equalsIgnoreCase("reset")) {
                if (offset == 0L) {
                    sender.sendMessage(ChatColor.YELLOW + "You currently have no time offset");
                } else {
                    sender.sendMessage(ChatColor.YELLOW + "Your time offset has been reset");
                    sender.sendMessage(ChatColor.YELLOW + "Your current time is " + ChatColor.GRAY + DescParseTickFormat.formatDateFormat(time, this.format));
                    player.resetPlayerTime();
                }
            } else {
                long ticks;
                try {
                    ticks = DescParseTickFormat.parse(args[0]);
                } catch (NumberFormatException e) {
                    sender.sendMessage(ChatColor.YELLOW + "Invalid time");
                    return;
                }
                player.resetPlayerTime();
                player.setPlayerTime(ticks, false);
                sender.sendMessage(ChatColor.YELLOW + "Time set to " + ChatColor.GRAY + DescParseTickFormat.formatDateFormat(ticks, this.format));
            }
        });
    }
}
