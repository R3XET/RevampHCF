package eu.revamp.hcf.classes.utils.events;

import lombok.Getter;
import org.bukkit.entity.Player;
import eu.revamp.hcf.classes.utils.ArmorClass;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

public class ArmorClassEquipEvent extends PlayerEvent
{
    private static HandlerList handlers = new HandlerList();
    @Getter private ArmorClass pvpClass;
    
    public ArmorClassEquipEvent(Player player, ArmorClass pvpClass) {
        super(player);
        this.pvpClass = pvpClass;
    }
    @Override
    public HandlerList getHandlers() {
        return ArmorClassEquipEvent.handlers;
    }
    public static HandlerList getHandlerList() {
        return ArmorClassEquipEvent.handlers;
    }
}
