package eu.revamp.tablist.playerversion;

import eu.revamp.tablist.interfaces.PlayerVersionInterface;
import org.bukkit.entity.Player;
import protocolsupport.api.ProtocolSupportAPI;

public class PlayerVersionProtocolSupport implements PlayerVersionInterface
{
    @Override
    public int getProtocolVersion(Player player) {
        return ProtocolSupportAPI.getProtocolVersion(player).getId();
    }
}
