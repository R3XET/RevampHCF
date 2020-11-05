package eu.revamp.hcf.commands.revive;

import eu.revamp.hcf.Language;
import eu.revamp.hcf.RevampHCF;
import eu.revamp.spigot.utils.chat.color.CC;
import eu.revamp.spigot.utils.time.TimeUtils;
import org.bukkit.OfflinePlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.command.CommandSender;

import java.io.File;
import eu.revamp.hcf.commands.BaseCommand;

public class RevampCommand extends BaseCommand
{
    private File deathbanFolder;
    private File deathbansFolder;
    
    public RevampCommand(RevampHCF plugin) {
        super(plugin);
        this.deathbanFolder = new File(RevampHCF.getInstance().getDataFolder(), "deathban");
        this.deathbansFolder = new File(this.deathbanFolder, "deathbans");
        this.command = "revamp";
        this.permission = "revamphcf.revive.revamp";
        this.forPlayerUseOnly = true;
    }
    
    @Override @SuppressWarnings("deprecation")
    public void execute(CommandSender sender, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player)sender;
            if (!RevampHCF.getInstance().getConfiguration().isKitMap()) {
                if (args.length == 2) {
                    if (args[0].equals("revive")) {
                        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[1]);
                        if (RevampHCF.getInstance().getHandlerManager().getRankReviveHandler().isActive(player)) {
                            player.sendMessage(CC.translate("&7You can't use &b&lRevamp &7revive for &c&l" + TimeUtils.formatMilisecondsToMinutes(RevampHCF.getInstance().getHandlerManager().getRankReviveHandler().getMillisecondsLeft(player)) + "&7 more minutes!"));
                        }
                        else {
                            this.revivePlayer(player, offlinePlayer);
                        }
                    }
                    else {
                        this.sendUsage(player);
                    }
                }
                else {
                    this.sendUsage(player);
                }
            }
            else if (RevampHCF.getInstance().getConfiguration().isKitMap()) {
                player.sendMessage(Language.COMMANDS_NO_KITMAP.toString());
            }
        }
    }
    
    public void revivePlayer(Player player, OfflinePlayer target) {
        File targetFile = new File(this.deathbansFolder, target.getUniqueId().toString() + ".yml");
        if (targetFile.exists()) {
            targetFile.delete();
            Bukkit.broadcastMessage(CC.translate("&b&lRevive &7➥ &b" + player.getName() + " &ehas used his &b&lRevamp Rank &bto revive &7" + target.getName() + "&7"));
            RevampHCF.getInstance().getHandlerManager().getRankReviveHandler().apply(player);
        }
        else {
            player.sendMessage(CC.translate("&c&l" + target.getName() + " &cis not death-banned."));
        }
    }
    
    public void sendUsage(Player player) {
        player.sendMessage(CC.translate("&cCorrect Usage: /revamp revive <playerName>."));
    }
}
