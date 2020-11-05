package eu.revamp.hcf.handlers.fix;

import eu.revamp.hcf.RevampHCF;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.entity.Player;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import eu.revamp.hcf.utils.Handler;

public class HitDetectionListener extends Handler implements Listener
{
    public HitDetectionListener(RevampHCF plugin) {
        super(plugin);
    }
    public void enable() {
        this.getInstance().getServer().getPluginManager().registerEvents(this, this.getInstance());
        for (Player player : Bukkit.getServer().getOnlinePlayers()) {
            player.setMaximumNoDamageTicks(this.getInstance().getConfig().getInt("HIT_DETECTION"));
        }
    }
    @Override
    public void disable(){
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        player.setMaximumNoDamageTicks(this.getInstance().getConfig().getInt("HIT_DETECTION"));
    }
}
