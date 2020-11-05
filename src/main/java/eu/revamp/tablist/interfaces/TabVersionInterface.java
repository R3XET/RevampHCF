package eu.revamp.tablist.interfaces;

import org.bukkit.entity.Player;

public interface TabVersionInterface
{
    void setup(Player p0);
    
    void addPlayerInfo(Player p0, Object p1);
    
    void removePlayerInfo(Player p0, Object p1);
    
    void update(Player p0);
    
    int getSlots(Player p0);
    
    Object createPlayer(Player p0, String p1);
    
    void setHeaderAndFooter(Player p0);
    
    void removePlayerInfoForEveryone(Player p0);
    
    void addAllOnlinePlayers(Player p0);
    
    void removeAllOnlinePlayers(Player p0);
}
