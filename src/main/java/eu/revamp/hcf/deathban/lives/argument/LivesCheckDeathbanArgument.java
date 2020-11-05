package eu.revamp.hcf.deathban.lives.argument;

import com.google.common.base.Strings;
import eu.revamp.hcf.RevampHCF;
import eu.revamp.hcf.deathban.Deathban;
import eu.revamp.hcf.factions.utils.FactionMember;
import eu.revamp.hcf.utils.command.CommandArgument;
import eu.revamp.spigot.utils.date.DateTimeFormats;
import org.apache.commons.lang.time.DurationFormatUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * An {@link CommandArgument} used to check the {@link Deathban} of a {@link Player}.
 *//*
public class LivesCheckDeathbanArgument extends CommandArgument {

    private final RevampHCF plugin;

    public LivesCheckDeathbanArgument(RevampHCF plugin) {
        super("checkdeathban", "Check the deathban cause of player");
        this.plugin = plugin;
        this.permission = "hcf.command.lives.argument." + getName();
    }

    @Override
    public String getUsage(String label) {
        return '/' + label + ' ' + getName() + " <playerName>";
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 2) {
            sender.sendMessage(ChatColor.RED + "Usage: " + getUsage(label));
            return true;
        }

        OfflinePlayer target = Bukkit.getOfflinePlayer(args[1]); //TODO: breaking

        if (!target.hasPlayedBefore() && !target.isOnline()) {
            sender.sendMessage(ChatColor.GOLD + "Player '" + ChatColor.WHITE + args[1]
                    + ChatColor.GOLD + "' not found.");

            return true;
        }

        Deathban deathban = plugin.getUserManager().getUser(target.getUniqueId()).getDeathban();

        if (deathban == null || !deathban.isActive()) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', RevampHCF.getInstance().getConfig().getString("cmd-messages.lives-check-deathban").replace("%target%", target.getName())));
            return true;
        }
        sender.sendMessage(ChatColor.GREEN + "Deathban cause of " + target.getName() + '.');
        sender.sendMessage(ChatColor.YELLOW + " Time: " + ChatColor.AQUA + DateTimeFormats.HR_MIN.format(deathban.getCreationMillis()));
        sender.sendMessage(ChatColor.YELLOW + " Duration: " + ChatColor.AQUA
                + DurationFormatUtils.formatDurationWords(deathban.getInitialDuration(), true, true));

        Location location = deathban.getDeathPoint();
        if (location != null) {
            sender.sendMessage(ChatColor.YELLOW + " Location: " + ChatColor.AQUA +  "("
                    + location.getBlockX() + ", " + location.getBlockY() + ", " + location.getBlockZ()
                    + ") - " + location.getWorld().getName());
        }

        sender.sendMessage(ChatColor.YELLOW + " Reason: " + ChatColor.AQUA + " [" + Strings.nullToEmpty(deathban.getReason())
                + ChatColor.AQUA + "]");
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (args.length != 2) {
            return Collections.emptyList();
        }

        List<String> results = new ArrayList<>();
        for (FactionMember factionUser : plugin.getUserManager().getUsers().values()) {
            Deathban deathban = factionUser.getDeathban();
            if (deathban != null && deathban.isActive()) {
                OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(factionUser.getUniqueID());
                String name = offlinePlayer.getName();
                if (name != null) {
                    results.add(name);
                }
            }
        }

        return results;
    }
}
*/