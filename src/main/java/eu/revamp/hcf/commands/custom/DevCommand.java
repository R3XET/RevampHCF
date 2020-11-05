package eu.revamp.hcf.commands.custom;

import eu.revamp.hcf.Language;
import eu.revamp.hcf.RevampHCF;
import eu.revamp.spigot.utils.chat.color.CC;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import eu.revamp.hcf.commands.BaseCommand;

public class DevCommand extends BaseCommand
{
    public DevCommand(RevampHCF plugin) {
        super(plugin);
        this.command = "dev";
    }
    
    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length == 0) {
            sendInfo(sender);
        }
        else if (args[0].equalsIgnoreCase("reload")) {
            if (sender.hasPermission("revamphcf.op")) {


                RevampHCF plugin = RevampHCF.getInstance();
                plugin.reloadFiles();

                RevampHCF.getInstance().saveData();
                RevampHCF.getInstance().getFactions().save();

                RevampHCF.getInstance().getConfig().load();
                RevampHCF.getInstance().getUtilities().load();
                RevampHCF.getInstance().getTablist().load();
                RevampHCF.getInstance().getLanguage().load();
                RevampHCF.getInstance().getLimiters().load();
                //RevampHCF.getInstance().getKitsFile().load();
                RevampHCF.getInstance().getTablist().load();
                RevampHCF.getInstance().getSchedules().load();
                RevampHCF.getInstance().getFactions().load();

                RevampHCF.getInstance().getHandlerManager().getConfigHandler().enable();
                //RevampHCF.getInstance().setupLanguage();
                RevampHCF.getInstance().getHandlerManager().getMapKitHandler().loadInventory();
                if (!this.getInstance().getConfiguration().isKitMap()) {
                    RevampHCF.getInstance().getHandlerManager().getMobStackHandler().loadEntityList();
                    RevampHCF.getInstance().getHandlerManager().getEnchantmentLimiterHandler().loadEnchantmentLimits();
                    RevampHCF.getInstance().getHandlerManager().getPotionLimitHandler().loadPotionLimits();
                }
                sender.sendMessage(CC.translate("&aFiles have been reloaded"/*&7: &7(&3ranks.yml&7, &3tags.yml&7, &3settings.yml&7, &3messages.yml&7, &3config.yml&7, &3lang.yml&7)"*/));

            }
            else {
                sender.sendMessage(Language.COMMANDS_NO_PERMISSION_MESSAGE.toString());
            }
        }
        else if ((args[0].equalsIgnoreCase("reboot") || args[0].equalsIgnoreCase("kitmap")) && sender.hasPermission("revamphcf.op")) {
            RevampHCF.getInstance().getHandlerManager().getRebootHandler().start(30 * 1000);
            //RevampHCF.getInstance().getConfiguration().setKitMap(true);
            Bukkit.broadcastMessage(RevampHCF.getInstance().getConfig().getString("broadcast") + "&eForce reboot will start in &c30 seconds&e.");
        }
    }
    
    public static void sendInfo(CommandSender sender) {
        sender.sendMessage(CC.translate("&7&m-------------------------"));
        sender.sendMessage(CC.translate("&b&lRevampMC &8- &aHCFCore"));
        sender.sendMessage("");
        sender.sendMessage(CC.translate(" &b» &aVersion&7: " + RevampHCF.getInstance().getDescription().getVersion()));
        sender.sendMessage(CC.translate(" &b» &aAuthor&7: @R3XET"));
        sender.sendMessage(CC.translate(" &b» &aLast Update&7: 08/01/20"));
        sender.sendMessage(CC.translate("&7&m-------------------------"));
    }
}
