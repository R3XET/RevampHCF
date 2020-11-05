package eu.revamp.tablist.playerversion;

import eu.revamp.tablist.interfaces.PlayerVersionInterface;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class PlayerVersion1_7 implements PlayerVersionInterface {
    @Override
    public int getProtocolVersion(Player player) {
        return ((CraftPlayer) player).getHandle().playerConnection.networkManager.getVersion();
    }
}
