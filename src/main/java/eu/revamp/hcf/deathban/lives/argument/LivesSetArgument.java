package eu.revamp.hcf.deathban.lives.argument;

import eu.revamp.hcf.RevampHCF;
import eu.revamp.hcf.utils.chat.JavaUtils;
import eu.revamp.hcf.utils.command.CommandArgument;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

/**
 * An {@link CommandArgument} used to set the lives of {@link Player}s.
 *//*
public class LivesSetArgument extends CommandArgument {

    private final RevampHCF plugin;

    public LivesSetArgument(RevampHCF plugin) {
        super("set", "Set how much lives a player has");
        this.plugin = plugin;
        this.permission = "hcf.command.lives.argument." + getName();
    }

    @Override
    public String getUsage(String label) {
        return '/' + label + ' ' + getName() + " <playerName> <amount>";
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 3) {
            sender.sendMessage(ChatColor.RED + "Usage: " + getUsage(label));
            return true;
        }

        Integer amount = JavaUtils.tryParseInt(args[2]);

        if (amount == null) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', RevampHCF.getInstance().getConfig().getString("cmd-messages.lives-set-error").replace("%argument%", args[2])));
            return true;
        }

        OfflinePlayer target = BukkitUtils.offlinePlayerWithNameOrUUID(args[1]);

        if (!target.hasPlayedBefore() && !target.isOnline()) {
            sender.sendMessage(String.format(IConstantsAPI.PLAYER_WITH_NAME_OR_UUID_NOT_FOUND, args[1]));
            return true;
        }

        plugin.getDeathbanManager().setLives(target.getUniqueId(), amount);

        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', RevampHCF.getInstance().getConfig().getString("cmd-messages.lives-set-message").replace("%target%", target.getName()).replace("%amount%", amount.toString())));
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        return args.length == 2 ? null : Collections.emptyList();
    }
}
*/