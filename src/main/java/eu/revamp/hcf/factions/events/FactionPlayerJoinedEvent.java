package eu.revamp.hcf.factions.events;

import com.google.common.base.Preconditions;
import lombok.Getter;
import lombok.Setter;
import eu.revamp.hcf.factions.type.PlayerFaction;
import javax.annotation.Nullable;
import java.util.UUID;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

@Getter
public class FactionPlayerJoinedEvent extends FactionEvent
{
    private static HandlerList handlers = new HandlerList();
    @Setter private boolean cancelled;
    private Player player;
    private CommandSender sender;
    private UUID playerUUID;

    public FactionPlayerJoinedEvent(CommandSender sender, @Nullable Player player, UUID playerUUID, PlayerFaction playerFaction) {
        super(playerFaction);
        Preconditions.checkNotNull((Object)sender, "Sender cannot be null");
        Preconditions.checkNotNull((Object)playerUUID, "Player UUID cannot be null");
        Preconditions.checkNotNull((Object)playerFaction, "Player faction cannot be null");
        this.sender = sender;
        if (player != null) {
            this.player = player;
        }
        this.playerUUID = playerUUID;
    }
    @Override
    public HandlerList getHandlers() {
        return FactionPlayerJoinedEvent.handlers;
    }
    public static HandlerList getHandlerList() {
        return FactionPlayerJoinedEvent.handlers;
    }

    @Override
    public PlayerFaction getFaction() {
        return (PlayerFaction)super.getFaction();
    }
}
