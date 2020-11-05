package eu.revamp.hcf.handlers.fix.glitch;

import eu.revamp.hcf.RevampHCF;
import eu.revamp.hcf.utils.Handler;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.ArrayList;
import java.util.List;

public class SugarCaneFixHandler extends Handler implements Listener {
    public SugarCaneFixHandler(RevampHCF plugin) {
        super(plugin);
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onPlace(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }
        if (player.getItemInHand().getType() != Material.SUGAR_CANE) {
            return;
        }
        if (player.getItemInHand().getType() == Material.SUGAR_CANE) {
            for (Block b : this.getUtil().getAround(event.getClickedBlock())) {
                if (b.getType() == Material.WATER || b.getType() == Material.STATIONARY_WATER) {
                    return;
                }
            }
        }
        event.setCancelled(true);
    }


    private Util util;

    public Util getUtil() {
        if (util == null) util = new Util();
        return this.util;
    }

    private class Util {
        public List<Block> getAround(Block block) {
            List<Block> b = new ArrayList<>();
            for (int x = -1; x <= 1; ++x) {
                for (int y = -1; y <= 1; ++y) {
                    for (int z = -1; z <= 1; ++z) {
                        b.add(block.getRelative(x, y, z));
                    }
                }
            }
            return b;
        }
    }
}
