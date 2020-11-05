package eu.revamp.hcf.factions.utils.games;

import java.util.ArrayList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableList;
import java.util.List;
import com.google.common.collect.ImmutableSet;
import java.util.Collection;

import eu.revamp.hcf.factions.utils.zone.CaptureZone;
import eu.revamp.spigot.utils.chat.color.CC;
import lombok.Getter;
import org.bukkit.Location;
import eu.revamp.hcf.utils.Utils;
import eu.revamp.hcf.factions.utils.zone.ClaimZone;
import eu.revamp.hcf.utils.inventory.BukkitUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import eu.revamp.hcf.games.GameType;

import java.util.Map;
import java.util.EnumMap;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

public class ConquestFaction extends CapturableFaction implements ConfigurationSerializable
{
    private EnumMap<ConquestZone, CaptureZone> captureZones;
    
    public ConquestFaction(String name) {
        super(name);
        this.captureZones = new EnumMap<>(ConquestZone.class);
    }
    
    public ConquestFaction(Map<String, Object> map) {
        super(map);
        this.captureZones = new EnumMap<>(ConquestZone.class);
        Object object;
        if ((object = map.get("red")) instanceof CaptureZone) {
            this.captureZones.put(ConquestZone.RED, (CaptureZone)object);
        }
        if ((object = map.get("green")) instanceof CaptureZone) {
            this.captureZones.put(ConquestZone.GREEN, (CaptureZone)object);
        }
        if ((object = map.get("blue")) instanceof CaptureZone) {
            this.captureZones.put(ConquestZone.BLUE, (CaptureZone)object);
        }
        if ((object = map.get("yellow")) instanceof CaptureZone) {
            this.captureZones.put(ConquestZone.YELLOW, (CaptureZone)object);
        }
    }
    
    public Map<String, Object> serialize() {
        Map<String, Object> map = super.serialize();
        for (Map.Entry<ConquestZone, CaptureZone> entry : this.captureZones.entrySet()) {
            map.put(entry.getKey().name().toLowerCase(), entry.getValue());
        }
        return map;
    }
    
    public GameType getEventType() {
        return GameType.CONQUEST;
    }
    
    public void printDetails(CommandSender sender) {
        sender.sendMessage(ChatColor.GRAY + BukkitUtils.STRAIGHT_LINE_DEFAULT);
        sender.sendMessage(CC.translate(this.getDisplayName(sender)));
        for (ClaimZone claim : this.claims) {
            Location location = claim.getCenter();
            sender.sendMessage(ChatColor.YELLOW + "Location: " + ChatColor.RESET + '(' + ConquestFaction.ENVIRONMENT_MAPPINGS.get(location.getWorld().getEnvironment()) + ", " + location.getBlockX() + " " + CC.STICK + " " + location.getBlockZ() + ')');
        }
        sender.sendMessage(ChatColor.GRAY + BukkitUtils.STRAIGHT_LINE_DEFAULT);
    }
    
    public void setZone(ConquestZone conquestZone, CaptureZone captureZone) {
        switch (conquestZone) {
            case RED: {
                this.captureZones.put(ConquestZone.RED, captureZone);
                break;
            }
            case BLUE: {
                this.captureZones.put(ConquestZone.BLUE, captureZone);
                break;
            }
            case GREEN: {
                this.captureZones.put(ConquestZone.GREEN, captureZone);
                break;
            }
            case YELLOW: {
                this.captureZones.put(ConquestZone.YELLOW, captureZone);
                break;
            }
            default: {
                throw new AssertionError("Unsupported operation");
            }
        }
    }
    
    public CaptureZone getRed() {
        return this.captureZones.get(ConquestZone.RED);
    }
    
    public CaptureZone getGreen() {
        return this.captureZones.get(ConquestZone.GREEN);
    }
    
    public CaptureZone getBlue() {
        return this.captureZones.get(ConquestZone.BLUE);
    }
    
    public CaptureZone getYellow() {
        return this.captureZones.get(ConquestZone.YELLOW);
    }
    
    public Collection<ConquestZone> getConquestZones() {
        return ImmutableSet.copyOf(this.captureZones.keySet());
    }
    
    public List<CaptureZone> getCaptureZones() {
        return ImmutableList.copyOf(this.captureZones.values());
    }
    
    public enum ConquestZone
    {
        RED("RED", 0, ChatColor.RED, "Red"), 
        BLUE("BLUE", 1, ChatColor.AQUA, "Blue"), 
        YELLOW("YELLOW", 2, ChatColor.YELLOW, "Yellow"), 
        GREEN("GREEN", 3, ChatColor.GREEN, "Green");
        
        @Getter private String name;
        @Getter private ChatColor color;
        private static Map<String, ConquestZone> BY_NAME;
        
        static {
            ImmutableMap.Builder<String, ConquestZone> builder = ImmutableMap.builder();
            ConquestZone[] values;
            for (int length = (values = values()).length, i = 0; i < length; ++i) {
                ConquestZone zone = values[i];
                builder.put(zone.name().toUpperCase(), zone);
            }
            ConquestZone.BY_NAME = builder.build();
        }
        
        ConquestZone(String s, int n, ChatColor color, String name) {
            this.color = color;
            this.name = name;
        }
        
        public String getDisplayName() {
            return this.color.toString() + this.name;
        }
        
        public static ConquestZone getByName(String name) {
            return ConquestZone.BY_NAME.get(name.toUpperCase());
        }
        
        public static Collection<String> getNames() {
            return new ArrayList<>(ConquestZone.BY_NAME.keySet());
        }
    }
}
