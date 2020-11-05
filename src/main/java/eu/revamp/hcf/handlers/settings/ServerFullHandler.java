package eu.revamp.hcf.handlers.settings;

import eu.revamp.hcf.RevampHCF;
import org.bukkit.event.EventPriority;
import org.bukkit.event.EventHandler;
import org.bukkit.entity.Player;
import eu.revamp.spigot.utils.chat.color.CC;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.Listener;
import eu.revamp.hcf.utils.Handler;

public class ServerFullHandler extends Handler implements Listener
{
    public ServerFullHandler(RevampHCF plugin) {
        super(plugin);
    }
    
    @Override
    public void enable() {
        this.getInstance().getServer().getPluginManager().registerEvents(this, this.getInstance());
    }
    
    @EventHandler(priority = EventPriority.MONITOR)
    public void onTryJoin(PlayerLoginEvent event) {
        final Player player = event.getPlayer();
        if (event.getResult().equals(PlayerLoginEvent.Result.KICK_FULL)) {
            if (player.hasPermission("utilities.player.staff") || player.hasPermission("utilities.player.donator")) {
                event.allow();
            }
            else {
                event.disallow(PlayerLoginEvent.Result.KICK_FULL, CC.translate("&cServer is full! \n\n &cBuy rank for unlock this &ffeature"));
            }
        }
    }
}
