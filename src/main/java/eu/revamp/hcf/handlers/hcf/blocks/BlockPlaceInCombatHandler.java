package eu.revamp.hcf.handlers.hcf.blocks;

import eu.revamp.hcf.Language;
import eu.revamp.hcf.RevampHCF;
import eu.revamp.hcf.utils.Handler;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

public class BlockPlaceInCombatHandler extends Handler implements Listener {
    public BlockPlaceInCombatHandler(RevampHCF instance) {
        super(instance);
    }
    @Override
    public void enable(){
        this.getInstance().getServer().getPluginManager().registerEvents(this, this.getInstance());
    }

    @EventHandler
    public void onPlaceBlocksInCombat(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        if (RevampHCF.getInstance().getHandlerManager().getTimerManager().getSpawnTagHandler().hasCooldown(player.getUniqueId()) && RevampHCF.getInstance().getFactionManager().getFactionAt(player.getLocation()).equals(RevampHCF.getInstance().getFactionManager().getPlayerFaction(player.getUniqueId()))) {
            event.getPlayer().sendMessage(Language.COMBAT_PLACE.toString());
            event.setCancelled(true);
        }
    }
}
