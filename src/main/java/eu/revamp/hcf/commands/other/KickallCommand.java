package eu.revamp.hcf.commands.other;

import eu.revamp.hcf.Language;
import eu.revamp.spigot.utils.chat.message.MessageUtils;
import eu.revamp.spigot.utils.generic.Tasks;
import org.bukkit.entity.Player;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import eu.revamp.hcf.RevampHCF;
import eu.revamp.hcf.commands.BaseCommand;

public class KickallCommand extends BaseCommand
{
    public KickallCommand(RevampHCF plugin) {
        super(plugin);
        this.command = "kickall";
        this.permission = "revamphcf.op";
    }
    
    @Override
    public void execute(CommandSender sender, String[] args) {
        Tasks.runAsync(this.getInstance(), () -> {
            if (args.length == 0) {
                sender.sendMessage(Language.KICKALL_COMMAND_USAGE.toString());
            }
            else {
                StringBuilder message = new StringBuilder();
                for (String arg : args) {
                    message.append(arg).append(" ");
                }
                for (Player online : Bukkit.getOnlinePlayers()) {
                    online.kickPlayer(message.toString().replaceAll("&", "§"));
                }
                MessageUtils.sendMessage(Language.KICKALL_KICKED.toString().replace("%player%", sender.getName()).replace("%reason%", message.toString()));
            }
        });
    }
}
