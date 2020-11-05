package eu.revamp.hcf.factions.events;

import lombok.Getter;
import lombok.Setter;
import eu.revamp.hcf.factions.Faction;
import org.bukkit.command.CommandSender;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Cancellable;

public class FactionCreateEvent extends FactionEvent implements Cancellable
{
    private static HandlerList handlers = new HandlerList();
    @Getter @Setter private boolean cancelled;
    @Getter private CommandSender sender;

    public FactionCreateEvent(Faction faction, CommandSender sender) {
        super(faction);
        this.sender = sender;
    }
    @Override
    public HandlerList getHandlers() {
        return FactionCreateEvent.handlers;
    }
    public static HandlerList getHandlerList() {
        return FactionCreateEvent.handlers;
    }
}
