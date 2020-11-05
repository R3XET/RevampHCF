package eu.revamp.hcf.commands.other;

import eu.revamp.hcf.Language;
import eu.revamp.hcf.RevampHCF;
import eu.revamp.spigot.utils.generic.ConversionUtils;
import eu.revamp.spigot.utils.generic.Tasks;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.command.CommandSender;
import eu.revamp.hcf.commands.BaseCommand;

public class ExpCommand extends BaseCommand
{
    public ExpCommand(RevampHCF plugin) {
        super(plugin);
        this.command = "exp";
        this.permission = "revamphcf.op";
        this.forPlayerUseOnly = true;
    }
    
    @Override
    public void execute(CommandSender sender, String[] args) {
        Tasks.runAsync(this.getInstance(), () -> {
            Player player = (Player)sender;
            if (args.length < 3) {
                player.sendMessage(Language.EXP_COMMAND_USAGE.toString());
            }
            else if (args[0].equalsIgnoreCase("set")) {
                Player target = Bukkit.getPlayer(args[1]);
                if (target == null) {
                    player.sendMessage(Language.COMMANDS_PLAYER_NOT_FOUND.toString());
                    return;
                }
                if (!ConversionUtils.isInteger(args[2])) {
                    player.sendMessage(Language.COMMANDS_MUST_BE_INTEGER.toString());
                }
                else {
                    int amount = Integer.parseInt(args[2]);
                    if (amount < 0) {
                        player.sendMessage(Language.COMMANDS_INVALID_NUMBER.toString());
                    }
                    else {
                        player.setLevel(amount);
                        if (target == player) {
                            player.sendMessage(Language.EXP_SET.toString().replace("%exp%", String.valueOf(player.getLevel())));
                        }
                        else {
                            player.sendMessage(Language.EXP_SET_OTHER.toString().replace("%exp%", String.valueOf(target.getLevel())).replace("%player%", target.getName()));
                            target.sendMessage(Language.EXP_SET_OTHER_TARGET.toString().replace("%exp%", String.valueOf(player.getLevel())).replace("%player%", player.getName()));
                        }
                    }
                }
            }
            else {
                player.sendMessage(Language.EXP_COMMAND_USAGE.toString());
            }
        });
    }
}
