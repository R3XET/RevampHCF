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

public class RecordingCommand extends BaseCommand
{
    
    public RecordingCommand(RevampHCF plugin) {
        super(plugin);
        this.command = "recording";
        this.permission = "revamphcf.command.recording";
        this.forPlayerUseOnly = true;
    }
    
    public void execute (CommandSender sender, String[] args) {
        Player player = (Player)sender;
        if (args.length != 1) {
            player.sendMessage(CC.translate("&cCorrect Usage: /recording (youtubeChannel)."));
        }
        else {
            if (CooldownManager.isOnCooldown("RECORDING_DELAY", player)) {
                player.sendMessage(Language.RECORDING_COOLDOWN.toString().replace("%time%", TimeUtils.getRemaining(CooldownManager.getCooldownMillis("RECORDING_DELAY", player), true) + "."));
                return;
            }
            for (String recording : this.getInstance().getLanguage().getStringList("RECORDING.MESSAGE")) {
                recording = recording.replace("%player%", player.getName());
                recording = recording.replace("&", "§");
                recording = recording.replace("%link%", StringUtils.join(args, ' ', 0, args.length).replace("https://", ""));
                Bukkit.broadcastMessage(recording);
            }
            CooldownManager.createCooldown("RECORDING_DELAY");
            CooldownManager.addCooldown("RECORDING_DELAY", player, this.getInstance().getConfig().getInt("COOLDOWNS.RECORDING"));
        }
    }
}