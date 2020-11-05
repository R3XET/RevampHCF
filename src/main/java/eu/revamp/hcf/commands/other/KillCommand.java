package eu.revamp.hcf.commands.other;

import eu.revamp.hcf.Language;
import eu.revamp.spigot.utils.generic.Tasks;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.command.CommandSender;
import eu.revamp.hcf.RevampHCF;
import eu.revamp.hcf.commands.BaseCommand;

public class KillCommand extends BaseCommand
{
    public KillCommand(RevampHCF plugin) {
        super(plugin);
        this.command = "kill";
        this.permission = "revamphcf.command.kill";
        this.forPlayerUseOnly = true;
    }
    
    @Override
    public void execute(CommandSender sender, String[] args) {
        Tasks.runAsync(this.getInstance(), () -> {
            Player player = (Player)sender;
            if (args.length == 0) {
                player.sendMessage(Language.KILL_COMMAND_USAGE.toString());
            }
            else {
                Player target = Bukkit.getPlayer(args[0]);
                if (target == null) {
                    sender.sendMessage(Language.COMMANDS_PLAYER_NOT_FOUND.toString());
                    return;
                }
                target.setHealth(0.0);
                target.sendMessage(Language.KILL_KILLED_OTHER_TARGET.toString().replace("%player%", player.getName()));
                player.sendMessage(Language.KILL_KILLED_OTHER.toString().replace("%player%", target.getName()));
            }
        });
    }
}
