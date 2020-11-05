package eu.revamp.hcf.games;

import eu.revamp.hcf.factions.utils.games.EventFaction;
import eu.revamp.hcf.factions.utils.zone.CaptureZone;
import eu.revamp.hcf.timers.GameHandler;
import org.bukkit.entity.Player;

public interface EventGameType
{
    GameType getEventType();
    
    void tick(GameHandler p0, EventFaction p1);
    
    void onContest(EventFaction p0, GameHandler p1);
    
    boolean onControlTake(Player p0, CaptureZone p1);
    
    void onControlLoss(Player p0, CaptureZone p1, EventFaction p2);
    
    void stopTiming();
}
