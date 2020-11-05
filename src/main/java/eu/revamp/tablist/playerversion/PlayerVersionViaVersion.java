package eu.revamp.tablist.playerversion;

import eu.revamp.tablist.interfaces.PlayerVersionInterface;
import org.bukkit.entity.Player;
import us.myles.ViaVersion.api.Via;

public class PlayerVersionViaVersion implements PlayerVersionInterface
{
    @Override
    public int getProtocolVersion(Player player) {
        return Via.getAPI().getPlayerVersion(player.getUniqueId());
    }
}
