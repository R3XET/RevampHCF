package eu.revamp.hcf.handlers.fix.security;

import eu.revamp.hcf.RevampHCF;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import eu.revamp.hcf.utils.Handler;

public class ColonCommandFixListener extends Handler implements Listener
{
    public ColonCommandFixListener(RevampHCF plugin) {
        super(plugin);
    }
    public void enable(){
        this.getInstance().getServer().getPluginManager().registerEvents(this, this.getInstance());
    }
    
    @EventHandler
    public void onPlayerColonCommand(PlayerCommandPreprocessEvent e) {
        if (e.getMessage().startsWith("/minecraft:") || e.getMessage().startsWith("bukkit:")) {
            e.setCancelled(true);
        }
        else if ((e.getMessage().startsWith("/ver") || e.getMessage().startsWith("/about")) && !e.getPlayer().isOp()) {
            e.setCancelled(true);
        }
    }
}
