package eu.revamp.hcf.commands.teleport;

import eu.revamp.hcf.Language;
import eu.revamp.hcf.RevampHCF;
import eu.revamp.hcf.commands.BaseCommand;
import eu.revamp.spigot.utils.chat.message.MessageUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TeleportallCommand extends BaseCommand
{
    public TeleportallCommand(RevampHCF plugin) {
        super(plugin);
        this.command = "teleportall";
        this.permission = "revamphcf.op";
        this.forPlayerUseOnly = true;
    }
    
    @Override
    public void execute(CommandSender sender, String[] args) {
        final Player player = (Player)sender;
        if (args.length == 0) {
            for (Player online : Bukkit.getOnlinePlayers()) {
                online.teleport(player.getLocation());
            }
            MessageUtils.sendMessage(Language.TELEPORTALL_TELEPORTED.toString().replace("%player%", player.getName()));
        }
    }
}
