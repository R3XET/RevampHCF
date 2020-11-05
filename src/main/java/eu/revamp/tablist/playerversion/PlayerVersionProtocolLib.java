package eu.revamp.tablist.playerversion;

import com.comphenix.protocol.ProtocolLibrary;
import eu.revamp.tablist.interfaces.PlayerVersionInterface;
import org.bukkit.entity.Player;

public class PlayerVersionProtocolLib implements PlayerVersionInterface
{
    @Override
    public int getProtocolVersion(Player player) {
        return ProtocolLibrary.getProtocolManager().getProtocolVersion(player);
    }
}
