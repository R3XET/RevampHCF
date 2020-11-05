package eu.revamp.hcf.factions.events;

import com.google.common.base.Preconditions;
import lombok.Getter;
import eu.revamp.hcf.factions.type.PlayerFaction;
import javax.annotation.Nullable;
import eu.revamp.hcf.factions.enums.FactionLeaveEnum;
import java.util.UUID;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

public class FactionPlayerLeftEvent extends FactionEvent
{
    private static HandlerList handlers = new HandlerList();
    @Getter private Player player;
    @Getter private CommandSender sender;
    @Getter private UUID uniqueID;
    @Getter private FactionLeaveEnum cause;
    @Getter private boolean isKick;
    @Getter private boolean force;

    public FactionPlayerLeftEvent(CommandSender sender, @Nullable Player player, UUID playerUUID, PlayerFaction playerFaction, FactionLeaveEnum cause, boolean isKick, boolean force) {
        super(playerFaction);
        Preconditions.checkNotNull((Object)sender, "Sender cannot be null");
        Preconditions.checkNotNull((Object)playerUUID, "Player UUID cannot be null");
        Preconditions.checkNotNull((Object)playerFaction, "Player faction cannot be null");
        Preconditions.checkNotNull((Object)cause, "Cause cannot be null");
        this.sender = sender;
        if (player != null) {
            this.player = player;
        }
        this.uniqueID = playerUUID;
        this.cause = cause;
        this.isKick = isKick;
        this.force = force;
    }
    @Override
    public HandlerList getHandlers() {
        return FactionPlayerLeftEvent.handlers;
    }
    public static HandlerList getHandlerList() {
        return FactionPlayerLeftEvent.handlers;
    }

    @Override
    public PlayerFaction getFaction() {
        return (PlayerFaction)super.getFaction();
    }
}
