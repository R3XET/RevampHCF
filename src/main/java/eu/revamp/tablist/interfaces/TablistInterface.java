package eu.revamp.tablist.interfaces;

import eu.revamp.tablist.element.TablistElement;
import org.bukkit.entity.Player;

import java.util.List;

public interface TablistInterface
{
    List<TablistElement> getTabElements(Player p0);
    
    String getTabHeader(Player player);

    String getTabFooter(Player player);
}
