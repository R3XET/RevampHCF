package eu.revamp.hcf.commands.staff;

import eu.revamp.hcf.Language;
import eu.revamp.hcf.RevampHCF;
import eu.revamp.hcf.playerdata.HCFPlayerData;
import eu.revamp.spigot.utils.chat.color.CC;
import org.bukkit.entity.Player;
import org.apache.commons.lang.time.DurationFormatUtils;
import org.bukkit.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import eu.revamp.hcf.commands.BaseCommand;

public class SeenCommand extends BaseCommand
{
    public SeenCommand(RevampHCF plugin) {
        super(plugin);
        this.command = "seen";
        this.permission = "revamphcf.staff";
    }
    
    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length < 1) {
            this.sendUsage(sender);
            return;
        }
        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            sender.sendMessage(Language.COMMANDS_PLAYER_NOT_FOUND.toString());
            return;
        }
        HCFPlayerData data = RevampHCF.getInstance().getHandlerManager().getHCFPlayerDataHandler().getPlayer(target);
        long lastSeen = data.getLastSeen();
        if (lastSeen <= 0L) {
            sender.sendMessage(CC.translate("&cThat player has not joined before"));
        }
        else {
            long now = System.currentTimeMillis();
            boolean online = target.isOnline();
            long time = now - lastSeen;
            sender.sendMessage(CC.translate("&b" + target.getName() + " &ehas been " + (online ? (ChatColor.GREEN + "online") : (ChatColor.RED + "offline")) + " &efor &a" + DurationFormatUtils.formatDurationWords(time, true, true)));
        }
    }
    
    public void sendUsage(CommandSender sender) {
        sender.sendMessage(CC.translate("&cCorrect Usage: /seen <player>"));
    }
}
