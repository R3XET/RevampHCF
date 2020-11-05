package eu.revamp.hcf.handlers.hcf.spawners.end;

import eu.revamp.hcf.RevampHCF;
import eu.revamp.hcf.commands.games.EOTWFFACommand;
import eu.revamp.hcf.utils.Handler;
import eu.revamp.spigot.utils.chat.color.CC;
import org.bukkit.World;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

public class AntiSpawnersPlaceEndHandler extends Handler implements Listener {
    public AntiSpawnersPlaceEndHandler(RevampHCF instance) {
        super(instance);
    }
    @Override
    public void enable(){
        this.getInstance().getServer().getPluginManager().registerEvents(this, this.getInstance());
    }
    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        if (EOTWFFACommand.isEOTWFFA()) {
            if (player.hasPermission("revamphcf.op")) return;
            event.setCancelled(true);
        }
        if (player.getWorld().getEnvironment() == World.Environment.THE_END && event.getBlock().getState() instanceof CreatureSpawner && !player.hasPermission("hcf.protection.admin")) {
            event.setCancelled(true);
            player.sendMessage(CC.translate("&cYou can't place spawners in the end."));
        }
    }
}
