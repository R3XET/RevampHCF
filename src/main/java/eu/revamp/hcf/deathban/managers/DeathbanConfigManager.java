package eu.revamp.hcf.deathban.managers;

import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

public class DeathbanConfigManager {
    private String name;
    private String banTime;

    public DeathbanConfigManager(String name, String banTime) {
        this.name = name;
        this.banTime = banTime;
    }

    public String getName() {
        return name;
    }

    public String getBanTime() {
        return banTime;
    }

    public Permission getPermission() {
        Permission permission = new Permission("deathban." + name + ".permission");
        permission.setDefault(PermissionDefault.FALSE);
        return permission;
    }
}