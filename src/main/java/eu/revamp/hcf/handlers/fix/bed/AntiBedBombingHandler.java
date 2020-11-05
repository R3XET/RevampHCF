package eu.revamp.hcf.handlers.fix.bed;

import eu.revamp.hcf.RevampHCF;
import eu.revamp.hcf.utils.Handler;
import eu.revamp.spigot.utils.chat.color.CC;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

public class AntiBedBombingHandler extends Handler implements Listener {
    public AntiBedBombingHandler(RevampHCF instance) {
        super(instance);
    }
    @Override
    public void enable(){
        this.getInstance().getServer().getPluginManager().registerEvents(this, this.getInstance());
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onBedBombing(PlayerInteractEvent event) {
        if (event.getPlayer().getItemInHand().getType().equals(Material.BED) && event.getPlayer().getLocation().getWorld().getEnvironment().equals(World.Environment.NETHER)){
            event.setCancelled(true);
            event.getPlayer().sendMessage(CC.translate("&cBed bombing is not enabled."));
        }
    }
}
