package eu.revamp.hcf.commands.teleport;

import eu.revamp.hcf.Language;
import eu.revamp.hcf.RevampHCF;
import eu.revamp.hcf.commands.BaseCommand;
import eu.revamp.spigot.utils.generic.ConversionUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetwarpCommand extends BaseCommand
{
    public SetwarpCommand(RevampHCF plugin) {
        super(plugin);
        this.command = "setwarp";
        this.permission = "revamphcf.op";
        this.forPlayerUseOnly = true;
    }
    
    @Override
    public void execute(CommandSender sender, String[] args) {
        final Player player = (Player)sender;
        if (args.length != 1) {
            sender.sendMessage(Language.SETWARP_COMMAND_USAGE.toString());
        }
        else {
            if (!ConversionUtils.isInteger(args[0])) {
                sender.sendMessage(Language.COMMANDS_MUST_BE_INTEGER.toString());
                return;
            }
            if (RevampHCF.getInstance().getUtilities().getConfigurationSection("warps") != null && RevampHCF.getInstance().getUtilities().getConfigurationSection("warps").contains(args[0].toLowerCase())) {
                player.sendMessage(Language.SETWARP_WARP_EXISTS.toString().replace("%warp%", args[0]));
                return;
            }
            RevampHCF.getInstance().getUtilities().set("warps." + args[0].toLowerCase() + ".world", player.getWorld().getName());
            RevampHCF.getInstance().getUtilities().set("warps." + args[0].toLowerCase() + ".x", player.getLocation().getX());
            RevampHCF.getInstance().getUtilities().set("warps." + args[0].toLowerCase() + ".y", player.getLocation().getY());
            RevampHCF.getInstance().getUtilities().set("warps." + args[0].toLowerCase() + ".z", player.getLocation().getZ());
            RevampHCF.getInstance().getUtilities().set("warps." + args[0].toLowerCase() + ".yaw", player.getLocation().getYaw());
            RevampHCF.getInstance().getUtilities().set("warps." + args[0].toLowerCase() + ".pitch", player.getLocation().getPitch());
            RevampHCF.getInstance().getUtilities().save();
            player.sendMessage(Language.SETWARP_SET.toString().replace("%warp%", args[0]));
        }
    }
}
