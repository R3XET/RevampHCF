package eu.revamp.hcf;

import net.md_5.bungee.api.ChatColor;

public class HCFSettings {

    public static ChatColor PRIMARY_COLOUR = ChatColor.YELLOW;
    public static ChatColor SECONDARY_COLOUR = ChatColor.GRAY;
    public static ChatColor LINE_COLOUR = ChatColor.DARK_GRAY;

    public boolean isIronHCF;

    public HCFSettings() {
        this.isIronHCF = true;
    }
}
