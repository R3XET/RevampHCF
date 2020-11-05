package eu.revamp.hcf.factions.events;

import java.util.UUID;

import lombok.Getter;
import org.bukkit.entity.Player;
import eu.revamp.hcf.factions.type.PlayerFaction;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Event;

public class FactionFocusChangeEvent extends Event
{
    private static HandlerList handlers = new HandlerList();
    @Getter private PlayerFaction senderFaction;
    @Getter private Player player;
    @Getter private UUID oldFocus;
    
    public FactionFocusChangeEvent(PlayerFaction senderFaction, final Player player, final UUID oldFocus) {
        this.senderFaction = senderFaction;
        this.player = player;
        this.oldFocus = oldFocus;
    }
    @Override
    public HandlerList getHandlers() {
        return FactionFocusChangeEvent.handlers;
    }
    public static HandlerList getHandlerList() {
        return FactionFocusChangeEvent.handlers;
    }
}
