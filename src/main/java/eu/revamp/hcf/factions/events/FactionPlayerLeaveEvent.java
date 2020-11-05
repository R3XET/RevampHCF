package eu.revamp.hcf.factions.events;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import eu.revamp.hcf.factions.enums.FactionLeaveEnum;
import eu.revamp.hcf.factions.type.PlayerFaction;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

import javax.annotation.Nullable;
import java.util.UUID;


public class FactionPlayerLeaveEvent extends FactionEvent implements Cancellable
{
    private static HandlerList handlers = new HandlerList();
    private CommandSender sender;
    private UUID uniqueID;
    private FactionLeaveEnum cause;
    private boolean isKick;
    private boolean force;
    private boolean cancelled;
    private Optional<Player> player;

    public FactionPlayerLeaveEvent(final CommandSender sender, @Nullable final Player player, final UUID playerUUID, final PlayerFaction playerFaction, final FactionLeaveEnum cause, final boolean isKick, final boolean force) {
        super(playerFaction);
        Preconditions.checkNotNull(sender, "Sender cannot be null");
        Preconditions.checkNotNull(playerUUID, "Player UUID cannot be null");
        Preconditions.checkNotNull(playerFaction, "Player faction cannot be null");
        Preconditions.checkNotNull(cause, "Cause cannot be null");
        this.sender = sender;
        if (player != null) {
            this.player = Optional.of(player);
        }
        this.uniqueID = playerUUID;
        this.cause = cause;
        this.isKick = isKick;
        this.force = force;
    }

    public static HandlerList getHandlerList() {
        return FactionPlayerLeaveEvent.handlers;
    }

    @Override
    public PlayerFaction getFaction() {
        return (PlayerFaction)super.getFaction();
    }

    public Optional<Player> getPlayer() {
        if (this.player == null) {
            this.player = Optional.fromNullable(Bukkit.getPlayer(this.uniqueID));
        }
        return this.player;
    }

    @Override
    public HandlerList getHandlers() {
        return FactionPlayerLeaveEvent.handlers;
    }

    public boolean isCancelled() {
        return this.cancelled;
    }

    public void setCancelled(final boolean cancelled) {
        this.cancelled = cancelled;
    }

    public CommandSender getSender() {
        return this.sender;
    }

    public UUID getPlayerUUID() {
        return this.uniqueID;
    }

    public FactionLeaveEnum getFactionLeaveEnum() {
        return this.cause;
    }

    public boolean isKick() {
        return this.isKick;
    }

    public boolean isForce() {
        return this.force;
    }
}
