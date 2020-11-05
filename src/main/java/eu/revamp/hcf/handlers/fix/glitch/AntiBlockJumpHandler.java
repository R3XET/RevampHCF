package eu.revamp.hcf.handlers.fix.glitch;

import org.bukkit.event.EventPriority;
import org.bukkit.event.EventHandler;
import org.bukkit.util.Vector;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.block.Sign;
import org.bukkit.GameMode;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.Bukkit;
import eu.revamp.hcf.RevampHCF;
import org.bukkit.event.Listener;
import eu.revamp.hcf.utils.Handler;

public class AntiBlockJumpHandler extends Handler implements Listener
{
    public AntiBlockJumpHandler(RevampHCF plugin) {
        super(plugin);
    }

    public void enable(){
        this.getInstance().getServer().getPluginManager().registerEvents(this, this.getInstance());
    }
    
    @EventHandler(ignoreCancelled = false, priority = EventPriority.MONITOR)
    public void onBlockBreak(BlockPlaceEvent event) {
        if (event.isCancelled()) {
            Player player = event.getPlayer();
            if (player.getGameMode() == GameMode.CREATIVE || player.getAllowFlight()) return;
            Block block = event.getBlockPlaced();
            if (block.getType().isSolid() && !(block.getState() instanceof Sign)) {
                int playerY = player.getLocation().getBlockY();
                int blockY = block.getLocation().getBlockY();
                if (playerY > blockY) {
                    Vector vector = player.getVelocity();
                    vector.setX(-0.1);
                    vector.setZ(-0.1);
                    player.setVelocity(vector.setY(vector.getY() - 0.41999998688697815));
                }
            }
        }
    }
}
