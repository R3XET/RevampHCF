package eu.revamp.hcf.handlers.settings;

import eu.revamp.hcf.RevampHCF;
import eu.revamp.hcf.utils.Handler;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

public class WhitelistHandler extends Handler implements Listener
{
    public WhitelistHandler(RevampHCF plugin) {
        super(plugin);
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }
    
    @EventHandler
    public void handleKicks(PlayerLoginEvent e) {
        if (e.getResult() == PlayerLoginEvent.Result.KICK_WHITELIST) {
            e.setKickMessage(RevampHCF.getInstance().getConfig().getString("WHITELIST_MESSAGE"));
        }
    }
}
