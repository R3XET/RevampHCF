package eu.revamp.hcf.commands.staff;

import eu.revamp.hcf.Language;
import eu.revamp.hcf.RevampHCF;
import eu.revamp.spigot.utils.chat.color.CC;
import eu.revamp.spigot.utils.world.WorldUtils;
import org.bukkit.Sound;
import org.bukkit.Location;
import org.bukkit.Bukkit;
import eu.revamp.hcf.utils.Utils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import java.util.concurrent.ConcurrentHashMap;
import eu.revamp.hcf.commands.BaseCommand;

public class SpawnCommand extends BaseCommand
{
    public static ConcurrentHashMap<Player, Integer> teleporting  = new ConcurrentHashMap<>();

    public SpawnCommand(RevampHCF plugin) {
        super(plugin);
        this.command = "spawn";
        this.permission = "revamphcf.command.spawn";
        this.forPlayerUseOnly = true;
    }
    
    @Override
    public void execute(CommandSender sender, String[] args) {
        Player player = (Player)sender;
        Location spawn = WorldUtils.destringifyLocation(RevampHCF.getInstance().getLocation().getString("World-Spawn.world-spawn"));
        if (this.getInstance().getConfiguration().isKitMap()) {
            if (player.hasPermission("revamphcf.staff")) {
                player.teleport(spawn);
            }
            else {
                if (this.getInstance().getHandlerManager().getTimerManager().getSpawnTagHandler().getRemaining(player) > 0L) {
                    sender.sendMessage(CC.translate("&cYou cannot spawn while you are Spawn Tagged!"));
                    return;
                }
                if (SpawnCommand.teleporting.containsKey(player)) {
                    sender.sendMessage(CC.translate("&cYou are already spawning."));
                    return;
                }
                SpawnCommand.teleporting.put(player, Bukkit.getScheduler().scheduleSyncRepeatingTask(RevampHCF.getInstance(), new Runnable() {
                    int i = 20;

                    @Override
                    public void run() {
                        if (this.i != 0) {
                            player.playSound(player.getLocation(), Sound.NOTE_BASS_DRUM, 1.0f, 1.0f);
                            player.sendMessage(CC.translate("&e&lSpawning... &ePlease wait &c" + this.i + " &eseconds."));
                            --this.i;
                            return;
                        }
                        player.teleport(spawn);
                        sender.sendMessage(CC.translate("&aSuccessfully teleported to spawn."));
                        Bukkit.getScheduler().cancelTask(SpawnCommand.teleporting.get(player));
                        SpawnCommand.teleporting.remove(player);
                    }
                }, 0L, 20L));
            }
        }
        else if (player.hasPermission("revamphcf.staff")) {
            if (args.length == 0) {
                if (spawn == null) {
                    System.out.print("SPAWN IS NULL!");
                    player.sendMessage(CC.translate("&c&lSpawn is not set please contact a Staff Member!"));
                    return;
                }
                player.teleport(spawn);
            }
            else {
                Player target = Bukkit.getPlayer(args[0]);
                if (target == null) {
                    sender.sendMessage(Language.COMMANDS_PLAYER_NOT_FOUND.toString());
                    return;
                }
                target.teleport(spawn);
                target.sendMessage(CC.translate("&aYou have been teleported to spawn by&f " + player.getName()));
                sender.sendMessage(CC.translate("&aYou have successfully teleport &f" + target.getName() + " &ato spawn."));
            }
        }
        else {
            sender.sendMessage(CC.translate("&cHCF does not have a spawn command! You must walk there! Spawn is located at 0,0."));
        }
    }
}
