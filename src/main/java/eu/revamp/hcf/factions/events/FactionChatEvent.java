package eu.revamp.hcf.factions.events;

import eu.revamp.system.enums.ChatChannel;
import lombok.Getter;
import lombok.Setter;
import eu.revamp.hcf.factions.type.PlayerFaction;
import eu.revamp.hcf.factions.utils.FactionMember;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

import java.util.Collection;

@Getter
public class FactionChatEvent extends FactionEvent implements Cancellable
{
    @Setter private boolean cancelled;
    private static HandlerList handlers = new HandlerList();
    private Player player;
    private FactionMember factionMember;
    private ChatChannel chatChannel;
    private String message;
    private Collection<? extends CommandSender> recipients;
    private String fallbackFormat;

    public FactionChatEvent(boolean async, PlayerFaction faction, Player player, ChatChannel chatChannel, Collection<? extends CommandSender> recipients, String message) {
        super(faction, async);
        this.player = player;
        this.factionMember = faction.getMember(player.getUniqueId());
        this.chatChannel = chatChannel;
        this.recipients = recipients;
        this.message = message;
        this.fallbackFormat = chatChannel.getRawFormat(player);
    }
    @Override
    public HandlerList getHandlers() {
        return FactionChatEvent.handlers;
    }
    public static HandlerList getHandlerList() {
        return FactionChatEvent.handlers;
    }
}
