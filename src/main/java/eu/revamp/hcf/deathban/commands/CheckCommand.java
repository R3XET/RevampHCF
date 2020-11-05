package eu.revamp.hcf.deathban.commands;

import eu.revamp.hcf.Language;
import eu.revamp.hcf.RevampHCF;
import eu.revamp.spigot.utils.chat.color.CC;
import org.apache.commons.lang.time.DurationFormatUtils;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.command.CommandSender;

import java.io.File;
import org.bukkit.event.Listener;
import eu.revamp.hcf.commands.BaseCommand;

public class CheckCommand extends BaseCommand implements Listener
{
    public static File deathbanFolder = new File(RevampHCF.getInstance().getDataFolder(), "deathban");
    public static File deathbansFolder = new File(CheckCommand.deathbanFolder, "deathbans");

    public CheckCommand(RevampHCF plugin) {
        super(plugin);
        this.command = "checkdeathban";
        this.permission = "revamphcf.staff";
    }
    
    @Override @SuppressWarnings("deprecation")
    public void execute(CommandSender sender, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player)sender;
            if (!RevampHCF.getInstance().getConfiguration().isKitMap()) {
                if (args.length == 0) {
                    this.sendUsage(player);
                    return;
                }
                OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);
                this.checkPlayer(player, target);
            }
            else if (RevampHCF.getInstance().getConfiguration().isKitMap()) {
                player.sendMessage(Language.COMMANDS_NO_KITMAP.toString());
            }
        }
        else if (sender instanceof ConsoleCommandSender) {
            if (args.length == 0) {
                this.sendUsage(sender);
                return;
            }
            OfflinePlayer target2 = Bukkit.getOfflinePlayer(args[0]);
            this.checkPlayer(sender, target2);
        }
    }
    
    public void checkPlayer(CommandSender sender, OfflinePlayer target) {
        File targetFile = new File(CheckCommand.deathbansFolder, target.getUniqueId().toString() + ".yml");
        YamlConfiguration configuration = YamlConfiguration.loadConfiguration(targetFile);
        long banTime = configuration.getLong("ban_until");
        if (targetFile.exists()) {
            String duration;
            if (banTime - System.currentTimeMillis() <= 0L) {
                duration = "Unbanned";
            }
            else {
                duration = DurationFormatUtils.formatDurationWords(banTime - System.currentTimeMillis(), true, true);
            }
            sender.sendMessage(CC.translate("&7&m-----------------------------"));
            sender.sendMessage(CC.translate(" &c&lDeathBan Check"));
            sender.sendMessage(CC.translate("  &ePlayer: &f" + target.getName()));
            sender.sendMessage(CC.translate("  &eDuration: &f" + duration));
            sender.sendMessage(CC.translate("  &eReason: &f" + configuration.getString("death_message")));
            sender.sendMessage(CC.translate("  &eLocation: &f" + configuration.getString("coords")));
            sender.sendMessage(CC.translate("&7&m-----------------------------"));
        }
        else {
            sender.sendMessage(CC.translate("&c&l" + target.getName() + " &cis not death-banned."));
        }
    }
    
    public void sendUsage(CommandSender sender) {
        sender.sendMessage(CC.translate("&cUsage: /checkdeathban (playerName)"));
    }
}
