package eu.revamp.hcf.factions.events;

import eu.revamp.hcf.factions.Faction;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

public class FactionRemoveEvent extends FactionEvent implements Cancellable
{
    private static HandlerList handlers = new HandlerList();
    @Getter @Setter
    private boolean cancelled;
    @Getter private CommandSender sender;

    public FactionRemoveEvent(Faction faction, CommandSender sender) {
        super(faction);
        this.sender = sender;
    }
    @Override
    public HandlerList getHandlers() {
        return FactionRemoveEvent.handlers;
    }
    public static HandlerList getHandlerList() {
        return FactionRemoveEvent.handlers;
    }
}
