package eu.revamp.hcf.factions.utils.struction;

import lombok.Getter;
import org.bukkit.ChatColor;

public enum RegenStatus
{
    FULL("FULL", 0, String.valueOf(ChatColor.GREEN.toString()) + '▶'),
    REGENERATING("REGENERATING", 1, String.valueOf(ChatColor.GOLD.toString()) + '⇪'),
    PAUSED("PAUSED", 2, String.valueOf(ChatColor.RED.toString()) + '■');
    
    @Getter private String symbol;
    
    RegenStatus(String s, int n, String symbol) {
        this.symbol = symbol;
    }
}
