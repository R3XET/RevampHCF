package eu.revamp.tablist.playerversion;

import eu.revamp.tablist.RevampTab;
import eu.revamp.tablist.interfaces.PlayerVersionInterface;
import org.bukkit.entity.Player;

public class PlayerVersionTinyProtocol implements PlayerVersionInterface
{
    @Override
    public int getProtocolVersion(Player player) {
        return RevampTab.getInstance().getTinyProtocol().getProtocolVersion(player);
    }
}
