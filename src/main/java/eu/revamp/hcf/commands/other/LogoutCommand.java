package eu.revamp.hcf.commands.other;

import eu.revamp.hcf.RevampHCF;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.Sound;
import org.bukkit.Bukkit;
import eu.revamp.spigot.utils.chat.color.CC;
import eu.revamp.hcf.factions.type.SpawnFaction;
import org.bukkit.entity.Player;
import org.bukkit.command.CommandSender;
import eu.revamp.hcf.commands.BaseCommand;

public class LogoutCommand extends BaseCommand {
    public LogoutCommand(RevampHCF plugin) {
        super(plugin);
        this.command = "logout";
        this.permission = "revamphcf.command.logout";
        this.forPlayerUseOnly = true;
    }
    
    @Override
    public void execute(CommandSender sender, String[] args) {
        Player player = (Player)sender;
        if (RevampHCF.getInstance().getFactionManager().getFactionAt(player.getLocation()) instanceof SpawnFaction) {
            sender.sendMessage(CC.translate("&cYou cannot use this command at &aSpawn &cclaim."));
            return;
        }
        if (RevampHCF.getInstance().getHandlerManager().getLogoutHandler().getTeleporting().containsKey(player)) {
            sender.sendMessage(CC.translate("&cYou are already logging out."));
            return;
        }
        RevampHCF.getInstance().getHandlerManager().getLogoutHandler().createLogout(player);
        RevampHCF.getInstance().getHandlerManager().getLogoutHandler().getTeleporting().put(player, Bukkit.getScheduler().scheduleSyncRepeatingTask(RevampHCF.getInstance(), new Runnable() {
            int i = 30;
            
            @Override
            public void run() {
                if (this.i != 0) {
                    player.playSound(player.getLocation(), Sound.NOTE_BASS_DRUM, 1.0f, 1.0f);
                    player.sendMessage(CC.translate("&e&lLogging out... &ePlease wait &c" + this.i + " &eseconds."));
                    --this.i;
                    return;
                }
                player.setMetadata("LogoutCommand", new FixedMetadataValue(RevampHCF.getInstance(), true));
                player.kickPlayer(CC.translate("&cYou have been safely logged out of the server!"));
                Bukkit.getScheduler().cancelTask(RevampHCF.getInstance().getHandlerManager().getLogoutHandler().getTeleporting().get(player));
                RevampHCF.getInstance().getHandlerManager().getLogoutHandler().getTeleporting().remove(player);
            }
        }, 0L, 20L));
    }
}
