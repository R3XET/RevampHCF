package eu.revamp.hcf.handlers.blocks;

import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.event.inventory.FurnaceBurnEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.block.BlockState;
import org.bukkit.block.Block;
import org.bukkit.block.Furnace;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.Bukkit;
import eu.revamp.hcf.RevampHCF;
import org.bukkit.event.Listener;
import eu.revamp.hcf.utils.Handler;

public class FurnaceSpeedHandler extends Handler implements Listener {
    public FurnaceSpeedHandler(RevampHCF plugin) {
        super(plugin);
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }
    
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Action action = event.getAction();
        if (action == Action.RIGHT_CLICK_BLOCK) {
            Block block = event.getClickedBlock();
            BlockState state = block.getState();
            if (state instanceof Furnace) {
                Furnace furnace = (Furnace)state;
                furnace.setCookTime((short)(furnace.getCookTime() + RevampHCF.getInstance().getConfig().getInt("FURNACE-SPEED")));
            }
        }
    }
    
    @EventHandler
    public void onFurnaceBurn(FurnaceBurnEvent event) {
        BlockState blockState = event.getBlock().getState();
        if (blockState instanceof Furnace) {
            Furnace furnace = (Furnace)blockState;
            new FurnaceUpdateTask(furnace).runTaskTimer(this.getInstance(), 2L, 6L);
        }
    }
    
    public static class FurnaceUpdateTask extends BukkitRunnable {
        private final Furnace furnace;
        
        public FurnaceUpdateTask(Furnace furnace) {
            this.furnace = furnace;
        }
        
        public void run() {
            this.furnace.setCookTime((short)(this.furnace.getCookTime() + RevampHCF.getInstance().getConfig().getInt("FURNACE-SPEED")));
            this.furnace.update();
            if (this.furnace.getBurnTime() <= 1) {
                this.cancel();
            }
        }
    }
}
