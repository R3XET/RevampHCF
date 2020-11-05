package eu.revamp.hcf.factions.type;

import eu.revamp.spigot.utils.chat.color.CC;
import eu.revamp.hcf.utils.inventory.BukkitUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import java.util.Map;
import eu.revamp.hcf.RevampHCF;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

public class RoadFaction extends ClaimableFaction implements ConfigurationSerializable
{
    public static int ROAD_MIN_HEIGHT = RevampHCF.getInstance().getConfig().getInt("FACTIONS-SETTINGS.ROAD-MIN-HEIGHT");
    public static int ROAD_MAX_HEIGHT = RevampHCF.getInstance().getConfig().getInt("FACTIONS-SETTINGS.ROAD-MAX-HEIGHT");

    public RoadFaction(String name) {
        super(name);
    }
    
    public RoadFaction(Map<String, Object> map) {
        super(map);
    }
    
    public String getDisplayName(CommandSender sender) {
        return RevampHCF.getInstance().getConfiguration().getRoadColor() + this.getName() + " Road";
    }
    
    @Override
    public void printDetails(CommandSender sender) {
        sender.sendMessage(ChatColor.GRAY + BukkitUtils.STRAIGHT_LINE_DEFAULT);
        sender.sendMessage(CC.translate(this.getDisplayName(sender)));
        sender.sendMessage(ChatColor.YELLOW + "Location: " + ChatColor.RESET + "None");
        sender.sendMessage(ChatColor.GRAY + BukkitUtils.STRAIGHT_LINE_DEFAULT);
    }
    
    public static class NorthRoadFaction extends RoadFaction implements ConfigurationSerializable
    {
        public NorthRoadFaction() {
            super("North");
        }
        
        public NorthRoadFaction(Map<String, Object> map) {
            super(map);
        }
    }
    
    public static class EastRoadFaction extends RoadFaction implements ConfigurationSerializable
    {
        public EastRoadFaction() {
            super("East");
        }
        
        public EastRoadFaction(Map<String, Object> map) {
            super(map);
        }
    }
    
    public static class SouthRoadFaction extends RoadFaction implements ConfigurationSerializable
    {
        public SouthRoadFaction() {
            super("South");
        }
        
        public SouthRoadFaction(Map<String, Object> map) {
            super(map);
        }
    }
    
    public static class WestRoadFaction extends RoadFaction implements ConfigurationSerializable
    {
        public WestRoadFaction() {
            super("West");
        }
        
        public WestRoadFaction(Map<String, Object> map) {
            super(map);
        }
    }
}
