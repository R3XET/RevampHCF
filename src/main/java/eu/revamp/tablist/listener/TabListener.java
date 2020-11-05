package eu.revamp.tablist.listener;

import eu.revamp.tablist.RevampTab;
import eu.revamp.tablist.manager.TablistManager;
import eu.revamp.tablist.playerversion.PlayerVersionTinyProtocol;
import eu.revamp.tablist.tabversion.TabVersionTinyProtocol;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Getter
@Setter
public class TabListener implements Listener {

    private RevampTab revampTab;

    public TabListener(RevampTab revampTab) {
        setRevampTab(revampTab);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        new BukkitRunnable(){
            @Override
            public void run() {
                getRevampTab().getTablistVersion().setup(event.getPlayer());
            }
        }.runTaskLater(getRevampTab().getJavaPlugin(), getRevampTab().getPlayerVersion() instanceof PlayerVersionTinyProtocol ? 60 : 40);
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onQuit(PlayerQuitEvent event) {
        TablistManager tablist = null;
        List<TablistManager> tablists = getRevampTab().getTablists();
        for (TablistManager value : tablists) {
            if (value.getPlayer() != event.getPlayer()) continue;
            tablist = value;
            break;
        }
        if (tablist != null) {
            tablist.destroy();
        }
    }

    @EventHandler
    public void onLogin(PlayerLoginEvent event) {
        if (getRevampTab().getTablistVersion() instanceof TabVersionTinyProtocol && System.currentTimeMillis() - getRevampTab().getStartTime() < TimeUnit.SECONDS.toMillis(6)) {
            event.disallow(PlayerLoginEvent.Result.KICK_OTHER, "\u00a7cThe server is still loading.");
        }
    }
}

