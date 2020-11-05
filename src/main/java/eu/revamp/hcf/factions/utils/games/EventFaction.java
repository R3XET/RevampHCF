package eu.revamp.hcf.factions.utils.games;

import eu.revamp.hcf.factions.utils.zone.CaptureZone;
import java.util.List;
import eu.revamp.hcf.games.GameType;
import org.bukkit.Location;
import eu.revamp.hcf.handlers.claim.ClaimHandler;
import eu.revamp.hcf.factions.utils.zone.ClaimZone;
import eu.revamp.hcf.games.cuboid.Cuboid;
import org.bukkit.command.CommandSender;
import org.bukkit.ChatColor;
import eu.revamp.hcf.factions.Faction;
import java.util.Map;
import eu.revamp.hcf.factions.type.ClaimableFaction;

public abstract class EventFaction extends ClaimableFaction
{
    public EventFaction(String name) {
        super(name);
        this.setDeathban(true);
    }
    
    public EventFaction(Map<String, Object> map) {
        super(map);
    }
    
    @Override
    public String getDisplayName(Faction faction) {
        return ChatColor.GOLD + this.getName() + ' ' + ChatColor.AQUA + this.getEventType().getDisplayName();
    }
    
    @Override
    public String getDisplayName(CommandSender sender) {
        return ChatColor.GOLD + this.getName() + ' ' + ChatColor.AQUA + this.getEventType().getDisplayName();
    }
    
    public String getScoreboardName() {
        return this.getName();
    }
    
    public void setClaim(Cuboid cuboid, CommandSender sender) {
        this.removeClaims(this.getClaims(), sender);
        final Location min = cuboid.getMinimumPoint();
        min.setY(ClaimHandler.MIN_CLAIM_HEIGHT);
        final Location max = cuboid.getMaximumPoint();
        max.setY(ClaimHandler.MAX_CLAIM_HEIGHT);
        this.addClaim(new ClaimZone(this, min, max), sender);
    }
    
    public abstract GameType getEventType();
    
    public abstract List<CaptureZone> getCaptureZones();
}
