package eu.revamp.hcf.classes.utils.events;

import lombok.Getter;
import org.bukkit.entity.Player;
import eu.revamp.hcf.classes.utils.ArmorClass;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

public class ArmorClassUnequipEvent extends PlayerEvent
{
    private static HandlerList handlers = new HandlerList();
    @Getter private ArmorClass pvpClass;

    public ArmorClassUnequipEvent(Player player, ArmorClass pvpClass) {
        super(player);
        this.pvpClass = pvpClass;
    }

    public static HandlerList getHandlerList() {
        return ArmorClassUnequipEvent.handlers;
    }

    @Override
    public HandlerList getHandlers() {
        return ArmorClassUnequipEvent.handlers;
    }

}
