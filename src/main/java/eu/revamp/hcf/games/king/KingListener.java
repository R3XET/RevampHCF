package eu.revamp.hcf.games.king;

import eu.revamp.hcf.commands.games.KingCommand;
import eu.revamp.hcf.RevampHCF;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class KingListener implements Listener {
	@Getter private RebootRunnable rebootRunnable;

	public boolean cancel() {
		if (this.rebootRunnable != null) {
			this.rebootRunnable.cancel();
			this.rebootRunnable = null;
			return true;
		}
		return false;
	}

	public void start(long millis) {
		if (this.rebootRunnable == null) {
			(this.rebootRunnable = new RebootRunnable(this, millis)).runTaskLater(RevampHCF.getInstance(),
					millis / 50L);
		}
	}

	public long getRemaining() {
		return RebootRunnable.endMillis - System.currentTimeMillis();
	}

	@EventHandler
	public void onDeath(PlayerDeathEvent e) {
		Player player = e.getEntity();
		if (KingCommand.player != null && player.getName().equals(KingCommand.kingName)) {
			Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "kingevent end");
			Bukkit.broadcastMessage(ChatColor.DARK_GREEN.toString() + ChatColor.BOLD
					+ "THE KING HAS DIED");
		}
	}

	static void access(KingListener kingListener, RebootRunnable rebootRunnable) {
		kingListener.rebootRunnable = rebootRunnable;
	}

	public static class RebootRunnable extends BukkitRunnable {
		private KingListener rebootTimer;
		private long startMillis;
		private static long endMillis;

		public RebootRunnable(KingListener rebootTimer, long duration) {
			this.rebootTimer = rebootTimer;
			this.startMillis = System.currentTimeMillis();
			RebootRunnable.endMillis = this.startMillis + duration;
		}

		public long getRemaining() {
			return RebootRunnable.endMillis - System.currentTimeMillis();
		}

		public void run() {
			this.cancel();
			KingListener.access(this.rebootTimer, null);
		}
	}
}
