package eu.revamp.hcf.commands.chat;

import eu.revamp.hcf.RevampHCF;
import eu.revamp.spigot.utils.generic.Tasks;
import net.minecraft.server.v1_8_R3.EntityTracker;
import org.spigotmc.SpigotWorldConfig;
import net.minecraft.server.v1_8_R3.EntityTrackerEntry;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.World;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.command.CommandSender;
import eu.revamp.hcf.commands.BaseCommand;

public class PlayerdistanceCommand extends BaseCommand
{
    public PlayerdistanceCommand(RevampHCF plugin) {
        super(plugin);
        this.command = "playerdistance";
        this.permission = "revamphcf.op";
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Tasks.runAsync(this.getInstance(), () -> {
            Player player = (Player)sender;
            if (args.length == 0) {
                player.sendMessage("§cPlayer Distance§7:");
                for (World world : Bukkit.getWorlds()) {
                    SpigotWorldConfig swc = ((CraftWorld)world).getHandle().spigotConfig;
                    player.sendMessage(" §e" + world.getName() + " §6» §c" + swc.playerTrackingRange);
                }
            }
            else {
                int distance = Integer.parseInt(args[0]);
                if (distance < 0 || distance > 64) {
                    player.sendMessage("§cNumber must be between 0-64");
                    return;
                }
                for (World world2 : Bukkit.getWorlds()) {
                    SpigotWorldConfig swc2 = ((CraftWorld)world2).getHandle().spigotConfig;
                    swc2.playerTrackingRange = distance;
                }
                for (Player online : Bukkit.getOnlinePlayers()) {
                    EntityTracker tracker = ((CraftWorld)online.getWorld()).getHandle().getTracker();
                    EntityTrackerEntry trackerEntry = tracker.trackedEntities.get(online.getEntityId());
                    trackerEntry.b = distance;
                }
                player.sendMessage("§ePlayer distance set to §6» §c" + args[0]);
            }
        });
    }
}
