package eu.revamp.hcf.integration;

import eu.revamp.hcf.RevampHCF;
import eu.revamp.hcf.integration.implement.RevampPermissionSystem;
import eu.revamp.hcf.utils.Handler;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

@Getter @Setter
public class PermissionManager extends Handler {

    private PermissionSystem permissionSystem;

    public PermissionManager(RevampHCF instance) {
        super(instance);
    }

    public void setupPermissionSystem() {

        if (Bukkit.getServer().getPluginManager().getPlugin("RevampSystem") != null) {
            this.permissionSystem = new RevampPermissionSystem(this.getInstance());
        } else {
            Bukkit.getConsoleSender().sendMessage("§bPermission system not found!");
            setPermissionSystem(new PermissionSystem() {
                @Override
                public String getName(Player player) {
                    return "Default";
                }

                @Override
                public String getPrefix(Player player) {
                    return "";
                }

                @Override
                public String getSuffix(Player player) {
                    return "";
                }
            });
        }
    }
}
