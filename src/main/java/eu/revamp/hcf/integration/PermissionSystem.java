package eu.revamp.hcf.integration;

import org.bukkit.entity.Player;

public interface PermissionSystem {
    String getName(Player player);

    String getPrefix(Player player);

    String getSuffix(Player player);
}
