package eu.revamp.hcf.games.king;

import eu.revamp.hcf.commands.games.KingCommand;
import eu.revamp.hcf.RevampHCF;
import eu.revamp.hcf.utils.timer.GlobalTimer;
import eu.revamp.hcf.utils.timer.events.TimerExpireEvent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import java.util.concurrent.TimeUnit;

public class KingTimer extends GlobalTimer implements Listener {
	private final RevampHCF plugin;

	public KingTimer(final RevampHCF plugin) {
		super("Key-All", TimeUnit.SECONDS.toMillis(1L));
		this.plugin = plugin;
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onExpire(TimerExpireEvent event) {
		if (event.getTimer() == this) {
			Bukkit.dispatchCommand(Bukkit.getConsoleSender(),
					"broadcast &7The &a&lKeyAll &7has happened.");
			Bukkit.dispatchCommand(Bukkit.getConsoleSender(),
					"cr giveallkey " + KingCommand.player + " 1");
		}
	}

	public String getScoreboardPrefix() {
		return ChatColor.GOLD.toString() + ChatColor.BOLD.toString();
	}
}
