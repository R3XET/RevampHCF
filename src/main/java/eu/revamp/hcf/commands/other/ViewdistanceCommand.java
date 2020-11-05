package eu.revamp.hcf.commands.other;

import eu.revamp.hcf.RevampHCF;
import eu.revamp.hcf.commands.BaseCommand;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.spigotmc.SpigotWorldConfig;

public class ViewdistanceCommand extends BaseCommand
{
    public ViewdistanceCommand(RevampHCF plugin) {
        super(plugin);
        this.command = "viewdistance";
        this.permission = "revamphcf.op";
    }
    
    @Override
    public void execute(CommandSender sender, String[] args) {
        Player player = (Player)sender;
        if (args.length == 0) {
            player.sendMessage("§cView Distance§7:");
            for (World world : Bukkit.getWorlds()) {
                SpigotWorldConfig swc = ((CraftWorld)world).getHandle().spigotConfig;
                player.sendMessage(" §e" + world.getName() + " §6§ §c" + swc.viewDistance);
            }
        }
        else {
            int distance = Integer.parseInt(args[0]);
            if (distance < 0 || distance > 16) {
                player.sendMessage("§cNumber must be between 0-16.");
                return;
            }
            for (World world2 : Bukkit.getWorlds()) {
                SpigotWorldConfig swc2 = ((CraftWorld)world2).getHandle().spigotConfig;
                swc2.viewDistance = distance;
            }
            new BukkitRunnable() {
                public void run() {
                    for (Player online : Bukkit.getOnlinePlayers()) {
                        online.spigot().setViewDistance(distance);
                    }
                }
            }.runTaskAsynchronously(RevampHCF.getInstance());
            player.sendMessage("§eView distance set to §6§ §c" + args[0]);
        }
    }
}
