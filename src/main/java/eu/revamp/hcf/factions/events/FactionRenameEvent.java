package eu.revamp.hcf.factions.events;

import eu.revamp.hcf.factions.Faction;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

@Getter
public class FactionRenameEvent extends FactionEvent implements Cancellable
{
    private static HandlerList handlers = new HandlerList();
    @Setter private boolean cancelled;
    private CommandSender sender;
    private String originalName;
    private String newName;
    
    public FactionRenameEvent(Faction faction, CommandSender sender, final String originalName, final String newName) {
        super(faction);
        this.sender = sender;
        this.originalName = originalName;
        this.newName = newName;
    }
    
    public void setNewName(String newName) {
        if (!newName.equals(this.newName)) {
            this.newName = newName;
        }
    }
    @Override
    public HandlerList getHandlers() {
        return FactionRenameEvent.handlers;
    }
    public static HandlerList getHandlerList() {
        return FactionRenameEvent.handlers;
    }
}
