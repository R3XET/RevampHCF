package eu.revamp.hcf.factions.events;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import eu.revamp.hcf.factions.type.PlayerFaction;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

import javax.annotation.Nullable;
import java.util.UUID;

@Getter
public class FactionPlayerJoinEvent extends FactionEvent implements Cancellable
{
    private static HandlerList handlers = new HandlerList();
    @Setter private boolean cancelled;
    private Optional<Player> player;
    private CommandSender sender;
    private UUID playerUUID;

    public FactionPlayerJoinEvent(CommandSender sender, @Nullable Player player, UUID playerUUID, PlayerFaction playerFaction) {
        super(playerFaction);
        Preconditions.checkNotNull(sender, "Sender cannot be null");
        Preconditions.checkNotNull(playerUUID, "Player UUID cannot be null");
        Preconditions.checkNotNull(playerFaction, "Player faction cannot be null");
        this.sender = sender;
        if (player != null) {
            this.player = Optional.of(player);
        }
        this.playerUUID = playerUUID;
    }

    public Optional<Player> getPlayer() {
        if (!this.player.isPresent()) {
            this.player = Optional.fromNullable(Bukkit.getPlayer(this.playerUUID));
        }
        return this.player;
    }

    public static HandlerList getHandlerList() {
        return FactionPlayerJoinEvent.handlers;
    }
    @Override
    public HandlerList getHandlers() {
        return FactionPlayerJoinEvent.handlers;
    }
    @Override
    public PlayerFaction getFaction() {
        return (PlayerFaction)super.getFaction();
    }
}
