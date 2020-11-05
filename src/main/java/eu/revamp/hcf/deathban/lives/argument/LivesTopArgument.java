package eu.revamp.hcf.deathban.lives.argument;

import eu.revamp.hcf.RevampHCF;
import eu.revamp.hcf.utils.command.CommandArgument;
import eu.revamp.hcf.utils.inventory.BukkitUtils;
import net.minecraft.util.gnu.trove.map.hash.TObjectIntHashMap;
import net.minecraft.util.gnu.trove.procedure.TObjectIntProcedure;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 * An {@link CommandArgument} used to check who has the most lives.
 */
public class LivesTopArgument extends CommandArgument {

    private static final int MAX_ENTRIES = 10;
    private final RevampHCF plugin;

    public LivesTopArgument(RevampHCF plugin) {
        super("top", "Check who has the most lives");
        this.plugin = plugin;
        this.permission = "hcf.command.lives.argument." + getName();
    }

    @Override
    public String getUsage(String label) {
        return '/' + label + ' ' + getName();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        synchronized (plugin.getDeathbanManager().getLivesMap()) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    TObjectIntHashMap<UUID> livesMap = (TObjectIntHashMap<UUID>) plugin.getDeathbanManager().getLivesMap();

                    if (livesMap.isEmpty()) {
                        sender.sendMessage(ChatColor.RED + "There are no lives stored.");
                        return;
                    }
                    sender.sendMessage(ChatColor.DARK_GRAY + BukkitUtils.STRAIGHT_LINE_DEFAULT);
                    sender.sendMessage(ChatColor.DARK_AQUA + ChatColor.BOLD.toString() + "Top " + MAX_ENTRIES + " Lives");
                    sender.sendMessage(ChatColor.DARK_GRAY + BukkitUtils.STRAIGHT_LINE_DEFAULT);
                    livesMap.forEachEntry(new TObjectIntProcedure<UUID>() {
                        int count = 0;

                        @Override
                        public boolean execute(UUID uuid, int balance) {
                            OfflinePlayer offlineNext = Bukkit.getOfflinePlayer(uuid);
                            sender.sendMessage(" " + ChatColor.GRAY + (++count) + ". " + ChatColor.DARK_AQUA + offlineNext.getName() + ChatColor.DARK_GRAY + ": " + ChatColor.GRAY + balance);
                            return count != MAX_ENTRIES;
                        }
                    });
                }
            }.runTaskAsynchronously(plugin);
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        return Collections.emptyList();
    }
}
