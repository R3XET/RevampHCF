package eu.revamp.hcf.deathban.lives.argument;

import eu.revamp.hcf.RevampHCF;
import eu.revamp.hcf.deathban.Deathban;
import eu.revamp.hcf.utils.chat.JavaUtils;
import eu.revamp.hcf.utils.command.CommandArgument;
import net.minecraft.util.org.apache.commons.lang3.time.DurationFormatUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.Collections;
import java.util.List;

/**
 * An {@link CommandArgument} used to set the base {@link Deathban} time, not including multipliers, etc.
 */
public class LivesSetDeathbanTimeArgument extends CommandArgument {

    public LivesSetDeathbanTimeArgument(RevampHCF plugin) {
        super("setdeathbantime", "Sets the base deathban time");
        this.permission = "hcf.command.lives.argument." + getName();
    }

    @Override
    public String getUsage(String label) {
        return '/' + label + ' ' + getName() + " <time>";
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 2) {
            sender.sendMessage(ChatColor.RED + "Usage: " + getUsage(label));
            return true;
        }

        Integer duration = JavaUtils.tryParseInt(args[1]);

        if (duration == null || duration == -1L) {
            sender.sendMessage(ChatColor.RED + "Invalid duration, use the correct format: 10m 1s");
            return true;
        }

        //plugin.getConfiguration().setDeathbanBaseDurationMinutes(duration);
        Command.broadcastCommandMessage(sender, ChatColor.YELLOW + "Base death-ban time set to " +
                DurationFormatUtils.formatDurationWords(duration, true, true) + " (not including multipliers, etc).");

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        return Collections.emptyList();
    }
}
