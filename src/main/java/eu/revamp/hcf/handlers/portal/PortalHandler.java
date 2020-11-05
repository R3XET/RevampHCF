package eu.revamp.hcf.handlers.portal;

import eu.revamp.hcf.Language;
import eu.revamp.hcf.RevampHCF;
import eu.revamp.hcf.playerdata.HCFPlayerData;
import eu.revamp.hcf.utils.Handler;
import gnu.trove.map.TObjectLongMap;
import gnu.trove.map.hash.TObjectLongHashMap;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPortalEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.potion.PotionEffectType;

import java.util.UUID;

public class PortalHandler extends Handler implements Listener
{
    private static long PORTAL_MESSAGE_DELAY_THRESHOLD = 2500L;
    private TObjectLongMap<UUID> messageDelays;

    public PortalHandler(RevampHCF plugin) {
        super(plugin);
        this.messageDelays = new TObjectLongHashMap<>();
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }
    
    @Override
    public void enable() {
        this.getInstance().getServer().getPluginManager().registerEvents(this, this.getInstance());
    }
    
    @Override
    public void disable() {
    }
    
    @EventHandler(ignoreCancelled = true, priority = EventPriority.NORMAL)
    public void onEntityPortal(EntityPortalEvent event) {
        if (event.getEntity() instanceof EnderDragon) {
            event.setCancelled(true);
        }
    }
    
    @EventHandler(ignoreCancelled = true, priority = EventPriority.NORMAL)
    public void onWorldChanged(PlayerChangedWorldEvent event) {
        Player player = event.getPlayer();
        World from = event.getFrom();
        World to = player.getWorld();
        if (from.getEnvironment() != World.Environment.THE_END && to.getEnvironment() == World.Environment.THE_END && player.hasPotionEffect(PotionEffectType.INCREASE_DAMAGE)) {
            player.removePotionEffect(PotionEffectType.INCREASE_DAMAGE);
        }
    }
    
    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
    public void onPortalEnter(PlayerPortalEvent event) {
        if (event.getCause() != PlayerTeleportEvent.TeleportCause.END_PORTAL) return;
        World toWorld = event.getTo().getWorld();
        if (toWorld == null) return;
        if (toWorld.getEnvironment() == World.Environment.THE_END) {
            Player player = event.getPlayer();
            if (!this.getInstance().getConfiguration().isKitMap()) {
                HCFPlayerData data = RevampHCF.getInstance().getHandlerManager().getHCFPlayerDataHandler().getPlayer(player);
                if (this.getInstance().getHandlerManager().getTimerManager().getSpawnTagHandler().getRemaining(player) > 0L && data.getSpawnTagCooldown() > 0L) {
                    this.message(player, Language.PORTAL_ENTER_TAGGED.toString());
                    event.setCancelled(true);
                    return;
                }
                if (this.getInstance().getHandlerManager().getTimerManager().getPvpTimerHandler().getRemaining(player) > 0L && data.getPvpTimerCooldown() > 0L) {
                    this.message(player, Language.PORTAL_ENTER_PVPTIMER.toString());
                    event.setCancelled(true);
                    return;
                }
            }
            event.useTravelAgent(false);
            event.setTo(toWorld.getSpawnLocation().add(0.5, 0.0, 0.5));
        }
    }
    
    private void message(Player player, String message) {
        long last = this.messageDelays.get(player.getUniqueId());
        long millis = System.currentTimeMillis();
        if (last != this.messageDelays.getNoEntryValue() && last + PortalHandler.PORTAL_MESSAGE_DELAY_THRESHOLD - millis > 0L) return;
        this.messageDelays.put(player.getUniqueId(), millis);
        player.sendMessage(message);
    }
}
