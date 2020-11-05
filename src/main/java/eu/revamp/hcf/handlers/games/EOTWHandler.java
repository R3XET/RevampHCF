package eu.revamp.hcf.handlers.games;

import eu.revamp.hcf.Language;
import eu.revamp.hcf.RevampHCF;
import eu.revamp.hcf.factions.Faction;
import eu.revamp.hcf.factions.enums.ClaimChangeEnum;
import eu.revamp.hcf.factions.events.FactionClaimChangeEvent;
import eu.revamp.hcf.factions.events.FactionCreateEvent;
import eu.revamp.hcf.factions.type.PlayerFaction;
import eu.revamp.hcf.playerdata.HCFPlayerData;
import eu.revamp.hcf.utils.Handler;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class EOTWHandler extends Handler implements Listener
{
    public EOTWHandler(RevampHCF plugin) {
        super(plugin);
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }
    
    @Override
    public void enable() {
        this.getInstance().getServer().getPluginManager().registerEvents(this, this.getInstance());
    }
    
    @Override
    public void disable() {
    }
    
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (RevampHCF.getInstance().getHandlerManager().getEotwUtils().isEndOfTheWorld()) {
            HCFPlayerData data = this.getInstance().getHandlerManager().getHCFPlayerDataHandler().getPlayer(player);
            if (data.getPvpTimerCooldown() > 0) {
                data.setPvpTimerCooldown(0);
                this.getInstance().getHandlerManager().getTimerManager().getPvpTimerHandler().clearCooldown(player);
            }
        }
    }
    
    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
    public void onFactionCreate(FactionCreateEvent event) {
        if (RevampHCF.getInstance().getHandlerManager().getEotwUtils().isEndOfTheWorld()) {
            Faction faction = event.getFaction();
            if (faction instanceof PlayerFaction) {
                event.setCancelled(true);
                event.getSender().sendMessage(Language.EOTW_FACTION_CREATE.toString());
            }
        }
    }
    
    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
    public void onFactionClaimChange(FactionClaimChangeEvent event) {
        if (RevampHCF.getInstance().getHandlerManager().getEotwUtils().isEndOfTheWorld() && event.getCause() == ClaimChangeEnum.CLAIM) {
            Faction faction = event.getClaimableFaction();
            if (faction instanceof PlayerFaction) {
                event.setCancelled(true);
                event.getSender().sendMessage(Language.EOTW_FACTION_CLAIM_CHANGE.toString());
            }
        }
    }
}
