package eu.revamp.hcf.integration.implement;

import eu.revamp.hcf.RevampHCF;
import eu.revamp.hcf.integration.PermissionSystem;
import eu.revamp.hcf.utils.Handler;
import eu.revamp.system.api.player.PlayerData;
import eu.revamp.system.plugin.RevampSystem;
import org.bukkit.entity.Player;

public class RevampPermissionSystem extends Handler implements PermissionSystem {
    public RevampPermissionSystem(RevampHCF plugin) {
        super(plugin);
    }

    @Override
    public String getName(Player player) {
        PlayerData targetProfile = RevampSystem.getINSTANCE().getPlayerManagement().getPlayerData(player.getUniqueId());
        return targetProfile != null ? targetProfile.getHighestRank().getDisplayName() : "Default";
    }

    @Override
    public String getPrefix(Player player) {
        PlayerData targetProfile = RevampSystem.getINSTANCE().getPlayerManagement().getPlayerData(player.getUniqueId());
        return targetProfile != null ? targetProfile.getHighestRank().getPrefix() : "";
    }

    @Override
    public String getSuffix(Player player) {
        PlayerData targetProfile = RevampSystem.getINSTANCE().getPlayerManagement().getPlayerData(player.getUniqueId());
        return targetProfile != null ? targetProfile.getHighestRank().getSuffix() : "";
    }
}

