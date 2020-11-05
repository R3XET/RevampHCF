package eu.revamp.hcf.commands.teleport;

import eu.revamp.hcf.Language;
import eu.revamp.hcf.RevampHCF;
import eu.revamp.spigot.utils.generic.Tasks;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.Location;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.command.CommandSender;
import eu.revamp.hcf.commands.BaseCommand;

public class WarpCommand extends BaseCommand
{
    public WarpCommand(RevampHCF plugin) {
        super(plugin);
        this.command = "warp";
        this.permission = "revamphcf.staff";
        this.forPlayerUseOnly = true;
    }
    
    @Override
    public void execute(CommandSender sender, String[] args) {
        Tasks.runAsync(this.getInstance(), () -> {
            Player player = (Player)sender;
            ConfigurationSection section = RevampHCF.getInstance().getUtilities().getConfigurationSection("warps");
            if (section == null) {
                player.sendMessage(Language.WARP_NO_WARPS.toString());
                return;
            }
            for (String name : section.getKeys(false)) {
                String replace = name.replace(name.charAt(0), name.toUpperCase().charAt(0));
                if (!RevampHCF.getInstance().getHandlerManager().getWarpHandler().getWarps().contains(replace)) {
                    RevampHCF.getInstance().getHandlerManager().getWarpHandler().getWarps().add(replace);
                }
            }
            if (args.length != 1) {
                if (!RevampHCF.getInstance().getHandlerManager().getWarpHandler().getWarps().isEmpty()) {
                    player.sendMessage(Language.WARP_LIST + Language.WARP_COLOR.toString() + RevampHCF.getInstance().getHandlerManager().getWarpHandler().getWarps().toString().replace("[", "").replace("]", "").replace(",", Language.WARP_COLOR_COMMA.getPath() + Language.WARP_COLOR.toString()));
                }
                else {
                    player.sendMessage(Language.WARP_NO_WARPS.toString());
                }
            }
            else if (RevampHCF.getInstance().getUtilities().getConfigurationSection("warps") != null) {
                if (RevampHCF.getInstance().getUtilities().getConfigurationSection("warps").contains(args[0].toLowerCase())) {
                    World world = Bukkit.getWorld(RevampHCF.getInstance().getUtilities().getString("warps." + args[0].toLowerCase() + ".world"));
                    int x = RevampHCF.getInstance().getUtilities().getInt("warps." + args[0].toLowerCase() + ".x");
                    int y = RevampHCF.getInstance().getUtilities().getInt("warps." + args[0].toLowerCase() + ".y");
                    int z = RevampHCF.getInstance().getUtilities().getInt("warps." + args[0].toLowerCase() + ".z");
                    float yaw = RevampHCF.getInstance().getUtilities().getFloat("warps." + args[0].toLowerCase() + ".yaw");
                    float pitch = RevampHCF.getInstance().getUtilities().getFloat("warps." + args[0].toLowerCase() + ".pitch");
                    Location location = new Location(world, x, y, z, yaw, pitch);
                    player.teleport(location);
                    player.sendMessage(Language.WARP_TELEPORTED.toString().replace("%warp%", args[0]));
                }
                else {
                    player.sendMessage(Language.WARP_DOES_NOT_EXIST.toString().replace("%warp%", args[0]));
                }
            }
        });
    }
}
