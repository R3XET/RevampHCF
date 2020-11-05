package eu.revamp.hcf.deathban.commands;

import eu.revamp.hcf.Language;
import eu.revamp.hcf.RevampHCF;
import eu.revamp.spigot.utils.chat.color.CC;
import eu.revamp.spigot.utils.chat.message.MessageUtils;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.command.CommandSender;

import java.io.File;
import org.bukkit.event.Listener;
import eu.revamp.hcf.commands.BaseCommand;

public class StaffReviveCommand extends BaseCommand implements Listener
{
    public static File deathbanFolder = new File(RevampHCF.getInstance().getDataFolder(), "deathban");
    public static File deathbansFolder = new File(StaffReviveCommand.deathbanFolder, "deathbans");
    private File inventoriesFolder;

    public StaffReviveCommand(RevampHCF plugin) {
        super(plugin);
        this.inventoriesFolder = new File(StaffReviveCommand.deathbanFolder, "inventories");
        this.command = "staffrevive";
        this.permission = "revamphcf.revive";
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
                this.revivePlayer(player, target);
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
            this.revivePlayer(sender, target2);
        }
    }
    
    public void revivePlayer(CommandSender sender, OfflinePlayer target) {
        File targetFile = new File(StaffReviveCommand.deathbansFolder, target.getUniqueId().toString() + ".yml");
        if (targetFile.exists()) {
            targetFile.delete();
            MessageUtils.sendMessage(CC.translate("&a&l" + sender.getName() + "&asuccessfully revived &l" + target.getName() + "&a."), "hcf.admin");
            RevampHCF.getInstance().getFactionManager().getPlayerFaction(target.getUniqueId()).setDeathsUntilRaidable(RevampHCF.getInstance().getFactionManager().getPlayerFaction(target.getUniqueId()).getDeathsUntilRaidable() + RevampHCF.getInstance().getConfig().getDouble("FACTIONS-SETTINGS.dtrLossMultiplier"));
            MessageUtils.sendMessage(CC.translate("&a&l" + sender.getName() + " &aadded &l" + RevampHCF.getInstance().getConfig().getDouble("FACTIONS-SETTINGS.dtrLossMultiplier") + "&a DTR &l" + target.getName() + "&a."), "hcf.admin");
        }
        else {
            sender.sendMessage(CC.translate("&c&l" + target.getName() + " &cis not death-banned."));
        }
    }
    
    public void sendUsage(CommandSender sender) {
        sender.sendMessage(Language.STAFFREVIVE_COMMAND_USAGE.toString());
    }
}
