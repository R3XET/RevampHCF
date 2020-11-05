package eu.revamp.hcf.handlers.hcf;

import eu.revamp.hcf.RevampHCF;
import eu.revamp.hcf.factions.Faction;
import eu.revamp.hcf.factions.type.SpawnFaction;
import eu.revamp.hcf.factions.utils.games.EventFaction;
import eu.revamp.hcf.utils.Handler;
import eu.revamp.spigot.utils.chat.color.CC;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class EnderpearlEventsHandler extends Handler implements Listener {
    public EnderpearlEventsHandler(RevampHCF instance) {
        super(instance);
    }
    @Override
    public void enable(){
        this.getInstance().getServer().getPluginManager().registerEvents(this, this.getInstance());
    }
    @EventHandler
    public void noPearlInEventsorSpawn(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Action action = event.getAction();
        Faction factionAt = RevampHCF.getInstance().getFactionManager().getFactionAt(player.getLocation());
        if (factionAt instanceof EventFaction && (action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK )&& player.getItemInHand().getType() == Material.ENDER_PEARL) {
            player.sendMessage(CC.translate("&CYou can't pearl in games!"));
            event.setCancelled(true);
            RevampHCF.getInstance().getHandlerManager().getEnderpearlHandler().quit(player);
        }
        if (factionAt instanceof SpawnFaction && (action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK )&& player.getItemInHand().getType() == Material.ENDER_PEARL) {
            player.sendMessage(CC.translate("&CYou can't pearl in games!"));
            event.setCancelled(true);
            RevampHCF.getInstance().getHandlerManager().getEnderpearlHandler().quit(player);
        }
    }
}
