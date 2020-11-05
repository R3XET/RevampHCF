package eu.revamp.hcf.kit.argument;

import eu.revamp.hcf.RevampHCF;
import eu.revamp.hcf.kit.Kit;
import eu.revamp.hcf.utils.chat.JavaUtils;
import eu.revamp.hcf.utils.command.CommandArgument;
import eu.revamp.spigot.utils.date.DateUtils;
import org.apache.commons.lang3.time.DurationFormatUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class KitSetDelayArgument extends CommandArgument {
    private RevampHCF plugin;

    public KitSetDelayArgument(RevampHCF plugin) {
        super("setdelay", "Sets the delay time of a kit");
        this.plugin = plugin;
        this.aliases = new String[]{"delay", "setcooldown", "cooldown"};
        this.permission = "manager.kit.argument." + this.getName();
    }

    @Override
    public String getUsage(String label) {
        return String.valueOf('/') + label + ' ' + this.getName() + " <kitName> <delay>";
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 3) {
            sender.sendMessage(ChatColor.RED + "Usage: " + this.getUsage(label));
            return true;
        }
        Kit kit = this.plugin.getKitManager().getKit(args[1]);
        if (kit == null) {
            sender.sendMessage(ChatColor.RED + "There is not a kit named " + args[1] + '.');
            return true;
        }
        long duration = DateUtils.parseTime(args[2]);
        if (duration == -1L) {
            sender.sendMessage(ChatColor.RED + "Invalid duration, use the correct format: 10m 1s");
            return true;
        }
        kit.setDelayMillis(duration);
        sender.sendMessage(ChatColor.YELLOW + "Set delay of kit " + kit.getName() + " to " + DurationFormatUtils.formatDurationWords(duration, true, true) + '.');
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (args.length != 2) {
            return Collections.emptyList();
        }
        List<Kit> kits = this.plugin.getKitManager().getKits();
        ArrayList<String> results = new ArrayList<>(kits.size());
        for (Kit kit : kits) {
            results.add(kit.getName());
        }
        return results;
    }
}
