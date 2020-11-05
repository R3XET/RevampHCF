package eu.revamp.hcf.commands.chat;

import eu.revamp.hcf.Language;
import eu.revamp.hcf.RevampHCF;
import eu.revamp.hcf.commands.BaseCommand;
import eu.revamp.hcf.managers.CooldownManager;
import eu.revamp.hcf.utils.Utils;
import eu.revamp.spigot.utils.chat.color.CC;
import eu.revamp.spigot.utils.time.TimeUtils;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class NewVideoCommand extends BaseCommand
{
    public NewVideoCommand(RevampHCF plugin) {
        super(plugin);
        this.command = "newvideo";
        this.permission = "revamphcf.command.newvideo";
        this.forPlayerUseOnly = true;
    }

    public void execute (CommandSender sender, String[] args) {
        Player player = (Player) sender;
        if (args.length != 1) {
            player.sendMessage(CC.translate("&cCorrect Usage: /newvideo (videolink)."));
        } else {
            if (CooldownManager.isOnCooldown("NEWVIDEO_DELAY", player)) {
                player.sendMessage(Language.NEWVIDEO_COOLDOWN.toString().replace("%time%", TimeUtils.getRemaining(CooldownManager.getCooldownMillis("NEWVIDEO_DELAY", player), true) + "."));
                return;
            }
            Bukkit.broadcastMessage(Language.NEWVIDEO_MESSAGE.toString().replace("%player%", player.getName().replace("&", "§").replace("%link%", StringUtils.join(args, ' ', 0, args.length).replace("https://www.youtube.com/watch?v=", "youtu.be/")).replace("https://", "")));
            CooldownManager.createCooldown("NEWVIDEO_DELAY");
            CooldownManager.addCooldown("NEWVIDEO_DELAY", player, this.getInstance().getConfig().getInt("COOLDOWNS.NEWVIDEO"));
        }
    }
}
