package eu.revamp.hcf.commands.teleport;

import eu.revamp.hcf.Language;
import eu.revamp.hcf.RevampHCF;
import eu.revamp.hcf.commands.BaseCommand;
import eu.revamp.spigot.utils.generic.Tasks;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

public class DelwarpCommand extends BaseCommand
{
    public DelwarpCommand(RevampHCF plugin) {
        super(plugin);
        this.command = "delwarp";
        this.permission = "revamphcf.op";
        this.forPlayerUseOnly = true;
    }
    
    @Override
    public void execute(CommandSender sender, String[] args) {
        Tasks.runAsync(this.getInstance(), () -> {
            Player player = (Player)sender;
            if (args.length != 1) {
                player.sendMessage(Language.DELWARP_COMMAND_USAGE.toString());
            }
            else if (RevampHCF.getInstance().getUtilities().getConfigurationSection("warps") != null) {
                if (RevampHCF.getInstance().getUtilities().getConfigurationSection("warps").contains(args[0].toLowerCase())) {
                    ConfigurationSection section = RevampHCF.getInstance().getUtilities().getConfigurationSection("warps");
                    for (String name : section.getKeys(false)) {
                        RevampHCF.getInstance().getHandlerManager().getWarpHandler().getWarps().remove(name);
                    }
                    RevampHCF.getInstance().getUtilities().set("warps." + args[0].toLowerCase(), null);
                    RevampHCF.getInstance().getSchedules().save();
                    player.sendMessage(Language.DELWARP_DELETED.toString().replace("%warp%", args[0]));
                }
                else {
                    player.sendMessage(Language.DELWARP_NOT_FOUND.toString());
                }
            }
        });
    }
}
