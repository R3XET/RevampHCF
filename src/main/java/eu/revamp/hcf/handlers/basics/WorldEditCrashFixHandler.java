package eu.revamp.hcf.handlers.basics;

import eu.revamp.hcf.RevampHCF;
import eu.revamp.spigot.utils.chat.message.MessageUtils;
import org.bukkit.event.EventHandler;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.Listener;
import eu.revamp.hcf.utils.Handler;

public class WorldEditCrashFixHandler extends Handler implements Listener
{
    public WorldEditCrashFixHandler(RevampHCF plugin) {
        super(plugin);
    }
    
    @Override
    public void enable() {
        this.getInstance().getServer().getPluginManager().registerEvents(this, this.getInstance());
    }

    @EventHandler
    public void onPreCommand(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();
        if (player.isOp()) return;
        if (player.hasPermission("revamphcf.op")) return;
        if (event.getMessage().startsWith("/worldedit:/calc") || event.getMessage().startsWith("/worldedit:/eval") || event.getMessage().startsWith("/worldedit:/solve") || event.getMessage().startsWith("//calc") || event.getMessage().startsWith("//eval") || event.getMessage().startsWith("//solve")) {
            event.setCancelled(true);
            MessageUtils.sendMessage("§4" + player.getName() + " §ctried to crash the server §7(§c" + event.getMessage() + "§7)", "hcf.staff");
        }
    }
}
